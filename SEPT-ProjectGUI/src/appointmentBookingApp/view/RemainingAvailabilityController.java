package appointmentBookingApp.view;

import appointmentBookingApp.MainApp;
import appointmentBookingApp.model.AvailabilityList;
import appointmentBookingApp.model.Bookings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.sql.SQLException;

public class RemainingAvailabilityController {
    private Stage dialogStage;
    private MainApp mainApp;
    ObservableList<AvailabilityList> availability = FXCollections.observableArrayList();

    //Allow for the control of the main app.
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Sets the stage of this dialog.
     */
    void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public void ini() throws SQLException {
//        sTimeColumn.setCellValueFactory(cellData -> cellData.getValue().sTimeProperty());
//        serviceColumn.setCellValueFactory(cellData -> cellData.getValue().serviceProperty());
//        customerColumn.setCellValueFactory(cellData -> cellData.getValue().customerProperty());
//        employeeColumn.setCellValueFactory(cellData -> cellData.getValue().empNameProperty());
//        employeeColumn.setCellValueFactory(cellData -> cellData.getValue().staffIDProperty());
        availability.setAll(AvailabilityList.remainingAvailability());
//        availabilityTable.setItems(availability);
//        filters();
    }
}
