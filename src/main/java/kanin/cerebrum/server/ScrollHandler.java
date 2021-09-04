package kanin.cerebrum.server;

import io.netty.channel.ChannelHandlerContext;
import javafx.event.EventHandler;
import javafx.scene.input.ScrollEvent;
import kanin.cerebrum.utility.Event;
import kanin.cerebrum.utility.Packet;

public class ScrollHandler implements EventHandler<ScrollEvent> {
    
    private final ChannelHandlerContext client;

    public ScrollHandler(ChannelHandlerContext client){this.client=client;}

    @Override
    public void handle(ScrollEvent e) {
        switch(e.getTextDeltaYUnits()){ 
            case LINES:
            case PAGES:
                client.writeAndFlush(new Packet(Event.SCROLL, "" + (int)e.getTextDeltaY()));
                break;
            case NONE:
                client.writeAndFlush(new Packet(Event.SCROLL, "" + (int)e.getDeltaY()));
        }
    }
}
