<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<%@page import="java.util.*" %>
	<%

	HashMap<?, ?> userDetails = (HashMap<?, ?>) session.getAttribute("userDetails");
	if(userDetails != null){
	
		session.invalidate();
		response.sendRedirect("home.jsp");
	}else{
		session.setAttribute("msg", "Plz login First!");
        response.sendRedirect("home.jsp");
	}
%>
</body>
</html>