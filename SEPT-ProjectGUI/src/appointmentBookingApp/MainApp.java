package appointmentBookingApp;

import appointmentBookingApp.util.CreateStage;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public final class MainApp extends Application {
    private static Stage primaryStage;
    private   static String business;

    @Override
    public void start(Stage primaryStage) throws Exception{
        setPrimaryStage(primaryStage);
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
            AnchorPane business = loader.load();
            getPrimaryStage().setTitle("Select Business");
            getPrimaryStage().setScene(new Scene(business));

            getPrimaryStage().show();
            SelectBusinessController controller = loader.getController();
            controller.setMainApp(this);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void showLogin(){
        try{
            String fxml = "view/Login.fxml";
            String title = getBusiness();
            LoginController controller = CreateStage.newStage(fxml,title).getController();
            controller.setMainApp(this);
            controller.ini(business);
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public void showBusinessHomepage() {
        try {
            String fxml = "view/BusinessHomepage.fxml";
            String title = getBusiness();
            BusinessHomepageController controller = CreateStage.newStage(fxml,title).getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCustomerHomepage(String sqlUsername) {
        try {
            String fxml = "view/CustomerHomepage.fxml";
            String title = getBusiness();
            CustomerHomepageController controller = CreateStage.newStage(fxml,title).getController();
            controller.setMainApp(this);
            controller.ini(sqlUsername);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Stage getPrimaryStage(){return primaryStage;}

    public static void setPrimaryStage(Stage primaryStage){
        MainApp.primaryStage = primaryStage;
    }

    public static String getBusiness() {
        return business;
    }

    public static void setBusiness(String business) {
        MainApp.business = business;
    }
}

