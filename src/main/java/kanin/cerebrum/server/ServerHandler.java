package kanin.cerebrum.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javafx.application.Platform;
import javafx.scene.control.SelectionMode;
import kanin.cerebrum.Main;
import kanin.cerebrum.Settings;
import kanin.cerebrum.utility.Data;

import static kanin.cerebrum.Main.clients;

public class ServerHandler extends SimpleChannelInboundHandler<Data> {
    
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        if(clients.size()<Settings.getMaxClients()){
            System.out.println(ctx.channel().remoteAddress()+" has connected.");
            Platform.runLater(()->{
                clients.put(ctx,new ControlPanel(ctx));
                Main.liveConnections.getItems().add(clients.get(ctx).getPanel().getTitle());
                Main.liveConnections.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                Main.liveConnections.setOnMouseClicked(e->{ //Disconnect on click
                    int i = 0;
                    if(Main.liveConnections.getSelectionModel().getSelectedItem()!=null && !Main.liveConnections.getSelectionModel().getSelectedItem().isEmpty())
                        for(ChannelHandlerContext connection:clients.keySet())
                            if(i==Main.liveConnections.getSelectionModel().getSelectedIndex()){ //Index based to ensure that devices with the same name don't conflict
                                disconnect(ctx);
                                connection.close();
                                return;
                            }
                            else
                                i++;
                });
            });
        }
        else
            ctx.channel().close(); //refuse extra connections
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        if(clients.containsKey(ctx)) //Close respective control panel on disconnect
            Platform.runLater(()->{
                disconnect(ctx);
            });
    }

    @Override //Host is the only one allowed to send data, client never sends any data ; disconnect for safety
    protected void channelRead0(ChannelHandlerContext ctx, Data x) throws Exception {ctx.close();}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {ctx.close();}
    
    public static void disconnect(ChannelHandlerContext ctx){ //Close the UI and remove from 'Live Connection' list
        Main.liveConnections.getItems().remove(clients.get(ctx).getPanel().getTitle());
        clients.get(ctx).getPanel().close();
        clients.remove(ctx);
    }
}
