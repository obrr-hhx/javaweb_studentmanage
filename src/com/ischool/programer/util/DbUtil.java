package com.ischool.programer.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 
 * @author hndsnhuang
 *���ݿ�����util
 */
public class DbUtil {

	private String dbUrl = "jdbc:mysql://localhost:3306/db_student_manager_web?useUnicode=true&characterEncoding=utf8";
	private String dbUser = "root";
	private String dbPassword = "15871008288a";
	private String jdbcName = "com.mysql.jdbc.Driver";
	private Connection connection = null;
	public Connection getConnection() {
		try {
			Class.forName(jdbcName);
			connection = DriverManager.getConnection(dbUrl,dbUser,dbPassword);
			System.out.println("���ݿ����ӳɹ�");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("���ݿ�����ʧ��");
			e.printStackTrace();
		}
		return connection;
	}
	
	public void closeConnect() {
		if(connection != null)
			try {
				connection.close();
				System.out.println("���ݿ����ӽ���");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DbUtil dbUtil = new DbUtil();
		dbUtil.getConnection();
	}

}
