package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.Teacher;
import cn.edu.sdjzu.xg.bysj.domain.User;
import cn.edu.sdjzu.xg.bysj.service.DegreeService;
import cn.edu.sdjzu.xg.bysj.service.DepartmentService;
import cn.edu.sdjzu.xg.bysj.service.ProfTitleService;
import cn.edu.sdjzu.xg.bysj.service.UserService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

public final class TeacherDao {
	private static TeacherDao teacherDao=new TeacherDao();
	public static TeacherDao getInstance(){
		return teacherDao;
	}

	public Collection<Teacher> findAll() throws SQLException {
		Collection<Teacher> teachers = new HashSet<>();
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from teacher");
		//从数据库中取出数据
		while (resultSet.next()){
			teachers.add(new Teacher(
					resultSet.getInt("id"),
					resultSet.getString("name"),
					resultSet.getString("no"),
					ProfTitleService.getInstance().find(resultSet.getInt("proftitle_id")),
					DegreeService.getInstance().find(resultSet.getInt("degree_id")),
					DepartmentService.getInstance().find(resultSet.getInt("department_id"))
			));
		}
		JdbcHelper.close(stmt,connection);
		return teachers;
	}
	
	public Teacher find(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String findTeacher_sql = "select * from teacher where id=?";
		PreparedStatement pstmt = connection.prepareStatement(findTeacher_sql);
		pstmt.setInt(1,id);
		ResultSet resultSet = pstmt.executeQuery();
		Teacher teacher = null;
		while (resultSet.next()){
            teacher = new Teacher(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("no"),
                    ProfTitleService.getInstance().find(resultSet.getInt("proftitle_id")),
                    DegreeService.getInstance().find(resultSet.getInt("degree_id")),
                    DepartmentService.getInstance().find(resultSet.getInt("department_id")));

        }
		return teacher;
	}
	
	public boolean update(Teacher teacher) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String updateTeacher_sql = "update teacher set name=?,no=?,proftitle_id=?,degree_id=?,department_id=? where id=?";
		PreparedStatement preparedStatement = connection.prepareStatement(updateTeacher_sql);
		preparedStatement.setString(1,teacher.getName());
		preparedStatement.setString(2,teacher.getNo());
		preparedStatement.setInt(3,teacher.getTitle().getId());
		preparedStatement.setInt(4,teacher.getDegree().getId());
		preparedStatement.setInt(5,teacher.getDepartment().getId());
		preparedStatement.setInt(6,teacher.getId());
		int affectedRowNum = preparedStatement.executeUpdate();
		return affectedRowNum>0;
	}
	
	public boolean add(Teacher teacher){
		Connection connection = null;
		PreparedStatement pstmt = null;
		int affectedRowNum = 0;
		try {
			connection = JdbcHelper.getConn();
			connection.setAutoCommit(false);
			//添加教师对象
			String addTeacher_sql = "insert into teacher (name,no,proftitle_id, degree_id, department_id) values (?,?,?,?,?)";
			pstmt = connection.prepareStatement(addTeacher_sql);
			pstmt.setString(1,teacher.getName());
			pstmt.setString(2,teacher.getNo());
			pstmt.setInt(3,teacher.getTitle().getId());
			pstmt.setInt(4,teacher.getDegree().getId());
			pstmt.setInt(5,teacher.getDepartment().getId());
			pstmt.executeUpdate();
			pstmt = connection.prepareStatement("select * from teacher where id=(select max(id) from teacher)");
			ResultSet resultSet = pstmt.executeQuery();
			resultSet.next();
			int teacherID=resultSet.getInt("id");
			teacher.setId(teacherID);
			User user = new User(
					resultSet.getString("no"),
					resultSet.getString("no"),
					teacher
					);
		 	UserService.getInstance().add(connection,user);
		}catch (SQLException e){
			e.printStackTrace();
			try {
				//回滚当前连接所做的操作
				if(connection !=null){
					connection.rollback();
				}
			}
			catch (SQLException e1){
				e1.printStackTrace();
			}
		} finally {
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
			JdbcHelper.close(pstmt,connection);
		}
		return affectedRowNum>0;
	}

	public boolean delete(Integer id){
		Connection connection = null;
		PreparedStatement pstmt = null;
		int affectedRowNum = 0;
		try {
			connection = JdbcHelper.getConn();
			connection.setAutoCommit(false);
			//根据参照完整性规则必须先删除教师的账号信息才能删除教师
			//删除教师所对应的账号信息
			UserService.getInstance().delete(id);
			//删除教师
			pstmt = connection.prepareStatement("delete from teacher where id=?");
			pstmt.setInt(1,id);
			affectedRowNum = pstmt.executeUpdate();
			System.out.println("已经删除教师...........................");

		}catch (SQLException e){
			e.printStackTrace();
			try {
				//回滚当前连接所做的操作
				if(connection !=null){
					connection.rollback();
				}
			} catch (SQLException e1){
				e1.printStackTrace();
			}
		} finally {
			try {
				//恢复自动提交
				if (connection!=null){
					connection.setAutoCommit(true);
				}
			}
			catch (SQLException e){
				e.printStackTrace();
			}
			JdbcHelper.close(pstmt,connection);
		}
		return affectedRowNum>0;
	}
	
}
