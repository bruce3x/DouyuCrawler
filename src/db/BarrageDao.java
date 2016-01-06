package db;

import model.Barrage;
import util.LogUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by zero on 2016/01/06.
 * Douyu
 */
public class BarrageDao {
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS Barrage(_id INT PRIMARY KEY AUTO_INCREMENT,uid INT NOT NULL,snick VARCHAR(64) NOT NULL,content VARCHAR(256) NOT NULL,date DATETIME NOT NULL, rid INT NOT NULL );";

    private static final String SQL_INSERT_BARRAGE = "INSERT INTO Barrage(uid,snick, content, date,rid) VALUES(?, ?, ?, ?, ?) ";

    public static void createTable() {
        DBUtil.execSQL(SQL_CREATE_TABLE);
    }

    public static boolean saveBarrage(List<Barrage> barrages) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBUtil.getConnection();

            conn.setAutoCommit(false);

            for (Barrage barrage : barrages) {


                try {
                    // TODO: 16-1-7 整合到 DBUtil.execSQL()里

                    stmt = conn.prepareStatement(SQL_INSERT_BARRAGE);
                    stmt.setInt(1, barrage.getUid());
                    stmt.setString(2, barrage.getSnick());
                    stmt.setString(3, barrage.getContent());
                    stmt.setTimestamp(4, new Timestamp(barrage.getDate().getTime()));
                    stmt.setInt(5, barrage.getRid());
                    stmt.executeUpdate();
                    LogUtil.d("DB", "Execute SQL statement [" + SQL_INSERT_BARRAGE + "]");
                } catch (SQLException ignored) {
                    LogUtil.d("DB", "插入失败，跳过： " + barrage);
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null)
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    LogUtil.d(e1.toString());
                }

            LogUtil.d(e.toString());
        } finally {
            if (stmt != null)
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LogUtil.w(e.toString());
                }
        }

        return false;
    }


}
