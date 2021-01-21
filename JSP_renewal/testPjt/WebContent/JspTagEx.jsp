<%@ page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
	<%@ include file="header.jsp" %>
	<%! 
		int num  = 10;
		String str ="jsp";
		ArrayList<String> l = new ArrayList<String>();
		
		public void jspMethod() {
			System.out.println("-- jspMEthod()--");
		}
	%>
	<!--  HTML 주석  -->
	<%-- JSP 주석 --%>
	<% if (num > 0) { %>
	<p> num이 0보다 크네요! </p>
	<% } else { %>
	<p> num이 0보다 작네요! </p>
	<% } %>
	
	<%-- 아래 부분은 HTML화면상에 출력 --%>
	<%= num %>
	<%-- 아래 부분은 콘솔창에 출력--%>
	<% System.out.println(num); %>
	
	<%@ include file="footer.jsp" %>
</body>
</html>