package com.ischool.programer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ischool.programer.model.Page;
import com.ischool.programer.model.Student;
import com.ischool.programer.util.StringUtil;
/**
 * 
 * @author hndsnhuang
 *学生封装操作
 */
public class StudentDao extends BaseDao{
	
	public Student login(String name, String password) {
		String sql = "select * from s_student where s_num = '"+name+"' and password = '"+password+"'";
		ResultSet resultset = query(sql);
		try {
			if(resultset.next()) {
				Student student = new Student();
				student.setId(resultset.getInt("id"));
				student.setS_num(resultset.getString("s_num"));
				student.setName(resultset.getString("name"));
				student.setPassword(resultset.getString("password"));
				student.setGender(resultset.getString("gender"));
				student.setPhoneNum(resultset.getString("phoneNum"));
				student.setQq(resultset.getString("qq"));
				student.setClazzId(resultset.getInt("clazzId"));
				student.setClazzName(resultset.getString("clazzName"));
				student.setPhoto(resultset.getBinaryStream("photo"));
				return student;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean setStudntPhoto(Student student) {
		String sql = "update s_student set photo = ? where id = ?";
		Connection connection = getConnection();
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(sql);
			prepareStatement.setBinaryStream(1, student.getPhoto());
			prepareStatement.setInt(2, student.getId());
			return prepareStatement.executeUpdate() > 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return update(sql);
	}
	
	public boolean deleteStudent(Student student) {
		String sql = "delete from s_student where id = "+student.getId();
		return update(sql);
	}
	
	public boolean addStudent(Student student) {
		String sql = "insert into s_student(`s_num`,`name`,`password`,`gender`,`phoneNum`,`qq`,`clazzId`,`clazzName`) values( '"+student.getS_num()+"','"+student.getName()+"','"+student.getPassword()+"','"+student.getGender()+"','"+student.getPhoneNum()+"','"+student.getQq()+"','"+student.getClazzId()+"','"+student.getClazzName()+"')";
		return update(sql);
	}
	
	public boolean editStudnt(Student student) {
		String sql = "update s_student set name = '"+student.getName()+"', gender = '"+student.getGender()+"', phoneNum = '"+student.getPhoneNum()+"', qq = '"+student.getQq()+"', clazzName = '"+student.getClazzName()+"', clazzId = "+student.getClazzId()+" where s_num = '"+student.getS_num()+"';";
		return update(sql);
	}
	
	public boolean editPassword(Student student) {
		String sql = "update s_student set password = '"+student.getPassword()+"' where id = "+student.getId()+";";
		return update(sql);
	}
	
	public String getClazzName(int clazzId) throws SQLException {
		String sql = "select * from s_class where id = "+clazzId+";";
		ResultSet resultSet = query(sql); 
		if(resultSet.next()){
			return resultSet.getString("name");
		}else {
			return null;
		}
	}
		
	public Student getStudent(int id) {
		String sql = "select * from s_student where id ="+id+";";
		Student student = null;
		ResultSet resultSet = query(sql);
		try {
			if(resultSet.next()) {
				student = new Student();
				student.setId(resultSet.getInt("id"));
				student.setName(resultSet.getString("name"));
				student.setS_num(resultSet.getString("s_num"));
				student.setGender(resultSet.getString("gender"));
				student.setPhoneNum(resultSet.getString("phoneNum"));
				student.setQq(resultSet.getString("qq"));
				student.setClazzId(resultSet.getInt("clazzId"));
				student.setClazzName(getClazzName(resultSet.getInt("clazzId")));
				student.setPhoto(resultSet.getBinaryStream("photo"));
				return student;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return student;
	}
	
	public List<Student> getStudentList(Student student, Page page){
		List<Student> ret = new ArrayList<Student>();
		String sql = "select * from s_student ";
		if(!StringUtil.isEmpty(student.getName())) {
			sql += "and name like '%"+student.getName()+"%' ";
		}
		if(student.getClazzId() != 0) {
			sql += "and clazzId = "+student.getClazzId()+" ";
		}
		if(student.getId() != 0) {
			sql += "and id = "+student.getId()+" ";
		}
		sql += "limit "+page.getStart()+","+page.getPageSize()+";";
		ResultSet resultSet = query(sql.replaceFirst("and", "where"));
		try {
			while(resultSet.next()) {
				Student std = new Student();
				std.setId(resultSet.getInt("id"));
				std.setS_num(resultSet.getString("s_num"));
				std.setName(resultSet.getString("name"));
				std.setPassword(resultSet.getString("password"));
				std.setGender(resultSet.getString("gender"));
				std.setPhoneNum(resultSet.getString("phoneNum"));
				std.setQq(resultSet.getString("qq"));
				std.setClazzId(resultSet.getInt("clazzId"));
				StudentDao studentDao = new StudentDao();
				std.setClazzName(studentDao.getClazzName(resultSet.getInt("clazzId")));
				studentDao.closeConnect();
				ret.add(std);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public int getStudentListTotal(Student student){
		int total = 0;
		String sql = "select count(*) as total from s_student ";
		if(!StringUtil.isEmpty(student.getName())) {
			sql += "and name like '%"+student.getName()+"%' ";
		}
		if(student.getClazzId() != 0) {
			sql += "and clazzId = "+student.getClazzId()+" ";
		}
		if(student.getId() != 0) {
			sql += "and id = "+student.getId()+" ";
		}
		ResultSet resultSet = query(sql.replaceFirst("and", "where"));
		try {
			while(resultSet.next()) {
				total = resultSet.getInt("total");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return total;
	}
}
