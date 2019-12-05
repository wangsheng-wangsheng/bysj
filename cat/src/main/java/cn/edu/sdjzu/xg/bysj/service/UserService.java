package cn.edu.sdjzu.xg.bysj.service;

import cn.edu.sdjzu.xg.bysj.dao.UserDao;
import cn.edu.sdjzu.xg.bysj.domain.User;

import java.sql.Connection;
import java.sql.SQLException;

public final class UserService {
    private static UserDao userDao = UserDao.getInstance();
    private static cn.edu.sdjzu.xg.bysj.service.UserService UserService=new UserService();
    public static cn.edu.sdjzu.xg.bysj.service.UserService getInstance(){return UserService;}

    //修改密码
    public boolean changePassword(User user,String oldPassword,String newPassword) throws SQLException {
        return userDao.changePassword(user,oldPassword,newPassword);
    }
    //登陆
    public User login(String username,String password) throws SQLException {
        return userDao.login(username,password);
    }
    public boolean add(User user) throws SQLException {
        return userDao.add(user);
    }
    public boolean add(Connection connection,User user) throws SQLException {
        return userDao.add(connection,user);
    }
    public boolean delete(Integer teacher_id) throws SQLException {
        return userDao.delete(teacher_id);
    }
    public User find(Integer id) throws SQLException {
        return userDao.find(id);
    }

}
