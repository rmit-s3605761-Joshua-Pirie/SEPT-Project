package appointmentBookingApp.view;

import appointmentBookingApp.util.Alerts;
import appointmentBookingApp.util.DbUtil;
import appointmentBookingApp.util.Validators;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterBusinessController {
    @FXML
    private TextField userName, businessName, firstName, surname, address, phone;

    @FXML
    private PasswordField password, password2;

    @FXML
    private ComboBox<String> sHour, sMin, eHour, eMin;
    private Stage dialogStage;
//  Sets the stage of this dialog.
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

//    This method is called by the FXMLLoader when initialization is complete
    public void initialize(){
        String[] hours = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
        String[] min = {"00","15","30","45"};
        sHour.getItems().addAll(hours);
        sMin.getItems().addAll(min);
        eHour.getItems().addAll(hours);
        eMin.getItems().addAll(min);
    }

//  When "cancel" button is selected, closes current dialog box.
    public void handleCancel() {
        dialogStage.close();
    }

//  Function called when "register" button is selected.
    public void addBusiness(){
        boolean useAlertBox = true;
        if(isValid(useAlertBox)){
            if(addBusinessToDB()){
                Alerts.confirm("Success", "Business Added to database.", "Click OK to continue");
            }
            else{
                Alerts.error("Error", "There was a problem adding the customer to database", "Please contact your system administrator");
            }
            dialogStage.close();
        }
    }

//  Validates all input fields in the current window.
    private boolean isValid(boolean useAlertBox){
        String errorMessage = "";

        if(userName.getText().trim().isEmpty() || password.getText().trim().isEmpty()
                || businessName.getText().trim().isEmpty() || firstName.getText().trim().isEmpty()
                || surname.getText().trim().isEmpty() || address.getText().trim().isEmpty()
                || phone.getText().trim().isEmpty() || sHour.getSelectionModel().isEmpty()
                || sMin.getSelectionModel().isEmpty() || eHour.getSelectionModel().isEmpty()
                || eMin.getSelectionModel().isEmpty()){
            errorMessage += "Please fill in all fields.\n";
        }else{
            if(!password.getText().equals(password2.getText()))
                errorMessage += "Passwords Do Not Match, please re-enter\n";
            if(!Validators.validate(password.getText().trim(), "password"))
                errorMessage += "Password must be 8 characters long, contain 1 upper case, 1 lower case and 1 symbol\n";
            if(businessNameExists(businessName.getText()))
                errorMessage += "Business name already exits\n";
            if(userNameExists(userName.getText()))
                errorMessage += "Username already exists\n";
            if(!Validators.validate(phone.getText().trim(), "phone"))
                errorMessage += "Invalid phone number, must be 8-10 digits\n";
        }

        return Alerts.genErrorMessage(errorMessage, useAlertBox);
    }

//    Checks database to ensure chosen username does not already exist.
    private boolean userNameExists(String userName) {
        String sql = "SELECT COUNT(*) AS total FROM customer WHERE userName = '" + userName +"'";
        int count = 0;
        try {
            ResultSet rs = DbUtil.getNewStatment().executeQuery(sql);
            while (rs.next())
                count += rs.getInt("total");
            sql = "SELECT COUNT(*) AS total FROM businessowner WHERE userName = '" + userName +"'";
            rs = DbUtil.getNewStatment().executeQuery(sql);
            while (rs.next())
                count += rs.getInt("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count > 0;
    }

//    Checks database to ensure chosen business name does not already exists
    private boolean businessNameExists(String businessName) {
        String sql = "SELECT COUNT(*) AS total FROM businessowner WHERE businessName = '" + businessName +"'";
        int count = 0;
        try {
            ResultSet rs = DbUtil.getNewStatment().executeQuery(sql);
            while (rs.next())
                count += rs.getInt("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count > 0;
    }

//    Adds new business details to the database.
    private boolean addBusinessToDB( ) {
        String sql = "INSERT INTO businessowner VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
            ps.setString(1, userName.getText());
            ps.setObject(2, password.getText());
            ps.setObject(3, firstName.getText());
            ps.setObject(4, surname.getText());
            ps.setObject(5, businessName.getText());
            ps.setObject(6, address.getText());
            ps.setObject(7, phone.getText());
            ps.setObject(8, sHour.getValue()+":"+sMin.getValue());
            ps.setObject(9, eHour.getValue()+":"+eMin.getValue());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
