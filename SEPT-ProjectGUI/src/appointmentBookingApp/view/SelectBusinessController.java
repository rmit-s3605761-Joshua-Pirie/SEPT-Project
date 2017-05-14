package appointmentBookingApp.view;

import appointmentBookingApp.MainApp;
import appointmentBookingApp.util.Alerts;
import appointmentBookingApp.util.DbUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectBusinessController {
    @FXML
    private ComboBox<String> SB_CBox;
    private MainApp mainApp;
    private String business;
    private ObservableList<String> businesses = FXCollections.observableArrayList();
    private FilteredList<String> filteredData = new FilteredList<>(businesses, p -> true);

    //    Allow for the control of the main app.
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

//  Setup the FXML page on load
    public void initialize(){
        SB_CBox.focusedProperty().addListener((obs, wasFocused, isFocused)->{
            if(isFocused){
                String sql = "SELECT businessName FROM businessowner";
                SB_CBox.getItems().clear();
                try {
                    PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(sql);
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()){
                        businesses.add(rs.getString("businessName"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                SB_CBox.setItems(businesses);
                SB_CBox.setVisibleRowCount(businesses.size());
            }
        });

//        filters();
    }

// Allows filtering of business to select from the combobox.
// Current disabled due to bug, indexoutofbounds, Unable to determine reasoning as of yet.
    public void filters() {

        filteredData.predicateProperty().bind(Bindings.createObjectBinding(() ->
                        businesses -> businesses.contains(SB_CBox.getEditor().getText()),

                SB_CBox.getEditor().textProperty()
        ));
        SB_CBox.setItems(filteredData);
//        int defaultRowCount = 5;
//        SB_CBox.getEditor().focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean wasFocused, Boolean isFocused) -> {
//            if(isFocused){
//                SB_CBox.getEditor().textProperty().addListener((ObservableValue<? extends String> obs, String oldText, String newText) -> {
//                    filteredData.predicateProperty().bind(Bindings.createObjectBinding(() ->
//                                    businesses -> businesses.contains(SB_CBox.getEditor().getText()),
//
//                            SB_CBox.getEditor().textProperty()
//                    ));
//                    SB_CBox.setItems(filteredData);
//                    if(filteredData.size() <= defaultRowCount){
//                        SB_CBox.setVisibleRowCount(filteredData.size());
////                        System.out.println(filteredData.size());
//                        SB_CBox.hide();
//                        SB_CBox.show();
//                    }else{
//                        SB_CBox.setVisibleRowCount(defaultRowCount);
//                    }
//                });
//            } else
//                SB_CBox.hide();
//        });
    }

//    When "OK" button pushed, shows login screen and sets the business data to be used.
    @FXML
    public void handleOK(){
        if(!SB_CBox.getSelectionModel().isEmpty()){
            mainApp.business = SB_CBox.getValue();
            System.out.println(SB_CBox.getValue());
            mainApp.showLogin();
        } else {
            Alerts.error("Error","Business not selected","Please select a business from the dropdown menu.");
        }
    }

    @FXML
    public void showLoginDialog(){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Login.fxml"));
            AnchorPane Login = loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Login");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(Login);
            dialogStage.setScene(scene);

            LoginController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(mainApp);
            controller.setIsSuperUser();

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
