package appointmentBookingApp.view;

import appointmentBookingApp.MainApp;
import appointmentBookingApp.model.AvailabilityList;
import appointmentBookingApp.model.Bookings;
import appointmentBookingApp.util.DbUtil;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Joshua on 08/05/2017.
 */
public class CreateAppOwnerController extends Application {
    @FXML
    private ComboBox customerCombo;

    @FXML
    private ComboBox employeeCombo;

    @FXML
    private ComboBox serviceCombo;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox sTimeCombo;

    @FXML
    private TableView<AvailabilityList> availabilityTable;
    @FXML
    private TableColumn<AvailabilityList, String> staffIDColumn;
    @FXML
    private TableColumn<AvailabilityList, String> empNameColumn;
    @FXML
    private TableColumn<AvailabilityList, String> dateColumn;
    @FXML
    private TableColumn<AvailabilityList, String> sTimeColumn;
    @FXML
    private TableColumn<AvailabilityList, String> eTimeColumn;

    private Stage dialogStage;
    private MainApp mainApp;

    private ArrayList<String> customerUsernames;
    private ArrayList<String> staffIDs;
    private ArrayList<String[]> services;
    private ArrayList<String[]> availableTimes;

    /**
     * Sets the stage of this dialog.
     */
    void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }

    //Allow for the control of the main app.
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    void initialize() {
        availableTimes = new ArrayList<>();
        staffIDColumn.setCellValueFactory(cellData -> cellData.getValue().staffIDProperty());
        empNameColumn.setCellValueFactory(cellData -> cellData.getValue().empNameProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        sTimeColumn.setCellValueFactory(cellData -> cellData.getValue().sTimeProperty());
        eTimeColumn.setCellValueFactory(cellData -> cellData.getValue().eTimeProperty());

        fillCustomerCombo();
        fillEmployeeCombo();
        fillServiceCombo();
        fillTable();
    }

    @FXML
    void handleUpdate() {
        int employeeIndex = employeeCombo.getSelectionModel().getSelectedIndex();
        int serviceIndex = serviceCombo.getSelectionModel().getSelectedIndex();
        LocalDate date = datePicker.getValue();
        if(employeeIndex != -1 && serviceIndex != -1 && date != null) {
            try {
                ObservableList<AvailabilityList> remainingAvailability = AvailabilityList.remainingAvailabilityDay(staffIDs.get(employeeIndex), date, mainApp.business);
                availableTimes.clear();
                sTimeCombo.getItems().clear();
                for(AvailabilityList a : remainingAvailability) {
                    availableTimes.add(new String[] { a.getsTime(), a.geteTime() });
                    sTimeCombo.getItems().add(a.getsTime() + " - " + a.geteTime());
                }
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
        fillTable();
    }

    @FXML
    void handleCreate() {

    }

    @FXML
    private void handleClose() {
        dialogStage.close();
    }

    private void fillCustomerCombo() {
        String sql = "SELECT username, firstName, lastName FROM customer";
        customerUsernames = new ArrayList<>();
        try {
            ResultSet rs = DbUtil.getNewStatment().executeQuery(sql);
            while(rs.next()) {
                customerUsernames.add(rs.getString("username"));
                customerCombo.getItems().add(rs.getString("firstName") + " " + rs.getString("lastName"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillEmployeeCombo() {
        String sql = "SELECT staffID, firstName, lastName FROM staff";
        staffIDs = new ArrayList<>();
        try {
            ResultSet rs = DbUtil.getNewStatment().executeQuery(sql);
            while(rs.next()) {
                staffIDs.add(rs.getString("staffID"));
                employeeCombo.getItems().add(rs.getString("firstName") + " " + rs.getString("lastName"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillServiceCombo() {
        String sql = "SELECT service, duration FROM services WHERE businessName=?";
        services = new ArrayList<>();
        try {
            PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
            ps.setString(1, mainApp.business);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                services.add(new String[] { rs.getString("service"), rs.getString("duration") });
                serviceCombo.getItems().add(rs.getString("service") + " - " + rs.getString("duration"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillTable() {
        int employeeIndex = employeeCombo.getSelectionModel().getSelectedIndex();
        LocalDate date = datePicker.getValue();

        try {
            ObservableList<AvailabilityList> remainingAvailability;

            if(employeeIndex == -1 && date == null)
                remainingAvailability = AvailabilityList.remainingAvailability(mainApp.business);
            else if(employeeIndex == -1)
                remainingAvailability = AvailabilityList.remainingAvailabilityDay(date, mainApp.business);
            else if(date == null)
                remainingAvailability = AvailabilityList.remainingAvailability(staffIDs.get(employeeIndex), mainApp.business);
            else
                remainingAvailability = AvailabilityList.remainingAvailabilityDay(staffIDs.get(employeeIndex), date, mainApp.business);

            availabilityTable.setItems(remainingAvailability);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        dialogStage = primaryStage;
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isBefore(LocalDate.now().plusDays(1))) {
                            setDisable(true);
                            setStyle("-fx-background-color: #EEEEEE;");
                        }
                    }
                };
            }
        };
        datePicker.setDayCellFactory(dayCellFactory);
        datePicker.setValue(LocalDate.now().plusDays(1));
        show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void show() {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CreateAppOwner.fxml"));
            AnchorPane login = (AnchorPane) loader.load();
            dialogStage.setTitle("Create Appointment");
            dialogStage.setScene(new Scene(login));

            dialogStage.show();
            createAppointmentController controller = loader.getController();
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
