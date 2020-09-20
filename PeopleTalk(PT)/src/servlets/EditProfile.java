package servlets;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class EditProfile
 */
@WebServlet("/EditProfile")
public class EditProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		try {
			String ph = request.getParameter("phone");
			String n = request.getParameter("name");
			String g = request.getParameter("gender");
			String c = request.getParameter("city");
			String a = request.getParameter("area");
			String s = request.getParameter("state");
			String e = request.getParameter("email");
			
			String dt = request.getParameter("dob");
			java.util.Date date = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(dt);
			java.sql.Date d = new java.sql.Date(date.getTime()); 
			
			db.DBConnect db = new db.DBConnect();
			String ss = db.editProfile(ph, n, g, d, s, c, a, e);
			
			if(ss.equalsIgnoreCase("done")) {
				HashMap<String, Object> userDetails =  (HashMap<String, Object>) session.getAttribute("userDetails");
				userDetails.put("email", e);
				userDetails.put("phone", ph);
				userDetails.put("gender", g);
				userDetails.put("dob", d);
				userDetails.put("state", s);
				userDetails.put("city", c);
				userDetails.put("area", a);
				userDetails.put("name", n);
				session.setAttribute("msg", "Profile Updated");
				response.sendRedirect("editprofile.jsp");
				
			}else if(ss.equalsIgnoreCase("failed")) {
				session.setAttribute("msg", "Details Updation Failed. Try Again!");
				response.sendRedirect("editprofile.jsp");
				
			}else if(ss.equalsIgnoreCase("FailedCatch")) {
				session.setAttribute("msg", "Details Updation Failed Catch. Try Again!");
				response.sendRedirect("editprofile.jsp");
			}
			
		}catch(Exception e) {
			session.setAttribute("msg", "Details Updation Failed. Try Again!"+ e);
			response.sendRedirect("editprofile.jsp");
		}
	}

	
}
