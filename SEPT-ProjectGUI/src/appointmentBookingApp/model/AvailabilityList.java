package appointmentBookingApp.model;

import appointmentBookingApp.MainApp;
import appointmentBookingApp.util.DbUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class AvailabilityList {
    private final StringProperty date;
    private final StringProperty day;
    private final StringProperty sTime;
    private final StringProperty eTime;
    private final StringProperty empName;
    private final StringProperty staffID;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final Day dayOfWeek;

    public AvailabilityList(){
        this(null, null, "00:00", "00:00", null, null ,null);
    }

    public AvailabilityList(String staffID, Day dayOfWeek, String sTime, String eTime, String empName){
        this(null, dayOfWeek.toString(), sTime, eTime, empName, staffID ,dayOfWeek);
    }

    public AvailabilityList(String staffID, Day dayOfWeek, String sTime, String eTime, String empName, String date){
        this(date, dayOfWeek.toString(), sTime, eTime, empName, staffID ,dayOfWeek);
    }

    public AvailabilityList(String date, String day, String sTime, String eTime, String empName, String staffID, Day dayOfWeek) {
        this.date = new SimpleStringProperty(date);
        this.day = new SimpleStringProperty(day);
        this.empName = new SimpleStringProperty(empName);
        this.sTime = new SimpleStringProperty(sTime);
        this.eTime = new SimpleStringProperty(eTime);
        this.staffID = new SimpleStringProperty(staffID);
        this.startTime = LocalTime.parse(sTime);
        this.endTime = LocalTime.parse(eTime);
        this.dayOfWeek = dayOfWeek;
    }

//  Programmatically determine the remaining availability of employees.
    public static ObservableList<AvailabilityList> remainingAvailability() throws SQLException {
        LocalDate date = LocalDate.now();
        return remainingAvailability(date, date.plusDays(6));
    }
    public static ObservableList<AvailabilityList> remainingAvailability(LocalDate startDate, LocalDate endDate) throws SQLException {
        ObservableList<AvailabilityList> remainingAvailability = FXCollections.observableArrayList();
        Set<AvailabilityList> availableTimeRanges = getAvailableTimeRanges();
        Set<AvailabilityList> bookingTimeRanges = getBookingTimeRanges(startDate, endDate);
        Set<AvailabilityList> tempAvailabilities = new LinkedHashSet<>();

        for(AvailabilityList availableTimeRange : availableTimeRanges) {
            tempAvailabilities.clear();

            // start by assuming availability at that time every week within the range
            for(LocalDate currDate = startDate; currDate.compareTo(endDate) <= 0; currDate = currDate.plusDays(1)) {
                if(currDate.getDayOfWeek().ordinal() == availableTimeRange.getDayOfWeek().ordinal()) {
                    tempAvailabilities.add(new AvailabilityList(currDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                            availableTimeRange.getDayOfWeek().toString(),
                            availableTimeRange.getStartTime().toString(),
                            availableTimeRange.getEndTime().toString(),
                            availableTimeRange.getEmpName(),
                            availableTimeRange.getStaffID(),
                            availableTimeRange.getDayOfWeek()));
                }
            }

            if(tempAvailabilities.size() == 0)
                continue;

            for(AvailabilityList bookingTimeRange : bookingTimeRanges) {
                for(AvailabilityList tempAvailability : tempAvailabilities) {
                    if(tempAvailability.getStaffID().equals(bookingTimeRange.getStaffID())
                        && tempAvailability.getDayOfWeek() == bookingTimeRange.getDayOfWeek()
                        && bookingTimeRange.getStartTime().compareTo(tempAvailability.getStartTime()) >= 0
                        && bookingTimeRange.getStartTime().compareTo(tempAvailability.getEndTime()) <= 0) {

                        // add shortened availability
                        if (tempAvailability.getStartTime().isBefore(bookingTimeRange.getStartTime())) {
                            tempAvailabilities.add(new AvailabilityList(bookingTimeRange.getDate(),
                                    tempAvailability.getDayOfWeek().toString(),
                                    tempAvailability.getStartTime().toString(),
                                    bookingTimeRange.getStartTime().toString(),
                                    tempAvailability.getEmpName(),
                                    tempAvailability.getStaffID(),
                                    tempAvailability.getDayOfWeek()));
                        }

                        if (bookingTimeRange.getEndTime().isBefore(tempAvailability.getEndTime())) {
                            tempAvailabilities.add(new AvailabilityList(bookingTimeRange.getDate(),
                                    tempAvailability.getDayOfWeek().toString(),
                                    bookingTimeRange.getEndTime().toString(),
                                    tempAvailability.getEndTime().toString(),
                                    tempAvailability.getEmpName(),
                                    tempAvailability.getStaffID(),
                                    tempAvailability.getDayOfWeek()));
                        }

                        tempAvailabilities.remove(tempAvailability);

                        break;
                    }
                }
            }

            remainingAvailability.addAll(tempAvailabilities);
        }
        System.out.println("Remaining Availability");
        for(AvailabilityList print : remainingAvailability) {
            System.out.println(print.getStaffID()+" "+print.getsTime()+" "+print.geteTime()+" Day: "+print.getDayOfWeek()+" Date: "+print.getDate());
        }
        return remainingAvailability;
    }

//  Get employee availability from database.
    private static Set<AvailabilityList> getAvailableTimeRanges(){
        Set<AvailabilityList> availableTimeRanges = new LinkedHashSet<>();
        String sql = "SELECT * FROM availability " +
                "NATURAL JOIN staff " +
                "WHERE businessName = ?";
        try {
            PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(sql);
            pstmt.setObject(1, MainApp.getBusiness());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                availableTimeRanges.add(new AvailabilityList(rs.getString("staffID"),
                        Day.values()[rs.getInt("dayofWeek")],
                        rs.getString("startTime"),
                        rs.getString("endTime"),
                        rs.getString("firstName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableTimeRanges;
    }

//  Get bookings from database.
    private static Set<AvailabilityList> getBookingTimeRanges(LocalDate startDate, LocalDate endDate){
        Set<AvailabilityList> bookingTimeRanges = new LinkedHashSet<>();
        String sql = "SELECT * FROM bookings " +
                "NATURAL JOIN staff " +
                "WHERE date BETWEEN ? AND ? " +
                "AND businessName = ?";
        try {
            PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(sql);
            pstmt.setDate(1,Date.valueOf(startDate));
            pstmt.setDate(2,Date.valueOf(endDate));
            pstmt.setObject(3,MainApp.getBusiness());
             ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                bookingTimeRanges.add(new AvailabilityList(rs.getString("staffID"),
                        Day.values()[rs.getInt("dayofWeek")],
                        rs.getString("sTime"),
                        rs.getString("eTime"),
                        rs.getString("firstName"),
                        rs.getString("date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingTimeRanges;
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

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Day getDayOfWeek() {
        return dayOfWeek;
    }
}
