<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
	<%! String adminName; %>
	
	<% adminName = getInitParameter("adminName");  %>
	<!-- Servlet 매핑 안해주면 값 못 받아옴! -->
	<%= adminName %>
</body>
</html>