package me.brucezz.crawler;

import me.brucezz.crawler.config.Config;
import me.brucezz.crawler.thread.CrawlerThread;

import java.util.Set;

/**
 * Created by Brucezz on 2016/01/04.
 * DouyuCrawler
 */
public class Main {
    public static void main(String[] args) {
        if (!Config.loadSuccess) return;

        Set<String> nameSet = Config.ROOM_MAP.keySet();

        for (String name : nameSet) {
            new Thread(new CrawlerThread(name, Config.ROOM_MAP.get(name)), "Crawler-"+name).start();
        }
    }
}
