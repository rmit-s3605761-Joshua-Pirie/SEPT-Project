package appointmentBookingApp.view;

import appointmentBookingApp.MainApp;
import appointmentBookingApp.util.Alerts;
import appointmentBookingApp.util.DbUtil;
import javafx.fxml.FXML;
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
    private MainApp mainApp;
    private String business;

    //Allow for the control of the main app.
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     *Initialize on controller load.
     */
    public void initialize() {
        serviceName.setPromptText("Enter name of service");
        duration.setPromptText("hh:mm");
    }

    /**
     *Set variables.
     */
    public void ini() {
        serviceName.setPromptText("Enter name of service");
        duration.setPromptText("hh:mm");
        this.business = MainApp.getBusiness();
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
            addServiceToDB(service, duration, business);
            addClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    public void handleCancel() {
        dialogStage.close();
    }

    public boolean isValid(String service, String duration, boolean useAlertBox){
        String errorMessage = "";

        if(service.length() == 0){
            errorMessage += "Please define a service you wish to add.\n";
//            System.err.println("Please define a service you wish to add.");
        }

        String querySQL = "SELECT service FROM services WHERE service=? AND businessName = ?";
        ResultSet resultSet;
        PreparedStatement pstmt;
        try {
            pstmt = DbUtil.getConnection().prepareStatement(querySQL);
            pstmt.setString(1,service);
            pstmt.setObject(2,business);
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
        return Alerts.genErrorMessage(errorMessage, useAlertBox);
    }

    public boolean addServiceToDB(String service, String duration, String business){
        try {
            String insertSQL = "INSERT INTO services VALUES (?,?,?)";
            PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(insertSQL);
            pstmt.setString(1,service);
            pstmt.setString(2,duration);
            pstmt.setObject(3,business);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
//            e.printStackTrace();
            System.err.println("Failed to add SERVICE to database");
            return false;
        }
    }

}


