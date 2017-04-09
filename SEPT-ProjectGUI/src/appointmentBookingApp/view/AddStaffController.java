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

import static appointmentBookingApp.util.DbUtil.*;


public class AddStaffController {

	@FXML
	private TextField FirstNameTxtBx;
	
	@FXML
	private TextField SurNameTxtBx;
	
	@FXML
	private TextField PhoneTxtBx;
	
	@FXML
	private TextField AddressTxtBx;
	
	@FXML 
	private Text newID;

	private Stage dialogStage;

	public void initialize(){
    		newID.setText(String.format("S%06d", getNextId()+1));
    }
	void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }
	
	public void addStaff(){
		if(FirstNameTxtBx.getText().trim().isEmpty() || SurNameTxtBx.getText().trim().isEmpty() || PhoneTxtBx.getText().trim().isEmpty() || AddressTxtBx.getText().trim().isEmpty()){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Missing Information");
			alert.setContentText("Please fill in all fields.");

			alert.showAndWait();
		}
		else{
			String firstName = FirstNameTxtBx.getText();
			String lastName = SurNameTxtBx.getText();
			String phone = PhoneTxtBx.getText();
			String Address = AddressTxtBx.getText();
			String ID = newID.getText();
			addStaffToDB(ID, firstName, lastName, phone, Address);

			dialogStage.close();
		}

	}
	
	
	public static int getNextId(){
		String query = "SELECT COUNT(*) AS total FROM staff";
		 int idCount = 0;
	      Statement st;
		try {
			st = getConnection().createStatement();
		      ResultSet rs = st.executeQuery(query);
		      while (rs.next()) {
		    	  idCount = rs.getInt("total");
		    	 }
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	      
		return idCount;
	}

	

	public static boolean addStaffToDB(String newId, String firstName, String lastName, String address, String phone) {
    String sql = "INSERT INTO staff VALUES (?, ?, ?, ?, ?, ?)";
	    try {
	        PreparedStatement ps = getConnection().prepareStatement(sql);
	        ps.setObject(1, newId);
			ps.setString(2, "a");
	        ps.setString(3, firstName);
	        ps.setObject(4, lastName);
	        ps.setObject(5, address);
	        ps.setObject(6, phone);
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
