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
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	HttpSession session = request.getSession();
	try {
		String e = request.getParameter("email");
		String p = request.getParameter("password");
		db.DBConnect db = new db.DBConnect();
		HashMap<?, ?> userDetails = db.checkLogin(e, p);
		if(userDetails != null) {
			session.setAttribute("userDetails", userDetails);
			response.sendRedirect("profile.jsp");
		}else {
			session.setAttribute("msg", "Wrong Entries!!!");
			response.sendRedirect("home.jsp");
		}
		
		
	}catch(Exception e) {
		session.setAttribute("msg", "Error Occurred: "+e);
		response.sendRedirect("home.jsp");
	}
	}

}
