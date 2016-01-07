package me.brucezz.crawler.model;

/**
 * Created by zero on 2016/01/05.
 * Douyu
 */
public class Request {

    public static String gid(int roomId, String devid, String rt, String vk) {

        return String.format("type@=loginreq/username@=/ct@=0/password@=/roomid@=%d/devid@=%s/rt@=%s/vk@=%s/ver@=20150929/", roomId, devid, rt, vk);
    }

    public static String barrageLogin(int roomId) {
        return String.format("type@=loginreq/username@=visitor34807350/password@=1234567890123456/roomid@=%d/", roomId);
    }

    public static String joinGroup(int rid, int gid) {
        return String.format("type@=joingroup/rid@=%d/gid@=%d/", rid, gid);
    }

    public static String keepLive(int tick) {
        return String.format("type@=keeplive/tick@=%d/", tick);
//        return String.format("type@=mrkl/", tick);
    }
}
