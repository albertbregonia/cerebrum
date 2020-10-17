package kanin.cerebrum.server;

import io.netty.channel.ChannelHandlerContext;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import kanin.cerebrum.utility.Data;

import java.util.ArrayList;

//--------WIP--------//
public class KeyHandler implements EventHandler<KeyEvent> {

    private final ChannelHandlerContext user;

    public KeyHandler(ChannelHandlerContext user){this.user=user;}
    
    @Override  //Send keyboard data to client
    public void handle(KeyEvent e) {
        switch(e.getEventType().getName()){
            case "KEY_PRESSED":
                user.writeAndFlush(new Data(4,"",e));
                break;
            case "KEY_RELEASED":
                user.writeAndFlush(new Data(5,"",e));
        }
    }
}
