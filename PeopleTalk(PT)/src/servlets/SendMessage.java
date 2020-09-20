package servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 * Servlet implementation class SendMessage
 */
@MultipartConfig
@WebServlet("/SendMessage")
public class SendMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		try {
			HashMap userDetails=(HashMap)session.getAttribute("userDetails");
			if(userDetails!=null) {
				String temail = request.getParameter("temail");
				String semail = (String) userDetails.get("email");
				String message = request.getParameter("message");
				Part p = request.getPart("ufile");
				InputStream is = null;
				String fname ="";
				if(p!=null) {
					fname = p.getSubmittedFileName();
					is = p.getInputStream();
				}
				
				System.out.println(temail+" "+ semail+" "+  message+" "+  fname+" ");
				
				db.DBConnect db = new db.DBConnect();
				String s = db.sendMessage(temail, semail, message, fname, is);
				
				if(s.equalsIgnoreCase("done")) {
					session.setAttribute("msg", "Message Sent Successfully!");
                }else{
                    session.setAttribute("msg", "Message Sending Failed!");
                }
                response.sendRedirect("talk.jsp?temail="+temail);
				
				
			}else {
				session.setAttribute("msg", "Plz login First!");
                response.sendRedirect("home.jsp");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
