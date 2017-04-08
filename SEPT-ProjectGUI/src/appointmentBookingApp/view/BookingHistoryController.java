package appointmentBookingApp.view;

import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * Created by Aydan on 8/04/2017.
 */
public class BookingHistoryController {

    private Stage dialogStage;

    /**
     * Sets the stage of this dialog.
     */
    void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
