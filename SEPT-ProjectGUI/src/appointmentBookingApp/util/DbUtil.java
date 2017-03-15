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
		
		try
		  {
		   String driver = "com.mysql.jdbc.Driver";
		   String url = "jdbc:mysql://localhost:3306/bookingsystem";
		   String user = "root";
		   String password = "pass";
		   
		   Class.forName(driver);
		   
		   connection = DriverManager.getConnection(url, user, password);
		   System.out.println("connected...");
		   Statement statment = connection.createStatement();
		   return statment;
		   
		  }
		  catch(Exception e)
		  {
		   System.out.println(e);
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
		Statement statement = connection.createStatement();
		return statement;
	}
	

	/**
	 * Used in generating a new customerID, this is done by counting the number of already existing customers.
	 * @return
	 */
	public static int getUserCount(){
    	ResultSet resultSet;
		try {
			resultSet = DbUtil.getNewStatment().executeQuery("SELECT COUNT(username) AS total FROM customers");
			if(resultSet.next()){
				System.out.println("total: "+ resultSet.getInt("total"));
				return resultSet.getInt("total");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
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
		String sql = "INSERT INTO customers VALUES ('" + username + "', '" + password + "', '" + address + "', '" + name + "', '" + phone + "')";
		try {
			DbUtil.getNewStatment().executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
