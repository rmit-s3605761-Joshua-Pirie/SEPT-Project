package appointmentBookingApp.tests;

import appointmentBookingApp.model.DbTableSaveLoad;
import appointmentBookingApp.model.TestData;
import appointmentBookingApp.util.DbUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


class AvailabilityListTest {
    private List<DbTableSaveLoad> dbTables = new ArrayList<>();

    @BeforeEach
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
    }

    @AfterEach
    void tearDown() {
        for(DbTableSaveLoad table : dbTables){
            table.loadTable();
        }
    }

    @Test
    void remainingAvailability() {

    }

}