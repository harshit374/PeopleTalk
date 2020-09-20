package servlets;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 * Servlet implementation class UpdatePhoto
 */
@MultipartConfig
@WebServlet("/UpdatePhoto")
public class UpdatePhoto extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		try {
			
			String e = request.getParameter("email");
			Part part = request.getPart("photo");
			InputStream is = null;
			if(part != null) {
				is = part.getInputStream();
			}
			
			System.out.println(e);
			
			db.DBConnect db = new db.DBConnect();
			String x = db.updatePhoto(e, is);
			
			if(x.equalsIgnoreCase("done")) {
				session.setAttribute("msg", "Profile Updated");
				response.sendRedirect("editprofile.jsp");
				
			}else if(x.equalsIgnoreCase("failed")) {
				session.setAttribute("msg", "Try Again...");
				response.sendRedirect("editprofile.jsp");
			}else {
				session.setAttribute("msg", "Pls Try Again");
				response.sendRedirect("editprofile.jsp");
			}
			
		}catch(Exception e) {
			session.setAttribute("msg", "Failed "+ e);
			response.sendRedirect("editprofile.jsp");
		}
	}


}
