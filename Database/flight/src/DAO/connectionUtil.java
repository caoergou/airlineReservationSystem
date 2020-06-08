package DAO;

import java.sql.*;


public class connectionUtil {

    //数据库相关配置
    //以下为 MySQL 8.0以上版本的驱动
    private static final String driver = "com.mysql.cj.jdbc.Driver";

    //以下为 MySQL 8.0以下版本的驱动
    //private static final String driver = "com.mysql.jdbc.Driver";

    private static final String url = "jdbc:mysql://localhost:3306/ travel?serverTimezone=UTC";
    private static final String user = "root";
    private static final String password = "12345678";//"Input Your MySQL Password Here";

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        return DriverManager.getConnection(url, user, password);
    }

    public static void closeAll(Statement st, Connection conn) {
    //先产生的后关闭，后产生的先关闭
        if (st != null)
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (conn != null)
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
            }
    }
}

