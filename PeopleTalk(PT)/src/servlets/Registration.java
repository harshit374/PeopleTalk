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


@MultipartConfig
@WebServlet("/Registration")
public class Registration extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		try {
			String e = request.getParameter("email");
			String ph = request.getParameter("phone");
			String n = request.getParameter("name");
			String g = request.getParameter("gender");
			String c = request.getParameter("city");
			String a = request.getParameter("area");
			String s = request.getParameter("state");
			String p = request.getParameter("password");
			System.out.println("convering to date...");
			
			//code to convert String into Date
			String dt = request.getParameter("dob");
			java.util.Date date=new java.text.SimpleDateFormat("dd/MM/yyyy").parse(dt);  
			java.sql.Date d = new java.sql.Date(date.getTime());
			
			//take photo as input in Part
			Part part = request.getPart("photo");
			InputStream is = null;
			if(part != null) {
				is = part.getInputStream();
			}
			
			//update all data in HashMap
			HashMap<String, Object> userDetails = new HashMap<>();
			userDetails.put("email", e);
			userDetails.put("name", n);
            userDetails.put("pass", p);
            userDetails.put("phone", ph);
            userDetails.put("gender", g);
            userDetails.put("dob", d);
            userDetails.put("state", s);
            userDetails.put("city", c);
            userDetails.put("area", a);
            userDetails.put("photo", is);
            
            db.DBConnect db = new db.DBConnect();
            String ss = db.inserUser(userDetails);
            
            if(ss.equalsIgnoreCase("Success")) 
            {
            	userDetails.remove("pass");
                userDetails.remove("photo");
            	session.setAttribute("userDetails", userDetails);
    			response.sendRedirect("profile.jsp");
            }
            else if(ss.equalsIgnoreCase("already")) 
            {
            	session.setAttribute("msg", "Email ID Already Exixts");
    			response.sendRedirect("home.jsp");
            }
            else {
            	session.setAttribute("msg", "Registration Failed");
    			response.sendRedirect("home.jsp");
            }
		}catch(Exception e) {
			
			session.setAttribute("msg", "Registration Failed: "+ e );
			e.printStackTrace();
			response.sendRedirect("home.jsp");
		}
		
	}

}
