package servlets;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetPhoto
 */
@WebServlet("/GetPhoto")
public class GetPhoto extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
            String e=request.getParameter("email");
            
            db.DBConnect db=new db.DBConnect();
            byte[] b=db.getPhoto(e);
            
            if(b==null){
                ServletContext ctx = request.getServletContext();
                InputStream fin = ctx.getResourceAsStream("/img/xyz.jpg");
                //FileInputStream fin=new FileInputStream("D:\\NetBeansProjects\\PeopleTalk\\web\\img\\xyz.jpg");
                b=new byte[3500];
                fin.read(b);
            }
            response.getOutputStream().write(b);
        } catch (Exception ex) {
            ex.printStackTrace();
            ServletContext ctx = request.getServletContext();
            InputStream fin = ctx.getResourceAsStream("/img/xyz.jpg");
            //FileInputStream fin=new FileInputStream("D:\\NetBeansProjects\\PeopleTalk\\web\\img\\xyz.jpg");
            byte []b=new byte[3500];
            fin.read(b);
            response.getOutputStream().write(b);
        }
	}


}
