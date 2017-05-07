package appointmentBookingApp.tests;

import appointmentBookingApp.util.DbUtil;
import appointmentBookingApp.view.AddServiceDialogController;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


class AddServiceDialogControllerTest {
    private String service, duration;
    private boolean useAlertBox = false;

    @BeforeAll
    static void setUp(){
        DbUtil.databaseConnect();
        String sql;
        try {
            sql = "INSERT INTO services VALUES('TEST2','99:00')";
            DbUtil.getNewStatment().executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void tearDown(){
        String sql;
        try {
            sql = "DELETE FROM services WHERE duration='99:00'";
            DbUtil.getNewStatment().executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    Testing validation of correct input.
    @Test
    void isVaild1() {
        service = "TEST";
        duration = "99:00";

        AddServiceDialogController test = new AddServiceDialogController();
        assertEquals(true,test.isValid(service, duration, useAlertBox));
    }

//    Testing that service needs to be defined.
    @Test
    void isVaild2() {
        service = "";
        duration = "99:00";

        AddServiceDialogController test = new AddServiceDialogController();
        assertEquals(false,test.isValid(service, duration, useAlertBox));
    }

//    Testing that validation fails where a service of the same name already exists.
    @Test
    void isVaild3() {
        service = "TEST2";
        duration = "99:00";

        AddServiceDialogController test = new AddServiceDialogController();
        assertEquals(false,test.isValid(service, duration, useAlertBox));
    }

//    Testing that validation fails where the duration field is empty.
    @Test
    void isVaild4() {
        service = "TEST";
        duration = "";

        AddServiceDialogController test = new AddServiceDialogController();
        assertEquals(false,test.isValid(service, duration, useAlertBox));
    }

//    Testing that validation fails where the duration field is not the correct format. (hh:mm)
    @Test
    void isVaild5() {
        service = "TEST";
        duration = "9";

        AddServiceDialogController test = new AddServiceDialogController();
        assertEquals(false,test.isValid(service, duration, useAlertBox));
    }

//    Testing that validation fails when letters used in the duration field.
    @Test
    void isVaild6() {
        service = "TEST";
        duration = "qw:er";

        AddServiceDialogController test = new AddServiceDialogController();
        assertEquals(false,test.isValid(service, duration, useAlertBox));
    }

//    Test confirming that a service of the same name can't be added to the database.
    @Test
    void addService1() {
        String business = "Baker";
        service = "TEST2";
        duration = "99:00";
        AddServiceDialogController test = new AddServiceDialogController();
        assertEquals(false,test.addServiceToDB(service, duration, business));
    }

//    Tests that a new service can be added to the database.
    @Test
    void addService2() {
        String business = "Baker";
        service = "TEST3";
        duration = "99:00";
        AddServiceDialogController test = new AddServiceDialogController();
        assertEquals(true,test.addServiceToDB(service, duration, business));
    }



}