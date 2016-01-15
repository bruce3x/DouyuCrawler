package me.brucezz.crawler.util;


public class HexUtil {

    private final static String HEX_STRING = "0123456789ABCDEF";
    private final static byte[] HEX_BYTES = HEX_STRING.getBytes();

    /**
     * 字符串用 '-' 连接
     */
    private static String bytes2Hex(byte[] b) {
        if (b == null || b.length <= 0) return null;

        byte[] buff = new byte[3 * b.length];
        for (int i = 0; i < b.length; i++) {
            buff[3 * i] = HEX_BYTES[(b[i] >> 4) & 0x0f];
            buff[3 * i + 1] = HEX_BYTES[b[i] & 0x0f];
            buff[3 * i + 2] = 45;
        }
        return new String(buff);
    }

    /**
     * @return 字节数组 转化为 16进制字符串，无间隔，小写
     */
    public static String bytes2HexString(byte[] b) {
        return bytes2Hex(b).replace("-", "");
    }

    /**
     * @return 大写
     */
    public static String bytes2HexStringUpperCase(byte[] b) {
        return bytes2Hex(b).toUpperCase();
    }

    /**
     * @return 两个字节之间用空格隔开
     */
    public static String bytes2HexStringWithSpace(byte[] b) {
        return bytes2Hex(b).replace("-", " ");
    }

    /**
     * 16进制字符串 转化为 字节数组
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
