package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.School;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class SchoolDao {
	private static SchoolDao schoolDao = new SchoolDao();
	private static Set<School> schools;
	
	public SchoolDao(){}
	
	public static SchoolDao getInstance(){
		return schoolDao;
	}

	public Collection<School> findAll() throws SQLException {
		schools = new HashSet<>();
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet  = stmt.executeQuery("select * from School");
		while (resultSet.next()){
			schools.add(new School(resultSet.getInt("id"),resultSet.getString("description"),
					resultSet.getString("no"),resultSet.getString("remarks")));
		}

		return SchoolDao.schools;
	}
	
	public School find(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String findSchool_sql = "select * from school where id=?";
		PreparedStatement pstmt = connection.prepareStatement(findSchool_sql);
		pstmt.setInt(1,id);
		ResultSet resultSet = pstmt.executeQuery();
		resultSet.next();
		School school = new School(
				resultSet.getInt("id"),
				resultSet.getString("description"),
				resultSet.getString("no"),
				resultSet.getString("remarks")
		);
		return school;
	}
	
	public boolean update(School school) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String updateSchool_sql = "update school set description=?,no=?,remarks=? where id=?";
		PreparedStatement preparedStatement = connection.prepareStatement(updateSchool_sql);
		preparedStatement.setString(1,school.getDescription());
		preparedStatement.setString(2,school.getNo());
		preparedStatement.setString(3,school.getRemarks());
		preparedStatement.setInt(4,school.getId());
		int affectedRowNum = preparedStatement.executeUpdate();
		return affectedRowNum>0;
	}

//    public School addWithSP(School school) throws SQLException{
//        //获得连接对象
//        Connection connection = JdbcHelper.getConn();
//        //根据连接对象准备可调用语句对象，sp_addSchool为存贮过程名称，后面为4个参数
//        CallableStatement callableStatement= connection.prepareCall("{CALL sp_addSchool(?,?,?,?)}");
//        //将第4个参数设置为输出参数，类型为长整型（数据库的数据类型）
//        callableStatement.registerOutParameter(4, Types.BIGINT);
//        callableStatement.setString(1,school.getDescription());
//        callableStatement.setString(2,school.getNo());
//        callableStatement.setString(3,school.getRemarks());
//        //执行可调用语句callableStatement
//        callableStatement.execute();
//        //获得第5个参数的值：数据库为该记录自动生成的id
//        int id = callableStatement.getInt(4);
//        //为参数school的id字段赋值
//        school.setId(id);
//        JdbcHelper.close(callableStatement,connection);
//        return school;
//    }

	public boolean add(School school) throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句
		String addSchool_sql = "insert into school (no,description,remarks) values (?,?,?)";
		PreparedStatement pstmt = connection.prepareStatement(addSchool_sql);
		//为预编译赋值
		pstmt.setString(1,school.getNo());
		pstmt.setString(2,school.getDescription());
		pstmt.setString(3,school.getRemarks());
		//获取添加记录的行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("添加了"+affectedRowNum+"条记录");
		return affectedRowNum>0;
	}
	
	public boolean delete(int id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		//删除数据
		String deleteSchool_sql = "delete from school where id=?";
		PreparedStatement pstmt = connection.prepareStatement(deleteSchool_sql);
		//为预编译参数赋值
		pstmt.setInt(1,id);
		//执行预编译对象的executeUpdate方法,获取删除记录的行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("删除了"+affectedRowNum+"条记录");
		return affectedRowNum>0;
	}
}
