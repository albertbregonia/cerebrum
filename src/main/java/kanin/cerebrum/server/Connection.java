package kanin.cerebrum.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import javafx.application.Platform;
import kanin.cerebrum.client.ClientHandler;
import kanin.cerebrum.utility.Packet;
import kanin.cerebrum.Main;

//TCP is used instead of UDP bc many events need to be acknowledged 
public class Connection extends Thread {
        
    private final String ip;
    private final int port;
    private final boolean server;
    private ChannelFuture connection;
    
    public Connection(String ip, int port, boolean server) {
        this.ip = ip;
        this.port = port; 
        this.server = server;
    }

    public ChannelFuture getConnection() { return connection; }

    @Override
    public void run() {
        if(server) { //Create Server
            EventLoopGroup master = new NioEventLoopGroup(1), 
                           worker = new NioEventLoopGroup();
            try {
                ServerBootstrap startup = new ServerBootstrap();
                startup.group(master, worker)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override protected void initChannel(SocketChannel sc) {
                        sc.pipeline().addLast(new ObjectEncoder());
                        sc.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(Packet.class.getClassLoader())));
                        sc.pipeline().addLast(new ServerHandler());
                    }
                });
                connection = startup.bind(port).sync();
                Platform.runLater(() -> Main.status_.setProgress(1));
                connection.channel().closeFuture().sync();
            }
            catch (InterruptedException e) {
                e.printStackTrace(); 
                Main.disconnectAll();
            } finally {
                master.shutdownGracefully();
                worker.shutdownGracefully();
                Platform.runLater(() -> Main.status_.setProgress(0));
            }
        } else { //Connect to Server
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap startup = new Bootstrap();
                startup.group(group)
                .option(ChannelOption.TCP_NODELAY, true)
                .channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                        @Override protected void initChannel(SocketChannel sc){
                        sc.pipeline().addLast(new ObjectEncoder());
                        sc.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(Packet.class.getClassLoader())));
                        sc.pipeline().addLast(new ClientHandler());
                    }
                });
                connection = startup.connect(ip, port).sync();
                Platform.runLater(() -> Main.status_.setProgress(1));
                connection.channel().closeFuture().sync();
            }
            catch (InterruptedException e) {
                e.printStackTrace(); 
                Main.disconnectAll();
            } finally {
                group.shutdownGracefully();
                Platform.runLater(() -> Main.status_.setProgress(0));
            }
        }
    }
}