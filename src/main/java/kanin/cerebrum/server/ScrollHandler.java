package kanin.cerebrum.server;

import io.netty.channel.ChannelHandlerContext;
import javafx.event.EventHandler;
import javafx.scene.input.ScrollEvent;
import kanin.cerebrum.utility.Data;

public class ScrollHandler implements EventHandler<ScrollEvent> {
    
    private final ChannelHandlerContext client;

    public ScrollHandler(ChannelHandlerContext client){this.client=client;}

    @Override
    public void handle(ScrollEvent e) {
        switch(e.getTextDeltaYUnits()){ 
            case LINES:
            case PAGES:
                client.writeAndFlush(new Data(6,""+(int)e.getTextDeltaY(), null));
                break;
            case NONE:
                client.writeAndFlush(new Data(6,""+(int)e.getDeltaY(), null));
        }
    }
}
