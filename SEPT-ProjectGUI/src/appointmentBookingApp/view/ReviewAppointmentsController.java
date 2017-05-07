package appointmentBookingApp.view;


import appointmentBookingApp.MainApp;
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

public class ReviewAppointmentsController {
    @FXML
    private TextField fDay;
    @FXML
    private TextField fService;
    @FXML
    private TextField fStartTime;
    @FXML
    private TableView<Bookings> bookingTable;
    @FXML
    private TableColumn<Bookings, String> dateColumn;
    @FXML
    private TableColumn<Bookings, String> dayColumn;
    @FXML
    private TableColumn<Bookings, String> serviceColumn;
    @FXML
    private TableColumn<Bookings, String> sTimeColumn;
    @FXML
    private TableColumn<Bookings, String> eTimeColumn;
    @FXML
    private TableColumn<Bookings, String> nameColumn;
    ObservableList<Bookings> bookings = FXCollections.observableArrayList();
    FilteredList<Bookings> filteredData = new FilteredList<>(bookings, p -> true);
    private Stage dialogStage;
    private MainApp mainApp;
    private String business;

    //Allow for the control of the main app.
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void ini(String customerUsername) throws SQLException {
        this.business = mainApp.business;
        System.out.println("BN from ReviewApp: "+mainApp.business);
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        dayColumn.setCellValueFactory(cellData -> cellData.getValue().dayProperty());
        serviceColumn.setCellValueFactory(cellData -> cellData.getValue().serviceProperty());
        sTimeColumn.setCellValueFactory(cellData -> cellData.getValue().sTimeProperty());
        eTimeColumn.setCellValueFactory(cellData -> cellData.getValue().eTimeProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().empNameProperty());
        bookings.setAll(Bookings.reviewAppointmentsCustomer(customerUsername, business));
        bookingTable.setItems(bookings);
        filters();
    }

    public void filters() {
        filteredData.predicateProperty().bind(Bindings.createObjectBinding(() ->
                        bookings -> bookings.getDay().contains(fDay.getText().toUpperCase())
                                && bookings.getService().contains(fService.getText())
                                && bookings.getsTime().contains(fStartTime.getText()),

                fDay.textProperty(),
                fService.textProperty(),
                fStartTime.textProperty()
        ));

        SortedList<Bookings> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(bookingTable.comparatorProperty());
        bookingTable.setItems(sortedData);
    }

    @FXML
    private void close() {
        dialogStage.close();
    }
}
