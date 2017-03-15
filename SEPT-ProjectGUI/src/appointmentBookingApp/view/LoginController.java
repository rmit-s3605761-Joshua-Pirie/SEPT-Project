package appointmentBookingApp.view;

import appointmentBookingApp.MainApp;
import appointmentBookingApp.util.DbUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Class is used to control user login and determine which view is to be shown
 * upon successful login.
 */
public class LoginController {
	@FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button login;
    @FXML
    private Label errorMessage;

	
	/**
	 *Initialize on controller load. 
	 */
	public void initialize() {
        errorMessage.setText("");
        username.setPromptText("username");
        password.setPromptText("password");
        
    }

	/**
	 * Called to verify login and determine which user view is to be shown.
	 * @throws SQLException
	 */
	@FXML
	private void handleView() throws SQLException{
		ResultSet resultSet;
        String querySQL;
		System.out.println("Username: " + username.getText().toUpperCase());
		System.out.println("Password: " + password.getText());

		querySQL = "SELECT*FROM customer WHERE Username = '" + username.getText().toUpperCase() + "' AND Password = '" + password.getText() + "'";
		resultSet = DbUtil.getNewStatment().executeQuery(querySQL);
		if(!resultSet.next()){
			System.out.println("Not customer login");
			querySQL = "SELECT*FROM businessowner WHERE Username = '" + username.getText().toUpperCase()
					+ "' AND Password = '" + password.getText() + "'";
			resultSet = DbUtil.getNewStatment().executeQuery(querySQL);
			if(!resultSet.next()){
				System.out.println("Not business owner login");
				querySQL = "SELECT*FROM staff WHERE Username = '" + username.getText().toUpperCase()
						+ "' AND Password = '" + password.getText() + "'";
				resultSet = DbUtil.getNewStatment().executeQuery(querySQL);
				if(!resultSet.next()){
					System.out.println("Not staff login");
					errorMessage.setText("Username/Password is incorrect");
				}
				else{
					System.out.println("Staff Login success");
				}
			}
			else{
				System.out.println("Business Owner Login success");
			}
		}
		else{
			System.out.println("Customer Login success");
		}
	}
}
