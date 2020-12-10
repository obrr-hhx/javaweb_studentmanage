package com.ischool.programer.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ischool.programer.dao.AdminDao;
import com.ischool.programer.dao.StudentDao;
import com.ischool.programer.dao.TeacherDao;
import com.ischool.programer.model.Admin;
import com.ischool.programer.model.Student;
import com.ischool.programer.model.Teacher;
/**
 * 
 * @author hndsnhuang
 *系统登陆后主界面
 */
public class SystemServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8199598723344474404L;
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String method = request.getParameter("method");
		
		if("toAdminView".equals(method)) {
			adminView(request,response);
		}else if("toPersonalView".equals(method)) {
			personalView(request,response);
		}else if("EditPasswod".equals(method)) {
			editPassword(request,response);
		}else if("toStudentView".equals(method)) {
			adminView(request,response);
			//studentView(request,response);
		}else if("toTeacherView".equals(method)) {
			adminView(request,response);
			//teacherView(request,response);
		}
	}
	private void teacherView(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		try {
			request.getRequestDispatcher("view/teacher/teacher.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void adminView(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		try {
			request.getRequestDispatcher("view/system.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void studentView(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		try {
			request.getRequestDispatcher("view/student/student.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void editPassword(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String password = request.getParameter("password");
		String newPassword = request.getParameter("newpassword");
		response.setCharacterEncoding("UTF-8");
		int userType = Integer.parseInt(request.getSession().getAttribute("userType").toString());
		//管理员
		if(userType==1) {
			Admin admin = (Admin)request.getSession().getAttribute("user");
			if(!admin.getPassword().equals(password)) {
				try {
					response.getWriter().write("原密码错误");
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				admin.setPassword(newPassword);
				AdminDao adminDao = new AdminDao();
				if(adminDao.editPassword(admin)) {
					try {
						response.getWriter().write("success");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						adminDao.closeConnect();
					}
				}else {
					try {
						response.getWriter().write("数据库修改错误");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						adminDao.closeConnect();
					}
				}
			}
		}
		//学生
		if(userType==2) {
			Student student = (Student)request.getSession().getAttribute("user");
			if(!student.getPassword().equals(password)) {
				try {
					response.getWriter().write("原密码错误");
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				student.setPassword(newPassword);
				StudentDao studentDao = new StudentDao();
				if(studentDao.editPassword(student)) {
					try {
						response.getWriter().write("success");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						studentDao.closeConnect();
					}
				}else {
					try {
						response.getWriter().write("数据库修改错误");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						studentDao.closeConnect();
					}
				}
			}
		}
		//老师
		if(userType==3) {
			Teacher teacher = (Teacher)request.getSession().getAttribute("user");
			if(!teacher.getPassword().equals(password)) {
				try {
					response.getWriter().write("原密码错误");
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				teacher.setPassword(newPassword);
				TeacherDao teacherDao = new TeacherDao();
				if(teacherDao.editPassword(teacher)) {
					try {
						response.getWriter().write("success");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						teacherDao.closeConnect();
					}
				}else {
					try {
						response.getWriter().write("数据库修改错误");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						teacherDao.closeConnect();
					}
				}
			}
		}
	}
	private void personalView(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		try {
			request.getRequestDispatcher("view/personalView.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
