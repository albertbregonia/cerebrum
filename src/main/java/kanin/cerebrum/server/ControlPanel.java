package kanin.cerebrum.server;

import io.netty.channel.ChannelHandlerContext;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.InetAddress;

import static kanin.cerebrum.Main.clients;

public class ControlPanel {
    
    private final Stage ctrl = new Stage();
    
    public ControlPanel(ChannelHandlerContext client){
        //Generate a translucent blue window that takes in data to be sent to the respective client
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        ctrl.setOpacity(0.2);
        Pane layout = new Pane();
        layout.setStyle("-fx-background-color: darkblue");
        ctrl.addEventHandler(MouseEvent.ANY, new MouseHandler(client));
        ctrl.addEventHandler(KeyEvent.ANY, new KeyHandler(client));
        ctrl.setScene(new Scene(layout));
        ctrl.setX(bounds.getMinX());
        ctrl.setY(bounds.getMinY());
        ctrl.setWidth(bounds.getWidth());
        ctrl.setHeight(bounds.getHeight());
        ctrl.setTitle(client.channel().remoteAddress().toString().replace("\\",""));
        ctrl.setOnCloseRequest(e->{
            clients.remove(client);
            client.close();
        });
        ctrl.show();
    }
    
    public Stage getPanel(){return ctrl;}
    
}
