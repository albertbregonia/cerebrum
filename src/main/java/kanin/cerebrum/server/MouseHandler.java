package kanin.cerebrum.server;

import io.netty.channel.ChannelHandlerContext;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import kanin.cerebrum.utility.Data;

import java.util.ArrayList;

import static kanin.cerebrum.Main.clients;


public class MouseHandler implements EventHandler<MouseEvent> {
    
    private final ArrayList<String> last = new ArrayList<>(3);
    private int ctr=0; //buffer to send mouse movement, setting it to a buffer of 3 decreases input delay
    private final ChannelHandlerContext client;
    
    public MouseHandler(ChannelHandlerContext client){this.client=client;}
    
    @Override //Send mouse data to client
    public void handle(MouseEvent e) {
        if(!last.isEmpty()){ //Ignores the first event, results in NullPointerException if not
            switch(e.getEventType().getName()){
                case "MOUSE_MOVED":
                    if(ctr==3){
                        client.writeAndFlush(new Data(0,(int)e.getSceneX()+","+(int)e.getSceneY(),null));
                        ctr=0;
                    }
                    else 
                        ctr++;
                    break;
                case "MOUSE_CLICKED":
                    //if statement included to fix bug where an extra click is simulated after a click and drag
                    if(!(last.get(last.size()-1).equalsIgnoreCase("MOUSE_RELEASED") && last.get(0).equalsIgnoreCase("MOUSE_DRAGGED")))
                        client.writeAndFlush(new Data(1,e.getButton().name(),null));//simple click
                    break;
                case "MOUSE_DRAGGED":
                    if(last.get(last.size()-1).equalsIgnoreCase("MOUSE_DRAGGED")){
                        if(ctr==3){
                            client.writeAndFlush(new Data(0,(int)e.getSceneX()+","+(int)e.getSceneY(),null)); //send movement
                            ctr=0;
                        } 
                        else 
                            ctr++;
                    }
                    else
                        client.writeAndFlush(new Data(2,e.getButton().name(),null)); //send initial drag
                    break;
            }
            if(last.get(last.size()-1).equalsIgnoreCase("MOUSE_DRAGGED") && !e.getEventType().getName().equalsIgnoreCase("MOUSE_DRAGGED"))
                client.writeAndFlush(new Data(3,e.getButton().name(),null)); //send release drag   
        }
        //queue of last 3 events
        if(last.size()==3) 
            last.remove(0);
        last.add(e.getEventType().getName());
    }
}
