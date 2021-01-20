package com.book;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class NewBook
 */
@WebServlet("/newBook")
public class NewBook extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public NewBook() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		// Request로부터 Parameter값 획득
		String bookName = request.getParameter("book_name");
		String bookLoc = request.getParameter("book_loc");
		
		// JDBC URL 입력
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url =  "jdbc:oracle:thin:@localhost:1521:xe";
		String id = "scott";
		String pw = "tiger";
		
		Connection con = null;
		Statement stmt = null;
		
		// JDBC API 이용 DB 접속
		try {
			Class.forName(driver);
			
			con = DriverManager.getConnection(url,id,pw);
			stmt = con.createStatement();
			
			String sql = "INSERT INTO book (book_id,book_name,book_loc)";
					sql += "VALUES (SEQ_BOOK_ID.NEXTVAL,'"+bookName+ "','" + bookLoc + "')";
			int result = stmt.executeUpdate(sql);
					
			if(result == 1) {
				out.print("INSERT Success!!");			
			} else {
				out.print("INSERT Fail!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
			con.close();
		}
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
