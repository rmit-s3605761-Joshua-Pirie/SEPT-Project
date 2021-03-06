package appointmentBookingApp.view;

import appointmentBookingApp.MainApp;
import appointmentBookingApp.model.Bookings;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.sql.SQLException;

/**
 * Created by Aydan on 8/04/2017.
 */
public class UpcomingBookingsListController {
    private Stage dialogStage;
    private String day, sTime, date;


    @FXML
    private Label title;
    @FXML
    private ComboBox fAvailability;
    @FXML
    private TextField fService;
    @FXML
    private TextField fCustomer;
    @FXML
    private TextField fStaffID;
    @FXML
    private TableView<Bookings> bookingTable;
    @FXML
    private TableColumn<Bookings, String> sTimeColumn;
    @FXML
    private TableColumn<Bookings, String> serviceColumn;
    @FXML
    private TableColumn<Bookings, String> customerColumn;
    @FXML
    private TableColumn<Bookings, String> employeeColumn;
    ObservableList<Bookings> bookings = FXCollections.observableArrayList();
    FilteredList<Bookings> filteredData = new FilteredList<>(bookings, p -> true);
    private MainApp mainApp;
    private String business;

    //Allow for the control of the main app.
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Sets the stage of this dialog.
     */
    void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }

    public void ini() throws SQLException {
        this.business = MainApp.getBusiness();
        sTimeColumn.setCellValueFactory(cellData -> cellData.getValue().sTimeProperty());
        serviceColumn.setCellValueFactory(cellData -> cellData.getValue().serviceProperty());
        customerColumn.setCellValueFactory(cellData -> cellData.getValue().customerProperty());
        employeeColumn.setCellValueFactory(cellData -> cellData.getValue().empNameProperty());
        bookings.setAll(Bookings.setBookings(date, sTime, business));
        bookingTable.setItems(bookings);
        filters();
    }

    public void filters() {
        filteredData.predicateProperty().bind(Bindings.createObjectBinding(() ->
                        bookings -> bookings.getService().contains(fService.getText())
                                && bookings.getCustomer().contains(fCustomer.getText())
                                && bookings.getStaffID().contains(fStaffID.getText()),

                fService.textProperty(),
                fCustomer.textProperty(),
                fStaffID.textProperty()
        ));




//        fCustomer.textProperty().addListener((observable, oldValue, newValue) -> {
//            filteredData.setPredicate(bookings -> {
//                // If filter text is empty, display all persons.
//                if (newValue == null || newValue.isEmpty()) {
//                    return true;
//                }
//
//                String lowerCaseFilter = newValue.toLowerCase();
//
//                if (bookings.getCustomer().toLowerCase().contains(lowerCaseFilter)) {
//                    System.out.println("true");
//                    return true; // Filter matches name.
//                }
//                return false; // Does not match.
//            });
//        });
        SortedList<Bookings> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(bookingTable.comparatorProperty());
        bookingTable.setItems(sortedData);
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    @FXML
    void comboBoxFilter() throws SQLException {
        String available = "Available Only";
        String booked = "Booked Only";
        String selection;

        if(fAvailability.getValue().equals(available)){
            selection = "available";
            System.out.println(fAvailability.getValue());
            bookings.setAll(Bookings.setBookings(selection, date, sTime, business));
            bookingTable.setItems(bookings);
            filters();
        }else if(fAvailability.getValue().equals(booked)){
            selection = "booked";
            System.out.println(fAvailability.getValue());
            bookings.setAll(Bookings.setBookings(selection, date, sTime, business));
            bookingTable.setItems(bookings);
            filters();
        }else{
            System.out.println(fAvailability.getValue());
            bookings.setAll(Bookings.setBookings(date, sTime, business));
            bookingTable.setItems(bookings);
            filters();
        }
    }

    public void setDayTime(String day, String sTime, String date) {
        this.day = day;
        this.sTime = sTime;
        this.date =date;
        System.out.println(this.day);
        System.out.println(this.sTime);
        title.setText(this.day+" "+this.date+" "+this.sTime);
    }
}
