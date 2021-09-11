package kanin.cerebrum;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import kanin.cerebrum.server.ServerHandler;
import kanin.cerebrum.server.Connection;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import io.netty.channel.ChannelHandlerContext;

import static kanin.cerebrum.server.ServerHandler.clients;

public class Main extends Application implements Initializable {

    @FXML private ListView<String> liveConnections;
    public static ListView<String> liveConnections_;
    
    @FXML private ProgressIndicator status;
    public static ProgressIndicator status_;

    public static void main(String[] args) { launch(args); }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Pass by references to use value in separate threads
        status_ = status;
        liveConnections_ = liveConnections;
        liveConnections_.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        liveConnections_.setOnMouseClicked(e -> { //Disconnect on click
            if(liveConnections_.getSelectionModel().getSelectedItem() != null && 
               !liveConnections_.getSelectionModel().getSelectedItem().isEmpty()) {
                    int i = 0;
                    for(ChannelHandlerContext client : clients.keySet())
                        if(i == liveConnections_.getSelectionModel().getSelectedIndex())
                            ServerHandler.disconnect(client);
                        else 
                            i++;
                }
        });
    }

    @Override
    public void start(Stage stage) throws Exception { //Load main UI
        stage.setScene(new Scene(FXMLLoader.load(
            Main.class.getClassLoader().getResource("main.fxml"))));
        stage.setTitle("Cerebrum by Albert Bregonia");
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> disconnectAll());
        stage.show();
    }

    //Ask for settings on first time use, otherwise simply connect
    @FXML 
    public void connect() throws IOException {
        if(!SettingsDialog.isConfigured())
            SettingsDialog.startup();
        if(SettingsDialog.isConfigured()) //Check for a successful configuration after initial setup
            if(SettingsDialog.hasValidAddress()) {
                if(connection == null || !connection.isAlive()) {
                    if(SettingsDialog.isHost())
                        connection = new Connection(SettingsDialog.getPort());
                    else
                        connection = new Connection(SettingsDialog.getPort(), SettingsDialog.getIP());
                    connection.start();
                    status.setProgress(-1);
                }
            } else
                alertMsg("Invalid settings!", "Please check your settings before your proceed.", Alert.AlertType.ERROR);
    }
    
    //Disconnect Button - Stops the server if this device is the host or disconnects from host if this device is a client
    @FXML 
    public void disconnect() { disconnectAll(); }
    
    public static void disconnectAll() {
        Platform.runLater(() -> { //Close all open Control Panels and set the service status to disconnected
            for(ChannelHandlerContext client : clients.keySet())
                ServerHandler.disconnect(client);
            status_.setProgress(0);
        });
        if(connection != null && connection.isAlive())
            connection.getConnection().channel().close();
        connection = null;
    }

    //Settings Button
    @FXML public void openSettings() { SettingsDialog.startup(); }

    public static Connection connection; //there should only be one connection thread

    //Dialog box - in some instances its used for error messaging, otherwise for confirmation
    public static boolean alertMsg(String content, String subtext, Alert.AlertType type) {
        Alert alert = new Alert(type,subtext,ButtonType.OK);
        alert.setTitle("Cerebrum by Albert Bregonia");
        alert.setHeaderText(content);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK; //If the OK button is pressed return true
    }
}
