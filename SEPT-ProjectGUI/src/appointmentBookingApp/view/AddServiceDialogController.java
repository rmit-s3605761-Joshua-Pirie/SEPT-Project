package appointmentBookingApp.view;

import appointmentBookingApp.util.DbUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }

    /**
     * @return Returns true if the user clicked Add, false otherwise.
     */
    boolean isAddClicked() {
        return addClicked;
    }

    @FXML
    public void handleAdd() throws SQLException {
        String service = serviceName.getText().toUpperCase();
        String duration = this.duration.getText();
        boolean useAlertBox = true;
        if(isValid(service, duration, useAlertBox)){
            addServiceToDB(service, duration);
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

    public boolean isValid(String service, String duration, boolean useAlertBox){
        String errorMessage = "";

        if(service.length() == 0){
            errorMessage += "Please define a service you wish to add.\n";
//            System.err.println("Please define a service you wish to add.");
        }

        String querySQL = "SELECT service FROM services WHERE service=?";
        ResultSet resultSet;
        PreparedStatement pstmt;
        try {
            pstmt = DbUtil.getConnection().prepareStatement(querySQL);
            pstmt.setString(1,service);
            resultSet = pstmt.executeQuery();
            if(resultSet.next()){
                errorMessage += "Service already exists.\n";
//                System.err.println("Service already exists.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        DateFormat timeFormat = new SimpleDateFormat("hh:mm");
        if(duration.length() == 0){
            errorMessage += "Duration field cannot be empty.\n";
//            System.err.println("Duration field cannot be empty.");
        }else{
            try {
                timeFormat.parse(duration);
            } catch (ParseException e) {
                errorMessage += "Time field is in the incorrect format.\n";
//                System.err.println("Time field is in the incorrect format.");
            }
        }


        if (errorMessage.length() == 0) {
            return true;
        } else {
            if(useAlertBox){
                // Show the error message.
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(dialogStage);
                alert.setTitle("Invalid Fields");
                alert.setHeaderText("Please correct invalid fields");
                alert.setContentText(errorMessage);

                alert.showAndWait();
            }
            return false;
        }
    }

    public boolean addServiceToDB(String service, String duration){
        try {
            String insertSQL = "INSERT INTO services VALUES (?,?)";
            PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(insertSQL);
            pstmt.setString(1,service);
            pstmt.setString(2,duration);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
//            e.printStackTrace();
            System.err.println("Failed to add SERVICE to database");
            return false;
        }
    }

}


