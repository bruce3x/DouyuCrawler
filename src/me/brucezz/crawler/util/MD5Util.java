package me.brucezz.crawler.util;

import java.security.MessageDigest;

/**
 * Created by Brucezz on 2016/01/04.
 * DouyuCrawler
 */
public class MD5Util {
    public static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要, 获得密文
            byte[] md = mdInst.digest(s.getBytes());
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte b : md) {
                str[k++] = hexDigits[b >>> 4 & 0xf];
                str[k++] = hexDigits[b & 0xf];
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}