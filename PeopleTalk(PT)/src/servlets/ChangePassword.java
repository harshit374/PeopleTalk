package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ChangePassword
 */
@WebServlet("/ChangePassword")
public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		try {
			String o = (String) request.getParameter("password");
			String p = (String) request.getParameter("newpassword");
			String c = (String) request.getParameter("confirmpassword");
			String e = (String) request.getParameter("email");
			
			db.DBConnect db = new db.DBConnect();
			String s = db.changePassword(o, p, c, e);
			
			if(s.equalsIgnoreCase("done")) {
				response.sendRedirect("profile.jsp");
				
			}else if(s.equalsIgnoreCase("matchFailed")) {
				session.setAttribute("msg", "Password not match");
				response.sendRedirect("changepassword.jsp");
				
			}else if(s.equalsIgnoreCase("wrongPass")) {
				session.setAttribute("msg", "Wrong Password");
				response.sendRedirect("changepassword.jsp");
				
			} else {
                session.setAttribute("msg", "Please Try Again!!");
                response.sendRedirect("changepassword.jsp");
            }
				
		}catch(Exception ex){
			session.setAttribute("msg", "Exception :" + ex);
            response.sendRedirect("changepassword.jsp");
		}
	
	}


}
