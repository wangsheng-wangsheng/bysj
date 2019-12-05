package cn.edu.sdjzu.xg.bysj.dao;
import cn.edu.sdjzu.xg.bysj.domain.Department;
import cn.edu.sdjzu.xg.bysj.service.SchoolService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

public final class DepartmentDao {
	private static DepartmentDao departmentDao=new DepartmentDao();
	private DepartmentDao(){}
	public static DepartmentDao getInstance(){
		return departmentDao;
	}


	public Collection<Department> findAll() throws SQLException {
		Collection<Department> departments = new HashSet<>();
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from department");
		//从数据库中取出数据
		while (resultSet.next()){
			//System.out.println(resultSet.getString("description"));
			departments.add(new Department(
					resultSet.getInt("id"), resultSet.getString("description"),
					resultSet.getString("no"), resultSet.getString("remarks"),
					SchoolService.getInstance().find(resultSet.getInt("school_id"))));
		}
		JdbcHelper.close(stmt,connection);
		return departments;
	}

	public Department find(Integer id) throws SQLException {
		Connection connection  = JdbcHelper.getConn();
		String findDepartment_sql = "select * from department where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(findDepartment_sql);
		pstmt.setInt(1,id);
		//返回结果集
		ResultSet resultSet = pstmt.executeQuery();
		//获取第二行
		resultSet.next();
		Department department = new Department(
				resultSet.getInt("id"),
				resultSet.getString("description"),
				resultSet.getString("no"),
				resultSet.getString("remarks"),
				SchoolService.getInstance().find(resultSet.getInt("school_id"))
				);
		return department;
	}

	public Collection<Department> findAllBySchool(Integer schoolId) throws SQLException {
		Collection<Department> bySchoolIdDepartments = new HashSet<>();
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("SELECT * FROM department");
		while (resultSet.next()){
			if(resultSet.getInt("school_id")==schoolId){
				bySchoolIdDepartments.add(new Department(
						resultSet.getInt("id"),
						resultSet.getString("description"),
						resultSet.getString("no"),
						resultSet.getString("remarks"),
						SchoolService.getInstance().find(resultSet.getInt("school_id")))
				);
			}
		}
		return bySchoolIdDepartments;
	}
	public boolean update(Department department) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String updateDepartment_sql = "update department set description=?,no=?,remarks=?,school_id=? where id=?";
		PreparedStatement preparedStatement = connection.prepareStatement(updateDepartment_sql);
		preparedStatement.setString(1,department.getDescription());
		preparedStatement.setString(2,department.getNo());
		preparedStatement.setString(3,department.getRemarks());
		preparedStatement.setInt(4,department.getSchool().getId());
		preparedStatement.setInt(5,department.getId());
		int affectedRowNum = preparedStatement.executeUpdate();
		return affectedRowNum>0;
	}

	public boolean add(Department department) throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句
		String addDepartment_sql = "insert into department (no,description,remarks,school_id) values (?,?,?,?)";
		PreparedStatement pstmt = connection.prepareStatement(addDepartment_sql);
		//为预编译赋值
		pstmt.setString(1,department.getNo());
		pstmt.setString(2,department.getDescription());
		pstmt.setString(3,department.getRemarks());
		//获取school对象然后获取他的id
		pstmt.setInt(4,department.getSchool().getId());
		//获取添加记录的行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("添加了"+affectedRowNum+"条记录");
		return affectedRowNum>0;
	}

	public boolean delete(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		//删除数据
		String deleteDepartment_sql = "delete from department where id=?";
		PreparedStatement pstmt = connection.prepareStatement(deleteDepartment_sql);
		//为预编译参数赋值
		pstmt.setInt(1,id);
		//执行预编译对象的executeUpdate方法,获取删除记录的行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("删除了"+affectedRowNum+"条记录");
		return affectedRowNum>0;
	}


//		Department department1 = DepartmentService.getInstance().find(2);
//		System.out.println("Description = "+department1.getDescription());

}

