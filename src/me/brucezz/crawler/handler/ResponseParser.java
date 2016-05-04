package me.brucezz.crawler.handler;

import me.brucezz.crawler.bean.Danmaku;
import me.brucezz.crawler.bean.ServerInfo;
import me.brucezz.crawler.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Brucezz on 2016/01/03.
 * DouyuCrawler
 */
public class ResponseParser {

    private static final String REGEX_ROOM_ID = "\"room_id\":(\\d*),";
    private static final String REGEX_ROOM_STATUS = "\"show_status\":(\\d*),";
    private static final String REGEX_SERVER = "%7B%22ip%22%3A%22(.*?)%22%2C%22port%22%3A%22(.*?)%22%7D%2C";
    private static final String REGEX_GROUP_ID = "type@=setmsggroup.*/rid@=(\\d*?)/gid@=(\\d*?)/";
    private static final String REGEX_DANMAKU_SERVER = "/ip@=(.*?)/port@=(\\d*?)/";
    private static final String REGEX_CHAT_DANMAKU = "type@=chatmsg/.*rid@=(\\d*?)/.*uid@=(\\d*).*nn@=(.*?)/txt@=(.*?)/(.*)/";


    private static Matcher getMatcher(String content, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        return pattern.matcher(content);
    }


    /**
     * 从房间页面解析出roomId
     */
    public static int parseRoomId(String content) {
        int rid = -1;
        if (content == null) return rid;

        Matcher matcher = getMatcher(content, REGEX_ROOM_ID);
        if (matcher.find()) {
            rid = Integer.parseInt(matcher.group(1));
        }

        LogUtil.d("Parse RoomId", rid + "");
        return rid;
    }

    /**
     * 解析当前直播状态
     * @return 若room_status == 1 则正在直播
     */
    public static boolean parseOnline(String content) {
        if (content == null) return false;

        Matcher matcher = getMatcher(content, REGEX_ROOM_STATUS);
        return matcher.find() && "1".equals(matcher.group(1));
    }

    /**
     * 解析出服务器地址
     */
    public static List<ServerInfo> parseServerInfo(String content) {
        if (content == null) return null;

        Matcher matcher = getMatcher(content, REGEX_SERVER);
        List<ServerInfo> serverList = new ArrayList<>();

        while (matcher.find()) {
            ServerInfo serverInfo = new ServerInfo(matcher.group(1), Integer.parseInt(matcher.group(2)));
            serverList.add(serverInfo);

            LogUtil.d("Parse ServerInfo", serverInfo.toString());
        }
        return serverList;
    }

    /**
     * 解析弹幕服务器地址
     */
    public static List<ServerInfo> parseDanmakuServer(String content) {
        if (content == null) return null;

        Matcher matcher = getMatcher(content, REGEX_DANMAKU_SERVER);
        List<ServerInfo> serverList = new ArrayList<>();

        while (matcher.find()) {
            ServerInfo serverInfo = new ServerInfo(matcher.group(1), Integer.parseInt(matcher.group(2)));
            serverList.add(serverInfo);

            LogUtil.d("Parse DanmakuServer", serverInfo.toString());
        }
        return serverList;
    }


    /**
     * 解析  gid
     */
    public static int parseID(String response) {
        if (response == null) return -1;

        Matcher matcher = getMatcher(response, REGEX_GROUP_ID);
        int gid = -1;
        if (matcher.find()) {
            gid = Integer.parseInt(matcher.group(2));
        }

        LogUtil.d("Parse ID", "GId -> " + gid);

        return gid;
    }

    /**
     * 解析弹幕信息
     */
    public static Danmaku parseDanmaku(String response) {
        if (response == null) return null;

        Matcher matcher = getMatcher(response, REGEX_CHAT_DANMAKU);
        Danmaku danmaku = null;

        if (matcher.find()) {
            danmaku = new Danmaku(Integer.parseInt(matcher.group(2)),
                    matcher.group(3),
                    matcher.group(4),
                    Integer.parseInt(matcher.group(1)));
        }

        LogUtil.d("Parse Danmaku", danmaku + "");

        return danmaku;
    }


}
