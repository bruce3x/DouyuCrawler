package me.brucezz.crawler.util;


public class HexUtil {
    private final static String HEX_STRING = "0123456789ABCDEF";
    private final static byte[] HEX_BYTE = HEX_STRING.getBytes();

    // 从字节数组到十六进制字符串转换  
    public static String Bytes2HexString(byte[] b) {
        byte[] buff = new byte[3 * b.length];
        for (int i = 0; i < b.length; i++) {
            buff[3 * i] = HEX_BYTE[(b[i] >> 4) & 0x0f];
            buff[3 * i + 1] = HEX_BYTE[b[i] & 0x0f];
            buff[3 * i + 2] = 45;
        }
        String re = new String(buff);
        return re.replace("-", " ");
    }

    public static String Bytes2HexStringWithOutSpace(byte[] b) {
        byte[] buff = new byte[3 * b.length];
        for (int i = 0; i < b.length; i++) {
            buff[3 * i] = HEX_BYTE[(b[i] >> 4) & 0x0f];
            buff[3 * i + 1] = HEX_BYTE[b[i] & 0x0f];
            buff[3 * i + 2] = 45;
        }
        String re = new String(buff);
        return re.replace("-", "");
    }

    // 从十六进制字符串到字节数组转换
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
