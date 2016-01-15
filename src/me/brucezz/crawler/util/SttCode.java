package me.brucezz.crawler.util;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by Brucezz on 2016/01/05.
 * DouyuCrawler
 *
 * 服务器返回数据 编解码， 根据JS代码转化过来的
 */
public class SttCode {

    public static String encode(String content) {
        if (content == null || content.length() <= 0) return null;
        return null;
    }

    public static Map<String, String> decode(String content) {
        if (content == null || content.length() <= 0) return null;


        String[] strs = content.split("/");
        Map<String, String> map = new HashMap<>();
        for (String str : strs) {

            if (str.contains("@=")) {
                String[] kv = str.split("@=");
                if (kv.length < 2) kv = new String[]{kv[0], ""};
                map.put(deFilterStr(kv[0]), deFilterStr(kv[1]));
            } else {
                map.put("", deFilterStr(str));
            }
        }

        return map;
    }

    public static String displayMap(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (String s : map.keySet()) {
            sb.append(s).append(":").append(map.get(s)).append("\n");
        }

        return sb.toString();
    }

    public static String deFilterStr(String str) {
        if (str == null) return null;
        return str.trim().replace("@A", "@").replace("@S", "/");
    }

    public static String filterStr(String str) {
        if (str == null) return null;
        return str.trim().replace("@", "@A").replace("/", "@S");
    }

}
