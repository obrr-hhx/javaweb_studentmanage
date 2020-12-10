package com.ischool.programer.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ischool.programer.dao.ClazzDao;
import com.ischool.programer.model.Clazz;
import com.ischool.programer.model.Page;
import com.ischool.programer.model.Teacher;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
/**
 * 
 * @author hndsnhuang
 *班级信息管理servlet
 */
public class ClassServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8276868754142780113L;
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String method = request.getParameter("method");
		if("toClazzListView".equals(method)) {
			clazzList(request, response);
		}else if("ClazzDetailList".equals(method)) {
			getClazzList(request, response);
		}else if("AddClazz".equals(method)) {
			addClazz(request, response);
		}else if("DeleteClazz".equals(method)) {
			deleteClazz(request, response);
		}else if("EditClazz".equals(method)) {
			editClazz(request, response);
		}
	}
	
	private void editClazz(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		Integer id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String info = request.getParameter("info");
		ClazzDao clazzDao = new ClazzDao();
		if(clazzDao.editClazz(id,name,info)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				clazzDao.closeConnect();
			}
		}
	}

	private void deleteClazz(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		Integer id = Integer.parseInt(request.getParameter("clazzid"));
		ClazzDao clazzDao = new ClazzDao();
		if(clazzDao.deleteClazz(id)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				clazzDao.closeConnect();
			}
		}
		
	}

	private void addClazz(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String name = request.getParameter("name");
		String info = request.getParameter("info");
		Clazz clazz = new Clazz();
		clazz.setName(name);
		clazz.setInfo(info);
		ClazzDao clazzDao = new ClazzDao();
		if(clazzDao.addClazz(clazz)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				clazzDao.closeConnect();
			}
			
		}
	}

	private void clazzList(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.getRequestDispatcher("view/clazz/clazzList.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getClazzList(HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("clazzName");
		String flag = request.getParameter("flag");//在查看学生时，教师可以查看到所有班级的学生，此时flag为1
		Integer pageSize = request.getParameter("rows") == null ? 999: Integer.parseInt(request.getParameter("rows"));
		Integer currentPage = request.getParameter("page") == null ? 1: Integer.parseInt(request.getParameter("page"));
		int userType = Integer.parseInt(request.getSession().getAttribute("userType").toString());
		
		Clazz clazz = new Clazz();
		clazz.setName(name);
		if(userType == 3 && !"1".equals(flag)) {
			//如果是教师，只能查看自己的班级
			Teacher crrentUser = (Teacher)request.getSession().getAttribute("user");
			clazz.setId(crrentUser.getClazzId());
		}
		ClazzDao clazzDao = new ClazzDao();
		List<Clazz> clazzList = clazzDao.getClazzList(clazz, new Page(currentPage, pageSize));
		int total = clazzDao.getClazzListTotal(clazz);
		clazzDao.closeConnect();
		response.setCharacterEncoding("UTF-8");
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("total", total);
		ret.put("rows", clazzList);
		try {
			String from = request.getParameter("from");
			if("combox".equals(from)) {
				response.getWriter().write(JSONArray.fromObject(clazzList).toString());			
				}else {
				response.getWriter().write(JSONObject.fromObject(ret).toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
