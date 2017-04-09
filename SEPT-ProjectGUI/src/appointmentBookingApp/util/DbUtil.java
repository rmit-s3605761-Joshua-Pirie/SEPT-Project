package appointmentBookingApp.util;

import java.sql.*;

/**
 * Utilities class that handles connections to the database.
 */
public class DbUtil {
	private static Connection connection;
	/**
	 * Opens connection to the database.
	 * @return
	 */
	public static Statement databaseConnect(){

		String driver = "com.mysql.jdbc.Driver";
//		String url = "jdbc:mysql://localhost:3306/bookingsystem?useSSL=false";
//		String user = "root";
//		String password = "pass";

		String url = "jdbc:mysql://jimpi27.arges.feralhosting.com:31337/bookingsystem";
		String user = "sept";
		String password = "septdb17";
		try {
		   	Class.forName(driver);
		   	connection = DriverManager.getConnection(url, user, password);
		   	System.out.println("connected...");
		   	return connection.createStatement();
		}catch(Exception e){
//			e.printStackTrace();
		}
		  return null;		
	}
	
	
	public static Connection getConnection(){
		return connection;
	}
	
	
	/**
	 * Creates new statement to be used for a result set.
	 * @return
	 * @throws SQLException
	 */
	public static Statement getNewStatment() throws SQLException{
		return connection.createStatement();
	}





	
}
