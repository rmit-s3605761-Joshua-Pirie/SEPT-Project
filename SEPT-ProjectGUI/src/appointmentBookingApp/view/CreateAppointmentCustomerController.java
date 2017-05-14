package appointmentBookingApp.view;

import appointmentBookingApp.MainApp;
import appointmentBookingApp.model.AvailabilityList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static appointmentBookingApp.util.DbUtil.getConnection;

/**
 * Created by David on 8/05/2017.
 */
public class CreateAppointmentCustomerController {

    @FXML
    private ComboBox ServicesBox;

    @FXML
    private ComboBox TimesBox;

    @FXML
    private TableView<AvailabilityList> BookingTable;
    @FXML
    private TableColumn<AvailabilityList, String> DateCol;
    @FXML
    private TableColumn<AvailabilityList, String> TimeCol;
    @FXML
    private TableColumn<AvailabilityList, String> ServiceCol;
    @FXML
    private TableColumn<AvailabilityList, String> StaffCol;


    @FXML
    private DatePicker DateBox;
    private Stage dialogStage;
    private LocalDate selectedDate = null;
    private Object selectedService = null;
    private LocalTime selectedTime = null;
    HashMap<String, LocalTime> services = new HashMap<String, LocalTime>();
    private ObservableList<String> serviceList = FXCollections.observableArrayList();
    private ObservableList<String> timesList = FXCollections.observableArrayList();
    ObservableList<AvailabilityList> remainingAvailability = FXCollections.observableArrayList();
    ObservableList<AvailabilityList> AvailableBookings = FXCollections.observableArrayList();
    private MainApp mainApp;
    private String user;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void initialize(){
        try{
            remainingAvailability = AvailabilityList.remainingAvailability("Baker");
        }
        catch (SQLException e){

        }


        String query = "SELECT * FROM services;";
        Statement st;

        try {
            st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                services.put(rs.getString("service"), rs.getTime("duration").toLocalTime());
                serviceList.add(rs.getString("service"));
            }
            ServicesBox.setItems(serviceList);
            ServicesBox.setVisibleRowCount(serviceList.size());
            for (Map.Entry<String, LocalTime> entry : services.entrySet()) {
                System.out.println("key, " + entry.getKey() + " value " + entry.getValue());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        LocalTime firstTime = LocalTime.of(9, 00);
        String time = null;
        for(int i = 0; i < 32; i++){

            time = firstTime.toString();
            timesList.add(time);
            firstTime = firstTime.plusMinutes(15);
        }
        TimesBox.setItems(timesList);
        TimesBox.setVisibleRowCount(timesList.size());

        DateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        TimeCol.setCellValueFactory(cellData -> cellData.getValue().sTimeProperty());
        StaffCol.setCellValueFactory(cellData -> cellData.getValue().empNameProperty());

    }

    @FXML
    private void dateSelect(){
        this.selectedDate = DateBox.getValue();
        //System.out.println("chosen date is: " +localDate );
    }



    @FXML
    private void serviceSelect() {
        this.selectedService = ServicesBox.getValue();


    }

    @FXML
    private void timeSelect(){
        this.selectedTime = LocalTime.parse((CharSequence) TimesBox.getValue());
        if (selectedDate != null && selectedService != null) {


            for (AvailabilityList list : remainingAvailability) {

                if (list.getDate() != null) {
                    System.out.println("Available1: " + list.getDate() + " Start: " + list.getsTime() + " End: " + list.geteTime() + " " + list.getStaffID());
                    if (LocalDate.parse(list.getDate()).equals(selectedDate)) {
                        System.out.println("Date Equals: " + list.getDate() + " Start: " + list.getsTime() + " End: " + list.geteTime() + " " + list.getStaffID());
                    if(LocalTime.parse(list.getsTime()).equals(selectedTime) || LocalTime.parse(list.getsTime()).isBefore(selectedTime)){
                        System.out.println("Time Equals: " + list.getDate() + " Start: " + list.getsTime() + " End: " + list.geteTime() + " " + list.getStaffID());

                        if (LocalTime.parse(list.getsTime()).plusMinutes(services.get(selectedService).getMinute()).isBefore(LocalTime.parse(list.geteTime()))) {
                            System.out.println("Acceptable time"+ list.getDate() + " Start: " + list.getsTime() + " End: " + list.geteTime() + " " + list.getStaffID());

                            AvailableBookings.add(list);

                        }
                    }
                    }
                }
            }
        }

        BookingTable.setItems(AvailableBookings);
    }

    @FXML
    private void submitBooking(){
        int selection = BookingTable.getSelectionModel().getSelectedIndex();
        String sql = "INSERT INTO bookings VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setObject(1, this.selectedDate);
            ps.setString(2, AvailableBookings.get(selection).getDay());
            ps.setObject(3, AvailableBookings.get(selection).getDayOfWeek().ordinal());
            ps.setObject(4, selectedTime);
            ps.setObject(5, selectedTime.plusMinutes(services.get(selectedService).getMinute()));
            ps.setObject(6, AvailableBookings.get(selection).getStaffID());
            ps.setObject(7, this.selectedService);
            ps.setObject(8, user);
            ps.setObject(9, mainApp.business);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void ini(String user) {
        this.user = user;
    }
}
