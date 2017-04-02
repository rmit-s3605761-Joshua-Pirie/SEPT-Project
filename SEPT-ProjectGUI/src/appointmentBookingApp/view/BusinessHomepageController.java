package appointmentBookingApp.view;

import appointmentBookingApp.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Aydan on 30/03/2017.
 */
public class BusinessHomepageController {
    private MainApp mainApp;
    //Allow for the control of the main app.
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public boolean showAddServiceDialog(){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/AddServiceDialog.fxml"));
            AnchorPane serviceDialog = (AnchorPane) loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Service");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(serviceDialog);
            dialogStage.setScene(scene);

            AddServiceDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isAddClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void logout(){
        mainApp.showLogin();
    }

}