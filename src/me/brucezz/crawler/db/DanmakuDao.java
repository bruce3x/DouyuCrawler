package me.brucezz.crawler.db;

import me.brucezz.crawler.bean.Danmaku;
import me.brucezz.crawler.util.DBUtil;
import me.brucezz.crawler.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brucezz on 2016/01/06.
 * DouyuCrawler
 */
public class DanmakuDao {

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS Danmaku(_id INT PRIMARY KEY AUTO_INCREMENT,uid INT NOT NULL,snick VARCHAR(64) NOT NULL,content VARCHAR(256) NOT NULL,date DATETIME NOT NULL, rid INT NOT NULL );";

    private static final String SQL_INSERT_DANMAKU = "INSERT INTO Danmaku(uid,snick, content, date,rid) VALUES(%d, '%s', '%s', '%s', %d) ";

    public static void createTable() {
        DBUtil.execSQL(SQL_CREATE_TABLE);
    }

    /**
     * 保存弹幕数据到数据库
     */
    public static boolean saveDanmaku(List<Danmaku> danmakuList) {
        List<String> sqlList = new ArrayList<>();
        for (Danmaku danmaku : danmakuList) {
            sqlList.add(String.format(
                    SQL_INSERT_DANMAKU,
                    danmaku.getUid(),
                    danmaku.getSnick(),
                    danmaku.getContent(),
                    DateUtil.datetime(danmaku.getDate()),
                    danmaku.getRid())
            );
        }
        return DBUtil.execSQL(sqlList);
    }

    public static void main(String[] args) {
        //测试
        List<Danmaku> danmakus = new ArrayList<>();
        danmakus.add(new Danmaku(99999999, "X", "X", 9999));
        danmakus.add(new Danmaku(99999999, "XX", "XX", 9999));
        danmakus.add(new Danmaku(99999999, "XX", "XX", 9999));

        saveDanmaku(danmakus);
    }
}
