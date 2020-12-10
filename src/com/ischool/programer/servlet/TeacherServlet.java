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
import com.ischool.programer.dao.TeacherDao;
import com.ischool.programer.model.Page;
import com.ischool.programer.model.Student;
import com.ischool.programer.model.Teacher;
import com.ischool.programer.util.SnGenerateUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author hndsnhuang
 * 老师信息管理servlet
 *
 */
public class TeacherServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5018174859417438253L;
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String method = request.getParameter("method");
		if("toTeacherListView".equals(method)) {
			teacherList(request, response);
		}else if("AddTeacher".equals(method)) {
			addTeacher(request,response);
		}else if("TeacherList".equals(method)) {
			getTeacherList(request,response);
		}else if("EditTeacher".equals(method)) {
			editTeacher(request,response);
		}else if("DeleteTeacher".equals(method)) {
			deleteTeacher(request,response);
		}
	}

	private void deleteTeacher(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String[] ids = request.getParameterValues("ids[]");
		int i = 0;//用于判断是否将删除操作全部执行
		for(String str : ids) {
			Teacher teacher = new Teacher();
			teacher.setId(Integer.parseInt(str));
			TeacherDao teacherDao = new TeacherDao();
			if(teacherDao.deleteStudent(teacher)) {
				i++;
				if(i == ids.length) {
					try {
						response.getWriter().write("success");
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

	private void editTeacher(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String name = request.getParameter("name");
		Integer id = Integer.parseInt(request.getParameter("id"));
		String tn = request.getParameter("tn");
		String sex = request.getParameter("sex");
		String phone = request.getParameter("phone");
		String qq = request.getParameter("qq");
		Integer clazzId = Integer.parseInt(request.getParameter("clazzid"));
		
		Teacher teacher = new Teacher();
		teacher.setName(name);
		teacher.setId(id);
		teacher.setTn(tn);
		teacher.setSex(sex);
		teacher.setPhone(phone);
		teacher.setQq(qq);
		teacher.setClazzId(clazzId);
		
		TeacherDao teacherDao = new TeacherDao();
		if(teacherDao.editTeacher(teacher)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				teacherDao.closeConnect();
			}
		}
	}

	private void getTeacherList(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String name = request.getParameter("teacherName");
		Integer pageSize = request.getParameter("rows") == null ? 999: Integer.parseInt(request.getParameter("rows"));
		Integer currentPage = request.getParameter("page") == null ? 1: Integer.parseInt(request.getParameter("page"));
		Integer clazzId = request.getParameter("clazzid") == null ? 0: Integer.parseInt(request.getParameter("clazzid"));
		//获取当前登录用户type
		int userType = Integer.parseInt(request.getSession().getAttribute("userType").toString());
		Teacher teacher  = new Teacher();
		teacher.setName(name);
		teacher.setClazzId(clazzId);
		if(userType == 3) {
			//如果是教师，只能查看自己的信息
			Teacher currentUser = (Teacher)request.getSession().getAttribute("user");
			teacher.setId(currentUser.getId());
		}
		TeacherDao teacherDao = new TeacherDao();
		List<Teacher> teacherList = teacherDao.getTeacherList(teacher, new Page(currentPage, pageSize));
		int total = teacherDao.getTeacherListTotal(teacher);
		teacherDao.closeConnect();
		response.setCharacterEncoding("UTF-8");
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("total", total);
		ret.put("rows", teacherList);
		try {
			String from = request.getParameter("from");
			if("combox".equals(from)) {
				response.getWriter().write(JSONArray.fromObject(teacherList).toString());			
				}else {
				response.getWriter().write(JSONObject.fromObject(ret).toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addTeacher(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		String sex = request.getParameter("sex");
		String phone = request.getParameter("phone");
		String qq = request.getParameter("qq");
		int clazzId = Integer.parseInt(request.getParameter("clazzId"));
		Teacher teacher = new Teacher();
		teacher.setName(name);
		teacher.setPassword(password);
		teacher.setSex(sex);
		teacher.setPhone(phone);
		teacher.setQq(qq);
		teacher.setClazzId(clazzId);
		teacher.setTn(SnGenerateUtil.generateTeacherSn(clazzId));
		TeacherDao teacherDao = new TeacherDao();
		if(teacherDao.addTeacher(teacher)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				teacherDao.closeConnect();
			}
		}
		
	}

	private void teacherList(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		try {
			request.getRequestDispatcher("view/teacher/teacherList.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
