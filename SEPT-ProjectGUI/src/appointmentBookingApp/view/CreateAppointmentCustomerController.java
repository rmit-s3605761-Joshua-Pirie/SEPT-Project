package appointmentBookingApp.view;

import appointmentBookingApp.model.AvailabilityList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private DatePicker DateBox;
    private Stage dialogStage;
    private LocalDate selectedDate = null;
    private Object selectedService = null;
    HashMap<String, LocalTime> services = new HashMap<String, LocalTime>();
    private ObservableList<String> serviceList = FXCollections.observableArrayList();
    private ObservableList<String> timesList = FXCollections.observableArrayList();
    ObservableList<AvailabilityList> remainingAvailability = FXCollections.observableArrayList();


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void initialize(){
        try{
            remainingAvailability = AvailabilityList.remainingAvailability("");
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

    }

    @FXML
    private void dateSelect(){
        this.selectedDate = DateBox.getValue();
        //System.out.println("chosen date is: " +localDate );
    }

    @FXML
    private void serviceSelect() {
        this.selectedService = ServicesBox.getValue();
        if (selectedDate != null) {
            for (AvailabilityList list : remainingAvailability) {
                if (list.getDate() != null)
                    if (LocalDate.parse(list.getDate()).equals(this.selectedDate) && !LocalTime.parse(list.getsTime()).plusMinutes(services.get(this.selectedService).getMinute()).equals(LocalTime.parse(list.geteTime()))) {
                        timesList.add(list.getsTime());
                    }
            }


            TimesBox.setItems(timesList);
            TimesBox.setVisibleRowCount(timesList.size());
            for (AvailabilityList list : remainingAvailability) {

                System.out.println(list.getDate() + " " + list.getsTime() + " " + list.geteTime() + " " + list.getStaffID());
            }


            System.out.println("service clicked ");
        }
    }

    @FXML
    private void submitBooking(){
        String sql = "INSERT INTO bookings VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setObject(1, this.selectedDate);
            ps.setString(2, null);
            ps.setString(3, null);
            ps.setObject(4, "00:00:00");
            ps.setObject(5, "00:00:00");
            ps.setObject(6, "s000001");
            ps.setObject(7, this.selectedService);
            ps.setObject(8, "CUST");
            ps.setObject(9, "Buis");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
