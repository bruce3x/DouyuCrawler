package me.brucezz.crawler.util;

/**
 * Created by Brucezz on 2016/01/04.
 * DouyuCrawler
 */
public class HexUtil {
    private final static String HEX_STRING = "0123456789ABCDEF";
    private final static byte[] HEX_BYTE = HEX_STRING.getBytes();

    /**
     * @return 字节数组 转换为 十六进制字符串， 大写， '-'连接
     */
    private static String bytes2Hex(byte[] b) {
        byte[] buff = new byte[3 * b.length];
        for (int i = 0; i < b.length; i++) {
            buff[3 * i] = HEX_BYTE[(b[i] >> 4) & 0x0f];
            buff[3 * i + 1] = HEX_BYTE[b[i] & 0x0f];
            buff[3 * i + 2] = 45;
        }
        return new String(buff);
    }

    /**
     * @return 大写，无间隔
     */
    public static String bytes2HexString(byte[] b) {
        return bytes2Hex(b).replace("-", "");
    }

    /**
     * @return 大写，' '空格连接
     */
    public static String bytes2HexStringWithSpace(byte[] b) {
        return bytes2Hex(b).replace("-", " ");
    }

    /**
     * @return 十六进制字符串 转换为 字节数组
     */
    public static byte[] hexString2Bytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.replace(" ", "");
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) HEX_STRING.indexOf(c);
    }

}
