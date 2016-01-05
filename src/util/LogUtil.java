package util;

import config.Config;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zero on 2016/01/05.
 * DouyuDanmu
 */
public class LogUtil {

    private static final boolean DEBUG_MODE = Config.DEBUG_MODE;

    private static String timestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    private static String logMessage(String tag, String message) {
        return "[" + timestamp() + "]" + "[" + tag + "]" + message;
    }

    public static void d(String message) {
        if (DEBUG_MODE) {
            d("DEBUG", message);
        }
    }

    public static void d(String tag, String message) {
        if (DEBUG_MODE) {
            System.out.println(logMessage(tag, message));
        }
    }

    public static void i(String message) {
        i("INFO", message);
    }

    public static void i(String tag, String message) {
        System.out.println(logMessage(tag, message));
    }

    public static void e(String message) {
        e("ERROR", message);
    }

    public static void e(String tag, String message) {
        System.err.println(logMessage(tag, message));
    }

    public static void main(String[] args) {
        //测试
        i("123");
        i("tag", "123");

        d("123");
        d("tag", "123");

        e("123");
        e("tag", "123");
    }
}
