package appointmentBookingApp.view;


import appointmentBookingApp.MainApp;
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
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/BookAppointment.fxml"));
            AnchorPane BookAppointment = (AnchorPane) loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Book Appointment");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(BookAppointment);
            dialogStage.setScene(scene);

//            BookAppointmentController controller = loader.getController();
//            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showReviewAppointments(){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ReviewAppointments.fxml"));
            AnchorPane RemainingAvailability = (AnchorPane) loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Review Appointments");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(RemainingAvailability);
            dialogStage.setScene(scene);

            ReviewAppointmentsController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.ini(user);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showUpdateDetails(){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/UpdateDetails.fxml"));
            AnchorPane UpdateDetails =loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Update Details");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(UpdateDetails);
            dialogStage.setScene(scene);

//            UpdateDetailsController controller = loader.getController();
//            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
