package kanin.cerebrum.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javafx.application.Platform;
import kanin.cerebrum.Settings;
import kanin.cerebrum.utility.Data;

import static kanin.cerebrum.Main.clients;

public class ServerHandler extends SimpleChannelInboundHandler<Data> {
    
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        if(clients.size()<Settings.getMaxClients()){
            System.out.println(ctx.channel().remoteAddress()+" has connected.");
            Platform.runLater(()->clients.put(ctx,new ControlPanel(ctx)));
        }
        else
            ctx.channel().close(); //refuse extra connections
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        if(clients.containsKey(ctx)) //Close respective control panel on disconnect
            Platform.runLater(()->{
                clients.get(ctx).getPanel().close();
                clients.remove(ctx);
            });
    }

    @Override //Host is the only one allowed to send data, client never sends any data ; disconnect for safety
    protected void channelRead0(ChannelHandlerContext ctx, Data x) throws Exception {ctx.close();}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {ctx.close();}
    
}
