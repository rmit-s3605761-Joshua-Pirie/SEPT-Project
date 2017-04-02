package appointmentBookingApp.util;

import java.sql.*;

/**
 * Utilities class that handles connections to the database.
 */
public class DbUtil {
	private static Connection connection;
	private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:3306/bookingsystem";
	private static String user = "root";
	private static String password = "pass";

//	String url = "jdbc:mysql://jimpi27.arges.feralhosting.com:31337/bookingsystem";
//	String user = "sept";
//	String password = "septdb17";

	
	/**
	 * Opens connection to the database.
	 * @return
	 */
	public static Statement databaseConnect(){
		
		try {
		   Class.forName(driver);
		   connection = DriverManager.getConnection(url, user, password);
		   System.out.println("connected...");
		   return connection.createStatement();
		}catch(Exception e){
			e.printStackTrace();
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
	


	
	/**
	 * adds new customer to the database.
	 * @param username
	 * @param password
	 * @param address
	 * @param name
	 * @param phone
	 */
	public static void addCustomerToDB(String username, String password, String address, String name, String phone){
		String sql = "INSERT INTO customer VALUES ('" + username + "', '" + password + "', '" + address + "', '" + name + "', '" + phone + "')";
		try {
			DbUtil.getNewStatment().executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean userExists(String userType, String username) {
		String sql = "SELECT COUNT(*) AS total FROM " + userType + " WHERE " + userType + ".Username = " + username;
		try {
			ResultSet rs = DbUtil.getNewStatment().executeQuery(sql);
			if(rs.getInt("total") > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Array getEmpAvailability(String username) {
		String sql = "SELECT * AS availability FROM availability WHERE availability.Username = " + username;
		try {
			ResultSet rs = DbUtil.getNewStatment().executeQuery(sql);
			return rs.getArray("availability");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
