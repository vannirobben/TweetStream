
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * <p>This is a code example of Twitter4J Streaming API - sample method support.<br>
 * Usage: java twitter4j.examples.PrintSampleStream<br>
 * </p>
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class TweetGet {
    
	/**
     * Main entry of this application.
     *
     * @param args
     */
	static PrintWriter writer;
	static int counter = 0;
	static TwitterStream twitterStream;
	
	//database
	static final String dbName = "TwitterDB";
    static final String userName = "vannirobben"; 
    static final String password = "918273Ybf"; 
    static final String hostname = "aa1jv5lzy76q9qz.cfhq9puir1a1.us-east-1.rds.amazonaws.com";
    static final String port = "3306";
    
    static Queue<Status> myQueue = new PriorityQueue<Status>();
    
    static Connection conn;
	
    public static void main(String[] args) throws TwitterException, InterruptedException {
    	//connect to database
    	try {
			connect_to_db();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    	
    	//Twitter connect
    	 ConfigurationBuilder cb = new ConfigurationBuilder();
         cb.setDebugEnabled(true)
           .setOAuthConsumerKey("Z3jU6h67JPSFpsvMFF1oRnkYE")
           .setOAuthConsumerSecret("iiy4BnLNgxg15NpUQ7frUMrySm4nzinSKibRbiPuutx0G5kxPJ")
           .setOAuthAccessToken("537403242-gwVka5YUGymdSTfCq5m8JBGnLkn7HE0UZxt8PGNF")
           .setOAuthAccessTokenSecret("M0g7mZLatLSrXWOj7vJtBFLFmcRu2bA0MH1EmsX97mIyc");
         
        twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        
        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
            	if (myQueue.size() < 10000 && status.getGeoLocation() != null)
            		myQueue.add(status);
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                //System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        twitterStream.addListener(listener);
        twitterStream.sample();
        
        while (counter>=0){
        	Thread.sleep(50);
        	if (!myQueue.isEmpty()){
        		handle_status(myQueue.remove());	
        	}
        }
    }
    
    public static void connect_to_db() throws ClassNotFoundException{
    	String jdbcUrl = "jdbc:mysql://" + hostname + ":" + "3306" + "/" + dbName + "?user=" + userName + "&password=" + password;
    	
    	Class.forName("com.mysql.jdbc.Driver");
    	
    	System.out.println("Start connecting");
    	try{
    		conn = DriverManager.getConnection(jdbcUrl);
    		System.out.println("Connected");
    	} catch (SQLException ex){
    		System.out.println("Connection error: " + ex.getMessage());
    		System.out.println("SQLstate: " + ex.getSQLState());
    		System.out.println("Vendor Error: " + ex.getErrorCode());
    	}
    
     }
    
    public static void handle_status(Status status){
    	double latitude = 0.0;
    	double longitude = 0.0;
    	
    	if (status.getGeoLocation() != null){
    		latitude = status.getGeoLocation().getLatitude();
    		longitude = status.getGeoLocation().getLongitude();
    	}
    	
    	
    	String name = status.getUser().getScreenName();
    	String time =  status.getCreatedAt().toString();
    	String text = status.getText();
       	
       	//write into database
       	Statement stmt = null;
       	PreparedStatement preStatement = null;
		try {
			stmt = conn.createStatement();
			String query = "insert into DB1 (tname, latitude, longitude, ttime, ttext) values (?, ?, ?, ?, ?)";
			preStatement = conn.prepareStatement(query);
			preStatement.setString(1, name);
			preStatement.setDouble(2, latitude);
			preStatement.setDouble(3, longitude);
			preStatement.setString(4, time);
			preStatement.setString(5, text);
			preStatement.executeUpdate();
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			twitterStream.shutdown();
			try {
				preStatement.close();
				conn.close();
			} catch (SQLException ee) {
				// TODO Auto-generated catch block
				ee.printStackTrace();
			}
		}finally{
			try{
				stmt.close();
			}catch (SQLException e){
			}
		}
       	
		//get row size
		int size = 0;
        try {
			stmt = conn.createStatement();
			String query = "SELECT COUNT(*) as Tnum FROM DB1;";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()){
				size = rs.getInt("Tnum");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally{
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
       
    	if (size > 1000000){
    		try {
				stmt = conn.createStatement();
				String query = "DELETE FROM DB1 ORDER BY ID ASC LIMIT 1";
				stmt.execute(query);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
    		
    	}
    }
}