package kanin.cerebrum.utility;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kanin.cerebrum.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class InputDialog implements Initializable {

    //values are static as only one InputDialog should ever exist
    private static final int[] dimensions = new int[2];
    private static boolean valid = false;
    private static Stage window;
    
    public static void display() {
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UTILITY);
        try {
            window.setScene(new Scene(FXMLLoader.load(
                InputDialog.class.getClassLoader().getResource("input.fxml"))));
        } catch(Exception ignored) {} //ignored bc this file will always be compiled with the jar
        window.isAlwaysOnTop();
        window.showAndWait();
    }
    
    @FXML private TextField input;
    
    @FXML
    public void apply() {
        try {
            String[] dimension = input.getText().split("x");
            dimensions[0] = Integer.parseInt(dimension[0].trim());
            dimensions[1] = Integer.parseInt(dimension[1].trim());
            window.close();
            valid = true;
        } catch(NumberFormatException e) {
            Platform.runLater(() -> Main.alertMsg(
                "Invalid Input! Error:" + e.getMessage(), 
                "Please follow the correct format. Example: 1920x1080", 
                Alert.AlertType.ERROR
            ));
        }
    }

    @FXML
    public void cancel() { window.close(); }
    
    public static boolean isValid() { return valid; }
    
    public static int[] getDimensions() { return dimensions; }

    @FXML
    private Button ok, cancel;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventHandler<KeyEvent> applyWithEnter = e -> { //Enter button works as OK button
            if(e.getCode().equals(KeyCode.ENTER))
                apply();
        };
        input.setOnKeyPressed(applyWithEnter);
        ok.setOnKeyPressed(applyWithEnter);
        cancel.setOnKeyPressed(e -> { //Escape button works as Cancel button
            if(e.getCode().equals(KeyCode.ESCAPE))
                cancel();
        });
    }
}
