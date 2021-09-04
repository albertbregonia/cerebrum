package kanin.cerebrum.server;

import io.netty.channel.ChannelHandlerContext;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import kanin.cerebrum.utility.InputDialog;

import java.net.InetAddress;

public class ControlPanel {
    
    private final Stage panel = new Stage();
    
    public ControlPanel(ChannelHandlerContext client) {
        //Generate a translucent blue window that takes in data to be sent to the respective client
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        panel.setOpacity(0.2);
        VBox layout = new VBox();
        layout.setStyle("-fx-background-color: #4081b3");
        layout.setAlignment(Pos.TOP_CENTER);

        //Window Size Presets
        Label presets = new Label("Right Click Me for Dimension Presets"); //Label for Dimension Presets
        presets.setStyle("-fx-text-fill: white");
        ContextMenu menu = new ContextMenu();
        menu.getItems().addAll( //Window Dimension Presets
            new MenuItem("3840x2160"), 
            new MenuItem("2560x1440"), 
            new MenuItem("1920x1080"), 
            new MenuItem("1600x900"), 
            new MenuItem("1366x768"), 
            new MenuItem("1280x720"), 
            new MenuItem("1024x768"), 
            new MenuItem("800x600"), 
            new MenuItem("Custom")
        );
        presets.setContextMenu(menu);
        for(int i=0; i<menu.getItems().size(); i++) {
            menu.getItems().get(i).setAccelerator( //Hotkey: Ctrl+1 - Ctrl+9
                new KeyCodeCombination(KeyCode.valueOf("DIGIT"+(i+1)),KeyCombination.CONTROL_DOWN)); 
            if(i != 8) { //Don't include the custom resolution key
                int I = i;
                menu.getItems().get(i).setOnAction(e -> {
                    String[] dimensions = menu.getItems().get(I).getText().split("x");
                    panel.setWidth(Double.parseDouble(dimensions[0]));
                    panel.setHeight(Double.parseDouble(dimensions[1]));
                });
            } else //Custom Resolution Dialog Box
                menu.getItems().get(i).setOnAction(e -> {
                    InputDialog.display();
                    if(InputDialog.isValid()) {
                        panel.setWidth(InputDialog.getDimensions()[0]);
                        panel.setHeight(InputDialog.getDimensions()[1]);
                    }
                });
        }
        layout.getChildren().add(presets);

        //IO Handlers
        panel.addEventHandler(MouseEvent.ANY, new MouseHandler(client));
        panel.addEventHandler(KeyEvent.ANY, new KeyHandler(client));
        panel.addEventHandler(ScrollEvent.ANY, new ScrollHandler(client));
        panel.setScene(new Scene(layout));
        panel.setX(bounds.getMinX());
        panel.setY(bounds.getMinY());
        panel.setWidth(bounds.getWidth());
        panel.setHeight(bounds.getHeight());
        String remote = client.channel().remoteAddress().toString().replace("/", "");
        try { panel.setTitle(InetAddress.getByName(remote.split(":")[0]).getHostName()); } //Try to get device name
        catch(Exception ignored) { panel.setTitle(remote); } //Otherwise, just put the remote IP Address
        panel.setOnCloseRequest(e -> ServerHandler.disconnect(client)); //Disconnect on close request
        panel.show();
    }
    
    public Stage getPanel() { return panel; }
    
}
