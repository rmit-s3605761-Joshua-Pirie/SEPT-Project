package appointmentBookingApp.view;

import appointmentBookingApp.MainApp;
import appointmentBookingApp.util.DbUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.PreparedStatement;
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
    private Label errorMessage;

    private MainApp mainApp;
    //Allow for the control of the main app.
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
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
	 * @throws SQLException produces stack trace.
	 */
	@FXML
	private void handleView() throws SQLException{
		ResultSet resultSet;
        String querySQL;
        String sqlUsername = username.getText().toUpperCase();
		String sqlPassword = password.getText();
		System.out.println("SQLUsername1: " + sqlUsername);
		System.out.println("SQLPassword1: " + sqlPassword);

		querySQL = "SELECT*FROM customer WHERE Username=? AND Password=?";
		PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(querySQL);
		pstmt.setString(1,sqlUsername);
		pstmt.setString(2,sqlPassword);
		resultSet = pstmt.executeQuery();
		if(!resultSet.next()){
			System.out.println("Not customer login");
			querySQL = "SELECT*FROM businessowner WHERE Username=? AND Password=?";
			pstmt = DbUtil.getConnection().prepareStatement(querySQL);
			pstmt.setString(1,sqlUsername);
			pstmt.setString(2,sqlPassword);
			resultSet = pstmt.executeQuery();
			if(!resultSet.next()){
				System.out.println("Not business owner login");
				querySQL = "SELECT*FROM staff WHERE Username=? AND Password=?";
				pstmt = DbUtil.getConnection().prepareStatement(querySQL);
				pstmt.setString(1,sqlUsername);
				pstmt.setString(2,sqlPassword);
				resultSet = pstmt.executeQuery();
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
				mainApp.showBusinessHomepage();
			}
		}
		else{
			System.out.println("Customer Login success");
		}
	}


    public void showBusinessHomepage() {
        mainApp.showBusinessHomepage();
    }
}
