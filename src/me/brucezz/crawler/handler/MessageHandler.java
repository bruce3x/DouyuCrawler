package me.brucezz.crawler.handler;

import me.brucezz.crawler.bean.Message;
import me.brucezz.crawler.util.HexUtil;
import me.brucezz.crawler.util.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Brucezz on 2016/01/04.
 * DouyuCrawler
 */
public class MessageHandler {

    /**
     * 发送消息
     */
    public static void send(Socket socket, String content) throws IOException {
        if (socket == null || !socket.isConnected()) return;

        Message message = new Message(content);
        OutputStream out = socket.getOutputStream();
        out.write(message.getBytes());

        LogUtil.d("Send Message", message.toString());
    }

    /**
     * 接收消息并处理
     */
    public static void receive(Socket socket, MessageHandler.OnReceiveListener listener)
            throws IOException {
        if (socket == null || !socket.isConnected()) return;

        int len;
        byte[] buffer = new byte[8 * 1024];
        InputStream in = socket.getInputStream();

        while (socket.isConnected() //链接结束
                && (len = in.read(buffer)) != -1 //输入流结束
                ) {
            if (listener != null) {
                listener.onReceive(splitResponse(Arrays.copyOf(buffer, len)));
                if (listener.isFinished()) return;
            }
        }
    }

    /**
     * 分离同时返回的多组数据
     * 不优雅的方法：
     *      1.先将字节数组转化为对应的十六进制字符串
     *      2.然后用斗鱼定义的请求码"b2020000"来分割字符串
     *      3.判断"00"为消息尾部
     *      4.遍历分离出多组Response
     */
    public static List<String> splitResponse(byte[] buffer) {
        if (buffer == null || buffer.length <= 0) return null;

        List<String> resList = new ArrayList<>();
        String byteArray = HexUtil.bytes2HexString(buffer).toLowerCase();

        String[] responseStrings = byteArray.split("b2020000");
        int end;
        for (int i = 1; i < responseStrings.length; i++) {
            if (!responseStrings[i].contains("00")) continue;
            end = responseStrings[i].indexOf("00");
            byte[] bytes = HexUtil.hexString2Bytes(responseStrings[i].substring(0, end));
            if (bytes != null) resList.add(new String(bytes));
        }

        return resList;
    }

    public interface OnReceiveListener {

        /**
         * 返回数据
         */
        void onReceive(List<String> responses);

        /**
         * 确认是否结束接收数据
         */
        boolean isFinished();
    }


}
