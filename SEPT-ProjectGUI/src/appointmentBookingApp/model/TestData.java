package appointmentBookingApp.model;

import appointmentBookingApp.util.DbUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
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
        data.add(new String[]{"S000004","0","08:00:00","18:00:00"});
        data.add(new String[]{"S000004","1","08:00:00","18:00:00"});
        data.add(new String[]{"S000004","2","08:00:00","18:00:00"});
        data.add(new String[]{"S000004","3","08:00:00","18:00:00"});
        data.add(new String[]{"S000004","4","08:00:00","18:00:00"});
        data.add(new String[]{"S000004","5","08:00:00","18:00:00"});
        data.add(new String[]{"S000004","6","08:00:00","18:00:00"});
        data.add(new String[]{"S000005","0","08:00:00","18:00:00"});
        data.add(new String[]{"S000005","1","08:00:00","18:00:00"});
        data.add(new String[]{"S000005","2","08:00:00","18:00:00"});
        data.add(new String[]{"S000005","3","08:00:00","18:00:00"});
        data.add(new String[]{"S000005","4","08:00:00","18:00:00"});
        data.add(new String[]{"S000005","5","08:00:00","18:00:00"});
        data.add(new String[]{"S000005","6","08:00:00","18:00:00"});
        data.add(new String[]{"S000006","0","08:00:00","18:00:00"});
        data.add(new String[]{"S000006","1","08:00:00","18:00:00"});
        data.add(new String[]{"S000006","2","08:00:00","18:00:00"});
        data.add(new String[]{"S000006","3","08:00:00","18:00:00"});
        data.add(new String[]{"S000006","4","08:00:00","18:00:00"});
        data.add(new String[]{"S000006","5","08:00:00","18:00:00"});
        data.add(new String[]{"S000006","6","08:00:00","18:00:00"});

        sendToDB(stmt, data);
    }
    public static void populateBookingsTable(){
        List<String[]> data = new ArrayList<>();
        String stmt = "INSERT INTO bookings (date, day, dayOfWeek, sTime, eTime, staffID, service, customerUsername, businessName) VALUES (?,?,?,?,?,?,?,?,?)";

        data.add(new String[]{getDate(0),getDay(0),getDayOfWeek(0),"09:30:00","10:00:00","S000001","a","a","Baker"});
        data.add(new String[]{getDate(0),getDay(0),getDayOfWeek(0),"10:30:00","11:00:00","S000001","b","a","Baker"});
        data.add(new String[]{getDate(0),getDay(0),getDayOfWeek(0),"09:30:00","10:00:00","S000002","c","a","Baker"});
        data.add(new String[]{getDate(7),getDay(7),getDayOfWeek(7),"09:30:00","10:00:00","S000003","a","a","Baker"});
        data.add(new String[]{getDate(1),getDay(1),getDayOfWeek(1),"09:30:00","10:00:00","S000001","b","a","Baker"});
        data.add(new String[]{getDate(2),getDay(2),getDayOfWeek(2),"09:30:00","10:00:00","S000002","c","a","Baker"});
        data.add(new String[]{getDate(3),getDay(3),getDayOfWeek(3),"09:30:00","10:00:00","S000003","a","a","Baker"});
        data.add(new String[]{getDate(4),getDay(4),getDayOfWeek(4),"09:30:00","10:00:00","S000001","b","a","Baker"});
        data.add(new String[]{getDate(5),getDay(5),getDayOfWeek(5),"09:30:00","10:00:00","S000002","c","a","Baker"});
        data.add(new String[]{getDate(6),getDay(0),getDayOfWeek(6),"09:30:00","10:00:00","S000003","a","a","Baker"});
        data.add(new String[]{getDate(-1),getDay(-1),getDayOfWeek(-1),"09:30:00","10:00:00","S000001","b","a","Baker"});
        data.add(new String[]{getDate(-1),getDay(-1),getDayOfWeek(-1),"09:30:00","10:00:00","S000002","c","a","Baker"});
        data.add(new String[]{getDate(-1),getDay(-1),getDayOfWeek(-1),"09:30:00","10:00:00","S000003","a","a","Baker"});
        data.add(new String[]{getDate(-8),getDay(-8),getDayOfWeek(-8),"09:30:00","10:00:00","S000001","b","a","Baker"});
        data.add(new String[]{getDate(-8),getDay(-8),getDayOfWeek(-8),"10:00:00","10:30:00","S000001","c","a","Baker"});

        data.add(new String[]{getDate(0),getDay(0),getDayOfWeek(0),"09:30:00","10:00:00","S000004","Llamas","a","Bill's Buildings"});
        data.add(new String[]{getDate(0),getDay(0),getDayOfWeek(0),"10:30:00","11:00:00","S000004","Cows","a","Bill's Buildings"});
        data.add(new String[]{getDate(0),getDay(0),getDayOfWeek(0),"09:30:00","10:00:00","S000005","Llamas","a","Bill's Buildings"});
        data.add(new String[]{getDate(7),getDay(7),getDayOfWeek(7),"09:30:00","10:00:00","S000006","Cows","a","Bill's Buildings"});
        data.add(new String[]{getDate(1),getDay(1),getDayOfWeek(1),"09:30:00","10:00:00","S000004","Llamas","a","Bill's Buildings"});
        data.add(new String[]{getDate(2),getDay(2),getDayOfWeek(2),"09:30:00","10:00:00","S000005","Cows","a","Bill's Buildings"});
        data.add(new String[]{getDate(3),getDay(3),getDayOfWeek(3),"09:30:00","10:00:00","S000006","Llamas","a","Bill's Buildings"});
        data.add(new String[]{getDate(4),getDay(4),getDayOfWeek(4),"09:30:00","10:00:00","S000004","Llamas","a","Bill's Buildings"});
        data.add(new String[]{getDate(5),getDay(5),getDayOfWeek(5),"09:30:00","10:00:00","S000005","Cows","a","Bill's Buildings"});
        data.add(new String[]{getDate(6),getDay(0),getDayOfWeek(6),"09:30:00","10:00:00","S000006","Llamas","a","Bill's Buildings"});
        data.add(new String[]{getDate(-1),getDay(-1),getDayOfWeek(-1),"09:30:00","10:00:00","S000004","Cows","a","Bill's Buildings"});
        data.add(new String[]{getDate(-1),getDay(-1),getDayOfWeek(-1),"09:30:00","10:00:00","S000005","Llamas","a","Bill's Buildings"});
        data.add(new String[]{getDate(-1),getDay(-1),getDayOfWeek(-1),"09:30:00","10:00:00","S000006","Cows","a","Bill's Buildings"});
        data.add(new String[]{getDate(-8),getDay(-8),getDayOfWeek(-8),"09:30:00","10:00:00","S000004","Llamas","a","Bill's Buildings"});
        data.add(new String[]{getDate(-8),getDay(-8),getDayOfWeek(-8),"10:00:00","10:30:00","S000006","Cows","a","Bill's Buildings"});

        sendToDB(stmt, data);
    }
    public static void populateBusinessOwnerTable(){
        List<String[]> data = new ArrayList<>();
        String stmt = "INSERT INTO businessowner (username, password, firstName, lastName, businessName, address, phone, sTime, eTime) VALUES (?,?,?,?,?,?,?,?,?)";

        data.add(new String[]{"b","Qwerty1!","Ben","Baker","Baker","","","09:00","18:00"});
        data.add(new String[]{"bill","Qwerty1!","Bill","Baker","Bill's Buildings","77 Same Street, Silverdale","6183059683","09:00","18:00"});

        sendToDB(stmt, data);
    }
    public static void populateCustomerTable(){
        List<String[]> data = new ArrayList<>();
        String stmt = "INSERT INTO customer (username, password, firstName, lastName, email, address, phone) VALUES (?,?,?,?,?,?,?)";

        data.add(new String[]{"a","Qwerty1!","Amanda","Smith","a@mail.com","32 Binary Ave, Logtable","1234567890"});
        data.add(new String[]{"bob","Qwerty1!","bob","bill","bob@mail.com","123 fake st","1234567890"});
        data.add(new String[]{"amy","Qwerty1!","amy","parker","amy@mail.com","123 fake st","1234567890"});
        data.add(new String[]{"tim","Qwerty1!","tim","parker","tim@mail.com","123 fake st","1234567890"});
        data.add(new String[]{"jenny","Qwerty1!","jenny","lo","jenny@mail.com","123 fake st","1234567890"});

        sendToDB(stmt, data);
    }
    public static void populateServicesTable(){
        List<String[]> data = new ArrayList<>();
        String stmt = "INSERT INTO services (service, duration, businessName) VALUES (?,?,?)";

        data.add(new String[]{"a","00:30:00","Baker"});
        data.add(new String[]{"b","00:30:00","Baker"});
        data.add(new String[]{"c","00:30:00","Baker"});
        data.add(new String[]{"Llamas","00:20:00","Bill's Buildings"});
        data.add(new String[]{"Cows","00:15:00","Bill's Buildings"});

        sendToDB(stmt, data);
    }
    public static void populateStaffTable(){
        List<String[]> data = new ArrayList<>();
        String stmt = "INSERT INTO staff (staffID, password, firstName, lastName, address, phone, businessName) VALUES (?,?,?,?,?,?,?)";

        data.add(new String[]{"S000001","Qwerty1!","catie","Redwood","34 Snoopy Street, Peargrove","61495838571","Baker"});
        data.add(new String[]{"S000002","Qwerty1!","andy","","","","Baker"});
        data.add(new String[]{"S000003","Qwerty1!","freddy","","","","Baker"});
        data.add(new String[]{"S000004","Qwerty1!","candy","","","","Bill's Buildings"});
        data.add(new String[]{"S000005","Qwerty1!","liam","","","","Bill's Buildings"});
        data.add(new String[]{"S000006","Qwerty1!","jane","","","","Bill's Buildings"});

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

    public static String getDate(int timeShift){
        return LocalDate.now().plusDays(timeShift).toString();
    }

    public static String getDay(int timeShift){
        return LocalDate.now().plusDays(timeShift).getDayOfWeek().toString();
    }

    public static String getDayOfWeek(int timeShift){
        return Integer.toString(Day.valueOf(LocalDate.now().plusDays(timeShift).getDayOfWeek().toString()).ordinal());
    }
}
