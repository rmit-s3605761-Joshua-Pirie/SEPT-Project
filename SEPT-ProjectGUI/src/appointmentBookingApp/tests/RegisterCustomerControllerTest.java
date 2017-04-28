package appointmentBookingApp.tests;

import appointmentBookingApp.util.DbUtil;
import appointmentBookingApp.view.AddStaffController;
import appointmentBookingApp.view.RegisterCustomerController;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by David on 10/04/2017.
 */
class RegisterCustomerControllerTest {
    @BeforeAll
    static void setUp(){
        DbUtil.databaseConnect();
        String sql;
        try {
            sql = "INSERT INTO customer VALUES('cccccc','aaaaa','aaaaa','aaaaa','aaaaa','aaaaa','aaaaa')";
            DbUtil.getNewStatment().executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void tearDown(){
        String sql;
        try {
            sql = "DELETE FROM customer WHERE userName in('cccccc','ted', 'bill')";
            DbUtil.getNewStatment().executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
        //will succeed
    void addCustomer() {
        String userNAme = "ted";
        String password = "bbbb";
        String firstName = "bbbb";
        String lastName = "bbbb";
        String eMail = "bbbb";
        String address = "bbbb";
        String phone = "bbbb";
        RegisterCustomerController test = new RegisterCustomerController();
        assertEquals(true,test.addToDB(userNAme, password, firstName, lastName, eMail, address, phone));
    }
    @Test
        //will fail phone number is too long
    void addCustomer1() {
        String userNAme = "bill";
        String password = "bbbb";
        String firstName = "bbbb";
        String lastName = "bbbb";
        String eMail = "bbbb";
        String address = "bbbb";
        String phone = "12345678901234";
        RegisterCustomerController test = new RegisterCustomerController();
        assertEquals(false,test.addToDB(userNAme, password, firstName, lastName, eMail, address, phone));
    }

    @Test
        //will fail repeated username
    void addCustomer2() {
        String userNAme = "ted";
        String password = "bbbb";
        String firstName = "bbbb";
        String lastName = "bbbb";
        String eMail = "bbbb";
        String address = "bbbb";
        String phone = "12345678901234";
        RegisterCustomerController test = new RegisterCustomerController();
        assertEquals(false,test.addToDB(userNAme, password, firstName, lastName, eMail, address, phone));
    }

    @Test
        //will fail username is null
    void addCustomer3() {
        String userNAme = null;
        String password = "bbbb";
        String firstName = "bbbb";
        String lastName = "bbbb";
        String eMail = "bbbb";
        String address = "bbbb";
        String phone = "12345678901234";
        RegisterCustomerController test = new RegisterCustomerController();
        assertEquals(false,test.addToDB(userNAme, password, firstName, lastName, eMail, address, phone));
    }
}