package me.brucezz.crawler.util;

import me.brucezz.crawler.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brucezz on 2016/01/04.
 * DouyuCrawler
 */
public class DBUtil {

    private static final String DB_NAME = Config.DB_NAME;
    private static final String USERNAME = Config.DB_USERNAME;
    private static final String PASSWORD = Config.DB_PASSWORD;

    private static final String JDBC_MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    private static final String CONNECTION_URL = "jdbc:mysql://localhost/" + DB_NAME
            + "?useUnicode=true&characterEncoding=utf8&useSSL=false";

    private static Connection instance;

    static {
        try {
            Class.forName(JDBC_MYSQL_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 单例模式，获取数据库连接对象
     */
    public static Connection getConnection() throws SQLException {
        if (instance == null)
            instance = DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD);

        return instance;
    }

    /**
     * 执行一条SQL语句
     */
    public static boolean execSQL(String sql) {
        List<String> sqlList = new ArrayList<>();
        sqlList.add(sql);
        return execSQL(sqlList);
    }

    /**
     * 在一个事务里，依次执行一系列SQL
     *
     * @return 如果执行成功返回 true, 否则 false
     */
    public static boolean execSQL(List<String> sqlList) {
        PreparedStatement stmt = null;
        Connection conn = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            for (String sql : sqlList) {
                stmt = conn.prepareStatement(sql);
                stmt.execute();
                LogUtil.d("DB", "Execute SQL statement [" + sql + "]");
            }

            conn.commit();
            //执行成功
            return true;

        } catch (SQLException e) {
            LogUtil.e(e.toString());
            if (conn != null)
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    LogUtil.e(e1.toString());
                }

        } finally {
            if (stmt != null)
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LogUtil.e(e.toString());
                }
        }
        //执行失败
        return false;
    }

    public static void main(String[] args) {
        // 测试
        List<String> sqls = new ArrayList<>();
        sqls.add("INSERT INTO Danmaku(uid,snick, content, date,rid) VALUES(99999999, 'XXxX', 'XXxX', '2016-1-1', 88888888);");
        sqls.add("INSERT INTO Danmaku(uid,snick, content, date,rid) VALUES(99999999, 'XXxxX', 'XXxxX', '2016-1-1', 88888888);");
        sqls.add("INSERT INTO Danmaku(uid,snick, content, date,rid) VALUES(99999999, 'XXxxxxX', 'XXxxxxX', '2016-1-1', 88888888);");

        execSQL(sqls);
        System.out.println("done!");
    }
}
