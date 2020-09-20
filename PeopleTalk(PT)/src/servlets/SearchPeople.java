package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class SearchPeople
 */
@WebServlet("/SearchPeople")
public class SearchPeople extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		try {
			HashMap<String, Object> userDetails = (HashMap<String, Object>) session.getAttribute("userDetails");
			if(userDetails != null) {
				String s = request.getParameter("state");
				String c = request.getParameter("city");
				String a = request.getParameter("area");
				String e =  (String) userDetails.get("email");

				db.DBConnect db = new db.DBConnect();
				ArrayList<HashMap<String, Object>> allUserDetails = db.searchPeople(s, c, a, e);
				
				if(!allUserDetails.isEmpty()) {
					HashMap<String, Object> address = new HashMap<String, Object>();
					address.put("state", s);
					address.put("city", c);
					address.put("area", a);
					session.setAttribute("allUserDetails",allUserDetails);
		            session.setAttribute("address",address);
		            response.sendRedirect("peoplesearch.jsp");
				}else {
					session.setAttribute("msg", "No Data Found!");
		            response.sendRedirect("profile.jsp");
				}
			}else {
				session.setAttribute("msg", "Login First");
	            response.sendRedirect("home.jsp");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}