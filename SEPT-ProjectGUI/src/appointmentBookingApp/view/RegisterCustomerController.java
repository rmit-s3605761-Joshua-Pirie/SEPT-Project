package appointmentBookingApp.view;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import appointmentBookingApp.util.Alerts;
import appointmentBookingApp.util.DbUtil;
import appointmentBookingApp.util.Validators;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static appointmentBookingApp.util.DbUtil.getConnection;


public class RegisterCustomerController {

	@FXML
	private TextField UserName;
	
	@FXML
	private PasswordField Password;

	@FXML
	private PasswordField Password2;
	
	@FXML
	private TextField eMail;
	
	@FXML
	private TextField FirstName;
	
	@FXML 
	private TextField Surname;
	
	@FXML 
	private TextField Address;
	
	@FXML 
	private TextField Phone;
	


	private Stage dialogStage;
	


	void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }

	public void addCustomer(){
		if(UserName.getText().trim().isEmpty() || Password.getText().trim().isEmpty() || eMail.getText().trim().isEmpty() || FirstName.getText().trim().isEmpty() || Surname.getText().trim().isEmpty() || Address.getText().trim().isEmpty() || Phone.getText().trim().isEmpty()){
			Alerts.error("Error", "Missing Information", "Please fill in all fields.");
		}
		else if(!Password.getText().equals(Password2.getText()))
			Alerts.error("Error", "Passwords Do Not Match", "Please re enter passwords.");
		else if(!Validators.validate(Password.getText().trim(), "password"))
			Alerts.error("Error", "Invalid E-Mail", "Password must be 8 characters long, contain 1 upper case, 1 lower case and 1 symbol");
		else if(!Validators.validate(eMail.getText().trim(), "email"))
			Alerts.error("Error", "Invalid E-Mail", "please re enter your email");
		else if(userNameExists(UserName.getText()))
			Alerts.error("Error", "Username already exists", "please choose another username");
		else if(!Validators.validate(Phone.getText().trim(), "phone"))
			Alerts.error("Error", "Invalid Phone Number", "please re enter your phone number");
		else{
			String UserNameSt = UserName.getText();
			String PasswordSt = Password.getText();
			String eMailSt = eMail.getText();
			String FirstNameSt = FirstName.getText();
			String SurnameSt = Surname.getText();
			String AddressSt = Address.getText();
			String PhoneSt = Phone.getText();
			if(addToDB( UserNameSt, PasswordSt, FirstNameSt, SurnameSt, eMailSt, AddressSt, PhoneSt)){
				Alerts.confirm("Success", "Customer Added to database.", "Click OK to continue");

			}
			else{
				Alerts.error("Error", "There was a problem adding the customer to database", "Please contact your system administrator");
			}
			dialogStage.close();
		}
	}

	
	public static boolean addToDB( String UserNameSt, String PasswordSt,
			String FirstNameSt, String SurnameSt, String eMailSt, String addressSt, String phoneSt) {
    String sql = "INSERT INTO customer VALUES ( ?, ?, ?, ?, ?, ?, ?)";
	    try {
	        PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
	        ps.setString(1, UserNameSt);
	        ps.setObject(2, PasswordSt);
	        ps.setObject(3, FirstNameSt);
	        ps.setObject(4, SurnameSt);
	        ps.setObject(5, eMailSt);
	        ps.setObject(6, addressSt);
	        ps.setObject(7, phoneSt);
	        ps.executeUpdate();
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	private boolean userNameExists(String username){
		String query = "SELECT COUNT(*) AS total FROM customer WHERE userName = '" + username +"'";
		int count = 0;
		Statement st;
		try {
			st = getConnection().createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				count = rs.getInt("total");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String query2 = "SELECT COUNT(*) AS total FROM businessowner WHERE userName = '" + username +"'";
		int count2 = 0;
		Statement stm;
		try {
			stm = getConnection().createStatement();
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				count = rs.getInt("total");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	if(count > 0)
		return true;
	else
		return false;
	}


	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
}
