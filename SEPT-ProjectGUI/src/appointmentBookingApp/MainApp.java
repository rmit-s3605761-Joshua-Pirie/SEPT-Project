package appointmentBookingApp;

import appointmentBookingApp.util.DbUtil;
import appointmentBookingApp.view.BusinessHomepageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


public class MainApp extends Application {
    public Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("view/Login.fxml"));
        this.primaryStage.setTitle("Hello World");
        this.primaryStage.setScene(new Scene(root));
//        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
//        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
//        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);

        this.primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
    public MainApp(){DbUtil.databaseConnect();}

    public void showBusinessHomepage() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("view/BusinessHomepage.fxml"));
            primaryStage.getScene().setRoot(root);
//            // Load the fxml file and create a new stage for the popup dialog.
//            System.out.println("Test line 1");
//            FXMLLoader loader = new FXMLLoader();
//            loader.load(MainApp.class.getResource("view/BusinessHomepage.fxml"));
//            AnchorPane BusinessHomepage = (AnchorPane)loader.load();
//            System.out.println("Test line 2");
//            // anchor Customer Login
////            primaryStage.setWidth(BusinessHomepage.getMinWidth() + widthPadding);
////            primaryStage.setHeight(BusinessHomepage.getMinHeight() + heightPadding);
//            primaryStage.setScene(new Scene(BusinessHomepage));
//            System.out.println("Test line 3");
//            primaryStage.show();

//            // Give the controller access to the main app.
//            BusinessHomepageController controller = loader.getController();
//            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();

        }

    }
}

