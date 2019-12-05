package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.ProfTitle;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;


public final class ProfTitleDao {
	private static ProfTitleDao profTitleDao=new ProfTitleDao();
	private ProfTitleDao(){}
	public static ProfTitleDao getInstance(){
		return profTitleDao;
	}

	//数据库数据获取
	public Collection<ProfTitle> findAll() throws SQLException {
		Collection<ProfTitle> profTitles = new HashSet<>();
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from proftitle");
		while (resultSet.next()){
			profTitles.add(new ProfTitle(resultSet.getInt("id"),resultSet.getString("description"),
					resultSet.getString("no"),resultSet.getString("remarks")));
		}
		JdbcHelper.close(stmt,connection);
		return profTitles;
	}
	//查询
	public ProfTitle find(Integer id) throws SQLException {
		Connection connection  = JdbcHelper.getConn();
		String findProfTitle_sql = "select * from proftitle where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(findProfTitle_sql);
		pstmt.setInt(1,id);
		//返回结果集
		ResultSet resultSet = pstmt.executeQuery();
		//获取第二行
		resultSet.next();
		ProfTitle profTitle = new ProfTitle(
				resultSet.getInt("id"),
				resultSet.getString("description"),
				resultSet.getString("no"),
				resultSet.getString("remarks")
		);
		return profTitle;
	}
	//修改
	public boolean update(ProfTitle profTitle) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String updateProfTitle_sql = "update proftitle set description=?,no=?,remarks=? where id=?";
		PreparedStatement preparedStatement = connection.prepareStatement(updateProfTitle_sql);
		preparedStatement.setString(1,profTitle.getDescription());
		preparedStatement.setString(2,profTitle.getNo());
		preparedStatement.setString(3,profTitle.getRemarks());
		preparedStatement.setInt(4,profTitle.getId());
		int affectedRowNum = preparedStatement.executeUpdate();
		return affectedRowNum>0;
	}
	//添加
	public boolean add(ProfTitle profTitle) throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句
		String addProfTitle_sql = "insert into proftitle (no,description,remarks) values (?,?,?)";
		PreparedStatement pstmt = connection.prepareStatement(addProfTitle_sql);
		System.out.println(profTitle.getDescription());
		//为预编译赋值
		pstmt.setString(1,profTitle.getNo());
		pstmt.setString(2,profTitle.getDescription());
		pstmt.setString(3,profTitle.getRemarks());
		//获取添加记录的行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("添加了"+affectedRowNum+"条记录");
		return affectedRowNum>0;
	}
	//删除
	public boolean delete(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		//删除数据
		String deleteProfTitle_sql = "delete from proftitle where id=?";
		PreparedStatement pstmt = connection.prepareStatement(deleteProfTitle_sql);
		//为预编译参数赋值
		pstmt.setInt(1,id);
		//执行预编译对象的executeUpdate方法,获取删除记录的行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("删除了"+affectedRowNum+"条记录");
		return affectedRowNum>0;
	}

}

