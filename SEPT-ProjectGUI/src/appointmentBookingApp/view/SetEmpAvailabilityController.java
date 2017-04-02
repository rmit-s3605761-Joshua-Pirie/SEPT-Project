package appointmentBookingApp.view;

import appointmentBookingApp.MainApp;
import appointmentBookingApp.util.DbUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Array;
import java.sql.ResultSet;

public class SetEmpAvailabilityController {
    @FXML
    private TextField username;
    @FXML
    private ComboBox dayCombo;

    private Stage dialogStage;
    private boolean addClicked = false;

    /**
     *Initialize on controller load.
     */
    public void initialize() {
        username.setPromptText("Enter staff ID");
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
    public void handleAdd() {
        String usernameStr = username.getText();
        if(DbUtil.userExists("staff", usernameStr)) {


            addClicked = true;
            dialogStage.close();
        }
        else
            System.err.println("User does not exist.");
    }

    @FXML
    public void handleDay() {
        String usernameStr = username.getText();
        if(DbUtil.userExists("staff", usernameStr)) {


            addClicked = true;
            dialogStage.close();
        }
        else
            System.err.println("User does not exist.");
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    public void handleCancel() {
        dialogStage.close();
    }
}