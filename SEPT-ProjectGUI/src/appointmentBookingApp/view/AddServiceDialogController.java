package appointmentBookingApp.view;

import appointmentBookingApp.util.DbUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class AddServiceDialogController {
    @FXML
    private TextField serviceName;
    @FXML
    private TextField duration;

    private Stage dialogStage;
    private boolean addClicked = false;

    /**
     *Initialize on controller load.
     */
    public void initialize() {
        serviceName.setPromptText("Enter name of service");
        duration.setPromptText("hh:mm");
    }

    /**
     * Sets the stage of this dialog.
     */
    public void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }

    @FXML
    public void handleAdd() throws SQLException {
        if(isValid()){
            String service = serviceName.getText().toUpperCase();
            Time myTime = Time.valueOf(duration.getText());
            String insertSQL;
            insertSQL = "INSERT INTO services VALUES (?,?)";
            PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(insertSQL);
            pstmt.setString(1,service);
            pstmt.setTime(2,myTime);
            pstmt.executeUpdate();

            addClicked = true;
            dialogStage.close();
        }



    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isValid(){
        String errorMessage = "";
        String service = serviceName.getText().toUpperCase();

        if(service.length() == 0){
            errorMessage += "Please define a service you wish to add.\n";
        }

        String querySQL = "";
        ResultSet resultSet;
        PreparedStatement pstmt;
        try {
            pstmt = DbUtil.getConnection().prepareStatement(querySQL);
            pstmt.setString(1,service);
            resultSet = pstmt.executeQuery();
            if(resultSet.next()){
                errorMessage += "Service already exists.\n";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        DateFormat timeFormat = new SimpleDateFormat("hh:mm");
        try {
            timeFormat.parse(duration.getText().toUpperCase());
        } catch (ParseException e) {
            errorMessage += "Time field is in the incorrect format\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

}


