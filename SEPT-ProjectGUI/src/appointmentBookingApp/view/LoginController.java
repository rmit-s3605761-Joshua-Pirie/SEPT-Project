package appointmentBookingApp.view;

import appointmentBookingApp.MainApp;
import appointmentBookingApp.model.TestData;
import appointmentBookingApp.util.DbUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Class is used to control user login and determine which view is to be shown
 * upon successful login.
 */
public class LoginController {
	@FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label errorMessage;
    @FXML
    private Button button;

    private MainApp mainApp;
    private boolean isSuperUser = false;
    private Stage dialogStage;
    private String business;

    //Allow for the control of the main app.
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    /**
     *Initialize on controller load.
     */
	public void initialize() {
        errorMessage.setText("");
        username.setPromptText("username");
        password.setPromptText("password");
        username.setText("b");
        password.setText("Qwerty1!");
        System.out.println("Current Style: "+ MainApp.getPrimaryStage().getScene().getStylesheets()+"\n");
    }

    public void ini(String business){
	    this.business = business;
        System.out.println("BN from Login: "+mainApp.business);
    }

	/**
	 * Called to verify login and determine which user view is to be shown.
	 * @throws SQLException produces stack trace.
	 */
	@FXML
	private void handleView() throws SQLException{
        String querySQL;
        String sqlUsername = username.getText();
		String sqlPassword = password.getText();
		System.out.println("SQLUsername1: " + sqlUsername);
		System.out.println("SQLPassword1: " + sqlPassword);

		if(username.getText().equals("~reset")){
            System.out.println("Resting database");
            TestData.clearAllTables();
            TestData.populateAllTables();
            System.out.println("Database reset to default values");
            username.clear();
            password.clear();
            username.requestFocus();
        }else if(isSuperUser){
		    querySQL = "SELECT*FROM suser WHERE username=? AND password=?";
		    if(!dbAccountSearch(querySQL,sqlUsername,sqlPassword).next())
                errorMessage.setText("Invalid Admin Account");
		    else{
                dialogStage.close();
                showRegisterBusiness();
            }
        }else{
            querySQL = "SELECT*FROM customer WHERE username=? AND password=? AND businessName=?";
            if(!dbAccountSearch(querySQL,sqlUsername,sqlPassword).next()){
                System.out.println("Not customer login");
                querySQL = "SELECT*FROM businessowner WHERE username=? AND password=? AND businessName=?";
                if(!dbAccountSearch(querySQL,sqlUsername,sqlPassword).next()){
                    System.out.println("Not business owner login");
                    querySQL = "SELECT*FROM staff WHERE staffID=? AND password=? AND businessName=?";
                    if(!dbAccountSearch(querySQL,sqlUsername,sqlPassword).next()){
                        System.out.println("Not staff login");
                        errorMessage.setText("Username/Password is incorrect");
                    }
                    else{
                        System.out.println("Staff Login success");
                    }
                }
                else{
                    System.out.println("Business Owner Login success");
                    showBusinessHomepage();
                }
            }
            else{
                System.out.println("Customer Login success");
                showCustomerHomepage(sqlUsername);
            }
        }
	}

//	Search database for account
	private ResultSet dbAccountSearch(String sql, String sqlUsername, String sqlPassword) throws SQLException {
		PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(sql);
		pstmt.setObject(1,sqlUsername);
		pstmt.setObject(2,sqlPassword);
		if(!isSuperUser)
		    pstmt.setObject(3,business);
		return pstmt.executeQuery();
	}

    private void showBusinessHomepage() {
        mainApp.showBusinessHomepage();
    }

    private void showCustomerHomepage(String sqlUsername){mainApp.showCustomerHomepage(sqlUsername);}

//    Opens pages to add a new customer to the database.
    public void showRegisterCustomer(){
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RegisterCustomer.fxml"));
			AnchorPane RegisterCustomer = (AnchorPane) loader.load();
			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Register Customer");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(MainApp.getPrimaryStage());
			dialogStage.setScene(new Scene(RegisterCustomer));
            dialogStage.getScene().getStylesheets().addAll(MainApp.getPrimaryStage().getScene().getStylesheets());

			RegisterCustomerController controller = loader.getController();
			controller.setDialogStage(dialogStage);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    // Opens registration page to add a new business
    void showRegisterBusiness(){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RegisterBusiness.fxml"));
            AnchorPane RegisterBusiness = loader.load();
            dialogStage.setTitle("Register Business");
            dialogStage.setScene(new Scene(RegisterBusiness));

            RegisterBusinessController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

//   Used to check if a superUser login is required, also modifies
//
    void setIsSuperUser() {
        isSuperUser = true;
        button.setText("Cancel");
        button.setOnAction((event)->{
            dialogStage.close();
        });
    }
}
