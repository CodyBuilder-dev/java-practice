package com.book;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BookServlet
 */
@WebServlet("/BookServlet")
public class BookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public BookServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		// JDBC URL 입력
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url =  "jdbc:oracle:thin:@localhost:1521:xe";
		String id = "scott";
		String pw = "tiger";
		
		Connection con = null;
		Statement stmt = null;
		ResultSet res = null;
		
		// JDBC API 이용 DB 접속
		try {
			Class.forName(driver);
			
			con = DriverManager.getConnection(url,id,pw);
			stmt = con.createStatement();
			
			String sql = "SELECT * FROM BOOK";
			res = stmt.executeQuery(sql);
					
			while(res.next()){
				int bookId = res.getInt("book_id");
				String bookName = res.getString("book_name");
				String bookLoc = res.getString("book_loc");
				
				out.print(bookId);
				out.print(bookName);
				out.print(bookLoc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			res.close();
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
