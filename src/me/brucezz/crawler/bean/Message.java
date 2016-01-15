package me.brucezz.crawler.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Brucezz on 2016/01/03.
 * DouyuCrawler
 */
public class Message {

    /**
     * 请求消息体包含五部分：
     * 1.计算后四部分的字节长度，占4个字节
     * 2.内容设置和第一部分一样
     * 3.请求代码，固定，发到斗鱼是0xb1,0x02,0x00,0x00,接收是0xb2,0x02,0x00,0x00，4个字节
     * 4.消息正文
     * 5.尾部1个空字节
     */

    private int[] length;
    private int[] code;
    private int[] magic;
    private String content;
    private int[] end;

    public Message(String content) {
        length = new int[]{calcMessageLength(content), 0x00, 0x00, 0x00};
        code = new int[]{calcMessageLength(content), 0x00, 0x00, 0x00};
        magic = new int[]{0xb1, 0x02, 0x00, 0x00};
        this.content = content;
        end = new int[]{0x00};
    }

    /**
     * 计算消息体长度
     */
    private int calcMessageLength(String content) {
        return 4 + 4 + (content == null ? 0 : content.length()) + 1;
    }

    public int[] getLength() {
        return length;
    }

    public void setLength(int[] length) {
        this.length = length;
    }

    public int[] getCode() {
        return code;
    }

    public void setCode(int[] code) {
        this.code = code;
    }

    public int[] getMagic() {
        return magic;
    }

    public void setMagic(int[] magic) {
        this.magic = magic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int[] getEnd() {
        return end;
    }

    public void setEnd(int[] end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Message{" +
                "length=" + Arrays.toString(length) +
                ", code=" + Arrays.toString(code) +
                ", magic=" + Arrays.toString(magic) +
                ", content='" + content + '\'' +
                ", end=" + Arrays.toString(end) +
                '}';
    }

    private  ByteArrayOutputStream baos;

    /**
     * 将Message对象转化为字节数组
     */
    public byte[] getBytes() throws IOException {
        if (baos == null ) baos = new ByteArrayOutputStream();
        baos.reset();

        for (int b : length) baos.write(b);
        for (int b : code) baos.write(b);
        for (int b : magic) baos.write(b);
        if (content != null) baos.write(content.getBytes());
        for (int b : end) baos.write(b);

        return baos.toByteArray();
    }
}
