package appointmentBookingApp.view;


import appointmentBookingApp.MainApp;
import appointmentBookingApp.util.CreateStage;
import appointmentBookingApp.util.DbUtil;
import appointmentBookingApp.util.Util;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerHomepageController {
    @FXML
    private Label title;
    private MainApp mainApp;
    private String user;

    //Allow for the control of the main app.
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void logout(){
        mainApp.showLogin();
    }

    public void ini(String sqlUsername) {
        user = sqlUsername;
        String sql = "SELECT firstName FROM customer WHERE username=?";
        try {
            PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(sql);
            pstmt.setString(1,sqlUsername);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next())
                title.setText(title.getText() + Util.toTitleCase(rs.getString("firstName")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showBookAppointment(){
        try {
            String fxml = "view/CreateAppointmentCustomer.fxml";
            String title = "Book Appointment";
            CreateAppointmentCustomerController controller = CreateStage.newDialogStage(fxml,title).getController();
            controller.setDialogStage(CreateStage.getDialogStage());
            controller.setMainApp(mainApp);
            controller.initialize(user);

            // Show the dialog and wait until the user closes it
            CreateStage.getDialogStage().showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showReviewAppointments(){
        try {
            String fxml = "view/ReviewAppointments.fxml";
            String title = "Review Appointments";
            ReviewAppointmentsController controller = CreateStage.newDialogStage(fxml,title).getController();
            controller.setDialogStage(CreateStage.getDialogStage());
            controller.setMainApp(mainApp);
            controller.ini(user);

            // Show the dialog and wait until the user closes it
            CreateStage.getDialogStage().showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showUpdateDetails(){
        try {
            String fxml = "view/UpdateDetails.fxml";
            String title = "Update Details";
            UpdateDetailsController controller = CreateStage.newDialogStage(fxml,title).getController();
            controller.setDialogStage(CreateStage.getDialogStage());
            controller.ini(user);

            // Show the dialog and wait until the user closes it
            CreateStage.getDialogStage().showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
