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
	<!--  HTML �ּ�  -->
	<%-- JSP �ּ� --%>
	<% if (num > 0) { %>
	<p> num�� 0���� ũ�׿�! </p>
	<% } else { %>
	<p> num�� 0���� �۳׿�! </p>
	<% } %>
	
	<%-- �Ʒ� �κ��� HTMLȭ��� ��� --%>
	<%= num %>
	<%-- �Ʒ� �κ��� �ܼ�â�� ���--%>
	<% System.out.println(num); %>
	
	<%@ include file="footer.jsp" %>
</body>
</html>