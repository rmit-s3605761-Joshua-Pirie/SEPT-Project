package appointmentBookingApp.util;

import javafx.scene.control.Alert;

/**
 * Created by David on 11/04/2017.
 */
public class Alerts {

    public static void error(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public static void confirm(String title, String header, String content){
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle(title);
        confirm.setHeaderText(header);
        confirm.setContentText(content);

        confirm.showAndWait();
    }
}
