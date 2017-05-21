package appointmentBookingApp.view;

import appointmentBookingApp.MainApp;
import appointmentBookingApp.model.Bookings;
import appointmentBookingApp.model.Day;
import appointmentBookingApp.util.CreateStage;
import appointmentBookingApp.util.DbUtil;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import java.io.IOException;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Aydan on 8/04/2017.
 */
public class BookingHistoryController {
    @FXML
    private Button lastButton;

    @FXML
    private Button nextButton;

    @FXML
    private Label dateRangeLabel;

    @FXML
    private GridPane dayPane;

    @FXML
    private GridPane timePane;

    @FXML
    private GridPane bookingsPane;

    private Stage dialogStage;
    private MainApp mainApp;
    private LocalDate minDate, maxDate, startDate, endDate;
    private ArrayList<Bookings> bookings;
    private Button[][] buttons;
    private int[][] numBookings;
    private ArrayList<String>[][] bookedStaff;

    //Allow for the control of the main app.
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Sets the stage of this dialog.
     */
    public void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }

    /**
     *Initialize on controller load.
     */
    public void initialize() {
        maxDate = LocalDate.now();
        minDate = maxDate.minusDays(27);
        endDate = maxDate;
        startDate = maxDate.minusDays(6);

        dateRangeLabel.setText(startDate.getDayOfMonth() + "/" + startDate.getMonthValue() + " - "
                + endDate.getDayOfMonth() + "/" + endDate.getMonthValue());

        setDayLabels();
        getBookings();
        populateGrid();
        displayBookings();
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    @FXML
    private void handleLastWeek() {
        startDate = startDate.minusDays(7);
        endDate = endDate.minusDays(7);

        dateRangeLabel.setText(startDate.getDayOfMonth() + "/" + startDate.getMonthValue() + " - "
                + endDate.getDayOfMonth() + "/" + endDate.getMonthValue());

        lastButton.setDisable(startDate.equals(minDate));
        nextButton.setDisable(endDate.equals(maxDate));

        displayBookings();
    }

    @FXML
    private void handleNextWeek() {
        startDate = startDate.plusDays(7);
        endDate = endDate.plusDays(7);

        dateRangeLabel.setText(startDate.getDayOfMonth() + "/" + startDate.getMonthValue() + " - "
                + endDate.getDayOfMonth() + "/" + endDate.getMonthValue());

        lastButton.setDisable(startDate.equals(minDate));
        nextButton.setDisable(endDate.equals(maxDate));

        displayBookings();
    }

    private void setDayLabels() {
        LocalDate d = startDate;
        for(int i = 0; i < 7; i++) {
            ((Label)dayPane.getChildren().get(i)).setText(d.getDayOfWeek().toString());
            d = d.plusDays(1);
        }
    }

    private void getBookings() {
        bookings = new ArrayList<>();

        try {
            String sql = "SELECT * FROM bookings NATURAL JOIN staff WHERE date >= ? AND date <= ? AND businessName = ?";
            PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(sql);

            pstmt.setString(1, minDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
            pstmt.setString(2, maxDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
            pstmt.setString(3, MainApp.getBusiness());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bookings.add(new Bookings(rs.getString("sTime"),
                        rs.getString("eTime"),
                        rs.getString("service"),
                        rs.getString("staff.firstName"),
                        rs.getString("date"),
                        Day.values()[rs.getInt("dayofWeek")]));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateGrid() {
        buttons = new Button[7][20];

        for(int y = 0; y < 20; y++) {
            for(int x = 0; x < 7; x++) {
                Button temp = new Button("Bookings: 0\nBooked Staff: 0");
                temp.setTextAlignment(TextAlignment.CENTER);
                temp.setFont(new Font(10));
                temp.setPadding(Insets.EMPTY);
                HBox.setHgrow(temp, Priority.ALWAYS);
                VBox.setVgrow(temp, Priority.ALWAYS);
                temp.setMaxWidth(Double.MAX_VALUE);
                temp.setMaxHeight(Double.MAX_VALUE);
                temp.setUserData(new String[] {
                    ((Label)dayPane.getChildren().get(x)).getText(),
                    ((Label)timePane.getChildren().get(y)).getText(),
                    x + ""
                });

                temp.setOnAction(e -> {
                    String[] data = (String[])temp.getUserData();
                    LocalDate newDate = startDate.plusDays(Integer.parseInt(data[2]));
                    showBookingHistoryList(data[0], data[1], newDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
                });

                buttons[x][y] = temp;
                bookingsPane.add(temp, x, y);
            }
        }
    }

    private void displayBookings() {
        numBookings = new int[7][20];
        bookedStaff = new ArrayList[7][20];

        for(int i = 0; i < bookedStaff.length; i++)
            for(int j = 0; j < bookedStaff[i].length; j++)
                bookedStaff[i][j] = new ArrayList<String>();

        for(Bookings b : bookings) {
            LocalDate date = LocalDate.parse(b.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);
            if(date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0) {
                int xa = b.getDayOfWeek().ordinal();
                int xb = startDate.getDayOfWeek().ordinal();
                int x = xa - xb;
                if(x < 0) x += 7;
                int y = (Integer.parseInt(b.getsTime().substring(0, 2)) - 8) * 2;
                if(Integer.parseInt(b.getsTime().substring(3, 5)) >= 30)
                    y++;

                numBookings[x][y]++;
                if(!bookedStaff[x][y].contains(b.getEmpName()))
                    bookedStaff[x][y].add(b.getEmpName());
            }
        }

        for(int y = 0; y < 20; y++)
            for(int x = 0; x < 7; x++)
                buttons[x][y].setText("Bookings: " + numBookings[x][y] + "\nBooked Staff: " + bookedStaff[x][y].size());
    }

    private void showBookingHistoryList(String day, String sTime, String date) {
        try {
            String fxml = "view/BookingHistoryList.fxml";
            String title = "Booking History List";
            BookingHistoryListController controller = CreateStage.newDialogStage(fxml,title).getController();
            controller.setDialogStage(CreateStage.getDialogStage());
            controller.setDayTime(day,sTime,date);
            controller.setMainApp(mainApp);
            controller.ini();

            // Show the dialog and wait until the user closes it
            CreateStage.getDialogStage().showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
