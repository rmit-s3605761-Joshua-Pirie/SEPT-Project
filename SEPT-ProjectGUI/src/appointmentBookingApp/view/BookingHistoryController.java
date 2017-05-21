package appointmentBookingApp.view;

import appointmentBookingApp.MainApp;
import appointmentBookingApp.model.Bookings;
import appointmentBookingApp.model.Day;
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
    private Date minDate, maxDate, startDate, endDate;
    private SimpleDateFormat dateFormat;
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
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.SECOND, -1);
        maxDate = c.getTime();
        endDate = c.getTime();
        c.add(Calendar.DATE, -7);
        c.add(Calendar.SECOND, 1);
        startDate = c.getTime();
        c.add(Calendar.DATE, -21);
        minDate = c.getTime();

        dateFormat = new SimpleDateFormat("dd/MM");
        dateRangeLabel.setText(dateFormat.format(startDate) + " - " + dateFormat.format(endDate));

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
        Calendar c = Calendar.getInstance();

        c.setTime(startDate);
        c.add(Calendar.DATE, -7);
        startDate = c.getTime();

        c.setTime(endDate);
        c.add(Calendar.DATE, -7);
        endDate = c.getTime();

        dateRangeLabel.setText(dateFormat.format(startDate) + " - " + dateFormat.format(endDate));

        lastButton.setDisable(startDate.equals(minDate));
        nextButton.setDisable(endDate.equals(maxDate));

        displayBookings();
    }

    @FXML
    private void handleNextWeek() {
        Calendar c = Calendar.getInstance();

        c.setTime(startDate);
        c.add(Calendar.DATE, 7);
        startDate = c.getTime();

        c.setTime(endDate);
        c.add(Calendar.DATE, 7);
        endDate = c.getTime();

        dateRangeLabel.setText(dateFormat.format(startDate) + " - " + dateFormat.format(endDate));

        lastButton.setDisable(startDate.equals(minDate));
        nextButton.setDisable(endDate.equals(maxDate));

        displayBookings();
    }

    private void setDayLabels() {
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        SimpleDateFormat df = new SimpleDateFormat("EEEE");
        for(int i = 0; i < 7; i++) {
            ((Label)dayPane.getChildren().get(i)).setText(df.format(c.getTime()));
            c.add(Calendar.DATE, 1);
        }
    }

    private void getBookings() {
        bookings = new ArrayList<>();

        try {
            String sql = "SELECT * FROM bookings NATURAL JOIN staff WHERE date >= ? AND date <= ?";
            PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(sql);

            pstmt.setDate(1, new java.sql.Date(minDate.getTime()));
            pstmt.setDate(2, new java.sql.Date(maxDate.getTime()));

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
                    Calendar c = Calendar.getInstance();
                    c.setTime(startDate);
                    c.add(Calendar.DATE, Integer.parseInt(data[2]));
                    showBookingHistoryList(data[0], data[1], new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()));
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
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(b.getDate());
                if(date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0) {
                    int x = b.getDayOfWeek().ordinal();
                    int y = (Integer.parseInt(b.getsTime().substring(0, 2)) - 8) * 2;
                    if(Integer.parseInt(b.getsTime().substring(3, 5)) >= 30)
                        y++;

                    numBookings[x][y]++;
                    if(!bookedStaff[x][y].contains(b.getEmpName()))
                        bookedStaff[x][y].add(b.getEmpName());
                }
            }
            catch(ParseException e) {
                e.printStackTrace();
            }
        }

        for(int y = 0; y < 20; y++)
            for(int x = 0; x < 7; x++)
                buttons[x][y].setText("Bookings: " + numBookings[x][y] + "\nBooked Staff: " + bookedStaff[x][y].size());
    }

    private void showBookingHistoryList(String day, String sTime, String date) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/BookingHistoryList.fxml"));
            AnchorPane BookingHistoryList = (AnchorPane) loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Booking History");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.dialogStage);
            dialogStage.setScene(new Scene(BookingHistoryList));
            dialogStage.getScene().getStylesheets().addAll(MainApp.getPrimaryStage().getScene().getStylesheets());

            BookingHistoryListController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setDayTime(day,sTime,date);
            controller.setMainApp(mainApp);
            controller.ini();

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
