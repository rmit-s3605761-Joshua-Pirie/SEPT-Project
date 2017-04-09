package appointmentBookingApp.tests;

import appointmentBookingApp.util.DbUtil;
import appointmentBookingApp.view.AddStaffController;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by David on 10/04/2017.
 */
class AddStaffControllerTest {
    @BeforeAll
    static void setUp(){
        DbUtil.databaseConnect();
        String sql;
        try {
            sql = "INSERT INTO staff VALUES('S000006','aaaaa','aaaaa','aaaaa','aaaaa','aaaaa')";
            DbUtil.getNewStatment().executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void tearDown(){
       String sql;
       try {
           sql = "DELETE FROM staff WHERE staffID in('S000006','s111111')";
           DbUtil.getNewStatment().executeUpdate(sql);
      } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    //will succeed
    void addStaffToDB() {
        String ID = "s111111";
        String firstName = "bbbbbb";
        String lastName = "bbbbbb";
        String phone = "aaaaa";
        String address = "bbbb";
        AddStaffController test = new AddStaffController();
        assertEquals(true,test.addStaffToDB(ID, firstName, lastName, address, phone));
    }
    @Test
    //will fail as ID is too long
    void addStaffToDB2() {
        String ID = "s111sdfsgasdg111";
        String firstName = "bbbbbb";
        String lastName = "bbbbbb";
        String phone = "1234567890";
        String address = "234 lala land";
        AddStaffController test = new AddStaffController();
        assertEquals(false,test.addStaffToDB(ID, firstName, lastName, address, phone));
    }
    @Test
    //will fail as primary key is null
    void addStaffToDB3() {
        String ID = null;
        String firstName = "bbbbbb";
        String lastName = "bbbbbb";
        String phone = "1234567890";
        String address = "123 fake street";
        AddStaffController test = new AddStaffController();
        assertEquals(false,test.addStaffToDB(ID, firstName, lastName, address, phone));
    }

    @Test
        //will fail as primary key is repeated
    void addStaffToDB4() {
        String ID = "S000006";
        String firstName = "bbbbbb";
        String lastName = "bbbbbb";
        String phone = "1234567890";
        String address = "123 fake street";
        AddStaffController test = new AddStaffController();
        assertEquals(false,test.addStaffToDB(ID, firstName, lastName, address, phone));
    }

    @Test
        //will fail as phone number is too long
    void addStaffToDB5() {
        String ID = "S000006";
        String firstName = "bbbbbb";
        String lastName = "bbbbbb";
        String phone = "123453423467890";
        String address = "123 fake street";
        AddStaffController test = new AddStaffController();
        assertEquals(false,test.addStaffToDB(ID, firstName, lastName, address, phone));
    }
}