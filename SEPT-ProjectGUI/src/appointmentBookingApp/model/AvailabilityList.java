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

    public AvailabilityList(String staffID, Day dayOfWeek, String sTime, String eTime){
        this.date = null;
        this.day = new SimpleStringProperty(dayOfWeek.toString());
        this.empName = null;
        this.sTime = new SimpleStringProperty(sTime);
        this.eTime = new SimpleStringProperty(eTime);
        this.staffID = new SimpleStringProperty(staffID);
        this.startTime = LocalTime.parse(sTime);
        this.endTime = LocalTime.parse(eTime);
        this.dayOfWeek = dayOfWeek;
    }

    public AvailabilityList(String date, String day, String sTime, String eTime, String empName, String staffID, Day dayOfWeek) {
        this.date = new SimpleStringProperty(date);
        this.day = new SimpleStringProperty(day);
        this.sTime = new SimpleStringProperty(sTime);
        this.eTime = new SimpleStringProperty(eTime);
        this.empName = new SimpleStringProperty(empName);
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
        sql = "SELECT * FROM availability";
        pstmt = DbUtil.getConnection().prepareStatement(sql);
        rs = pstmt.executeQuery();
        while (rs.next()){
            availableTimeRanges.add(new AvailabilityList(rs.getString("staffID"),
                    Day.values()[rs.getInt("dayofWeek")],
                    rs.getString("startTime"),
                    rs.getString("endTime")));
        }
        sql = "SELECT * FROM bookings";
        pstmt = DbUtil.getConnection().prepareStatement(sql);
        rs = pstmt.executeQuery();
        while (rs.next()){
            bookingTimeRanges.add(new AvailabilityList(rs.getString("staffID"),
                    Day.values()[rs.getInt("dayofWeek")],
                    rs.getString("sTime"),
                    rs.getString("eTime")));
        }

        for(AvailabilityList bookingTimeRange : bookingTimeRanges) {
            remainingTimeRanges.clear();
            toRemove.clear();
            for(AvailabilityList availableTimeRange : availableTimeRanges) {
                if(availableTimeRange.getStaffID().equals(bookingTimeRange.getStaffID())
                        && availableTimeRange.getDayOfWeek() == bookingTimeRange.getDayOfWeek()){
                    if(bookingTimeRange.getStartTime().isAfter(availableTimeRange.getStartTime())
                            && bookingTimeRange.getStartTime().isBefore(availableTimeRange.getEndTime())) {
                        remainingTimeRanges.add(new AvailabilityList(null,
                                availableTimeRange.getDayOfWeek().toString(),
                                availableTimeRange.getStartTime().toString(),
                                bookingTimeRange.getStartTime().toString(),
                                null,
                                availableTimeRange.getStaffID(),
                                availableTimeRange.getDayOfWeek()));
                        if(bookingTimeRange.getEndTime().isBefore(availableTimeRange.getEndTime())){
                            remainingTimeRanges.add(new AvailabilityList(null,
                                    availableTimeRange.getDayOfWeek().toString(),
                                    bookingTimeRange.getEndTime().toString(),
                                    availableTimeRange.getEndTime().toString(),
                                    null,
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
            System.out.println(print.getStaffID()+" "+print.getsTime()+" "+print.geteTime()+" Day: "+print.getDayOfWeek());
        }
//        while(rs.next())
//        {
//            System.out.println(rs.getString("day"));
//            remainingAvailability.add(new AvailabilityList(rs.getString("date"),
//                    rs.getString("day"),
//                    rs.getString("service"),
//                    rs.getString("sTime"),
//                    rs.getString("firstName"),
//                    rs.getString("staffID")));
//
//        }
        return remainingAvailability;
    }

//    public ObservableList<AvailabilityList> genDay(AvailabilityList remainingAvailability){
//        for ()
//        switch (remainingAvailability.getDayOfWeek()){
//
//        }
//        return genDay();
//    }

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
