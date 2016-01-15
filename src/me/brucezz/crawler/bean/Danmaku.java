package me.brucezz.crawler.bean;

import java.util.Date;

/**
 * Created by Brucezz on 2016/01/03.
 * DouyuCrawler
 */
public class Danmaku {

    private int uid;//用户id
    private String snick;//昵称
    private String content;//内容
    private Date date;//发布时间
    private int rid;//房间号

    public Danmaku(int uid, String snick, String content, int rid) {
        this.uid = uid;
        this.snick = snick;
        this.content = content;
        this.date = new Date();
        this.rid = rid;
    }

    @Override
    public String toString() {
        return "Danmaku{" +
                "uid=" + uid +
                ", snick='" + snick + '\'' +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", rid=" + rid +
                '}';
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getSnick() {
        return snick;
    }

    public void setSnick(String snick) {
        this.snick = snick;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }
}
