package appointmentBookingApp.view;

import appointmentBookingApp.MainApp;
import appointmentBookingApp.model.AvailabilityList;
import appointmentBookingApp.model.Bookings;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class RemainingAvailabilityController {
    private Stage dialogStage;
    private MainApp mainApp;
    @FXML
    private TextField fsTime;
    @FXML
    private TextField fDay;
    @FXML
    private TextField fStaffID;
    @FXML
    private TableView<AvailabilityList> availabilityTable;
    @FXML
    private TableColumn<AvailabilityList, String> staffIDColumn;
    @FXML
    private TableColumn<AvailabilityList, String> nameColumn;
    @FXML
    private TableColumn<AvailabilityList, String> dayColumn;
    @FXML
    private TableColumn<AvailabilityList, String> sTimeColumn;
    @FXML
    private TableColumn<AvailabilityList, String> eTimeColumn;
    ObservableList<AvailabilityList> availability = FXCollections.observableArrayList();
    FilteredList<AvailabilityList> filteredData = new FilteredList<>(availability, p -> true);
    private String business;

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
        this.business = mainApp.business;
        System.out.println("BN from RemainingAvail: "+mainApp.business);
        staffIDColumn.setCellValueFactory(cellData -> cellData.getValue().staffIDProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().empNameProperty());
        dayColumn.setCellValueFactory(cellData -> cellData.getValue().dayProperty());
        sTimeColumn.setCellValueFactory(cellData -> cellData.getValue().sTimeProperty());
        eTimeColumn.setCellValueFactory(cellData -> cellData.getValue().eTimeProperty());
        availability.setAll(AvailabilityList.remainingAvailability(business));
        availabilityTable.setItems(availability);
        filters();
    }

    public void filters() {
        filteredData.predicateProperty().bind(Bindings.createObjectBinding(() ->
                        availability -> availability.getStaffID().contains(fStaffID.getText().toUpperCase())
                                && availability.getDay().contains(fDay.getText().toUpperCase())
                                && availability.getsTime().contains(fsTime.getText()),

                fStaffID.textProperty(),
                fDay.textProperty(),
                fsTime.textProperty()
        ));

        SortedList<AvailabilityList> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(availabilityTable.comparatorProperty());
        availabilityTable.setItems(sortedData);
    }
}
