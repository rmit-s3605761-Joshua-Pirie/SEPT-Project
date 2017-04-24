package appointmentBookingApp.view;

import appointmentBookingApp.model.Availability;
import appointmentBookingApp.model.Day;
import appointmentBookingApp.util.DbUtil;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by Joshua on 19/04/2017.
 */
public class SetEmpAvailabilityController {

    @FXML
    private TextField staffID;
    @FXML
    private ComboBox dayCombo;
    @FXML
    private CheckBox toggle8;
    @FXML
    private CheckBox toggle9;
    @FXML
    private CheckBox toggle10;
    @FXML
    private CheckBox toggle11;
    @FXML
    private CheckBox toggle12;
    @FXML
    private CheckBox toggle13;
    @FXML
    private CheckBox toggle14;
    @FXML
    private CheckBox toggle15;
    @FXML
    private CheckBox toggle16;
    @FXML
    private CheckBox toggle17;

    private Stage dialogStage;
    private boolean addClicked = false;
    private CheckBox[] toggles;
    private Availability availability;
    private Day day;

    /**
     *Initialize on controller load.
     */
    public void initialize() {
        dayCombo.getSelectionModel().select("Monday");
        day = Day.MONDAY;
        availability = new Availability();
        toggles = new CheckBox[]{toggle8, toggle9, toggle10, toggle11, toggle12, toggle13, toggle14, toggle15, toggle16, toggle17};
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
        if(userExists(staffID.getText())) {
            availability.addAvailabilityToDB(staffID.getText());
            addClicked = true;
            dialogStage.close();
        }
        else
            System.err.println("User does not exist.");
    }

    @FXML
    public void handleDay() {
        day = Day.values()[dayCombo.getSelectionModel().getSelectedIndex()];
        for(CheckBox c : toggles)
            c.setSelected(false);
        for(int t : availability.getAvailability(day))
            toggles[t - 8].setSelected(true);
    }

    @FXML
    public void handleToggle() {
        availability.resetDay(day);
        for(int i = 0; i < toggles.length; i++)
            if(toggles[i].isSelected())
                availability.setAvailability(day, i + 8);
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    public void handleCancel() {
        dialogStage.close();
    }

    public static boolean userExists(String staffID) {
        String sql = "SELECT COUNT(*) FROM staff WHERE staff.staffID='" + staffID + "'";
        try {
            ResultSet rs = DbUtil.getNewStatment().executeQuery(sql);
            rs.next();
            if(rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
