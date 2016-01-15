package me.brucezz.crawler.bean;

/**
 * Created by Brucezz on 2016/01/05.
 * DouyuCrawler
 */
public class Request {
    /**
     * 程序需要发送的各种请求正文
     */

    public static String gid(int roomId, String devid, String rt, String vk) {
        return String.format("type@=loginreq/username@=/ct@=0/password@=/roomid@=%d/devid@=%s/rt@=%s/vk@=%s/ver@=20150929/", roomId, devid, rt, vk);
    }

    public static String danmakuLogin(int roomId) {
        return String.format("type@=loginreq/username@=visitor34807350/password@=1234567890123456/roomid@=%d/", roomId);
    }

    public static String joinGroup(int rid, int gid) {
        return String.format("type@=joingroup/rid@=%d/gid@=%d/", rid, gid);
    }

    public static String keepLive(int tick) {
        return String.format("type@=keeplive/tick@=%d/", tick);
    }
}
