package com.ischool.programer.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ischool.programer.dao.StudentDao;
import com.ischool.programer.model.Page;
import com.ischool.programer.model.Student;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 
 * @author hndsnhuang
 *学生信息管理servlet
 */
public class StudentServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8320907832240411557L;
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String method = request.getParameter("method");
		if("toStudentListView".equals(method)) {
			studentList(request, response);
		}else if("AddStudent".equals(method)) {
			addStudnt(request,response);
		}else if("StudentList".equals(method)) {
			getStudentList(request,response);
		}else if("EditStudent".equals(method)) {
			editStudent(request,response);
		}else if("DeleteStudent".equals(method)) {
			deleteStudent(request,response);
		}
	}
	

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String[] ids = request.getParameterValues("ids[]");
		int i = 0;//用于判断是否将删除操作全部执行
		for(String str : ids) {
			Student student = new Student();
			student.setId(Integer.parseInt(str));
			StudentDao studentDao = new StudentDao();
			if(studentDao.deleteStudent(student)) {
				i++;
				if(i == ids.length) {
					try {
						response.getWriter().write("success");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						studentDao.closeConnect();
					}
				}
			}
		}
	}
	
	private void editStudent(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String name = request.getParameter("name");
		Integer id = Integer.parseInt(request.getParameter("id"));
		String s_num = request.getParameter("s_num");
		String gender = request.getParameter("gender");
		String phoneNum = request.getParameter("phoneNum");
		String qq = request.getParameter("qq");
		Integer clazzId = Integer.parseInt(request.getParameter("clazzid"));
		
		Student student = new Student();
		student.setName(name);
		student.setId(id);
		student.setS_num(s_num);
		student.setGender(gender);
		student.setPhoneNum(phoneNum);
		student.setQq(qq);
		student.setClazzId(clazzId);
		
		StudentDao studentDao = new StudentDao();
		try {
			student.setClazzName(studentDao.getClazzName(clazzId));;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(studentDao.editStudnt(student)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				studentDao.closeConnect();
			}
		}
		
	}
	
	private void getStudentList(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String name = request.getParameter("studentName");
		Integer pageSize = request.getParameter("rows") == null ? 999: Integer.parseInt(request.getParameter("rows"));
		Integer currentPage = request.getParameter("page") == null ? 1: Integer.parseInt(request.getParameter("page"));
		Integer clazzId = request.getParameter("clazzid") == null ? 0: Integer.parseInt(request.getParameter("clazzid"));
		//获取当前登录用户type
		int userType = Integer.parseInt(request.getSession().getAttribute("userType").toString());
		Student student = new Student();
		student.setName(name);
		student.setClazzId(clazzId);
		if(userType == 2) {
			//如果是学生，只能查看自己的信息
			Student currentUser = (Student)request.getSession().getAttribute("user");
			student.setId(currentUser.getId());
		}
		StudentDao studentDao = new StudentDao();
		List<Student> studentList = studentDao.getStudentList(student, new Page(currentPage, pageSize));
		int total = studentDao.getStudentListTotal(student);
		studentDao.closeConnect();
		response.setCharacterEncoding("UTF-8");
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("total", total);
		ret.put("rows", studentList);
		try {
			String from = request.getParameter("from");
			if("combox".equals(from)) {
				response.getWriter().write(JSONArray.fromObject(studentList).toString());			
				}else {
				response.getWriter().write(JSONObject.fromObject(ret).toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void addStudnt(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		String number = request.getParameter("number");
		String gender = request.getParameter("sex");
		String phoneNum = request.getParameter("phone");
		String qq = request.getParameter("qq");
		Integer clazzId = Integer.parseInt(request.getParameter("clazzid"));		
		Student student = new Student();
		student.setName(name);
		student.setPassword(password);
		student.setS_num(number);
		student.setGender(gender);
		student.setPhoneNum(phoneNum);
		student.setQq(qq);
		student.setClazzId(clazzId);;
		
		StudentDao studentDao = new StudentDao();
		String clazzName;
		try {
			clazzName = studentDao.getClazzName(clazzId);
			student.setClazzName(clazzName);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(studentDao.addStudent(student)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				studentDao.closeConnect();
			}
		}
	}

	private void studentList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			request.getRequestDispatcher("view/student/studentList.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
