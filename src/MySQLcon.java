import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Have it as a singeton class.
public class MySQLcon {

	final static String JDBC_DRIVER="com.mysql.jdbc.Driver";  
    final static String DB_URL="jdbc:mysql://localhost/bms";

     //  Database credentials
     final static String USER = "root";
     final static String PASS = "SqlRoot3!";

	public static Connection DBManager()
	{
		Connection con=null;
		try{
	    	  Class.forName(JDBC_DRIVER);
	    	  con=DriverManager.getConnection(DB_URL,USER,PASS);
		}
		catch(SQLException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			return con;
		}
	}
}
