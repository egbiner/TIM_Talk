package dao;

import db.DButil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Statisticdao {
    /**
     * 获取注册用户的数量
     * @return
     * @throws SQLException
     */
    public static int getRigisters() throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "SELECT COUNT(*) FROM user ";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            return Integer.parseInt(rs.getString("COUNT(*)"));
        }else{
            return  -1;
        }
    }

    /**
     * 获取发送信息数目
     * @return
     * @throws SQLException
     */
    public static int getMsgcount() throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "SELECT Msgcount FROM T_Statistic";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            return Integer.parseInt(rs.getString("Msgcount"));
        }else {
            return -1;
        }
    }
}
