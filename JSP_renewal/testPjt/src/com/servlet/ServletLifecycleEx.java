package com.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@WebServlet("/lifecycle")
public class ServletLifecycleEx extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ServletLifecycleEx() {
        super();
        // TODO Auto-generated constructor stub
    }

    @PostConstruct
    public void postConstruct() {
    	System.out.println("--postConstruct()--");
    }
    @Override
    public void init() {
    	System.out.println("--init()--");
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		System.out.println("--doGet()--");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	@Override
	public void destroy() {
		System.out.println("--destroy()--");
	}
	
	@PreDestroy
	public void preDestroy() {
		System.out.println("--preDestroy()--");
	}
}
