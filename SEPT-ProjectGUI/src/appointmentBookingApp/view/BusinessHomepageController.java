package appointmentBookingApp.view;

import appointmentBookingApp.MainApp;
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
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/UpcomingBookings.fxml"));
            AnchorPane UpcomingBookings = loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Upcoming Booking");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(MainApp.getPrimaryStage());
            dialogStage.setScene(new Scene((UpcomingBookings)));
            dialogStage.getScene().getStylesheets().addAll(MainApp.getPrimaryStage().getScene().getStylesheets());

            UpcomingBookingsController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(mainApp);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showBookingHistory(){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/BookingHistory.fxml"));
            AnchorPane BookingHistory = loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Booking History");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(MainApp.getPrimaryStage());
            dialogStage.setScene(new Scene(BookingHistory));
            dialogStage.getScene().getStylesheets().addAll(MainApp.getPrimaryStage().getScene().getStylesheets());

            BookingHistoryController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(mainApp);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public boolean showAddServiceDialog(){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/AddServiceDialog.fxml"));
            AnchorPane serviceDialog = loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Service");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(MainApp.getPrimaryStage());
            dialogStage.setScene(new Scene(serviceDialog));
            dialogStage.getScene().getStylesheets().addAll(MainApp.getPrimaryStage().getScene().getStylesheets());

            AddServiceDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(mainApp);
            controller.ini();

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isAddClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    public void showRemainingAvailability(){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RemainingAvailability.fxml"));
            AnchorPane RemainingAvailability = loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Remaining Availability");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(MainApp.getPrimaryStage());
            dialogStage.setScene(new Scene(RemainingAvailability));
            dialogStage.getScene().getStylesheets().addAll(MainApp.getPrimaryStage().getScene().getStylesheets());

            RemainingAvailabilityController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(mainApp);
            controller.ini();

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showAddStaffDialog(){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/AddStaff.fxml"));
            AnchorPane AddStaff = loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Staff");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(MainApp.getPrimaryStage());
            dialogStage.setScene(new Scene((AddStaff)));
            dialogStage.getScene().getStylesheets().addAll(MainApp.getPrimaryStage().getScene().getStylesheets());

            AddStaffController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public boolean showSetEmpAvailabilityDialog(){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/SetEmpAvailability.fxml"));
            AnchorPane empAvailabilityDialog = loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Set Employee Availability");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(MainApp.getPrimaryStage());
            dialogStage.setScene(new Scene(empAvailabilityDialog));
            dialogStage.getScene().getStylesheets().addAll(MainApp.getPrimaryStage().getScene().getStylesheets());

            SetEmpAvailabilityController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isAddClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    public void showUpdateStaffDialog(){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/UpdateStaffDetails.fxml"));
            AnchorPane UpdateStaff = (AnchorPane) loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Update Staff Details");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(MainApp.getPrimaryStage());
            dialogStage.setScene(new Scene(UpdateStaff));
            dialogStage.getScene().getStylesheets().addAll(MainApp.getPrimaryStage().getScene().getStylesheets());

            UpdateStaffDetailsController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showCreateAppointmentDialog(){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CreateAppOwner.fxml"));
            AnchorPane CreateAppointment = loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Create Appointments");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(MainApp.getPrimaryStage());
            dialogStage.setScene(new Scene(CreateAppointment));
            dialogStage.getScene().getStylesheets().addAll(MainApp.getPrimaryStage().getScene().getStylesheets());

            CreateAppOwnerController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(mainApp);
            controller.initialize();

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
            setTheme(theme);
            System.out.println("New Style: "+ MainApp.getPrimaryStage().getScene().getStylesheets()+"\n");
        }));
    }

    public void setTheme(String theme){
        String css = "appointmentBookingApp/css/"+theme+".css";
        String resource = this.getClass().getClassLoader().getResource(css).toExternalForm();
        String sql;
        PreparedStatement pstmt;
        try {
            sql = "SELECT * FROM customization WHERE businessName = ?";
            pstmt = DbUtil.getConnection().prepareStatement(sql);
            pstmt.setObject(1,mainApp.business);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                sql = "UPDATE customization SET theme = ? WHERE businessName = ?";
                pstmt = DbUtil.getConnection().prepareStatement(sql);
                pstmt.setObject(1,theme);
                pstmt.setObject(2,mainApp.business);
            }else{
                sql = "INSERT INTO customization(businessName, theme) VALUES (?,?)";
                pstmt = DbUtil.getConnection().prepareStatement(sql);
                pstmt.setObject(1,mainApp.business);
                pstmt.setObject(2,theme);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(!theme.isEmpty())
            MainApp.getPrimaryStage().getScene().getStylesheets().add(resource);
    }
}
