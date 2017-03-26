package appointmentBookingApp.util;

import java.sql.*;

/**
 * Utilities class that handles connections to the database.
 */
public class DbUtil {
	private static Connection connection;
	private static String driver = "com.mysql.jdbc.Driver";
//	private static String url = "jdbc:mysql://localhost:3306/bookingsystem";
//	private static String user = "root";
//	private static String password = "pass";

    private static String url = "jdbc:mysql://jimpi27.arges.feralhosting.com:31337/bookingsystem";
    private static String user = "sept";
    private static String password = "septdb17";

	
	/**
	 * Opens connection to the database.
	 * @return
	 */
	public static Statement databaseConnect(){
		
		try
		  {

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
	 * adds new customer to the database.
	 * @param username
	 * @param password
	 * @param address
	 * @param firstName
     * @param lastName
	 * @param phone
     * @return whether adding the customer succeeded
	 */
	public static boolean addCustomerToDB(String username, String password, String firstName, String lastName, String address, String phone){
		String sql = "INSERT INTO customer VALUES ('" + username + "', '" + password + "', '" + firstName + "', '" + lastName + "', '" + address + "', '" + phone + "')";
		try {
			DbUtil.getNewStatment().executeUpdate(sql);
            return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}


    /**
     * adds a booking to the database.
     * @param username user making the booking
     * @param startTime time the booking starts
     * @param endTime time the booking ends
     * @return whether adding the booking succeeded
     */
    public static boolean addBookingToDB(String username, java.util.Date startTime, java.util.Date endTime) {
        Timestamp startTimeTs = new java.sql.Timestamp(startTime.getTime());
        Timestamp endTimeTs = new java.sql.Timestamp(endTime.getTime());
        String sql = "INSERT INTO booking VALUES ('?', '?', '?')";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(0, username);
            ps.setObject(1, startTimeTs);
            ps.setObject(2, endTimeTs);
            ps.executeQuery();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
