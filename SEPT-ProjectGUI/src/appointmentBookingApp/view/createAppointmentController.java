package appointmentBookingApp.view;

import appointmentBookingApp.MainApp;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;


public class createAppointmentController extends Application {
    @FXML
    private DatePicker datePicker;
    private Stage dialogStage;

    /**
     * Sets the stage of this dialog.
     */
    void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }

    void handlePicker(){
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        dialogStage = primaryStage;
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isBefore(LocalDate.now().plusDays(1))) {
                            setDisable(true);
                            setStyle("-fx-background-color: #EEEEEE;");
                        }
                    }
                };
            }
        };
        datePicker.setDayCellFactory(dayCellFactory);
        datePicker.setValue(LocalDate.now().plusDays(1));
        show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void show(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/createAppointment.fxml"));
            AnchorPane login = (AnchorPane) loader.load();
            dialogStage.setTitle("Hello World");
            dialogStage.setScene(new Scene(login));

            dialogStage.show();
            createAppointmentController controller = loader.getController();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    @FXML
    private void close() {
        dialogStage.close();
    }
}
