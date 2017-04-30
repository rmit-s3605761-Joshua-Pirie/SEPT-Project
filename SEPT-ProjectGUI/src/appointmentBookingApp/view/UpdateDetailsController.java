package appointmentBookingApp.view;

import appointmentBookingApp.util.Alerts;
import appointmentBookingApp.util.DbUtil;
import appointmentBookingApp.util.Validators;
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
public class UpdateDetailsController {
    @FXML
    private Text UserNameT;

    @FXML
    private TextField PasswordTB;

    @FXML
    private TextField ConfirmPassworkTB;

    @FXML
    private TextField EmailTB;

    @FXML
    private TextField FirstNameTB;

    @FXML
    private TextField SurNameTB;

    @FXML
    private TextField AddressTB;

    @FXML
    private TextField PhoneTB;

    @FXML
    private Text UserNameTxt;

    private Stage dialogStage;
    private String userName;
    String oldPassword;
    String newPassword;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void ini(String user) {
        userName = user;

        String query = "SELECT * FROM customer WHERE username = '" + userName+"'";
        Statement st;
        try {
            st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                UserNameT.setText(rs.getString("username"));
                oldPassword = rs.getString("password");
                EmailTB.setText(rs.getString("eMail"));
                FirstNameTB.setText(rs.getString("firstName"));
                SurNameTB.setText(rs.getString("lastName"));
                AddressTB.setText(rs.getString("address"));
                PhoneTB.setText(rs.getString("phone"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
       }
    }

    public void submit(){
        newPassword = oldPassword;
        if(!PasswordTB.getText().trim().isEmpty() && !ConfirmPassworkTB.getText().trim().isEmpty()){
            if(!PasswordTB.getText().equals(ConfirmPassworkTB.getText()))
                Alerts.error("Error", "Passwords Do Not Match", "Please re enter passwords.");
            else if(!Validators.validate(PasswordTB.getText().trim(), "password"))
                Alerts.error("Error", "Invalid Password", "Password must be 8 characters long, contain 1 upper case, 1 lower case and 1 symbol");
            else
                newPassword = PasswordTB.getText();
        }
        if(EmailTB.getText().trim().isEmpty() || FirstNameTB.getText().trim().isEmpty() || SurNameTB.getText().trim().isEmpty() || AddressTB.getText().trim().isEmpty() || PhoneTB.getText().trim().isEmpty())
            Alerts.error("Error", "Missing Information", "Please fill in all fields.");
        else if(!Validators.validate(EmailTB.getText().trim(), "email"))
            Alerts.error("Error", "Invalid E-Mail", "please re enter your email");
        else if(!Validators.validate(PhoneTB.getText().trim(), "phone"))
            Alerts.error("Error", "Invalid Phone Number", "please re enter your phone number");
        else{
            String Email = EmailTB.getText();
            String Password = newPassword;
            String FirstName = FirstNameTB.getText();
            String SurName = SurNameTB.getText();
            String Address = AddressTB.getText();
            String Phone = PhoneTB.getText();
            if(updateDB( Email, Password, FirstName, SurName, Address, Phone)){
                Alerts.confirm("Success", "Your details were successfully updated", "Click OK to continue");

            }
            else{
                Alerts.error("Error", "There was a problem updating your details", "Please contact your system administrator");
            }
            dialogStage.close();
        }

    }

    private boolean updateDB(String email, String password, String firstName, String surName, String address, String phone){
        String sql = "UPDATE customer SET password = ?, firstName = ?, lastName = ?, eMail = ?, address = ?, phone = ? WHERE username = '"+userName+"'";
        try {
            PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
            ps.setString(1, password);
            ps.setObject(2, firstName);
            ps.setObject(3, surName);
            ps.setObject(4, email);
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
