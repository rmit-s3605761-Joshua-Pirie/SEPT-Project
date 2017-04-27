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
import java.time.LocalDate;
import java.time.LocalTime;
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

    public AvailabilityList(String staffID, Day dayOfWeek, String sTime, String eTime, String empName){
        this(null, dayOfWeek.toString(), sTime, eTime, empName, staffID ,dayOfWeek);
//        this.date = null;
//        this.day = new SimpleStringProperty(dayOfWeek.toString());
//        this.empName = new SimpleStringProperty(empName);
//        this.sTime = new SimpleStringProperty(sTime);
//        this.eTime = new SimpleStringProperty(eTime);
//        this.staffID = new SimpleStringProperty(staffID);
//        this.startTime = LocalTime.parse(sTime);
//        this.endTime = LocalTime.parse(eTime);
//        this.dayOfWeek = dayOfWeek;
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

    public static ObservableList<AvailabilityList> remainingAvailability() throws SQLException {
        int minutes = 29;
        String sql;
        PreparedStatement pstmt;
        ResultSet rs;
        LocalDate date1 = LocalDate.now();
        LocalDate date2 = date1.plusDays(6);

        ObservableList<AvailabilityList> remainingAvailability = FXCollections.observableArrayList();
        Set<AvailabilityList> availableTimeRanges = new LinkedHashSet<>();
        Set<AvailabilityList> bookingTimeRanges = new LinkedHashSet<>();
        Set<AvailabilityList> remainingTimeRanges = new LinkedHashSet<>();
        ArrayList<AvailabilityList> toRemove = new ArrayList<>();

//        LocalTime time1 = LocalTime.parse(iniTime);
//        LocalTime time2 = time1.plusMinutes(minutes);

//        String sql = "SELECT *, dayname(bookings.date) as 'day' " +
//                "FROM availability " +
//                "JOIN bookings ON availability.staffID = bookings.staffID " +
//                "   AND availability.dayOfWeek = bookings.dayOfWeek " +
//                "JOIN staff ON bookings.staffID = staff.staffID " +
//                "WHERE (bookings.sTime between availability.startTime and availability.endTime)";
        sql = "SELECT * FROM availability " +
                "NATURAL JOIN staff";
        pstmt = DbUtil.getConnection().prepareStatement(sql);
        rs = pstmt.executeQuery();
        while (rs.next()){
            availableTimeRanges.add(new AvailabilityList(rs.getString("staffID"),
                    Day.values()[rs.getInt("dayofWeek")],
                    rs.getString("startTime"),
                    rs.getString("endTime"),
                    rs.getString("firstName")));
        }
        sql = "SELECT * FROM bookings " +
                "NATURAL JOIN staff " +
                "WHERE date BETWEEN ? AND ?";
        pstmt = DbUtil.getConnection().prepareStatement(sql);
        pstmt.setDate(1,Date.valueOf(date1));
        pstmt.setDate(2,Date.valueOf(date2));
        rs = pstmt.executeQuery();
        while (rs.next()){
            bookingTimeRanges.add(new AvailabilityList(rs.getString("staffID"),
                    Day.values()[rs.getInt("dayofWeek")],
                    rs.getString("sTime"),
                    rs.getString("eTime"),
                    rs.getString("firstName"),
                    rs.getString("date")));
        }

        for(AvailabilityList bookingTimeRange : bookingTimeRanges) {
            remainingTimeRanges.clear();
            toRemove.clear();
            for(AvailabilityList availableTimeRange : availableTimeRanges) {
                if(availableTimeRange.getStaffID().equals(bookingTimeRange.getStaffID())
                        && availableTimeRange.getDayOfWeek() == bookingTimeRange.getDayOfWeek()){
                    if(bookingTimeRange.getStartTime().isAfter(availableTimeRange.getStartTime())
                            && bookingTimeRange.getStartTime().isBefore(availableTimeRange.getEndTime())) {
                        remainingTimeRanges.add(new AvailabilityList(bookingTimeRange.getDate(),
                                availableTimeRange.getDayOfWeek().toString(),
                                availableTimeRange.getStartTime().toString(),
                                bookingTimeRange.getStartTime().toString(),
                                availableTimeRange.getEmpName(),
                                availableTimeRange.getStaffID(),
                                availableTimeRange.getDayOfWeek()));
                        if(bookingTimeRange.getEndTime().isBefore(availableTimeRange.getEndTime())){
                            remainingTimeRanges.add(new AvailabilityList(bookingTimeRange.getDate(),
                                    availableTimeRange.getDayOfWeek().toString(),
                                    bookingTimeRange.getEndTime().toString(),
                                    availableTimeRange.getEndTime().toString(),
                                    availableTimeRange.getEmpName(),
                                    availableTimeRange.getStaffID(),
                                    availableTimeRange.getDayOfWeek()));
                        }
                        toRemove.add(availableTimeRange);
                    }
                    else{
                        remainingTimeRanges.add(availableTimeRange);
                    }
                }else {
                        remainingTimeRanges.add(availableTimeRange);
                }
            }
            if(!remainingTimeRanges.isEmpty()){
                remainingAvailability.clear();
                remainingAvailability.addAll(remainingTimeRanges);
            }
            availableTimeRanges.addAll(remainingTimeRanges);
            availableTimeRanges.removeAll(toRemove);
        }


        System.out.println("RA");
        for(AvailabilityList print : remainingAvailability){
            System.out.println(print.getStaffID()+" "+print.getsTime()+" "+print.geteTime()+" Day: "+print.getDayOfWeek()+" Date: "+print.getDate());
        }

        return remainingAvailability;
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
