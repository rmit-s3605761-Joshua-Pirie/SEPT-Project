package appointmentBookingApp.view;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import appointmentBookingApp.util.DbUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class RegisterCustomerController {

	@FXML
	private TextField UserName;
	
	@FXML
	private TextField Password;
	
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
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Missing Information");
				alert.setContentText("Please fill in all fields.");
		
				alert.showAndWait();
		}
		else{
			String UserNameSt = UserName.getText();
			String PasswordSt = Password.getText();
			String eMailSt = eMail.getText();
			String FirstNameSt = FirstName.getText();
			String SurnameSt = Surname.getText();
			String AddressSt = Address.getText();
			String PhoneSt = Phone.getText();
			if(addToDB( UserNameSt, PasswordSt, FirstNameSt, SurnameSt, eMailSt, AddressSt, PhoneSt)){
				Alert confirm = new Alert(AlertType.CONFIRMATION);
				confirm.setTitle("Success");
				confirm.setHeaderText("The information was added to the database");
				confirm.setContentText("Click OK to continue");
		
				confirm.showAndWait();

				dialogStage.close();
			}
				
			
			
		}

	}

	
	public static boolean addToDB( String UserNameSt, String PasswordSt,
			String FirstNameSt, String SurnameSt, String eMailSt, String addressSt, String phoneSt) {
    String sql = "INSERT INTO customer VALUES ( ?, ?, ?, ?, ?, ?, ?)";
	    try {
	        PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
	        ps.setString(1, UserNameSt);
	        ps.setObject(2, PasswordSt);
	        ps.setObject(3, eMailSt);
	        ps.setObject(4, FirstNameSt);
	        ps.setObject(5, SurnameSt);
	        ps.setObject(6, addressSt);
	        ps.setObject(7, phoneSt);
	        ps.executeUpdate();
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
}
