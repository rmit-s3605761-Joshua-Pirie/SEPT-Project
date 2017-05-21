package appointmentBookingApp.util;

import appointmentBookingApp.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public final class CreateStage {
    private static Stage dialogStage;
    public static FXMLLoader newStage(String fxml, String title) throws IOException {
        // Load the fxml file and create a new stage for the primary stage.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource(fxml));
        AnchorPane page = loader.load();
        MainApp.getPrimaryStage().setTitle(title);
        if(MainApp.getPrimaryStage().getScene().getStylesheets().isEmpty()){
            System.out.println("Checking database for theme.");
            CssUtil.getTheme();
        }
        MainApp.getPrimaryStage().getScene().setRoot(page);
        MainApp.getPrimaryStage().sizeToScene();

        MainApp.getPrimaryStage().show();
        return loader;
    }

    public static FXMLLoader newDialogStage(String fxml, String title) throws IOException {
        // Load the fxml file and create a new stage for the popup dialog.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource(fxml));
        AnchorPane page = loader.load();
        setDialogStage(new Stage());
//        Stage dialogStage = new Stage();
        getDialogStage().setTitle(title);
        getDialogStage().initModality(Modality.WINDOW_MODAL);
        getDialogStage().initOwner(MainApp.getPrimaryStage());
        getDialogStage().setScene(new Scene((page)));
        getDialogStage().getScene().getStylesheets().addAll(MainApp.getPrimaryStage().getScene().getStylesheets());
        return loader;
    }

    public static Stage getDialogStage() {
        return dialogStage;
    }

    public static void setDialogStage(Stage dialogStage) {
        CreateStage.dialogStage = dialogStage;
    }
}
