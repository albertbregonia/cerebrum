package kanin.cerebrum.server;

import io.netty.channel.ChannelHandlerContext;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import kanin.cerebrum.utility.Event;
import kanin.cerebrum.utility.Packet;

import java.util.ArrayList;

public class MouseHandler implements EventHandler<MouseEvent> {
    
    private final ArrayList<String> last3Events = new ArrayList<>(3); //used as queue
    private int ctr = 0; //buffer counter to send mouse movement; setting it to a buffer of 3 events decreases input delay
    private final ChannelHandlerContext client;
    
    public MouseHandler(ChannelHandlerContext client) { this.client = client; }
    
    @Override //Send mouse data to client
    public void handle(MouseEvent e) {
        switch(e.getEventType().getName()) {
            case "MOUSE_MOVED":
                bufferedMouseMove(e);
                break;
            case "MOUSE_CLICKED":
                //if statement included to ignore extra click after a click and drag
                if(last3Events.isEmpty() || !(last3Events.get(last3Events.size() - 1).equalsIgnoreCase("MOUSE_RELEASED") &&
                                             last3Events.get(0).equalsIgnoreCase("MOUSE_DRAGGED")))
                    client.writeAndFlush(new Packet(Event.MOUSE_CLICK, e.getButton().name()));//simple click
                break;
            case "MOUSE_DRAGGED":
                if(last3Events.isEmpty() || last3Events.get(last3Events.size() - 1).equalsIgnoreCase("MOUSE_DRAGGED"))
                    bufferedMouseMove(e);
                else
                    client.writeAndFlush(new Packet(Event.MOUSE_HOLD, e.getButton().name())); //send initial drag
        }
        if(last3Events.isEmpty() || (last3Events.get(last3Events.size() - 1).equalsIgnoreCase("MOUSE_DRAGGED") &&
                                     !e.getEventType().getName().equalsIgnoreCase("MOUSE_DRAGGED")))
            client.writeAndFlush(new Packet(Event.MOUSE_RELEASE, e.getButton().name())); //send release drag
        if(last3Events.size() == 3) 
            last3Events.remove(0); //pop head
        last3Events.add(e.getEventType().getName());
    }
    
    public void bufferedMouseMove(MouseEvent e) {
        if(ctr == 3) {
            client.writeAndFlush(new Packet(Event.MOUSE_MOVE, String.format("%d,%d", (int) e.getSceneX(), (int) e.getSceneY())));
            ctr = 0;
        } else
            ctr++;
    }
}
