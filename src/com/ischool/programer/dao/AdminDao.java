package com.ischool.programer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ischool.programer.model.Admin;

/**
 * 
 * @author hndsnhuang
 *管理员操作封装
 */
public class AdminDao extends BaseDao{
	public Admin login(String name, String password) {
		String sql = "select * from s_admin where name = '"+name+"' and password = '"+password+"'";
		ResultSet resultset = query(sql);
		try {
			if(resultset.next()) {
				Admin admin = new Admin();
				admin.setId(resultset.getInt("id"));
				admin.setName(resultset.getString("name"));
				admin.setPassword(resultset.getString("password"));
				admin.setStatus(resultset.getInt("status"));
				return admin;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean editPassword(Admin admin) {
		String sql = "update s_admin set password = '"+admin.getPassword()+"' where id = "+admin.getId()+";";
		return update(sql);
	}
	
}
