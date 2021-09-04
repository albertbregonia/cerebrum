package kanin.cerebrum.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javafx.application.Platform;
import kanin.cerebrum.Main;
import kanin.cerebrum.SettingsDialog;
import kanin.cerebrum.utility.Packet;
import java.util.concurrent.ConcurrentHashMap;

public class ServerHandler extends SimpleChannelInboundHandler<Packet> {
    
    public static final ConcurrentHashMap<ChannelHandlerContext, ControlPanel> clients = new ConcurrentHashMap<>();

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        if(clients.size() < SettingsDialog.getMaxClients()) {
            System.out.println(ctx.channel().remoteAddress() + " has connected.");
            Platform.runLater(() -> {
                clients.put(ctx, new ControlPanel(ctx));
                Main.liveConnections_.getItems().add(clients.get(ctx).getPanel().getTitle());
            });
        } else
            ctx.channel().close(); //refuse extra connections
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        if(clients.containsKey(ctx)) //Close respective control panel on disconnect
            Platform.runLater(() -> disconnect(ctx));
    }

    @Override //Host is the only one allowed to send data, client never sends any data ; disconnect for safety
    protected void channelRead0(ChannelHandlerContext ctx, Packet x) throws Exception { ctx.close(); }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception { ctx.close(); }
    
    public static void disconnect(ChannelHandlerContext ctx) { //Close the UI and remove from 'Live Connection' list
        Main.liveConnections_.getItems().remove(clients.get(ctx).getPanel().getTitle());
        clients.get(ctx).getPanel().close();
        clients.remove(ctx);
        ctx.close();
    }
}
