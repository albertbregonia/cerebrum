package kanin.cerebrum.server;

import io.netty.channel.ChannelHandlerContext;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import kanin.cerebrum.utility.Data;

import java.util.ArrayList;

//--------WIP--------//
public class KeyHandler implements EventHandler<KeyEvent> {

    private final ArrayList<String> last = new ArrayList<>(3);

    private final ChannelHandlerContext user;

    public KeyHandler(ChannelHandlerContext user){this.user=user;}
    
    @Override  //Send keyboard data to client
    public void handle(KeyEvent e) {
        switch(e.getEventType().getName()){ //Only handles simple typing as of 10/9/2020
            case "KEY_RELEASED":
                if(last.get(last.size()-1).equalsIgnoreCase("KEY_TYPED"))
                    user.writeAndFlush(new Data(4,e.getText(),e));
                break;
        }
        //queue of last 3 events
        if(last.size()==3)
            last.remove(0);
        last.add(e.getEventType().getName());
    }
}
