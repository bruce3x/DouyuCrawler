package me.brucezz.crawler.thread;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import me.brucezz.crawler.handler.HttpHandler;
import me.brucezz.crawler.model.Request;
import me.brucezz.crawler.model.ServerInfo;
import me.brucezz.crawler.util.LogUtil;
import me.brucezz.crawler.util.MD5Util;
import me.brucezz.crawler.db.BarrageDao;
import me.brucezz.crawler.handler.MessageHandler;
import me.brucezz.crawler.handler.ResponseParser;
import me.brucezz.crawler.model.Barrage;
import me.brucezz.crawler.util.SttCode;
import me.brucezz.crawler.util.TimeHelper;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by zero on 2016/01/04.
 * Douyu
 */
public class CrawlerThread implements Runnable {

    private String roomName;
    private String roomUrl;
    private int rid = -1;
    private int gid = -1;
    List<ServerInfo> barrageServers = new ArrayList<>();


    private MessageHandler.OnReceiveListener loginListener = new MessageHandler.OnReceiveListener() {
        private boolean finish = false;
        private TimeHelper helper = new TimeHelper(10 * 1000);//10秒等待超时

        @Override
        public void onReceive(List<String> responses) {
            boolean f1 = false, f2 = false;

            for (String response : responses) {
                LogUtil.d("Receive Response", response);
                if (response.contains("msgrepeaterlist")) {
                    //获取弹幕服务器地址
                    LogUtil.i("获取弹幕服务器地址 ...");
                    String barrageServerStr = SttCode.deFilterStr(SttCode.deFilterStr(response));
                    barrageServers = ResponseParser.parseBarrageServer(barrageServerStr);
                    LogUtil.i("获取到 " + barrageServers.size() + " 个服务器地址 ...");
                    f1 = true;
                }

                if (response.contains("setmsggroup")) {
                    //获取gid
                    LogUtil.i("获取弹幕群组ID ...");
                    gid = ResponseParser.parseID(response);
                    LogUtil.i("弹幕群组ID：" + gid);
                    f2 = true;
                }
            }

            long now = System.currentTimeMillis();

            //获取到弹幕服务器地址和gid 或者超时之后，结束此次监听
            finish = f1 && f2 || helper.checkTimeout();
        }

        @Override
        public boolean isFinished() {
            return finish;
        }
    };

    private MessageHandler.OnReceiveListener barrageListener = new MessageHandler.OnReceiveListener() {

        private boolean finished = false;
        private List<Barrage> barrages = new ArrayList<>();
        private TimeHelper helper = new TimeHelper(20 * 60 * 1000);//间隔20min检测一次直播状态

        @Override
        public void onReceive(List<String> responses) {

            for (String response : responses) {
                LogUtil.d("Receive Response", response);

                if (!response.contains("chatmessage")) continue;

                //解析弹幕
                Barrage barrage = ResponseParser.parseBarrage(response);
                if (barrage == null) continue;

                barrages.add(barrage);
                LogUtil.i("Barrage", barrage.getSnick() + ":" + barrage.getContent());

                if (barrages.size() >= 20 && BarrageDao.saveBarrage(barrages)) {
                    LogUtil.i("DB", "保存弹幕到数据库 ...");
                    barrages.clear();
                }
            }

            //检测不在直播， 结束抓取
            if (helper.checkTimeout() && !isOnline()) finished = true;

        }

        @Override
        public boolean isFinished() {
            return finished;
        }
    };

    public CrawlerThread(String roomName, String roomUrl) {
        this.roomName = roomName;
        this.roomUrl = roomUrl;
        LogUtil.i("Crawler启动: " + roomName + " >> " + roomUrl + "");
    }

    @Override
    public void run() {
        //获取房间页面
        LogUtil.i("获取房间页面 ...");
        String pageHtml = HttpHandler.get(roomUrl);

        //获取roomId
        LogUtil.i("获取直播房间ID ...");
        rid = ResponseParser.parseRoomId(pageHtml);

        //检查是否在线
        boolean online = ResponseParser.parseOnline(pageHtml);
        if (!online) {
            LogUtil.w("该房间还没有直播！" + roomUrl);
            return;
        }

        //获取服务器IP列表
        LogUtil.i("获取服务器列表 ...");
        List<ServerInfo> serverList = ResponseParser.parseServerInfo(pageHtml);

        if (serverList == null || serverList.size() <= 0) {
            LogUtil.w("获取服务器列表失败！");
            LogUtil.i("Crawler结束 ...");
            return;
        }

        //登陆服务器
        loginRequest(serverList);

        if (rid == -1 || gid == -1) {
            LogUtil.i("Crawler结束 ...");
            return;
        }

        //开始抓取弹幕
        startCrawler();

        LogUtil.i("Crawler结束 ...");
    }

    private void startCrawler() {
        try {

            if (barrageServers == null || barrageServers.size() <= 0) {
                LogUtil.w("没有可用的弹幕服务器 ...");
                return;
            }
            ServerInfo barrageServer = barrageServers.get((int) (Math.random() * barrageServers.size()));
            Socket socket = new Socket(barrageServer.getHost(), barrageServer.getPort());
            LogUtil.i("登陆到弹幕服务器 " + barrageServer.getHost() + ":" + barrageServer.getPort());
            MessageHandler.send(socket, Request.barrageLogin(rid));
            LogUtil.i("进入 " + rid + " 号房间， " + gid + " 号弹幕群组 ...");
            MessageHandler.send(socket, Request.joinGroup(rid, gid));

            //心跳包线程
            new Thread(new KeepLiveThread(socket)).start();

            LogUtil.i("开始接收弹幕 ...");
            LogUtil.i("----------------------------------------------------------------");

            BarrageDao.createTable();

            MessageHandler.receive(socket, barrageListener);


        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.d("Error", e.toString());
            LogUtil.w("与服务器连接失败!");
        }
    }

    /**
     * 登陆到服务器 获取弹幕服务器地址 / gid
     */
    private void loginRequest(List<ServerInfo> ipList) {
        Socket socket = null;

        ServerInfo server = ipList.get((int) (Math.random() * ipList.size()));

        try {
            LogUtil.i("连接服务器 " + server.getHost() + ":" + server.getPort());
            socket = new Socket(server.getHost(), server.getPort());

            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
            String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            String vk = MD5Util.MD5(timestamp + "7oE9nPEG9xXV69phU31FYCLUagKeYtsF" + uuid);
            //发送登陆请求
//            MessageHandler.send(socket, Request.gid(rid, uuid, timestamp, vk));
            rid = 25515;
            uuid = "1C01371705D8B13361396AE2FAD50D6F";
            vk = "dedd39371447fa3fd0bc41dd828e8109";
            timestamp = "1452868253";
            MessageHandler.send(socket, Request.gid(rid, uuid, timestamp, vk));
//            MessageHandler.send(socket, Request.qrl(rid));
            //等待接收
            MessageHandler.receive(socket, loginListener);

        } catch (IOException e) {
            LogUtil.d("Error", e.toString());
            LogUtil.w("登陆到服务器失败！");
        } finally

        {
            if (socket != null)
                try {
                    socket.close();
                } catch (IOException e) {
                    LogUtil.d("Error", e.toString());
                    LogUtil.w("连接关闭异常！");
                }
        }
    }

    /**
     * 判断当前房间是否在直播
     */
    private boolean isOnline() {
        String pageHtml = HttpHandler.get(roomUrl);
        return pageHtml != null && ResponseParser.parseOnline(pageHtml);
    }

}


