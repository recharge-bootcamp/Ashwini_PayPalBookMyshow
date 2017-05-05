

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;


@WebServlet(description = "Creates DB instance and gets City list", urlPatterns = { "/Setup" })
public class Setup extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	// SQl Connection
	private Connection mySqlCon = SetupDBConnection();
    
	// load all movies map and theatre map.
	private Map<String, String> movie_map = new HashMap<String, String>();
	private Map<String, String> theatre_map = new HashMap<String, String>();
	private Map<String, String> seat_map = new HashMap<String, String>();
	
	// user selection details.
	private static int selected_city_id, selected_movie_id, selected_theatre_id;
	
	
	
    public Setup() {
        super();
        // TODO Auto-generated constructor stub
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	

		String dd = request.getParameter("dd"); // ID of child DD to fill options for. (movieID, TheatreID)
	    String val = request.getParameter("val"); // Value of parent DD to find associated child DD options for.( CityID, MovieID,theatreID)
	    
	    String seats= request.getParameter("NumOfSeats"); // Get user input frm the form. 
	    
		    if(seats!=null){
		    /*	 fill the available seat maps.
	    		 hard coding the seat maps for now. Plan is to fetch from the DB.
	    		 Total seats = 60. All even seats are booked.*/
	    		for (int i=1; i <= 60; i++)
	    		{	
	    			if (i % 2 == 0)
	    				seat_map.put(String.valueOf(i) , "BOOKED" );
	    			else 
	    				seat_map.put(String.valueOf(i) , "AVAILABLE" );
	    		}
	    		String json = new Gson().toJson(seat_map);
			    response.setContentType("application/json");
			    response.setCharacterEncoding("UTF-8");
			    response.getWriter().write(json);
		    }
	    
		    if ( val == null) {
	    	request.setAttribute("citylist", GetCityList());
		    } 
		    else {
		    		if ( selected_city_id == 0)
		    		{	
		    				selected_city_id = GetCityId(val);
		    				Map<String, String> options = LoadMovies(selected_city_id);
		    				String json = new Gson().toJson(options);
		    				response.setContentType("application/json");
		    				response.setCharacterEncoding("UTF-8");
		    				response.getWriter().write(json);
		    		}else if (selected_movie_id == 0)
		    		{	
		    			// we are getting ID in val, rather than string value. Hence doing ParseInt. Bug.
				    		selected_movie_id = Integer.parseInt(val);
				    		Map<String, String> options_theater = LoadTheatres(selected_city_id, selected_movie_id);
						    String json = new Gson().toJson(options_theater);
						    response.setContentType("application/json");
						    response.setCharacterEncoding("UTF-8");
						    response.getWriter().write(json);
				    	} else if (selected_theatre_id == 0){
				    		// we are getting ID in val, rather than string value. Hence doing ParseInt. Bug.
				    		selected_theatre_id = Integer.parseInt(val);
				    		// display the number of seats + booking details
				    		
				    	} else {
				    		// fill the available seat maps.
				    		// hard coding the seat maps for now. Plan is to fetch from the DB.
				    		// Total seats = 60. All even seats are booked.
				    		for (int i=1; i <= 60; i++)
				    		{	
				    			if (i % 2 == 0)
				    				seat_map.put(String.valueOf(i) , "BOOKED" );
				    			else 
				    				seat_map.put(String.valueOf(i) , "AVAILABLE" );
				    		}
				    		String json = new Gson().toJson(seat_map);
						    response.setContentType("application/json");
						    response.setCharacterEncoding("UTF-8");
						    response.getWriter().write(json);
				    		}
	    	
	    }
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
	
						private Connection SetupDBConnection (){
							Connection con= MySQLcon.DBManager();
							return con;
						}
						
						private ArrayList<City> GetCityList(){
							ArrayList<City> arList = new ArrayList<City>();
							try {
								Statement stmt	=	mySqlCon.createStatement();
								String sql= "select * from cities";
								ResultSet rs= stmt.executeQuery(sql);
								while(rs.next()){
									City cities=new City();
									cities.setCid(rs.getInt(1));
									cities.setCname(rs.getString(2));
									arList.add(cities);
								}
							}
							catch(SQLException e){
									e.printStackTrace();
							}
							return arList;
						}

					private Map<String, String> LoadMovies(int city_id)
					{
						// Load movies into Movie Dropdown
						//take selected cityID is passed.
						try {
							Statement stmt	=	mySqlCon.createStatement();
							String sql= "select * from movies where city_id=" + city_id;
							ResultSet rs= stmt.executeQuery(sql);
							while(rs.next()){
								Movies m=new Movies();
								m.setMovieId(Integer.parseInt(rs.getString(1)));
								m.setMovieName(rs.getString(2));
								movie_map.put(rs.getString(1), rs.getString(2));
							}
						}
						catch(SQLException e){
								e.printStackTrace();
						}
						return movie_map;
					}
	
				private Map<String, String> LoadTheatres(int city_id, int movie_id){
					//Loads theatres according to the selected movieID and CityID
					try {
						Statement stmt	=	mySqlCon.createStatement();
						String sql= "select * from theatres t where t.city_id=" + city_id + "&& t.movie_id=" + movie_id;
						ResultSet rs= stmt.executeQuery(sql);
						while(rs.next()){
							Theatres th=new Theatres();
							th.setTheatre_id(Integer.parseInt(rs.getString(1)));
							th.setTheatre_name(rs.getString(2));
							theatre_map.put(rs.getString(1),rs.getString(2) );
						} 
					}
					catch(SQLException e){
							e.printStackTrace();
					}
					return theatre_map;
				}
				
				
				private int GetTheatreId( String name){
					//Get selected theatreID
					int theatre_id = 0;
					try {
						Statement stmt	= mySqlCon.createStatement();
						String sql= "select theatre_id from theatres where theatre_name=" + "'" + name + "'";
						ResultSet rs= stmt.executeQuery(sql);
						while(rs.next()){
							theatre_id  = rs.getInt(1);
						}
					}
					catch(SQLException e){
						e.printStackTrace();
					}
					return theatre_id ;
				}	
	
	
				private int GetMovieId( String name){
					//Get selected MovieID to dropdown
					int movie_id = 0;
					try {
						Statement stmt	= mySqlCon.createStatement();
						String sql= "select movie_id from movies where movie_name=" + "'" + name + "'";
						ResultSet rs= stmt.executeQuery(sql);
						while(rs.next()){
							movie_id = rs.getInt(1);
						}
					}
					catch(SQLException e){
						e.printStackTrace();
					}
					return movie_id;
				}
				
				
				private int GetCityId( String name){
					//Get Selected City ID
					int city_id = 0;
					try {
						Statement stmt	= mySqlCon.createStatement();
						String sql= "select city_id from cities where city_name=" + "'" + name + "'";
						ResultSet rs= stmt.executeQuery(sql);
						while(rs.next()){
							city_id = rs.getInt(1);
						}
					}
					catch(SQLException e){
						e.printStackTrace();
					}
					return city_id;
				}

					private int getPrice(String price) {
						int total_price=50*Integer.parseInt(price);
						System.out.println(total_price);
						return total_price;
						
					}
	
}
