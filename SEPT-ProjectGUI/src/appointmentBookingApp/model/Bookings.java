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

public class Bookings {
    private final StringProperty sTime;
    private final StringProperty eTime;
    private final StringProperty service;
    private final StringProperty customer;
    private final StringProperty empName;
    private final StringProperty staffID;
    private final StringProperty customerUsername;
    private final StringProperty date;
    private final StringProperty day;
    private final Day dayOfWeek;

    public Bookings(String sTime, String eTime, String service, String customer, String empName, String staffID, Day dayOfWeek){
        this(sTime, eTime, service, customer, empName, staffID, null, null, dayOfWeek);
    }

    public Bookings(String sTime, String eTime, String service, String empName, String date, Day dayOfWeek){
        this(sTime, eTime, service, null, empName, null, null, date, dayOfWeek);
    }

    public Bookings(String sTime, String eTime, String service, String customer, String empName, String staffID,
                    String customerUsername, String date, Day dayOfWeek){
        this.sTime = new SimpleStringProperty(sTime);
        this.eTime = new SimpleStringProperty(eTime);
        this.service = new SimpleStringProperty(service);
        this.customer = new SimpleStringProperty(customer);
        this.empName = new SimpleStringProperty(empName);
        this.staffID = new SimpleStringProperty(staffID);
        this.customerUsername = new SimpleStringProperty(customerUsername);
        this.date = new SimpleStringProperty(date);
        this.day = new SimpleStringProperty((dayOfWeek != null) ? dayOfWeek.toString() : null);
        this.dayOfWeek = dayOfWeek;
    }

//  Generate a list of bookings based on date, start time, customer and business;
    public static ObservableList<Bookings> setBookings(String date, String iniTime, String business) throws SQLException {
        int minutes = 29;
        ObservableList<Bookings> bookings = FXCollections.observableArrayList();
        LocalTime time1 = LocalTime.parse(iniTime);
        LocalTime time2 = time1.plusMinutes(minutes);
        System.out.println("startTime: "+time1);
        System.out.println("endTime: "+time2);

        String sql = "SELECT bookings.*, staff.firstName " +
                "FROM bookings " +
                "NATURAL JOIN staff " +
                "WHERE date=? AND sTime BETWEEN ? AND ? " +
                "AND businessName = ?";
        PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(sql);
        pstmt.setDate(1, Date.valueOf(date));
        pstmt.setString(2,time1.toString());
        pstmt.setString(3,time2.toString());
        pstmt.setObject(4,business);
        ResultSet rs = pstmt.executeQuery();
        while(rs.next())
        {
            String customer = "";
            String sql2 = "SELECT * FROM customer WHERE username=?";
            PreparedStatement pstmt2 = DbUtil.getConnection().prepareStatement(sql2);
            pstmt2.setString(1,rs.getString("customerUsername"));
            ResultSet rs2 = pstmt2.executeQuery();
            if(rs2.next()){
                customer = rs2.getString("firstName");
            }
            bookings.add(new Bookings(rs.getString("sTime"),
                    rs.getString("eTime"),
                    rs.getString("service"),
                    customer,
                    rs.getString("staff.firstName"),
                    rs.getString("staffID"),
                    Day.values()[rs.getInt("dayofWeek")]));

        }
        return bookings;
    }

    //  Generate a list of bookings based on availability selection, date, start time, customer and business;
    public static ObservableList<Bookings> setBookings(String selection, String date, String iniTime, String business) throws SQLException {
//        String iniTime = "09:30";
        int minutes = 29;
        ObservableList<Bookings> bookings = FXCollections.observableArrayList();
        LocalTime time1 = LocalTime.parse(iniTime);
        LocalTime time2 = time1.plusMinutes(minutes);
        System.out.println("startTime: "+time1);
        System.out.println("endTime: "+time2);
        String sql;

        if(selection.equals("available")){
            sql = "SELECT bookings.*, staff.firstName " +
                    "FROM bookings " +
                    "NATURAL JOIN staff " +
                    "WHERE customerUsername = '' AND date=? AND sTime BETWEEN ? AND ? " +
                    "AND businessName = ?";
        }else if(selection.equals("booked")){
            sql = "SELECT bookings.*, staff.firstName " +
                    "FROM bookings " +
                    "NATURAL JOIN staff " +
                    "WHERE customerUsername <> '' AND date=? AND sTime BETWEEN ? AND ? " +
                    "AND businessName = ?";
        }else{
            sql = "SELECT bookings.*, staff.firstName " +
                    "FROM bookings " +
                    "NATURAL JOIN staff " +
                    "WHERE date=? AND sTime BETWEEN ? AND ? " +
                    "AND businessName = ?";
        }
        PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(sql);
        pstmt.setDate(1, Date.valueOf(date));
        pstmt.setString(2,time1.toString());
        pstmt.setString(3,time2.toString());
        pstmt.setObject(4,business);
        ResultSet rs = pstmt.executeQuery();
        while(rs.next())
        {
            String customer = "";
            String sql2 = "SELECT * FROM customer WHERE username=?";
            PreparedStatement pstmt2 = DbUtil.getConnection().prepareStatement(sql2);
            pstmt2.setString(1,rs.getString("customerUsername"));
            ResultSet rs2 = pstmt2.executeQuery();
            if(rs2.next()){
                customer = rs2.getString("firstName");
            }
            bookings.add(new Bookings(rs.getString("sTime"),
                    rs.getString("eTime"),
                    rs.getString("service"),
                    customer,
                    rs.getString("staff.firstName"),
                    rs.getString("staffID"),
                    Day.values()[rs.getInt("dayofWeek")]));
        }
        return bookings;
    }

    //  Generate a list of bookings based on  current date, customer and business;
    public static ObservableList<Bookings> reviewAppointmentsCustomer(String customerUsername, String business) throws SQLException {
        ObservableList<Bookings> bookings = FXCollections.observableArrayList();
        LocalDate currentDate = LocalDate.now();
        String sql = "SELECT * FROM bookings b " +
                "NATURAL JOIN staff s " +
                "JOIN customer c on b.customerUsername = c.username " +
                "WHERE b.customerUsername =? AND date >= ? " +
                "AND b.businessName = ?";
        ResultSet rs;
        PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(sql);
        pstmt.setString(1,customerUsername);
        pstmt.setDate(2,Date.valueOf(currentDate));
        pstmt.setObject(3,business);
        rs = pstmt.executeQuery();
        while(rs.next()){
            bookings.add(new Bookings(rs.getString("sTime"),
                    rs.getString("eTime"),
                    rs.getString("service"),
                    rs.getString("s.firstName"),
                    rs.getString("date"),
                    Day.values()[rs.getInt("dayofWeek")]));
        }
        return bookings;
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

    public String getService() {
        return service.get();
    }

    public StringProperty serviceProperty() {
        return service;
    }

    public void setService(String service) {
        this.service.set(service);
    }

    public String getCustomer() {
        return customer.get();
    }

    public StringProperty customerProperty() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer.set(customer);
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

    public String getCustomerUsername() {
        return customerUsername.get();
    }

    public StringProperty customerUsernameProperty() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername.set(customerUsername);
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

    public String getDay() {
        return day.get();
    }

    public StringProperty dayProperty() {
        return day;
    }

    public void setDay(String day) {
        this.day.set(day);
    }

    public Day getDayOfWeek() {
        return dayOfWeek;
    }
}

