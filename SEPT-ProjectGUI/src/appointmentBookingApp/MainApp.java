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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public final class MainApp extends Application {
    private static Stage primaryStage;
    public  String business;

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
            AnchorPane login = (AnchorPane) loader.load();
            getPrimaryStage().setTitle("Select Business");
            getPrimaryStage().setScene(new Scene(login));

            getPrimaryStage().show();
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
            AnchorPane login = loader.load();
            getPrimaryStage().setTitle(business);
            if(getPrimaryStage().getScene().getStylesheets().isEmpty()){
                System.out.println("Checking database for theme.");
                setStyleDB();
            }
            getPrimaryStage().getScene().setRoot(login);
            getPrimaryStage().sizeToScene();

            getPrimaryStage().show();
            LoginController controller = loader.getController();
            controller.setMainApp(this);
            controller.ini(business);
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
            getPrimaryStage().sizeToScene();
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
            getPrimaryStage().sizeToScene();
            primaryStage.show();
            // Give the controller access to the main app.
            CustomerHomepageController controller = loader.getController();
            controller.setMainApp(this);
            controller.ini(sqlUsername);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStyleDB(){
        String sql = "SELECT * FROM customization WHERE businessName=?";

        try {
            PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(sql);
            pstmt.setObject(1,business);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                if(!rs.getString("theme").isEmpty() || rs.getString("theme").equals("")){
                    String css = "appointmentBookingApp/css/"+rs.getString("theme")+".css";
                    String resource = this.getClass().getClassLoader().getResource(css).toExternalForm();
                    getPrimaryStage().getScene().getStylesheets().add(resource);
                    System.out.println("New Style: "+ getPrimaryStage().getScene().getStylesheets()+"\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Stage getPrimaryStage(){return primaryStage;}

    public static void setPrimaryStage(Stage primaryStage){
        MainApp.primaryStage = primaryStage;
    }
}

