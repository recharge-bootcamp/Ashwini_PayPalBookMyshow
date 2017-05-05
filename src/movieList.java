
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class movieList
 */
@WebServlet("/movieList")
public class movieList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public movieList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
			
		response.getWriter().append("Setrved at: ").append(request.getContextPath());
		
		 final String JDBC_DRIVER="com.mysql.jdbc.Driver";  
	     final String DB_URL="jdbc:mysql://localhost/bms";

	      //  Database credentials
	      final String USER = "root";
	      final String PASS = "Hisweety@03";
	      ArrayList<Movies> movieList=new ArrayList<Movies>();
	      try{
	    	  Class.forName(JDBC_DRIVER);
	    	  Connection con=DriverManager.getConnection(DB_URL,USER,PASS);
	    	  String InputVal=request.getParameter("cities");
	    	  PreparedStatement stmt=con.prepareStatement("select movie_name from movies where city_id=("
	    	  		+ "select city_id from cities where city_name=?)");
	    	  stmt.setString(1, InputVal);
	    	  ResultSet rs=stmt.executeQuery();
	    	  
	    	  while(rs.next()){
	    		System.out.println(" Movie name are city "+rs.getString(1));
	    		Movies m=new Movies();
	    		m.setMovieName(rs.getString(1));
	    		movieList.add(m);
	    	  }
	    	  
	    	  request.setAttribute("list", movieList);
	    	  
	    	  RequestDispatcher rd=request.getRequestDispatcher("movielist.jsp");
	    	  rd.forward(request, response);
	    	  
	    	 
	    	  
	    	  rs.close();
	    	  stmt.close();
	    	  con.close();
	      }
	      catch(SQLException | ClassNotFoundException se){
	    	  se.printStackTrace();
	      }
	      
		
	      
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
