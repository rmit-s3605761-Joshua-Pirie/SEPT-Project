package appointmentBookingApp.view;

import appointmentBookingApp.MainApp;
import appointmentBookingApp.util.CreateStage;
import appointmentBookingApp.util.CssUtil;
import appointmentBookingApp.util.DbUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Aydan on 30/03/2017.
 */
public class BusinessHomepageController {
    @FXML
    ComboBox<String> cbStyle;
    private MainApp mainApp;
    //Allow for the control of the main app.
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    //  Setup the FXML page on load
    public void initialize(){
        String[] styles = {"Modena","Black","Win7"};
        cbStyle.getItems().addAll(styles);
        setStyle();
    }

    @FXML
    public void logout(){
        mainApp.showLogin();
    }

    @FXML
    public void showUpcomingBookings(){
        try {
            String fxml = "view/UpcomingBookings.fxml";
            String title = "Upcoming Booking";
            UpcomingBookingsController controller = CreateStage.newDialogStage(fxml,title).getController();
            controller.setDialogStage(CreateStage.getDialogStage());
            controller.setMainApp(mainApp);

            // Show the dialog and wait until the user closes it
            CreateStage.getDialogStage().showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showBookingHistory(){
        try {
            String fxml = "view/BookingHistory.fxml";
            String title = "Booking History";
            BookingHistoryController controller = CreateStage.newDialogStage(fxml,title).getController();
            controller.setDialogStage(CreateStage.getDialogStage());
            controller.setMainApp(mainApp);

            // Show the dialog and wait until the user closes it
            CreateStage.getDialogStage().showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public boolean showAddServiceDialog(){
        try {
            String fxml = "view/AddServiceDialog.fxml";
            String title = "Add Service";
            AddServiceDialogController controller = CreateStage.newDialogStage(fxml,title).getController();
            controller.setDialogStage(CreateStage.getDialogStage());
            controller.setMainApp(mainApp);
            controller.ini();

            // Show the dialog and wait until the user closes it
            CreateStage.getDialogStage().showAndWait();
            return controller.isAddClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    public void showRemainingAvailability(){
        try {
            String fxml = "view/RemainingAvailability.fxml";
            String title = "Remaining Availability";
            RemainingAvailabilityController controller = CreateStage.newDialogStage(fxml,title).getController();
            controller.setDialogStage(CreateStage.getDialogStage());
            controller.setMainApp(mainApp);
            controller.ini();

            // Show the dialog and wait until the user closes it
            CreateStage.getDialogStage().showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showAddStaffDialog(){
        try {
            String fxml = "view/AddStaff.fxml";
            String title = "Add Staff";
            AddStaffController controller = CreateStage.newDialogStage(fxml,title).getController();
            controller.setDialogStage(CreateStage.getDialogStage());

            // Show the dialog and wait until the user closes it
            CreateStage.getDialogStage().showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public boolean showSetEmpAvailabilityDialog(){
        try {
            String fxml = "view/SetEmpAvailability.fxml";
            String title = "Set Employee Availability";
            SetEmpAvailabilityController controller = CreateStage.newDialogStage(fxml,title).getController();
            controller.setDialogStage(CreateStage.getDialogStage());

            // Show the dialog and wait until the user closes it
            CreateStage.getDialogStage().showAndWait();
            return controller.isAddClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    public void showUpdateStaffDialog(){
        try {
            String fxml = "view/UpdateStaffDetails.fxml";
            String title = "Update Staff Details";
            UpdateStaffDetailsController controller = CreateStage.newDialogStage(fxml,title).getController();
            controller.setDialogStage(CreateStage.getDialogStage());

            // Show the dialog and wait until the user closes it
            CreateStage.getDialogStage().showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showCreateAppointmentDialog(){
        try {
            String fxml = "view/CreateAppOwner.fxml";
            String title = "Create Appointments";
            CreateAppOwnerController controller = CreateStage.newDialogStage(fxml,title).getController();
            controller.setDialogStage(CreateStage.getDialogStage());
            controller.setMainApp(mainApp);
            controller.initialize();

            // Show the dialog and wait until the user closes it
            CreateStage.getDialogStage().showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    Selects css style to be used by the business.
    @FXML
    public void setStyle(){

        cbStyle.valueProperty().addListener(((observable, oldValue, newValue) -> {
            String theme = "";
            if(!MainApp.getPrimaryStage().getScene().getStylesheets().isEmpty()){
                System.out.println("Current Style: "+ MainApp.getPrimaryStage().getScene().getStylesheets());
                MainApp.getPrimaryStage().getScene().getStylesheets().clear();
            }

            switch (cbStyle.getValue()){
                case "Black":
                    theme = "black";
                    break;
                case "Modena":
                    theme = "modena";
                    break;
                case "Win7":
                    theme = "Win7";
                    break;
                default:
            }
            CssUtil.setTheme(theme);
            System.out.println("New Style: "+ MainApp.getPrimaryStage().getScene().getStylesheets()+"\n");
        }));
    }
}
