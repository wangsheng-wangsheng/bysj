package cn.edu.sdjzu.xg.bysj.service;


import cn.edu.sdjzu.xg.bysj.dao.SchoolDao;
import cn.edu.sdjzu.xg.bysj.domain.School;

import java.sql.SQLException;
import java.util.Collection;

public final class SchoolService {
	private static SchoolDao schoolDao= SchoolDao.getInstance();
	private static SchoolService schoolService=new SchoolService();
	
	
	public static SchoolService getInstance(){
		return schoolService;
	}

	public Collection<School> findAll() throws SQLException {
		return schoolDao.findAll();
	}
	
	public School find(Integer id) throws SQLException {
		return schoolDao.find(id);
	}

	public boolean update(School school) throws SQLException {
		return schoolDao.update(school);
	}
	
	public boolean add(School school) throws SQLException {
		return schoolDao.add(school);
	}

	public boolean delete(Integer id)throws SQLException{
		return SchoolDao.getInstance().delete(id);
	}
	
//	public boolean delete(School school_json){
//		//获得所有下一级单位（Department）
//		Collection<Department> departmentSet =
//				DepartmentService.getInstance().getAll(school_json);
//		//若没有二级单位，则能够删除
//		if(departmentSet.size()==0){
//			return schoolDao.delete(school_json);
//		}else {
//			return false;
//		}
//	}

//	public String delete(School school)throws SQLException {
//		//获得所有下一级单位（Department）
//		Collection<Department> departmentSet =
//				DepartmentService.getInstance().getAll(school);
//		//若没有二级单位，则能够删除
//		if(departmentSet.size()==0){
//			schoolDao.delete(school);
//			return "DELETED";
//		}else {
//			return "{\"result\":\"having departments\"}";
//		}
//	}
}
