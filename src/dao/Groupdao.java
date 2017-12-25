package dao;

import db.DButil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Groupdao {
    /**
     * 通过群号获取群名
     */
    public static String getgoupename(String number) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "SELECT groupname FROM t_group WHERE groupnumber = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,number);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("groupname");
        }else{
            return new String("error");
        }
    }

    /**
     *通过群名获取群号
     */
    public static String getgroupnumber(String name) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "SELECT groupnumber FROM t_group WHERE groupname = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,name);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("groupnumber");
        }else{
            return new String("error");
        }
    }

    /**
     * 获取群成员
     */
    public static String getMembers(String groupnumber) throws SQLException {
        String members = new String();
        Connection conn = DButil.getConn();
        String sql = "SELECT users FROM t_group WHERE groupnumber = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,groupnumber);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            return rs.getString("users");
        }else{
            return "error";
        }
    }
}
