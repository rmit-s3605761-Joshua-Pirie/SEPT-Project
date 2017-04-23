package appointmentBookingApp.model;


import appointmentBookingApp.util.DbUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

public class AvailabilityList {
    private final StringProperty date;
    private final StringProperty day;
    private final StringProperty sTime;
    private final StringProperty eTime;
    private final StringProperty empName;
    private final StringProperty staffID;

    public AvailabilityList(String date, String day, String sTime, String eTime, String empName, String staffID) {
        this.date = new SimpleStringProperty(date);
        this.day = new SimpleStringProperty(day);
        this.sTime = new SimpleStringProperty(sTime);
        this.eTime = new SimpleStringProperty(eTime);
        this.empName = new SimpleStringProperty(empName);
        this.staffID = new SimpleStringProperty(staffID);
    }

    public static ObservableList<AvailabilityList> remainingAvailability() throws SQLException {
        int minutes = 29;
        ObservableList<AvailabilityList> availability = FXCollections.observableArrayList();
//        LocalTime time1 = LocalTime.parse(iniTime);
//        LocalTime time2 = time1.plusMinutes(minutes);

        String sql = "SELECT *, dayname(bookings.date) " +
                "FROM availability " +
                "JOIN bookings ON availability.staffID = bookings.staffID " +
                "   AND availability.dayOfWeek = bookings.dayOfWeek" +
                "WHERE (bookings.sTime between availability.startTime and availability.endTime)";
        PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(sql);
//        pstmt.setDate(1, Date.valueOf(date));
//        pstmt.setString(2,time1.toString());
//        pstmt.setString(3,time2.toString());
        ResultSet rs = pstmt.executeQuery();
        while(rs.next())
        {
            availability.add(new AvailabilityList(rs.getString("date"),
                    rs.getString("dayname(booking.date)"),
                    rs.getString("service"),
                    rs.getString("sTime"),
                    rs.getString("staff.firstName"),
                    rs.getString("staffID")));

        }
        return availability;
    }

    public String getDay() {
        return day.get();
    }

    public StringProperty dayProperty() {
        return day;
    }

    public void setDay(String day) {
        this.day.set(day);
    }

    public String getsTime() {
        return sTime.get();
    }

    public StringProperty sTimeProperty() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime.set(sTime);
    }

    public String geteTime() {
        return eTime.get();
    }

    public StringProperty eTimeProperty() {
        return eTime;
    }

    public void seteTime(String eTime) {
        this.eTime.set(eTime);
    }

    public String getEmpName() {
        return empName.get();
    }

    public StringProperty empNameProperty() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName.set(empName);
    }

    public String getStaffID() {
        return staffID.get();
    }

    public StringProperty staffIDProperty() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID.set(staffID);
    }
}
