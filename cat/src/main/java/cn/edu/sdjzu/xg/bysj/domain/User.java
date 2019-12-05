package cn.edu.sdjzu.xg.bysj.domain;

public class User  implements Comparable<User>{
	private Integer id;
	private String username;
	private String password;
	private Teacher teacher;

	public User(Integer id, String username, String password, Teacher teacher) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.teacher = teacher;
	}

	public User(String username, String password, Teacher teacher) {
		this.username = username;
		this.password = password;
		this.teacher = teacher;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public String toString()
	{
	    final String TAB = "    ";
	    
	    String retValue = "";
	    
	    retValue = "Login ( "
	        + super.toString() + TAB
	        + "id = " + this.id + TAB
	        + "username = " + this.username + TAB
	        + "password = " + this.password + TAB
	        + "teacher = " + this.teacher + TAB
	        + " )";
	
	    return retValue;
	}

	@Override
	public int compareTo(User o) {
		// TODO Auto-generated method stub
		return this.id-o.id;
	}

}
