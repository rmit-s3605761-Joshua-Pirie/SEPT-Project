package appointmentBookingApp.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Created by Aydan on 8/04/2017.
 */
public class UpcomingBookingsListController {
    private Stage dialogStage;
    private String day, sTime;

    @FXML
    private Label title;

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

    public void setDayTime(String day, String sTime) {
        this.day = day;
        this.sTime = sTime;
        System.out.println(this.day);
        System.out.println(this.sTime);
        title.setText(this.day+" "+this.sTime);
    }
}
