package appointmentBookingApp.tests;

import appointmentBookingApp.model.AvailabilityList;
import appointmentBookingApp.model.Day;
import appointmentBookingApp.model.DbTableSaveLoad;
import appointmentBookingApp.model.TestData;
import appointmentBookingApp.util.DbUtil;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class AvailabilityListTest {
    private List<DbTableSaveLoad> dbTables = new ArrayList<>();
    String business ="Baker";

    @BeforeAll
    void setUp() {
        DbUtil.databaseConnect();

        String table1 = "bookings";
        String table2 = "availability";
        try {
            dbTables.add( new DbTableSaveLoad().saveTable(table1));
            dbTables.add( new DbTableSaveLoad().saveTable(table2));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        TestData.clearAvailabilityTable();
        TestData.clearBookingsTable();
    }

    @AfterAll
    void tearDown() {
        for(DbTableSaveLoad table : dbTables){
            table.loadTable();
        }
    }

    /*Testing the remaining availability of an employee for the week.
    * @expected 2, employee is only allocated one booking during there
    * availability for that day and bookings do not take up the entirety of
    * that days availability */
    @Test
    void remainingAvailability1() {
        List<String[]> data1 = new ArrayList<>();
        List<String[]> data2 = new ArrayList<>();
        String stmt1 = "INSERT INTO availability (staffID, dayOfWeek, startTime, endTime) VALUES (?,?,?,?)";
        String stmt2 = "INSERT INTO bookings (date, day, dayOfWeek, sTime, eTime, staffID, service, customerUsername) VALUES (?,?,?,?,?,?,?,?)";
        data1.add(new String[]{"S000001", Integer.toString(Day.valueOf(LocalDate.now().getDayOfWeek().toString()).ordinal()),"08:00:00","12:00:00"});
        data2.add(new String[]{LocalDate.now().toString(),LocalDate.now().getDayOfWeek().toString(), Integer.toString(Day.valueOf(LocalDate.now().getDayOfWeek().toString()).ordinal()),"09:30:00","10:00:00","S000001","a",""});
        TestData.sendToDB(stmt1, data1);
        TestData.sendToDB(stmt2, data2);

        AvailabilityList test = new AvailabilityList();
        try {
            assertEquals(2,test.remainingAvailability().size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        TestData.clearAvailabilityTable();
        TestData.clearBookingsTable();
    }

    /*Testing the remaining availability of an employee for the week.
    * @expected 2, employee is allocated two bookings during there
    * availability for that day and bookings do not take up the entirety of
    * that days availability and they are right after one another*/
    @Test
    void remainingAvailability2() {
        List<String[]> data1 = new ArrayList<>();
        List<String[]> data2 = new ArrayList<>();
        String stmt1 = "INSERT INTO availability (staffID, dayOfWeek, startTime, endTime) VALUES (?,?,?,?)";
        String stmt2 = "INSERT INTO bookings (date, day, dayOfWeek, sTime, eTime, staffID, service, customerUsername) VALUES (?,?,?,?,?,?,?,?)";
        data1.add(new String[]{"S000001", Integer.toString(Day.valueOf(LocalDate.now().getDayOfWeek().toString()).ordinal()),"08:00:00","12:00:00"});
        data2.add(new String[]{LocalDate.now().toString(),LocalDate.now().getDayOfWeek().toString(), Integer.toString(Day.valueOf(LocalDate.now().getDayOfWeek().toString()).ordinal()),"09:30:00","10:00:00","S000001","a",""});
        data2.add(new String[]{LocalDate.now().toString(),LocalDate.now().getDayOfWeek().toString(), Integer.toString(Day.valueOf(LocalDate.now().getDayOfWeek().toString()).ordinal()),"10:00:00","10:30:00","S000001","a",""});
        TestData.sendToDB(stmt1, data1);
        TestData.sendToDB(stmt2, data2);

        try {
            assertEquals(2,AvailabilityList.remainingAvailability().size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        TestData.clearAvailabilityTable();
        TestData.clearBookingsTable();
    }

}