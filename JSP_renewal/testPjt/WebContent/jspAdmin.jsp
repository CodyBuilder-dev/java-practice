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
	<!-- Servlet ���� �����ָ� �� �� �޾ƿ�! -->
	<%= adminName %>
</body>
</html>