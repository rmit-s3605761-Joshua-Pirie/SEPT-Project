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
    public void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }

    /**
     * Returns true if the user clicked Add, false otherwise.
     *
     * @return
     */
    public boolean isAddClicked() {
        return addClicked;
    }

    @FXML
    public void handleAdd() throws SQLException {
        if(isValid()){
            String service = serviceName.getText().toUpperCase();
            String myTime = duration.getText();
            String insertSQL;
            insertSQL = "INSERT INTO services VALUES (?,?)";
            PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(insertSQL);
            pstmt.setString(1,service);
            pstmt.setString(2,myTime);
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
        String duration = this.duration.getText().toUpperCase();

        if(service.length() == 0){
            errorMessage += "Please define a service you wish to add.\n";
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        DateFormat timeFormat = new SimpleDateFormat("hh:mm");
        if(duration.length() == 0){
            errorMessage += "Duration field cannot be empty.\n";
        }else{
            try {
                timeFormat.parse(duration);
            } catch (ParseException e) {
                errorMessage += "Time field is in the incorrect format\n";
            }
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


