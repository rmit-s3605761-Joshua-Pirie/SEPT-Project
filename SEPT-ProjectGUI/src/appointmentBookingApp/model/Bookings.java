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

    public Bookings(){this(null);}

    public Bookings(String sTime){
        this.sTime = new SimpleStringProperty();
        this.eTime = null;
        this.service = null;
        this.customer = null;
        this.empName = null;
        this.staffID = null;
    }

    public Bookings(String sTime, String eTime, String service, String customer, String empName, String staffID){
        this.sTime = new SimpleStringProperty(sTime);
        this.eTime = new SimpleStringProperty(eTime);
        this.service = new SimpleStringProperty(service);
        this.customer = new SimpleStringProperty(customer);
        this.empName = new SimpleStringProperty(empName);
        this.staffID = new SimpleStringProperty(staffID);
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

    public static ObservableList<Bookings> setBookings(String date, String iniTime) throws SQLException {
//        String sTime = "09:30";
        int minutes = 29;
        ObservableList<Bookings> bookings = FXCollections.observableArrayList();
        LocalTime time1 = LocalTime.parse(iniTime);
        LocalTime time2 = time1.plusMinutes(minutes);
        System.out.println("startTime: "+time1);
        System.out.println("endTime: "+time2);

        String sql = "SELECT bookings.*, staff.firstName " +
                    "FROM bookings " +
                    "JOIN staff ON bookings.staffID = staff.staffID " +
                    "WHERE date=? AND sTime BETWEEN ? AND ?";
        PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(sql);
        pstmt.setDate(1, Date.valueOf(date));
        pstmt.setString(2,time1.toString());
        pstmt.setString(3,time2.toString());
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
                    rs.getString("staffID")));

        }
        return bookings;
    }

    public static ObservableList<Bookings> setBookings(String selection, String date, String iniTime) throws SQLException {
//        String iniTime = "09:30";
        int minutes = 29;
        ObservableList<Bookings> bookings = FXCollections.observableArrayList();
//        LocalDate dt1 = LocalDate.now().plusDays(2);
//        LocalDate dt2 = LocalDate.now().plusDays(7);
//        System.out.println("startDate: "+dt1);
//        System.out.println("endDate: "+dt2);
        LocalTime time1 = LocalTime.parse(iniTime);
        LocalTime time2 = time1.plusMinutes(minutes);
        System.out.println("startTime: "+time1);
        System.out.println("endTime: "+time2);

        String sql, username ="";

        if(selection.equals("available")){
            sql = "SELECT bookings.*, staff.firstName " +
                    "FROM bookings " +
                    "JOIN staff ON bookings.staffID = staff.staffID " +
                    "WHERE customerUsername = '' AND date=? AND sTime BETWEEN ? AND ?";
        }else if(selection.equals("booked")){
            sql = "SELECT bookings.*, staff.firstName " +
                    "FROM bookings " +
                    "JOIN staff ON bookings.staffID = staff.staffID " +
                    "WHERE customerUsername <> '' AND date=? AND sTime BETWEEN ? AND ?";
        }else{
            sql = "SELECT bookings.*, staff.firstName " +
                    "FROM bookings " +
                    "JOIN staff ON bookings.staffID = staff.staffID " +
                    "WHERE date=? AND sTime BETWEEN ? AND ?";
        }
        PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(sql);
        pstmt.setDate(1, Date.valueOf(date));
        pstmt.setString(2,time1.toString());
        pstmt.setString(3,time2.toString());;
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
                    rs.getString("staffID")));
        }
        return bookings;
    }
}

