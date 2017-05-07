package appointmentBookingApp;

import appointmentBookingApp.util.DbUtil;
import appointmentBookingApp.view.BusinessHomepageController;
import appointmentBookingApp.view.CustomerHomepageController;
import appointmentBookingApp.view.LoginController;
import appointmentBookingApp.view.SelectBusinessController;
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
    public  String business;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
//        showLogin();
        showSelectBusiness();
    }


    public static void main(String[] args) {
        launch(args);
    }
    public MainApp(){DbUtil.databaseConnect();}

    public void showSelectBusiness(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/SelectBusiness.fxml"));
            AnchorPane login = (AnchorPane) loader.load();
            this.primaryStage.setTitle("Select Business");
            this.primaryStage.setScene(new Scene(login));

            this.primaryStage.show();
            SelectBusinessController controller = loader.getController();
            controller.setMainApp(this);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void showLogin(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Login.fxml"));
            AnchorPane login = (AnchorPane) loader.load();
            this.primaryStage.setTitle(business);
            this.primaryStage.setScene(new Scene(login));

            this.primaryStage.show();
            LoginController controller = loader.getController();
            controller.setMainApp(this);
            controller.ini();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public void showBusinessHomepage() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/BusinessHomepage.fxml"));
            AnchorPane businessHomepage = loader.load();
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

    public void showCustomerHomepage(String sqlUsername) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CustomerHomepage.fxml"));
            AnchorPane customerHomepage = loader.load();
            primaryStage.close();
            primaryStage.getScene().setRoot(customerHomepage);
            primaryStage.show();
            // Give the controller access to the main app.
            CustomerHomepageController controller = loader.getController();
            controller.setMainApp(this);
            controller.ini(sqlUsername);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage(){return primaryStage;}
}

