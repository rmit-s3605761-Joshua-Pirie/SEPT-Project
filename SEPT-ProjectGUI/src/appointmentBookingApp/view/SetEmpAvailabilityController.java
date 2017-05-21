package appointmentBookingApp.view;

import appointmentBookingApp.MainApp;
import appointmentBookingApp.model.Availability;
import appointmentBookingApp.model.Day;
import appointmentBookingApp.util.DbUtil;
import com.sun.org.apache.xerces.internal.dom.ChildNode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.scene.control.*;

import javax.swing.*;
import java.sql.PreparedStatement;
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
    private GridPane availabilityGrid;

    private Stage dialogStage;
    private boolean addClicked = false;
    private ArrayList<CheckBox> toggles = new ArrayList<>();
    private Availability availability;
    private Day day;
    private int startHour = 0;
    private int endHour = 0;

    /**
     *Initialize on controller load.
     */
    public void initialize() {
        dayCombo.getSelectionModel().select("Monday");
        day = Day.MONDAY;
        availability = new Availability();
        populateGrid();
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

    void populateGrid() {
        String sql = "SELECT sTime, eTime FROM businessowner WHERE businessName = ?";
        try {
            PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
            ps.setString(1, MainApp.getBusiness());
            ResultSet rs = ps.executeQuery();
            rs.next();
            startHour = Integer.parseInt(rs.getString("sTime").substring(0, 2));
            endHour = Integer.parseInt(rs.getString("eTime").substring(0, 2));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int column = 0;
        int row = 0;
        availabilityGrid.setPadding(new Insets(0, 0, 0, 20));
        for(int i = startHour; i < endHour; i++) {
            CheckBox c = new CheckBox(i + ":00");
            c.setMinHeight(30);
            c.setMinWidth(100);
            c.setOnMouseClicked(e -> {
                availability.resetDay(day);
                for(int j = 0; j < toggles.size(); j++)
                    if(toggles.get(j).isSelected())
                        availability.setAvailability(day, j + startHour);
            });
            toggles.add(c);
            availabilityGrid.add(c, column, row);

            column++;
            if(column > 2) {
                column = 0;
                row++;
            }
        }
    }

    @FXML
    public void handleAdd() {
        if(userExists(staffID.getText())) {
            availability.addAvailabilityToDB(staffID.getText().toUpperCase());
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
            toggles.get(t - startHour).setSelected(true);
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
