<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
	<%! String m_name; %>
	<% m_name = (String)pageContext.getSession().getAttribute("m_name"); %>
	<p> Welcome! <%= m_name %> </p>
	
</body>
</html>