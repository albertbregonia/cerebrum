package kanin.cerebrum.server;

import io.netty.channel.ChannelHandlerContext;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import kanin.cerebrum.utility.Data;

public class KeyHandler implements EventHandler<KeyEvent> {

    private final ChannelHandlerContext client;

    public KeyHandler(ChannelHandlerContext client){this.client=client;}
    
    @Override  //Send keyboard data to client
    public void handle(KeyEvent e) {
        switch(e.getEventType().getName()){
            case "KEY_PRESSED":
                client.writeAndFlush(new Data(4,"",e.getCode()));
                break;
            case "KEY_RELEASED":
                client.writeAndFlush(new Data(5,"",e.getCode()));
        }
    }
}
