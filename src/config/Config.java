package config;


import util.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by zero on 2016/01/04.
 * Douyu
 */
public class Config {
    private static final String PROPERTIES_NAME = "conf.properties";
    public static boolean loadSuccess = false;

    public static boolean DEBUG_MODE = true;

    /**
     * 键  房间简称( 即room.url.XXX中的XXX )
     * 值  房间地址
     */
    public static Map<String, String> ROOM_MAP = new HashMap<>();

    static {
        InputStream in = null;
        try {
            Properties properties = new Properties();
            in = new FileInputStream(new File(PROPERTIES_NAME));
            properties.load(in);

            DEBUG_MODE = Boolean.parseBoolean(properties.getProperty("debug"));

            Set<Object> objects = properties.keySet();
            for (Object object : objects) {
                String key = (String) object;
                if (key.startsWith("room.url.")) {
                    String name = key.substring(9).trim();
                    if (name.length() > 0)
                        ROOM_MAP.put(name, properties.getProperty(key));
                }
            }

            loadSuccess = true;
            LogUtil.i("读取配置信息成功！");

            displayConfig();

        } catch (Exception e) {
            LogUtil.e(e.toString());
            LogUtil.w("读取配置信息失败！");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LogUtil.e(e.toString());
                }
            }
        }
    }

    private static void displayConfig() {
        LogUtil.d("Config", "----------------------------------------------------------------");

        LogUtil.d("Config", "DEBUG_MODE: " + DEBUG_MODE);
        Set<String> nameSet = ROOM_MAP.keySet();
        for (String name : nameSet) {
            LogUtil.d("Config", "ROOM_URL: " + name +" >> "+ROOM_MAP.get(name));
        }

        LogUtil.d("Config", "----------------------------------------------------------------");
    }

    public static void main(String[] args) {

    }
}
