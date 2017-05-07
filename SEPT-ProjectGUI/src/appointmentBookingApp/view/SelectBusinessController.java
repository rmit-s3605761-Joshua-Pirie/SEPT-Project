package appointmentBookingApp.view;

import appointmentBookingApp.MainApp;
import appointmentBookingApp.util.Alerts;
import appointmentBookingApp.util.DbUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectBusinessController {
    @FXML
    private ComboBox SB_CBox;
    private MainApp mainApp;
    private String business;
    private ObservableList<String> businesses = FXCollections.observableArrayList();

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void initialize(){
        String sql = "SELECT businessName FROM businessowner";
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
    }

    @FXML
    public void handleOK(){
        if(!SB_CBox.getSelectionModel().isEmpty()){
            mainApp.business = SB_CBox.getValue().toString();
            System.out.println(SB_CBox.getValue().toString());
            mainApp.showLogin();
        } else {
            Alerts.error("Error","Business not selected","Please select a business from the dropdown menu.");
        }
    }
}
