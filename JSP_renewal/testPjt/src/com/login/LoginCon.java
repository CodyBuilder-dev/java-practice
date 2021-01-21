package com.login;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Login")
public class LoginCon extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public LoginCon() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		PrintWriter out = response.getWriter();
		
		String mId = request.getParameter("mId");
		String mPw = request.getParameter("mPw");
		
		out.print("mId : " + mId);
		out.print("mPw : " + mPw);
		
		// 하나의 request에 cookie가 여러개일 수 있는듯
		Cookie[] cookies = request.getCookies();
		Cookie cookie = null;
		
		for(Cookie c : cookies) {
			System.out.println("c.getName() : " + c.getName() + " c.getValue()" + c.getValue());
			if (c.getName().equals("memberId")) {
				cookie = c;
			}
		}
		if (cookie == null) {
			System.out.println("cookie is null");
			cookie = new Cookie("memberId",mId);
		}
		
		// Cookie 응답 추가
		response.addCookie(cookie);
		// Cookie TTL 설정
		cookie.setMaxAge(60*60);
		
		response.sendRedirect("loginOk.jsp");
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
