package appointmentBookingApp.tests;

import appointmentBookingApp.util.DbUtil;
import appointmentBookingApp.view.RegisterCustomerController;
import appointmentBookingApp.view.SetEmpAvailabilityController;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Joshua on 20/04/2017.
 */
class SetEmpAvailabilityControllerTest {
    @BeforeAll
    static void setUp(){
        DbUtil.databaseConnect();
    }

    @Test
        //will succeed
    void checkExists1() {
        SetEmpAvailabilityController test = new SetEmpAvailabilityController();
        assertEquals(true, test.userExists("S000001"));
    }

    @Test
        //will succeed
    void checkExists2() {
        SetEmpAvailabilityController test = new SetEmpAvailabilityController();
        assertEquals(true, test.userExists("S000002"));
    }

    @Test
        //will succeed
    void checkExists3() {
        SetEmpAvailabilityController test = new SetEmpAvailabilityController();
        assertEquals(true, test.userExists("S000003"));
    }

    @Test
        //will succeed
    void checkExists4() {
        SetEmpAvailabilityController test = new SetEmpAvailabilityController();
        assertEquals(true, test.userExists("S000004"));
    }

    @Test
        //will fail
    void checkExists5() {
        SetEmpAvailabilityController test = new SetEmpAvailabilityController();
        assertEquals(true, test.userExists(""));
    }

    @Test
        //will fail
    void checkExists6() {
        SetEmpAvailabilityController test = new SetEmpAvailabilityController();
        assertEquals(true, test.userExists("S000000"));
    }

    @Test
        //will fail
    void checkExists7() {
        SetEmpAvailabilityController test = new SetEmpAvailabilityController();
        assertEquals(true, test.userExists("S111111"));
    }
}
