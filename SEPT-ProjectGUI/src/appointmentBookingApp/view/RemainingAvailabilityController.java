package appointmentBookingApp.view;

import appointmentBookingApp.MainApp;
import javafx.stage.Stage;

public class RemainingAvailabilityController {
    private Stage dialogStage;
    private MainApp mainApp;

    //Allow for the control of the main app.
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Sets the stage of this dialog.
     */
    void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }
}
