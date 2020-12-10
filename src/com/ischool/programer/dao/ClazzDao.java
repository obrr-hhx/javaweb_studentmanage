package com.ischool.programer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ischool.programer.model.Clazz;
import com.ischool.programer.model.Page;
import com.ischool.programer.util.StringUtil;

/**
 * 
 * @author hndsnhuang
 *班级信息封装操作
 */
public class ClazzDao extends BaseDao {
	public List<Clazz> getClazzList(Clazz clazz, Page page){
		List<Clazz> ret = new ArrayList<Clazz>();
		String sql = "select * from s_class ";
		if(!StringUtil.isEmpty(clazz.getName())) {
			sql += "and name like '%"+clazz.getName()+"%' ";
		}
		if(clazz.getId() != 0) {
			sql += "and id = "+clazz.getId()+" ";
		}
		sql += "limit "+page.getStart()+","+page.getPageSize()+";";
		ResultSet resultSet = query(sql.replaceFirst("and", "where"));
		try {
			while(resultSet.next()) {
				Clazz cl = new Clazz();
				cl.setId(resultSet.getInt("id"));
				cl.setName(resultSet.getString("name"));
				//cl.setGradeId(resultSet.getString("grade"));
				cl.setInfo(resultSet.getString("info"));
				ret.add(cl);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public int getClazzListTotal(Clazz clazz){
		int total = 0;
		String sql = "select count(*) as total from s_class ";
		if(!StringUtil.isEmpty(clazz.getName())) {
			sql += "where name like '%"+clazz.getName()+"%'";
		}
		ResultSet resultSet = query(sql);
		try {
			while(resultSet.next()) {
				Clazz cl = new Clazz();
				total = resultSet.getInt("total");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return total;
		
	}
	
	public boolean addClazz(Clazz clazz) {
		String sql = "insert into s_class(`name`,`info`) values( '"+clazz.getName()+"','"+clazz.getInfo()+"')";
		return update(sql);
	}
	
	public boolean deleteClazz(int id) {
		String sql = "delete from s_class where id = "+id+";";
		return update(sql);
	}
	
	public boolean editClazz(int id, String name, String info) {
		String sql = "update s_class set name = '"+name+"', info = '"+info+"' where id = "+id+";";
		return update(sql);
	}
}
