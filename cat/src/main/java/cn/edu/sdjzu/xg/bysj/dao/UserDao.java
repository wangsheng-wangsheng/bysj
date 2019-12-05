package cn.edu.sdjzu.xg.bysj.dao;

import cn.edu.sdjzu.xg.bysj.domain.User;
import cn.edu.sdjzu.xg.bysj.service.TeacherService;
import cn.edu.sdjzu.xg.bysj.service.UserService;
import util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class UserDao {
    private static UserDao userDao = new UserDao();
    public static UserDao getInstance(){ return userDao;}

    public boolean changePassword(User user, String oldPassword, String newPassword) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        String changePassword_str = "update user set password=? where id=?";
        PreparedStatement pstmt = connection.prepareStatement(changePassword_str);
        int affectedRowNum = 0;
        if (user.getPassword().equals(oldPassword)) {
            pstmt.setString(1,newPassword);
            pstmt.setInt(2,user.getId());
            affectedRowNum = pstmt.executeUpdate();
        }
        return affectedRowNum>0;
    }

    public User login(String username, String password) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        User user = null;
        PreparedStatement pstmt = connection.prepareStatement("select * from user where username=? and password=?");
        pstmt.setString(1,username);
        pstmt.setString(2,password);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next())
            user = UserService.getInstance().find(resultSet.getInt("id"));
        return user;
    }

    public boolean add(User user) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        PreparedStatement pstmt = connection.prepareStatement("insert into user (username, password,teacher_id) values (?,?,?)");
        pstmt.setString(1,user.getUsername());
        pstmt.setString(2,user.getPassword());
        pstmt.setInt(3,user.getTeacher().getId());
        int affectedRowNum  = pstmt.executeUpdate();
        return affectedRowNum>0;
    }
    public boolean add(Connection connection, User user) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("insert into user (username, password,teacher_id) values (?,?,?)");
        pstmt.setString(1,user.getUsername());
        pstmt.setString(2,user.getPassword());
        pstmt.setInt(3,user.getTeacher().getId());
        int affectedRowNum  = pstmt.executeUpdate();
        return affectedRowNum>0;

    }

    public boolean delete(Integer teacher_id) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        PreparedStatement pstmt = connection.prepareStatement("delete from user where teacher_id=?");
        pstmt.setInt(1,teacher_id);
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("已经删除教师账户信息...................");
        return affectedRowNum>0;
    }

    public User findByUsername(String username) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        String findUser_sql = "select * from user where username = ?";
        PreparedStatement pstmt = connection.prepareStatement(findUser_sql);
        pstmt.setString(1,username);
        ResultSet resultSet = pstmt.executeQuery();
        resultSet.next();
        User user = new User(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                TeacherService.getInstance().find(resultSet.getInt("teacher_id"))
        );
        return user;
    }

    public User find(Integer id) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        String findUser_sql = "select * from user where id = ?";
        PreparedStatement pstmt = connection.prepareStatement(findUser_sql);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        resultSet.next();
        User user = new User(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                TeacherService.getInstance().find(resultSet.getInt("teacher_id"))
                );
        return user;
    }
}
