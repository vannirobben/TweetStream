import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Servlet implementation class getTweet
 */
@WebServlet("/getTweet")
public class getTweet extends HttpServlet {
	static final String dbName = "";
    static final String userName = "";
    static final String password = "";
    static final String hostname = "";
    static final String port = "";
	Connection conn = null;
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getTweet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		String keyword = request.getParameter("keyword");
		//System.out.println(keyword);
		getTweetLatlngByKeyWord(response, keyword);
	}
	
	@SuppressWarnings("unchecked")
	private void getTweetLatlngByKeyWord(HttpServletResponse response, String keyword) {
		connectDB();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			String query = "SELECT latitude, longitude from DB1 WHERE ttext LIKE" + 
						   "'" + keyword + "'";
			rs = stmt.executeQuery(query);
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    // constructing the json array
		JSONArray listofLatlng = new JSONArray();
		try {
				while(rs.next()) {
					Double lat = rs.getDouble("latitude");
					Double lng = rs.getDouble("longitude");
					//System.out.println(lat + " " + lng);
					JSONArray latlng = new JSONArray();
					latlng.add(lat);
					latlng.add(lng);
					listofLatlng.add(latlng);
				}
			    response.getWriter().write(listofLatlng.toString());
			    //System.out.println(listofLatlng.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try{
			stmt.close();
			conn.close();
		}catch (SQLException e){
			System.out.println("SQLException: " + e.getMessage());
		}
	}

	private void connectDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
		    conn = DriverManager.getConnection(jdbcUrl);
		} catch (SQLException e) {
		    // handle any errors
		    System.out.println("SQLException: " + e.getMessage());
		}
	}
}
