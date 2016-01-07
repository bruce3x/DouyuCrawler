package me.brucezz.crawler.db;

import me.brucezz.crawler.util.LogUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBUtil {

    private static final String JDBC_MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_NAME = "DouyuCrawler";
    private static final String CONNECTION_URL = "jdbc:mysql://localhost/" + DB_NAME
            + "?useUnicode=true&characterEncoding=utf8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";


    private static Connection conn;

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
        if (conn == null)
            conn = DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD);

        return conn;
    }


    public static void execSQL(String sql) {
        List<String> sqlList = new ArrayList<>();
        sqlList.add(sql);
        execSQL(sqlList);
    }

    public static void execSQL(List<String> sqlList) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();

            for (String sql : sqlList) {
                stmt = conn.prepareStatement(sql);
                stmt.execute();
                LogUtil.d("DB", "Execute SQL statement [" + sql + "]");
            }
        } catch (SQLException e) {
            LogUtil.w(e.toString());
        } finally {
            if (stmt != null)
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LogUtil.w(e.toString());
                }
        }
    }

    public static void main(String[] args) {
        // 测试
        try {
            if (getConnection() != null)
                System.out.println("success");
//            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
