package appointmentBookingApp;

import appointmentBookingApp.util.DbUtil;
import appointmentBookingApp.view.BusinessHomepageController;
import appointmentBookingApp.view.LoginController;
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
    public  Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        showLogin();
    }


    public static void main(String[] args) {
        launch(args);
    }
    public MainApp(){DbUtil.databaseConnect();}

    public void showLogin(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Login.fxml"));
            AnchorPane login = (AnchorPane) loader.load();
            this.primaryStage.setTitle("Hello World");
            this.primaryStage.setScene(new Scene(login));

            this.primaryStage.show();
            LoginController controller = loader.getController();
            controller.setMainApp(this);
        }catch(IOException e){
            e.printStackTrace();
        }

    }
    public void showBusinessHomepage() {
        System.out.println("Test line 1");
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/BusinessHomepage.fxml"));
            AnchorPane businessHomepage = (AnchorPane) loader.load();
            primaryStage.close();
            primaryStage.getScene().setRoot(businessHomepage);
            primaryStage.show();
            // Give the controller access to the main app.
            BusinessHomepageController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage(){return primaryStage;}
}

