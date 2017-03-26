package appointmentBookingApp.util;

import org.testng.annotations.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class DbUtilTest {
    @Test
    public void addCustomerToDB() throws Exception {
        boolean successful = DbUtil.addCustomerToDB("fred", "fred", "Fred", "Rogers", "2 Burnley Crescent, Aldon", "61486495812");
        assertEquals(true, successful);
    }

    @Test
    public void addBookingToDB() throws Exception {
        boolean successful = DbUtil.addBookingToDB("bill", new Date(2017, 03, 26), new Date(2017, 03, 30));
        assertEquals(true, successful);
    }

}