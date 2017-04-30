package appointmentBookingApp.view;

import appointmentBookingApp.util.Alerts;
import appointmentBookingApp.util.DbUtil;
import appointmentBookingApp.util.Validators;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static appointmentBookingApp.util.DbUtil.getConnection;

/**
 * Created by David on 30/04/2017.
 */
public class UpdateStaffDetailsController {
    @FXML
    private TextField staffIDTB;

    @FXML
    private TextField passwordTB;

    @FXML
    private TextField confirnPasswordTB;

    @FXML
    private TextField firstNameTB;

    @FXML
    private TextField lastNameTB;

    @FXML
    private TextField addressTB;

    @FXML
    private TextField phoneTB;


    private Stage dialogStage;
    private String userName;
    String oldPassword;
    String newPassword;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    public void find() {
        userName = staffIDTB.getText();

        String query = "SELECT * FROM staff WHERE staffID = '" + staffIDTB.getText()+"'";
        Statement st;
        try {
            st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                oldPassword = rs.getString("password");
                firstNameTB.setText(rs.getString("firstName"));
                lastNameTB.setText(rs.getString("lastName"));
                addressTB.setText(rs.getString("address"));
                phoneTB.setText(rs.getString("phone"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void submit(){
        newPassword = oldPassword;
        if(!passwordTB.getText().trim().isEmpty() && !confirnPasswordTB.getText().trim().isEmpty()){
            if(!passwordTB.getText().equals(confirnPasswordTB.getText()))
                Alerts.error("Error", "Passwords Do Not Match", "Please re enter passwords.");
            else if(!Validators.validate(passwordTB.getText().trim(), "password"))
                Alerts.error("Error", "Invalid Password", "Password must be 8 characters long, contain 1 upper case, 1 lower case and 1 symbol");
            else
                newPassword = passwordTB.getText();
        }
        if(firstNameTB.getText().trim().isEmpty() || lastNameTB.getText().trim().isEmpty() || addressTB.getText().trim().isEmpty() || phoneTB.getText().trim().isEmpty())
            Alerts.error("Error", "Missing Information", "Please fill in all fields.");
        else if(!Validators.validate(phoneTB.getText().trim(), "phone"))
            Alerts.error("Error", "Invalid Phone Number", "please re enter your phone number");
        else{
            String Password = newPassword;
            String FirstName = firstNameTB.getText();
            String SurName = lastNameTB.getText();
            String Address = addressTB.getText();
            String Phone = phoneTB.getText();
            if(updateDB(Password, FirstName, SurName, Address, Phone)){
                Alerts.confirm("Success", "Your details were successfully updated", "Click OK to continue");

            }
            else{
                Alerts.error("Error", "There was a problem updating your details", "Please contact your system administrator");
            }
            dialogStage.close();
        }

    }

    private boolean updateDB(String password, String firstName, String surName, String address, String phone){
        String sql = "UPDATE staff SET password = ?, firstName = ?, lastName = ?, address = ?, phone = ? WHERE staffID = '"+userName+"'";
        try {
            PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
            ps.setString(1, password);
            ps.setObject(2, firstName);
            ps.setObject(3, surName);
            ps.setObject(4, address);
            ps.setObject(5, phone);
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
