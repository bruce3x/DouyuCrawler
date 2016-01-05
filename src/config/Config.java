package config;


import util.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Created by zero on 2016/01/04.
 * Douyu
 */
public class Config {
    private static final String PROPERTIES_NAME = "conf.properties";
    public static boolean loadSuccess = false;

    public static boolean DEBUG_MODE = true;

    public static List<String> ROOM_LIST = new ArrayList<>();

    static {
        InputStream in= null;
        try {
            Properties properties = new Properties();
            in = new FileInputStream(new File(PROPERTIES_NAME));
            properties.load(in);

            DEBUG_MODE = Boolean.parseBoolean(properties.getProperty("debug"));

            Set<Object> objects = properties.keySet();
            for (Object object : objects) {
                String key = (String) object;
                if (key.startsWith("room.url.")) {
                    ROOM_LIST.add(properties.getProperty(key));
                }
            }

            loadSuccess = true;
            LogUtil.i("读取配置信息成功！");

            displayConfig();

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("读取配置信息失败！");
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void displayConfig() {
        LogUtil.d("Config", "################################");

        LogUtil.d("Config", "DEBUG_MODE: " + DEBUG_MODE);
        for (String s : ROOM_LIST) {
            LogUtil.d("Config", "ROOM_LIST_URL: " + s);
        }

        LogUtil.d("Config", "################################");
    }


    public static void main(String[] args) {
        System.out.println(DEBUG_MODE);
        for (String s : ROOM_LIST) {
            System.out.println(s);
        }
    }
}
