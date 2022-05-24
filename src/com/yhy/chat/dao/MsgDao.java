package com.yhy.chat.dao;

import com.yhy.chat.model.User;
import com.yhy.chat.model.msg.ChatMsg;
import com.yhy.chat.model.msg.Msg;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 杨海勇
 * 聊天记录存储数据库---未完成
 **/
public class MsgDao extends BaseDao {
    //查询函数
    public List<Msg> getMsg(ChatMsg msg) {
        Connection conn = getConnection();
        List<Msg> list = new ArrayList<>();
        String sql = "select * from chatmsg where user_id1 = ? and user_id2 = ?";
        PreparedStatement ps = this.prepare(conn, sql);
        ResultSet rs = null;
        try {
            ps.setInt(1, msg.getUser().getId());
            ps.setInt(2, msg.getTargetUser().getId());
            rs = ps.executeQuery();
            if (rs.next()) {
                ChatMsg u = new ChatMsg();
                u.getUser().setId(rs.getInt("user_id1"));
                u.getTargetUser().setId(rs.getInt("user_id2"));
                u.setBuf(rs.getBytes("content"));
                u.setDate(rs.getDate("sendtime"));
                list.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(ChatMsg msg) {
        Connection conn = getConnection();
        String sql = "insert into ChatMsg values (?,'',?,?)";
        PreparedStatement ps = this.prepare(conn, sql);
        try {
            ps.setInt(1, msg.getUser().getId());
            ps.setInt(2, msg.getTargetUser().getId());
            ps.setBytes(3, msg.getBuf());
            ps.setDate(4, (Date) msg.getDate());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(ps);
            close(conn);
        }
    }

    public boolean removeMsg(ChatMsg msg1, ChatMsg msg2) {
        Connection conn = getConnection();
        String sql = "delete from chatmsg where ( user_id1 = ? and user_id2 = ? ) or ( user_id2 = ? and user_id1 = ? )";
        PreparedStatement ps = this.prepare(conn, sql);
        try {
            ps.setInt(1, msg1.getUser().getId());
            ps.setInt(2, msg2.getUser().getId());
            ps.setInt(3, msg1.getUser().getId());
            ps.setInt(4, msg2.getUser().getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(ps);
            close(conn);
        }
    }
}