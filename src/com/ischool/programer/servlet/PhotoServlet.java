package com.ischool.programer.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;

import com.ischool.programer.dao.StudentDao;
import com.ischool.programer.dao.TeacherDao;
import com.ischool.programer.model.Student;
import com.ischool.programer.model.Teacher;
import com.lizhou.exception.FileFormatException;
import com.lizhou.exception.NullFileException;
import com.lizhou.exception.ProtocolException;
import com.lizhou.exception.SizeException;
import com.lizhou.fileload.FileUpload;
/**
 * 
 * @author hndsnhuang
 *头像图片处理servlet
 */
public class PhotoServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1482996926488228025L;
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String method = request.getParameter("method");
		if("getPhoto".equals(method)) {
			getPhoto(request,response);
		}else if("SetPhoto".equals(method)) {
			setPhoto(request,response);
		}
	}
	private void setPhoto(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		int sid = request.getParameter("sid") == null ? 0 : Integer.parseInt(request.getParameter("sid"));
		int tid = request.getParameter("tid") == null ? 0 : Integer.parseInt(request.getParameter("tid"));
		
		FileUpload fileUpload = new FileUpload(request);
		fileUpload.setFileFormat("jpg");
		fileUpload.setFileFormat("png");
		fileUpload.setFileFormat("jpeg");
		fileUpload.setFileFormat("gif");
		fileUpload.setFileSize(2048);
		
		response.setCharacterEncoding("UTF-8");
		try {
			InputStream uploadInputStream = fileUpload.getUploadInputStream();
			if(sid != 0) {
				Student student = new Student();
				student.setId(sid);
				student.setPhoto(uploadInputStream);
				StudentDao studentDao = new StudentDao();
				if(studentDao.setStudntPhoto(student)) {
					response.getWriter().write("<div id='message'>上传成功</div>");
				}else {
					response.getWriter().write("<div id='message'>上传失败</div>");
				}
			}
			
			if(tid != 0) {
				Teacher teacher = new Teacher();
				teacher.setId(tid);
				teacher.setPhoto(uploadInputStream);
				TeacherDao teacherDao = new TeacherDao();
				if(teacherDao.setTeacherPhoto(teacher)) {
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write("<div id='message'>上传成功</div>");
				}else {
					response.getWriter().write("<div id='message'>上传失败</div>");
				}
			}
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			try {
				response.getWriter().write("<div id='message'>上传协议错误</div>");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}catch (NullFileException e1) {
			// TODO: handle exception
			try {
				response.getWriter().write("<div id='message'>上传文件为空</div>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			e1.printStackTrace();
		}catch (SizeException e2) {
			// TODO: handle exception
			try {
				response.getWriter().write("<div id='message'>上传文件大小不能超过"+fileUpload.getFileSize()+"</div>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			e2.printStackTrace();
		}catch (IOException e3) {
			// TODO: handle exception
			try {
				response.getWriter().write("<div id='message'>读取文件出错</div>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			e3.printStackTrace();
		}catch (FileFormatException e4) {
			// TODO: handle exception
			try {
				response.getWriter().write("<div id='message'>上传文件格式出错，请上传 "+fileUpload.getFileFormat()+" 格式的文件</div>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			e4.printStackTrace();
		}catch (FileUploadException e5) {
			// TODO: handle exception
			try {
				response.getWriter().write("<div id='message'>文件上传错误</div>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			e5.printStackTrace();
		}
		
		
	}
	private void getPhoto(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		int sid = request.getParameter("sid") == null ? 0 : Integer.parseInt(request.getParameter("sid"));
		int tid = request.getParameter("tid") == null ? 0 : Integer.parseInt(request.getParameter("tid"));
		String type = request.getParameter("type");
		if(sid != 0) {
			//学生头像
			StudentDao studentDao = new StudentDao();
			Student student = studentDao.getStudent(sid);
			studentDao.closeConnect();
			if(student != null) {
				InputStream photo = student.getPhoto();
				if(photo != null) {
					try {
						byte[] b = new byte[photo.available()];
						photo.read(b);
						response.getOutputStream().write(b,0,b.length);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {//学生默认头像
					String path = request.getSession().getServletContext().getRealPath("");
					File file = new File(path+"\\file\\avatar.jpg");
					try {
						FileInputStream fis = new FileInputStream(file);
						byte[] b = new byte[fis.available()];
						fis.read(b);
						response.getOutputStream().write(b,0,b.length);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return;
			}
		}
		
		if(tid != 0) {
			//学生头像
			TeacherDao teacherDao = new TeacherDao();
			Teacher teacher = teacherDao.getTeacher(tid);
			teacherDao.closeConnect();
			if(teacher != null) {
				InputStream photo = teacher.getPhoto();
				if(photo != null) {
					try {
						byte[] b = new byte[photo.available()];
						photo.read(b);
						response.getOutputStream().write(b,0,b.length);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {//教师默认头像
					String path = request.getSession().getServletContext().getRealPath("");
					File file = new File(path+"\\file\\avatar_t.jpg");
					try {
						FileInputStream fis = new FileInputStream(file);
						byte[] b = new byte[fis.available()];
						fis.read(b);
						response.getOutputStream().write(b,0,b.length);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return;
			}
		}
		
		if("2".equals(type)) {//学生默认头像
			String path = request.getSession().getServletContext().getRealPath("");
			File file = new File(path+"\\file\\avatar.jpg");
			try {
				FileInputStream fis = new FileInputStream(file);
				byte[] b = new byte[fis.available()];
				fis.read(b);
				response.getOutputStream().write(b,0,b.length);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if("3".equals(type)) {//教师默认头像
			String path = request.getSession().getServletContext().getRealPath("");
			File file = new File(path+"\\file\\avatar_t.jpg");
			try {
				FileInputStream fis = new FileInputStream(file);
				byte[] b = new byte[fis.available()];
				fis.read(b);
				response.getOutputStream().write(b,0,b.length);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
