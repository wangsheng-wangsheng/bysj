package cn.edu.sdjzu.xg.bysj.dao;

import cn.edu.sdjzu.xg.bysj.domain.Degree;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public final class DegreeDao {
	private static DegreeDao degreeDao=
			new DegreeDao();
	private DegreeDao(){}
	public static DegreeDao getInstance(){
		return degreeDao;
	}
	private static Set<Degree> degrees;

	public Collection<Degree> findAll() throws SQLException {
		degrees = new HashSet<>();
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from Degree");
		//从数据库中取出数据
		while (resultSet.next()){
			degrees.add(new Degree(resultSet.getInt("id"), resultSet.getString("description"),
					resultSet.getString("no"),resultSet.getString("remarks")));
		}
		JdbcHelper.close(stmt,connection);
		return degrees;
	}

	public Degree find(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String findSchool_sql = "select * from degree where id=?";
		PreparedStatement pstmt = connection.prepareStatement(findSchool_sql);
		pstmt.setInt(1,id);
		ResultSet resultSet = pstmt.executeQuery();
		resultSet.next();
		Degree degree = new Degree(
				resultSet.getInt("id"),
				resultSet.getString("description"),
				resultSet.getString("no"),
				resultSet.getString("remarks")
		);
		return degree;
	}

	public boolean update(Degree degree) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String updateDegree_sql = "update degree set description=?,no=?,remarks=? where id=?";
		PreparedStatement preparedStatement = connection.prepareStatement(updateDegree_sql);
		preparedStatement.setString(1,degree.getDescription());
		preparedStatement.setString(2,degree.getNo());
		preparedStatement.setString(3,degree.getRemarks());
		preparedStatement.setInt(4,degree.getId());
		int affectedRowNum = preparedStatement.executeUpdate();
		return affectedRowNum>0;
	}
	

	public boolean add(Degree degree) throws SQLException{
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句
		String addDegree_sql = "insert into Degree (no,description,remarks) values (?,?,?)";
		PreparedStatement pstmt = connection.prepareStatement(addDegree_sql);
		System.out.println(degree.getDescription());
		//为预编译赋值
		pstmt.setString(1,degree.getNo());
		pstmt.setString(2,degree.getDescription());
		pstmt.setString(3,degree.getRemarks());
		//获取添加记录的行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("添加了"+affectedRowNum+"条记录");
		return affectedRowNum>0;
	}

	public boolean delete(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		//删除数据
		String deleteDegree_sql = "delete from degree where id=?";
		PreparedStatement pstmt = connection.prepareStatement(deleteDegree_sql);
		//为预编译参数赋值
		pstmt.setInt(1,id);
		//执行预编译对象的executeUpdate方法,获取删除记录的行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("删除了"+affectedRowNum+"条记录");
		return affectedRowNum>0;
	}

}

