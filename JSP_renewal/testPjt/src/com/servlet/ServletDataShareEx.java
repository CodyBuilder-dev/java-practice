package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ServletDataShareEx
 */

public class ServletDataShareEx extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ServletDataShareEx() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		String adminId = getServletConfig().getInitParameter("adminId");
		String adminPw = getServletConfig().getInitParameter("adminId");
		String testId = getServletContext().getInitParameter("testId");		
		PrintWriter o = response.getWriter();
		o.print(adminId +' '+ adminPw + ' ' + testId+'\n');
		
		// 이미 초기화된 init param은 수정 불가능
		//getServletContext().setInitParameter("testId","noTest");
		String testPw = (String) getServletContext().getAttribute("testPw");
		
		// 페이지상에서 처음에는 null로 나오다가, 새로고침을 하면 21이 된다. 동작 파악 필요
		getServletContext().setAttribute("testPw", "21");
		
		o.print(adminId +' '+ adminPw + ' ' + testId+ ' '+testPw+'\n');
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
