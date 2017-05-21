package appointmentBookingApp.util;

import appointmentBookingApp.MainApp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CssUtil {
//    Gets businesses css theme choice from the database.
    public static void getTheme(){
        String sql = "SELECT * FROM customization WHERE businessName=?";

        try {
            PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(sql);
            pstmt.setObject(1,MainApp.getBusiness());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                if(!rs.getString("theme").isEmpty() || !rs.getString("theme").equals("")){
                    String css = "appointmentBookingApp/css/"+rs.getString("theme")+".css";
                    String resource = MainApp.class.getClassLoader().getResource(css).toExternalForm();
                    MainApp.getPrimaryStage().getScene().getStylesheets().add(resource);
                    System.out.println("New Style: "+ MainApp.getPrimaryStage().getScene().getStylesheets()+"\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    Sets business css them choice in the database.
    public static void setTheme(String theme){
        String css = "appointmentBookingApp/css/"+theme+".css";
        String resource = MainApp.class.getClassLoader().getResource(css).toExternalForm();
        String sql;
        PreparedStatement pstmt;
        try {
            sql = "SELECT * FROM customization WHERE businessName = ?";
            pstmt = DbUtil.getConnection().prepareStatement(sql);
            pstmt.setObject(1,MainApp.getBusiness());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                sql = "UPDATE customization SET theme = ? WHERE businessName = ?";
                pstmt = DbUtil.getConnection().prepareStatement(sql);
                pstmt.setObject(1,theme);
                pstmt.setObject(2,MainApp.getBusiness());
            }else{
                sql = "INSERT INTO customization(businessName, theme) VALUES (?,?)";
                pstmt = DbUtil.getConnection().prepareStatement(sql);
                pstmt.setObject(1,MainApp.getBusiness());
                pstmt.setObject(2,theme);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(!theme.isEmpty())
            MainApp.getPrimaryStage().getScene().getStylesheets().add(resource);
    }
}
