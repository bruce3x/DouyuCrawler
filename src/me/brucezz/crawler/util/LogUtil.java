package me.brucezz.crawler.util;

import me.brucezz.crawler.config.Config;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zero on 2016/01/05.
 * Douyu
 */
public class LogUtil {
    // TODO: 16-1-7 Log持久化到本地

    private static final boolean DEBUG_MODE = Config.DEBUG_MODE;
    private static final String DEFAULT_TAG = "TAG";
//    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 非调试状态
     */
    private static final String LEVEL_INFO = "INFO";
    private static final String LEVEL_WARNING = "WARNING";

    /**
     * 调试状态
     */
    private static final String LEVEL_DEBUG = "DEBUG";
    private static final String LEVEL_ERROR = "ERROR";


    private static String timestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    private static String logMessage(String level, String tag, String message) {
        /**
         * Log格式
         *
         * - 非调试模式
         * [timestamp][level][TAG][Message]
         *
         * - 调试模式
         * [timestamp][level][TAG][Caller][Message]
         */
        // TODO: 16-1-7 Log格式添加 多任务id

        return String.format("[%s][%s][%s] %s", timestamp(), level, tag, message);
    }

    private static String logMessage(String level, String tag, String className, String message) {

        return String.format("[%s][%s][%s][%s] %s", timestamp(), level, tag, className, message);


    }


    private static void printOut(String log) {
        System.out.println(log);
    }

    private static void printErr(String log) {
        System.err.println(log);
    }


    /**
     * DEBUG级别Log， 调试阶段使用
     */
    public static void d(String message) {
        if (DEBUG_MODE) {
            printOut(logMessage(LEVEL_DEBUG, DEFAULT_TAG, getInvokeInfo(), message));
        }
    }

    public static void d(String tag, String message) {
        if (DEBUG_MODE) {
            printOut(logMessage(LEVEL_DEBUG, tag, getInvokeInfo(), message));
        }
    }

    /**
     * ERROR级别Log， 调试阶段使用
     */
    public static void e(String message) {
        if (DEBUG_MODE) {
            printErr(logMessage(LEVEL_ERROR, DEFAULT_TAG, getInvokeInfo(), message));
        }
    }

    public static void e(String tag, String message) {
        if (DEBUG_MODE) {
            printErr(logMessage(LEVEL_ERROR, tag, getInvokeInfo(), message));
        }
    }


    /**
     * INFO级别Log， 非调试阶段使用
     */
    public static void i(String message) {
        i(DEFAULT_TAG, message);
    }

    public static void i(String tag, String message) {
        printOut(logMessage(LEVEL_INFO, tag, message));
    }

    /**
     * ERROR级别Log, 非调试阶段使用
     */
    public static void w(String message) {
        w(DEFAULT_TAG, message);
    }

    public static void w(String tag, String message) {
        printErr(logMessage(LEVEL_WARNING, tag, message));
    }

    /**
     * 获取调用信息
     * 格式： simpleName:linenumber
     */
    private static String getInvokeInfo() {
        String result;
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[2];
        result = thisMethodStack.getClassName();
        int lastIndex = result.lastIndexOf(".");
        result = result.substring(lastIndex + 1, result.length());
        int line = thisMethodStack.getLineNumber();

        if (result.contains("$"))
            result = result.substring(0, result.indexOf("$"));

        return result + ":" + line;
    }

    public static void main(String[] args) {
        //测试
        i("123");
        i("iiiiiiii", "123");

        d("123");
        d("ddddd", "123");

        w("123");
        w("twwwwwag", "123");

        e("123");
        e("eeeeeee", "123");
    }
}
