<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
	<%!
		String m_name;
		String m_pass;
		String[] m_hobby;
	%>
	
	<%
		m_name = request.getParameter("m_name");
		m_pass = request.getParameter("m_pass");
		m_hobby = request.getParameterValues("m_hobby");
	%>
	
	m_name = <%= m_name %>
	m_pass = <%= m_pass %>
	m_hobby(wrong) = <%= m_hobby %>
	
	</br>
	
	<% for (int i = 0 ; i < m_hobby.length; ++i) {%>
		hobby = <%= m_hobby[i] %> </br>
	<% } %>
	
	<%-- 리다이렉트 및 파라미터 전달 --%>
  	<%
        pageContext.getSession().setAttribute("m_name", m_name);
        
        response.sendRedirect("Welcome.jsp");
    %>
	
</body>
</html>