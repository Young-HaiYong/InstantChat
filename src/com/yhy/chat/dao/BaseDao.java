package com.yhy.chat.dao;

import java.sql.*;

/**
 * @author: 杨海勇
 * 数据库连接等功能
 **/
public abstract class BaseDao {
    //数据库连接属性
    public static final String CLASS_NAME = "com.mysql.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost:3306/chat";
    public static final String USER_NAME = "root";
    public static final String USER_PASSWORD = "1234";

    //静态代码块自动加载
    static{
        try {
            Class.forName(CLASS_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //加载驱动
    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER_NAME, USER_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public PreparedStatement prepare(Connection conn, String sql) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ps;
    }

    public void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void close(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
