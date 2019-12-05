package test;

import util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionTest {
    public static void main(String[] args) throws SQLException {
        //模拟异常，使no 字段变为唯一字段
        //ALTER TABLE school ADD UNIQUE(no);
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = JdbcHelper.getConn();
            //关闭自动提交
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement("INSERT INTO school (description,no,remarks) VALUES (?,?,?)");
            preparedStatement.setString(1,"土木工程");
            preparedStatement.setString(2,"02");
            preparedStatement.setString(3,"");
            //执行第一条预编译语句
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("INSERT INTO school (description,no,remarks) VALUES (?,?,?)");
            preparedStatement.setString(1,"艺术");
            preparedStatement.setString(2,"22");
            preparedStatement.setString(3,"best");
            preparedStatement.executeUpdate();
            //提交命令，真正写入数据库
            connection.commit();
        }

        catch (SQLException e){
            System.out.println(e.getMessage()+"\n errorCode = "+ e.getErrorCode());
            try {
                //回滚当前连接所做的操作
                if(connection !=null){
                    connection.rollback();
                }
            }
            catch (SQLException e1){
                e1.printStackTrace();
            }
        }

        finally {
            try {
                //恢复自动提交
                if (connection!=null){
                    connection.setAutoCommit(true);
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
            //关闭
            JdbcHelper.close(preparedStatement,connection);
        }
    }
}
