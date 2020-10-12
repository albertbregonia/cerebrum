package kanin.cerebrum.utility;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kanin.cerebrum.Main;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class InputDialog implements Initializable {

    private static final int[] dimensions = new int[2];
    private static boolean valid = false;
    private static Stage window;
    
    public static void display(){
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UTILITY);
        Scene main = new Scene(new Pane());
        try {main = new Scene(FXMLLoader.load(Objects.requireNonNull(InputDialog.class.getClassLoader().getResource("input.fxml"))));}catch(IOException err){err.printStackTrace();}
        window.setScene(main);
        window.isAlwaysOnTop();
        window.showAndWait();
    }
    
    @FXML private TextField input;
    
    @FXML
    public void accept(){
        try{
            String custom = input.getText();
            String[] dimension = custom.split("x");
            dimension[0]=dimension[0].trim();
            dimension[1]=dimension[1].trim();
            dimensions[0]=Integer.parseInt(dimension[0]);
            dimensions[1]=Integer.parseInt(dimension[1]);
            window.close();
            valid = true;
        }
        catch(Exception e){
            Platform.runLater(()->Main.alertMsg("Invalid Input!","Please follow the correct format. Example: 1920x1080", Alert.AlertType.ERROR));
        }
    }

    @FXML
    public void cancel(){window.close();}
    
    public static boolean isValid(){return valid;}
    
    public static int[] getDimensions(){return dimensions;}

    @FXML
    private Button okBtn;
    @FXML
    private Button cancelBtn;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        input.setOnKeyPressed(e->{ //Enter button works as OK button
            if(e.getCode().equals(KeyCode.ENTER))
                accept();
        });
        okBtn.setOnKeyPressed(e->{ //Enter button works as OK button
            if(e.getCode().equals(KeyCode.ENTER))
                accept();
        });
        cancelBtn.setOnKeyPressed(e->{ //Escape button works as Cancel button
            if(e.getCode().equals(KeyCode.ESCAPE))
                cancel();
        });
    }
}
