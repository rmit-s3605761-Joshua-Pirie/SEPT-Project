package appointmentBookingApp.tests;

import appointmentBookingApp.model.DbTableSaveLoad;
import appointmentBookingApp.util.DbUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Aydan on 29/04/2017.
 */
class AvailabilityListTest {
    String table;
    ResultSet rs;
    ResultSetMetaData meta;
    List<String> columns = new ArrayList<>();
    List<DbTableSaveLoad> dbTables = new ArrayList<>();

    @BeforeEach
    void setUp() {
        DbUtil.databaseConnect();
        String sql;
        PreparedStatement pstmt;

        String table1 = "businessowner";
        String table2 = "availability";
        dbTables.add( new DbTableSaveLoad().saveTable(table1));
        dbTables.add( new DbTableSaveLoad().saveTable(table2));
//        try {
//            sql = "SELECT * FROM "+table;
//            pstmt = DbUtil.getConnection().prepareStatement(sql);
//            rs = pstmt.executeQuery();
//            meta =rs.getMetaData();
//            for (int i = 1; i <= meta.getColumnCount(); i++)
//                columns.add(meta.getColumnName(i));
//            sql = "DELETE FROM "+table;
//            pstmt = DbUtil.getConnection().prepareStatement(sql);
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    @AfterEach
    void tearDown() {
        for(DbTableSaveLoad table : dbTables){
            table.loadTable();
        }
//        try (PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(
//                "INSERT INTO " + table + " ("
//                        + columns.stream().collect(Collectors.joining(", "))
//                        + ") VALUES ("
//                        + columns.stream().map(c -> "?").collect(Collectors.joining(", "))
//                        + ")"
//        )) {
//
//            while (rs.next()) {
//                for (int i = 1; i <= meta.getColumnCount(); i++)
//                    pstmt.setObject(i, rs.getObject(i));
//
//                pstmt.addBatch();
//            }
//
//            pstmt.executeBatch();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    void remainingAvailability() {
    }

}