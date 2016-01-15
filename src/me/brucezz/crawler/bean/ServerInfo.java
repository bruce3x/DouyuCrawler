package me.brucezz.crawler.bean;


/**
 * Created by Brucezz on 2016/01/05.
 * DouyuCrawler
 */
public class ServerInfo {
    private String host;
    private int port;


    @Override
    public java.lang.String toString() {
        return "ServerInfo{" +
                "host=" + host +
                ", port=" + port +
                '}';
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ServerInfo(String host, int port) {
        this.host = host;
        this.port = port;

    }
}
