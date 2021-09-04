package kanin.cerebrum.server;

import io.netty.channel.ChannelHandlerContext;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import kanin.cerebrum.utility.Event;
import kanin.cerebrum.utility.Packet;

public class KeyHandler implements EventHandler<KeyEvent> {

    private final ChannelHandlerContext client;

    public KeyHandler(ChannelHandlerContext client){this.client=client;}
    
    @Override  //Send keyboard data to client
    public void handle(KeyEvent e) {
        switch(e.getEventType().getName()){
            case "KEY_PRESSED":
                client.writeAndFlush(new Packet(Event.KEY_PRESS, e.getCode().toString()));
                break;
            case "KEY_RELEASED":
                client.writeAndFlush(new Packet(Event.KEY_RELEASE, e.getCode().toString()));
        }
    }
}
