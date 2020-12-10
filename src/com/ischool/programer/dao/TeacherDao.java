package com.ischool.programer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ischool.programer.model.Page;
import com.ischool.programer.model.Teacher;
import com.ischool.programer.util.StringUtil;

/**
 * 
 * @author hndsnhuang
 *教师封装操作
 */
public class TeacherDao extends BaseDao {
	public Teacher login(String name, String password) {
		String sql = "select * from s_teacher where name = '"+name+"' and password = '"+password+"'";
		ResultSet resultset = query(sql);
		try {
			if(resultset.next()) {
				Teacher teacher = new Teacher();
				teacher.setId(resultset.getInt("id"));
				teacher.setTn(resultset.getString("tn"));
				teacher.setName(resultset.getString("name"));
				teacher.setPassword(resultset.getString("password"));
				teacher.setSex(resultset.getString("sex"));
				teacher.setPhone(resultset.getString("phone"));
				teacher.setQq(resultset.getString("qq"));
				teacher.setClazzId(resultset.getInt("clazzId"));
				teacher.setPhoto(resultset.getBinaryStream("photo"));
				return teacher;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean addTeacher(Teacher teacher) {
		String sql = "insert into s_teacher(`tn`,`name`,`password`,`sex`,`phone`,`qq`,`clazzId`) values( '"+teacher.getTn()+"','"+teacher.getName()+"','"+teacher.getPassword()+"','"+teacher.getSex()+"','"+teacher.getPhone()+"','"+teacher.getQq()+"','"+teacher.getClazzId()+"')";
		return update(sql);
	}
	
	public boolean editTeacher(Teacher teacher) {
		String sql = "update s_teacher set name = '"+teacher.getName()+"', sex = '"+teacher.getSex()+"', phone = '"+teacher.getPhone()+"', qq = '"+teacher.getQq()+"', clazzId = "+teacher.getClazzId()+" where tn = '"+teacher.getTn()+"';";
		return update(sql);
	}
	
	public boolean editPassword(Teacher teacher) {
		String sql = "update s_teacher set password = '"+teacher.getPassword()+"' where id = "+teacher.getId()+";";
		return update(sql);
	}
	
	public boolean deleteStudent(Teacher teacher) {
		String sql = "delete from s_teacher where id = "+teacher.getId();
		return update(sql);
	}
	
	public boolean setTeacherPhoto(Teacher teacher) {
		String sql = "update s_teacher set photo = ? where id = ?";
		Connection connection = getConnection();
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(sql);
			prepareStatement.setBinaryStream(1, teacher.getPhoto());
			prepareStatement.setInt(2, teacher.getId());
			return prepareStatement.executeUpdate() > 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return update(sql);
	}
	
	public List<Teacher> getTeacherList(Teacher teacher, Page page){
		List<Teacher> ret = new ArrayList<Teacher>();
		String sql = "select * from s_teacher ";
		if(!StringUtil.isEmpty(teacher.getName())) {
			sql += "and name like '%"+teacher.getName()+"%' ";
		}
		if(teacher.getClazzId() != 0) {
			sql += "and clazzId = "+teacher.getClazzId()+" ";
		}
		if(teacher.getId() != 0) {
			sql += "and id = "+teacher.getId()+" ";
		}
		sql += "limit "+page.getStart()+","+page.getPageSize()+";";
		ResultSet resultSet = query(sql.replaceFirst("and", "where"));
		try {
			while(resultSet.next()) {
				Teacher teacher1 = new Teacher();
				teacher1.setId(resultSet.getInt("id"));
				teacher1.setTn(resultSet.getString("tn"));
				teacher1.setName(resultSet.getString("name"));
				teacher1.setPassword(resultSet.getString("password"));
				teacher1.setSex(resultSet.getString("sex"));
				teacher1.setPhone(resultSet.getString("phone"));
				teacher1.setQq(resultSet.getString("qq"));
				teacher1.setClazzId(resultSet.getInt("clazzId"));
				ret.add(teacher1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public int getTeacherListTotal(Teacher teacher){
		int total = 0;
		String sql = "select count(*) as total from s_teacher ";
		if(!StringUtil.isEmpty(teacher.getName())) {
			sql += "and name like '%"+teacher.getName()+"%' ";
		}
		if(teacher.getClazzId() != 0) {
			sql += "and clazzId = "+teacher.getClazzId()+" ";
		}
		if(teacher.getId() != 0) {
			sql += "and id = "+teacher.getId()+" ";
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

	public Teacher getTeacher(int id) {
		String sql = "select * from s_teacher where id ="+id+";";
		Teacher teacher = null;
		ResultSet resultSet = query(sql);
		try {
			if(resultSet.next()) {
				teacher = new Teacher();
				teacher.setId(resultSet.getInt("id"));
				teacher.setName(resultSet.getString("name"));
				teacher.setTn(resultSet.getString("tn"));
				teacher.setSex(resultSet.getString("sex"));
				teacher.setPhone(resultSet.getString("phone"));
				teacher.setQq(resultSet.getString("qq"));
				teacher.setClazzId(resultSet.getInt("clazzId"));
				teacher.setPhoto(resultSet.getBinaryStream("photo"));
				return teacher;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return teacher;
	}
}
