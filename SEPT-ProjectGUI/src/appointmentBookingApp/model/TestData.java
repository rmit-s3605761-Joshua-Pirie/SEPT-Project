package appointmentBookingApp.model;

import appointmentBookingApp.util.DbUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aydan on 30/04/2017.
 */
public final class TestData {
    public static void populateAllTables(){
        populateAvailabilityTable();
        populateBusinessOwnerTable();
        populateCustomerTable();
        populateServicesTable();
        populateStaffTable();
        populateBookingsTable();
    }
    public static void populateAvailabilityTable(){
        List<String[]> data = new ArrayList<>();
        String stmt = "INSERT INTO availability (staffID, dayOfWeek, startTime, endTime) VALUES (?,?,?,?)";

        data.add(new String[]{"S000001","0","08:00:00","12:00:00"});
        data.add(new String[]{"S000001","0","13:00:00","18:00:00"});
        data.add(new String[]{"S000002","0","08:00:00","18:00:00"});
        data.add(new String[]{"S000002","1","08:00:00","18:00:00"});
        data.add(new String[]{"S000002","2","08:00:00","18:00:00"});
        data.add(new String[]{"S000002","3","08:00:00","18:00:00"});
        data.add(new String[]{"S000002","4","08:00:00","18:00:00"});
        data.add(new String[]{"S000002","5","08:00:00","18:00:00"});
        data.add(new String[]{"S000002","6","08:00:00","18:00:00"});
        data.add(new String[]{"S000003","0","08:00:00","18:00:00"});
        data.add(new String[]{"S000003","1","08:00:00","18:00:00"});
        data.add(new String[]{"S000003","2","08:00:00","18:00:00"});
        data.add(new String[]{"S000003","3","08:00:00","18:00:00"});
        data.add(new String[]{"S000003","4","08:00:00","18:00:00"});
        data.add(new String[]{"S000003","5","08:00:00","18:00:00"});
        data.add(new String[]{"S000003","6","08:00:00","18:00:00"});

        sendToDB(stmt, data);
    }
    public static void populateBookingsTable(){
        List<String[]> data = new ArrayList<>();
        String stmt = "INSERT INTO bookings (date, day, dayOfWeek, sTime, eTime, staffID, service, customerUsername) VALUES (?,?,?,?,?,?,?,?)";

        data.add(new String[]{"2017-04-17","MONDAY","0","09:30:00","10:00:00","S000001","a",""});
        data.add(new String[]{"2017-04-10","MONDAY","0","09:30:00","10:00:00","S000001","b",""});
        data.add(new String[]{"2017-04-10","MONDAY","0","09:30:00","10:00:00","S000002","c",""});
        data.add(new String[]{"2017-04-10","MONDAY","0","09:30:00","10:00:00","S000003","a",""});
        data.add(new String[]{"2017-04-23","SUNDAY","6","09:30:00","10:00:00","S000001","b",""});
        data.add(new String[]{"2017-04-27","THURSDAY","3","09:30:00","10:00:00","S000002","c",""});
        data.add(new String[]{"2017-04-28","FRIDAY","4","09:30:00","10:00:00","S000003","a",""});
        data.add(new String[]{"2017-04-29","SATURDAY","5","09:30:00","10:00:00","S000001","b",""});
        data.add(new String[]{"2017-04-30","SUNDAY","6","09:30:00","10:00:00","S000002","c",""});
        data.add(new String[]{"2017-05-03","WEDNESDAY","2","09:30:00","10:00:00","S000003","a",""});
        data.add(new String[]{"2017-05-04","THURSDAY","3","09:30:00","10:00:00","S000001","b",""});
        data.add(new String[]{"2017-05-05","FRIDAY","4","09:30:00","10:00:00","S000002","c",""});
        data.add(new String[]{"2017-05-06","SATURDAY","5","09:30:00","10:00:00","S000003","a",""});
        data.add(new String[]{"2017-05-08","MONDAY","0","09:30:00","10:00:00","S000001","b",""});
        data.add(new String[]{"2017-05-08","MONDAY","0","10:00:00","10:30:00","S000001","c",""});

        sendToDB(stmt, data);
    }
    public static void populateBusinessOwnerTable(){
        List<String[]> data = new ArrayList<>();
        String stmt = "INSERT INTO businessowner (username, password, firstName, lastName, businessName, address, phone) VALUES (?,?,?,?,?,?,?)";

        data.add(new String[]{"b","b","","","","",""});
        data.add(new String[]{"bill","bill","Bill","Baker","Bill's Buildings","77 Same Street, Silverdale","6183059683"});

        sendToDB(stmt, data);
    }
    public static void populateCustomerTable(){
        List<String[]> data = new ArrayList<>();
        String stmt = "INSERT INTO customer (username, password, firstName, lastName, email, address, phone) VALUES (?,?,?,?,?,?,?)";

        data.add(new String[]{"a","a","Amanda","Smith","a@mail.com","32 Binary Ave, Logtable","1234567890"});
        data.add(new String[]{"bob","bob","bob","bill","bob@mail.com","123 fake st","1234567890"});
        data.add(new String[]{"amy","idontno1!","amy","parker","amy@mail.com","123 fake st","1234567890"});
        data.add(new String[]{"tim","idontno1!","tim","parker","tim@mail.com","123 fake st","1234567890"});
        data.add(new String[]{"jenny","idontno1!","jenny","lo","jenny@mail.com","123 fake st","1234567890"});

        sendToDB(stmt, data);
    }
    public static void populateServicesTable(){
        List<String[]> data = new ArrayList<>();
        String stmt = "INSERT INTO services (service, duration) VALUES (?,?)";

        data.add(new String[]{"a","00:30:00"});
        data.add(new String[]{"b","00:30:00"});
        data.add(new String[]{"c","00:30:00"});
        data.add(new String[]{"Llamas","00:20:00"});
        data.add(new String[]{"TEST","00:15:00"});

        sendToDB(stmt, data);
    }
    public static void populateStaffTable(){
        List<String[]> data = new ArrayList<>();
        String stmt = "INSERT INTO staff (staffID, password, firstName, lastName, address, phone) VALUES (?,?,?,?,?,?)";

        data.add(new String[]{"S000001","idontno1!","catie","Redwood","34 Snoopy Street, Peargrove","61495838571"});
        data.add(new String[]{"S000002","idontno1!","andy","","",""});
        data.add(new String[]{"S000003","idontno1!","freddy","","",""});
        data.add(new String[]{"S000004","idontno1!","candy","","",""});

        sendToDB(stmt, data);
    }

    public static void clearAllTables(){
        clearAvailabilityTable();
        clearBookingsTable();
        clearBusinessOwnerTable();
        clearCustomerTable();
        clearServicesTable();
        clearStaffTable();
    }
    public static void clearAvailabilityTable(){
        String sql = "DELETE FROM availability";
        try (PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(sql);){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void clearBookingsTable(){
        String sql = "DELETE FROM bookings";
        try (PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(sql);){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void clearBusinessOwnerTable(){
        String sql = "DELETE FROM businessowner";
        try (PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(sql);){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void clearCustomerTable(){
        String sql = "DELETE FROM customer";
        try (PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(sql);){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void clearServicesTable(){
        String sql = "DELETE FROM services";
        try (PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(sql);){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void clearStaffTable(){
        String sql = "DELETE FROM staff";
        try (PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(sql);){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void sendToDB(String stmt, List<String[]> data){
        try {
            PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(stmt);
            for(String[] entrys : data){
                int count = 1;
                for (String sql : entrys){
                    pstmt.setObject(count++,sql);
                }
                pstmt.addBatch();
                System.out.println(pstmt);
            }
            pstmt.executeLargeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
