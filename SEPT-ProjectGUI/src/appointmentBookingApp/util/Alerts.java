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

//    Generate error message to be display either using the alert box or printing it to console.
    public static boolean genErrorMessage(String errorMessage, boolean useAlertBox ){
        if (errorMessage.length() == 0) {
            return true;
        } else {
            if(useAlertBox){
                Alerts.error("Invalid Fields","Please correct invalid fields",errorMessage);
            }else
                System.out.println("Invalid Fields:\n"+errorMessage);
            return false;
        }
    }
}
