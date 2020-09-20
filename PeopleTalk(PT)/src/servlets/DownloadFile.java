package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DownloadFile
 */
@WebServlet("/DownloadFile")
public class DownloadFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		try {
			String fname = request.getParameter("filename");
			int pid = Integer.parseInt(request.getParameter("pid"));
			
			db.DBConnect db=new db.DBConnect();
			byte[] b=db.getFile(pid);
			if(b!=null){
			    response.setContentType("APPLICATION/OCTET-STREAM");   
			    response.setHeader("Content-Disposition","attachment; filename="+fname); 
			    response.getOutputStream().write(b);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

