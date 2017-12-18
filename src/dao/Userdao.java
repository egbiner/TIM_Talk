package dao;

import db.DButil;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Userdao {
    /**
     * 添加用户
     * @param u
     * @return
     * @throws SQLException
     */
    public static int addUser(User u) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "INSERT INTO user(username,password,email) VALUES (?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, u.getUsername());
        ps.setString(2, u.getPassword());
        ps.setString(3, u.getEmail());
        return ps.executeUpdate();
    }

    /**
     * 传入用户对象对用户进行更新
     * @param u
     * @throws SQLException
     */
    public void updateUser(User u) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "UPDATE  user SET password=? WHERE  username=?";
        //准备
        PreparedStatement ps = conn.prepareStatement(sql);
        //设置参数(对应于数据库的数据类型)
        ps.setString(2, u.getUsername());
        ps.setString(1, u.getPassword());
        //执行
        ps.execute();
    }

    /**
     * 通过username删除用户
     * @param username
     * @throws SQLException
     */
    public void deleteUser(String username) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "DELETE FROM user WHERE username = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ps.execute();
    }
    /**
     * 通过账号获取用户名
     */
    public static String getusernamebyaccount(String account) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "SELECT username FROM user WHERE account = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,account);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("username");
        }else{
            return new String("error");
        }

    }

    /**
     * 返回用户集合
     * @return
     * @throws SQLException
     */
    public List<User> query() throws SQLException {
        Connection conn = DButil.getConn();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM user");
        List<User> getlist = new ArrayList<User>();
        User u = null;
        while (rs.next()) {
            u = new User();
            u.setUsername(rs.getString("username"));
            u.setPassword(rs.getString("password"));
            getlist.add(u);
        }
        return getlist;
    }

    /**
     * 通过username获取用户
     * @param username
     * @return
     * @throws SQLException
     */
    public User get(String username) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "SELECT * FROM user WHERE username=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);

        ResultSet rs = ps.executeQuery();

        User u = new User();
        //不要忘记了加上rs.next
        while (rs.next()) {
            u.setUsername(rs.getString("username"));
            u.setPassword(rs.getString("password"));
        }
        return u;
    }

    /**
     * 用户登录
     * @param u
     * @return
     * @throws SQLException
     */
    public static int login(User u) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "SELECT  * FROM user WHERE account = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, u.getAccount());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String password = rs.getString("password");
            if (password.equals(u.getPassword())) {
                return 1;
            } else {
                return 0;
            }
        }
        return 0;
    }

    /**
     * 用户注册
     * @param username
     * @param password
     * @return
     * @throws SQLException
     */
    public static int Register(String username, String password) throws SQLException {
        Connection conn = DButil.getConn();
        //判断用户名是否重复
        PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM user WHERE username=?");
        ps1.setString(1, username);
        ResultSet rs = ps1.executeQuery();
        while (rs.next()) {
            return -1;
        }
        String sql = "INSERT INTO user (username,password) VALUES (?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        return ps.executeUpdate();
    }
}