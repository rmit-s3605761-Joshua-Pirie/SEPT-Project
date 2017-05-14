package appointmentBookingApp.view;

import appointmentBookingApp.MainApp;
import appointmentBookingApp.model.AvailabilityList;
import appointmentBookingApp.model.Bookings;
import appointmentBookingApp.util.Alerts;
import appointmentBookingApp.util.DbUtil;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Joshua on 08/05/2017.
 */
public class CreateAppOwnerController extends Application {
    @FXML
    private ComboBox customerCombo;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField hourText;

    @FXML
    private TextField minuteText;

    @FXML
    private ComboBox serviceCombo;

    @FXML
    private ComboBox employeeCombo;

    @FXML
    private Button createButton;

    private Stage dialogStage;
    private MainApp mainApp;

    private ArrayList<String> customerUsernames;
    private ArrayList<String> staffIDs;
    private ArrayList<String[]> services;
    private ObservableList<AvailabilityList> availability;

    /**
     * Sets the stage of this dialog.
     */
    void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }

    //Allow for the control of the main app.
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    void initialize() {
        String sql = "SELECT username, firstName, lastName FROM customer WHERE businessName=?";
        customerUsernames = new ArrayList<>();
        try {
            PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
            ps.setString(1, mainApp.business);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                customerUsernames.add(rs.getString("username"));
                customerCombo.getItems().add(rs.getString("firstName") + " " + rs.getString("lastName"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        sql = "SELECT service, duration FROM services WHERE businessName=?";
        services = new ArrayList<>();
        try {
            PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
            ps.setString(1, mainApp.business);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                services.add(new String[]{rs.getString("service"), rs.getString("duration")});
                serviceCombo.getItems().add(rs.getString("service") + " - " + rs.getString("duration"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            availability = AvailabilityList.remainingAvailability(mainApp.business);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleCustomer() {
        setEnabled(true, false, false, false, false);
        clearInput(true, true, true, true);
    }

    @FXML
    void handleDate() {
        setEnabled(true, true, false, false, false);
        clearInput(false, true, true, true);
    }

    @FXML
    void handleTime() {
        if(isValidTime(hourText.getText(), minuteText.getText())) {
            setEnabled(true, true, true, false, false);
            clearInput(false, false, true, true);
        }
    }

    @FXML
    void handleService() {
        LocalDate date = datePicker.getValue();
        int duration = Integer.parseInt(services.get(serviceCombo.getSelectionModel().getSelectedIndex())[1].substring(3, 5));
        LocalTime inputSTime = LocalTime.of(Integer.parseInt(hourText.getText()), Integer.parseInt(minuteText.getText()));
        LocalTime inputETime = inputSTime.plusMinutes(duration);
        staffIDs = new ArrayList<>();
        for(AvailabilityList a : availability) {
            LocalTime sTime = LocalTime.of(Integer.parseInt(a.getsTime().substring(0, 2)), Integer.parseInt(a.getsTime().substring(3, 5)));
            LocalTime eTime = LocalTime.of(Integer.parseInt(a.geteTime().substring(0, 2)), Integer.parseInt(a.geteTime().substring(3, 5)));
            if(a.getDayOfWeek().ordinal() == date.getDayOfWeek().ordinal()
               && (inputSTime.isAfter(sTime) || inputSTime.equals(sTime))
               && (inputETime.isBefore(eTime) || inputETime.equals(eTime))) {
                staffIDs.add(a.getStaffID());
                employeeCombo.getItems().add(a.getEmpName());
            }
        }
        setEnabled(true, true, true, true, false);
        clearInput(false, false, false, true);
    }

    @FXML
    void handleEmployee() {
        setEnabled(true, true, true, true, true);
    }

    @FXML
    void handleCreate() {
        if(!isValidTime(hourText.getText(), minuteText.getText()))
            Alerts.error("Error", "Invalid Time Format", "Please enter a valid time format.");
        else {
            String customer = customerUsernames.get(customerCombo.getSelectionModel().getSelectedIndex());
            LocalDate date = datePicker.getValue();
            String service = services.get(serviceCombo.getSelectionModel().getSelectedIndex())[0];
            int duration = Integer.parseInt(services.get(serviceCombo.getSelectionModel().getSelectedIndex())[1].substring(3, 5));
            LocalTime inputSTime = LocalTime.of(Integer.parseInt(hourText.getText()), Integer.parseInt(minuteText.getText()));
            LocalTime inputETime = inputSTime.plusMinutes(duration);
            String staffID = staffIDs.get(employeeCombo.getSelectionModel().getSelectedIndex());
            String sql = "INSERT INTO bookings (date, day, dayOfWeek, sTime, eTime, staffID, service, customerUsername, businessName)"
                       + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try {
                PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql);
                ps.setString(1, date.format(DateTimeFormatter.ISO_LOCAL_DATE));
                ps.setString(2, date.getDayOfWeek().toString());
                ps.setInt(3, date.getDayOfWeek().ordinal());
                ps.setString(4, inputSTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
                ps.setString(5, inputETime.format(DateTimeFormatter.ISO_LOCAL_TIME));
                ps.setString(6, staffID);
                ps.setString(7, service);
                ps.setString(8, customer);
                ps.setString(9, mainApp.business);
                ps.executeUpdate();
                dialogStage.close();
            } catch (SQLException e) {
                e.printStackTrace();
                Alerts.error("Error", "Database Communication Error", "There was an error communicating with the database.");
            }
        }
    }

    @FXML
    private void handleClose() {
        dialogStage.close();
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

    private boolean isValidTime(String hour, String minute) {
        try {
            int hourNum = Integer.parseInt(hour);
            int minuteNum = Integer.parseInt(minute);
            if(hourNum < 0 || hourNum > 23 || minuteNum < 0 || minuteNum > 59)
                return false;
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private void setEnabled(boolean date, boolean time, boolean service, boolean employee, boolean create) {
        datePicker.setDisable(!date);
        hourText.setDisable(!time);
        minuteText.setDisable(!time);
        serviceCombo.setDisable(!service);
        employeeCombo.setDisable(!employee);
        createButton.setDisable(!create);
    }

    private void clearInput(boolean date, boolean time, boolean service, boolean employee) {
        EventHandler<ActionEvent> e;
        if(date) {
            e = datePicker.getOnAction();
            datePicker.setOnAction(null);
            datePicker.setValue(null);
            datePicker.setOnAction(e);
        }
        if(time) {
            e = hourText.getOnAction();
            hourText.setOnAction(null);
            hourText.setText(null);
            hourText.setOnAction(e);
            e = minuteText.getOnAction();
            minuteText.setOnAction(null);
            minuteText.setText(null);
            minuteText.setOnAction(e);
        }
        if(service) {
            e = serviceCombo.getOnAction();
            serviceCombo.setOnAction(null);
            serviceCombo.getSelectionModel().clearSelection();
            serviceCombo.setOnAction(e);
        }
        if(employee) {
            e = employeeCombo.getOnAction();
            employeeCombo.setOnAction(null);
            employeeCombo.getSelectionModel().clearSelection();
            employeeCombo.setOnAction(e);
        }
    }
}
