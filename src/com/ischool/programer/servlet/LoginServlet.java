package com.ischool.programer.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ischool.programer.dao.AdminDao;
import com.ischool.programer.dao.StudentDao;
import com.ischool.programer.dao.TeacherDao;
import com.ischool.programer.model.Admin;
import com.ischool.programer.model.Student;
import com.ischool.programer.model.Teacher;
import com.ischool.programer.util.StringUtil;

/**
 * 
 * @author hndsnhuang
 *登陆验证servlet
 */

public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -569693573461602800L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request,response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String method = request.getParameter("method");
		if("LoginOut".equals(method)) {
			logOut(request, response);
			return;
		}
		String vcode = request.getParameter("vcode");
		String name = request.getParameter("account");
		String password = request.getParameter("password");
		int type = Integer.parseInt(request.getParameter("type"));
		String loginCaptcha = request.getSession().getAttribute("loginCaptcha").toString();
		if(StringUtil.isEmpty(vcode)) {
			response.getWriter().write("vcodeError");
			return;
		}
		if(!vcode.toUpperCase().equals(loginCaptcha.toUpperCase())) {
			response.getWriter().write("vcodeError");
			return;
		}
		//验证码通过,对比用户名，密码是否正确
		String loginStatus = "loginFailed";
		switch(type) {
			case 1:{
				AdminDao adminDao = new AdminDao();
				Admin admin = adminDao.login(name, password);
				adminDao.closeConnect();
				if(admin == null) {
					response.getWriter().write("loginError");
					return;
				}
				HttpSession session = request.getSession();
				session.setAttribute("user", admin);
				session.setAttribute("userType", type);
				loginStatus = "adminLoginSuccess";
				break;
			}
			case 2:{
				StudentDao studentDao = new StudentDao();
				Student student = studentDao.login(name, password);//学生需要用学号登录
				studentDao.closeConnect();
				if(student == null) {
					response.getWriter().write("loginError");
					return;
				}
				HttpSession session = request.getSession();
				session.setAttribute("user", student);
				session.setAttribute("userType", type);
				loginStatus = "studentLoginSuccess";
				break;
			}
			case 3:{
				TeacherDao teacherDao = new TeacherDao();
				Teacher teacher = teacherDao.login(name, password);
				teacherDao.closeConnect();
				if(teacher == null) {
					response.getWriter().write("loginError");
					return;
				}
				HttpSession session = request.getSession();
				session.setAttribute("user", teacher);
				session.setAttribute("userType", type);
				loginStatus = "teacherLoginSuccess";
				break;
			}
			default:
				break;
		}
		response.getWriter().write(loginStatus);
		
		//说明用户名，密码正确
	}
	
	private void logOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().removeAttribute("user");
		request.getSession().removeAttribute("userType");
		response.sendRedirect("index.jsp");
	}
}
