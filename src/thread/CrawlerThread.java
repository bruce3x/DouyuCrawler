package thread;

import handler.HttpHandler;
import handler.MessageHandler;
import handler.ResponseParser;
import model.Barrage;
import model.Request;
import model.ServerInfo;
import util.LogUtil;
import util.MD5Util;
import util.SttCode;

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

    private String roomUrl;
    private int rid = -1;
    private int gid = -1;
    List<ServerInfo> barrageServers = new ArrayList<>();


    private MessageHandler.OnReceiveListener loginListener = new MessageHandler.OnReceiveListener() {
        private boolean finish = false;

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

            finish = f1 && f2;
        }

        @Override
        public boolean isFinished() {
            return finish;
        }
    };

    private MessageHandler.OnReceiveListener barrageListener = new MessageHandler.OnReceiveListener() {
        @Override
        public void onReceive(List<String> responses) {

            for (String response : responses) {
                LogUtil.d("Receive Response", response);

                if (response.contains("chatmessage")) {
                    //解析弹幕
                    Barrage barrage = ResponseParser.parseBarrage(response);
                    if (barrage != null) LogUtil.i("Barrage", barrage.getName() + ":" + barrage.getContent());
                }
            }
        }

        @Override
        public boolean isFinished() {
            return false;
        }
    };

    public CrawlerThread(String roomUrl) {
        this.roomUrl = roomUrl;
        LogUtil.i("Crawler启动 ...");
    }

    @Override
    public void run() {
        //获取房间页面
        LogUtil.i("获取房间页面 ...");
        String s = HttpHandler.get(roomUrl);
        //获取roomId
        LogUtil.i("获取直播房间ID ...");
        rid = ResponseParser.parseRoomId(s);
        //获取服务器IP列表
        LogUtil.i("获取服务器列表 ...");
        List<ServerInfo> serverList = ResponseParser.parseServerInfo(s);

        if (serverList == null || serverList.size() <= 0) {
            LogUtil.e("获取服务器列表失败！");
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
                LogUtil.e("没有可用的弹幕服务器 ...");
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
            MessageHandler.receive(socket, barrageListener);


        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.d("Error", e.toString());
            LogUtil.e("与服务器连接失败!");
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
            MessageHandler.send(socket, Request.gid(rid, uuid, timestamp, vk));
            //等待5s，获取返回数据
            MessageHandler.receive(socket, 5 * 1000, loginListener);

        } catch (IOException e) {
            LogUtil.d("Error", e.toString());
            LogUtil.e("登陆到服务器失败！");
        } finally

        {
            if (socket != null)
                try {
                    socket.close();
                } catch (IOException e) {
                    LogUtil.d("Error", e.toString());
                    LogUtil.e("连接关闭异常！");
                }
        }
    }

}


