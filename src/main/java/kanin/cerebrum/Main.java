package kanin.cerebrum;

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
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import kanin.cerebrum.client.ClientHandler;
import kanin.cerebrum.server.ControlPanel;
import kanin.cerebrum.server.ServerHandler;
import kanin.cerebrum.utility.Data;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

public class Main extends Application implements Initializable {
    
    public static final ConcurrentHashMap<ChannelHandlerContext,ControlPanel> clients = new ConcurrentHashMap<>();
    
    @FXML private ListView<String> list;
    public static ListView<String> liveConnections;
    
    @FXML private ProgressIndicator status;
    public static ProgressIndicator stat;

    public static void main(String[] args){launch(args);}
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Pass by references to use value in separate threads
        stat=status;
        liveConnections=list;
    }

    @Override
    public void start(Stage stage) throws Exception { //Load main UI
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(Main.class.getClassLoader().getResource("main.fxml")))));
        stage.setTitle("Cerebrum by Albert Bregonia");
        stage.setResizable(false);
        stage.show();
    }

    //Ask for settings on first time use, otherwise simply connect
    @FXML 
    public void connect() throws IOException{
        if(!Settings.isConfigured())
            Settings.startup();
        if(Settings.isConfigured())
            if(Settings.isValid()){
                if (current == null || !current.isAlive()) {
                    current = new Connection(Settings.getIP(), Settings.getPort(), Settings.isHost());
                    current.start();
                    status.setProgress(-1);
                }
            }
            else
                alertMsg("Invalid settings!","Please check your settings before your proceed.", Alert.AlertType.ERROR);
    }
    
    //Disconnect Button - Stops the server if this device is the host or disconnects from host if this device is a client
    @FXML 
    public void disconnect(){disconnect0();}
    
    public static void disconnect0(){
        Platform.runLater(()->{ //Close all open Control Panels and set the service status to disconnected
            for(ChannelHandlerContext client:clients.keySet())
                clients.get(client).getPanel().close();
            stat.setProgress(0);
        });
        if(current!=null && current.isAlive())
            current.getConnection().channel().close();
        current=null;
    }

    //Settings Button
    @FXML public void openSettings(){Settings.startup();}

    public static Connection current; //Connection Attempt Thread
    public static class Connection extends Thread{
        
        private final String ip;
        private final int port;
        private final boolean master;
        private ChannelFuture net;
        
        public Connection(String ip, int port, boolean master){
            this.ip=ip;
            this.port=port; 
            this.master=master;
        }
        
        @Override
        public void run() {
            if(master){ //Create Server
                EventLoopGroup master = new NioEventLoopGroup(1);
                EventLoopGroup worker = new NioEventLoopGroup();
                try
                {
                    ServerBootstrap startup = new ServerBootstrap();
                    startup.group(master, worker)
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>(){
                        @Override protected void initChannel(SocketChannel sc){
                        sc.pipeline().addLast(new ObjectEncoder());
                        sc.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(Data.class.getClassLoader())));
                        sc.pipeline().addLast(new ServerHandler());}
                });
                    net = startup.bind(port).sync();
                    Platform.runLater(()->stat.setProgress(1));
                    net.channel().closeFuture().sync();
                }
                catch (InterruptedException e){e.printStackTrace(); disconnect0();}
                finally
                {
                    master.shutdownGracefully();
                    worker.shutdownGracefully();
                    Platform.runLater(()->stat.setProgress(0));
                }
            }
            else{ //Connect to Server
                EventLoopGroup group = new NioEventLoopGroup();
                try
                {
                    Bootstrap startup = new Bootstrap();
                    startup.group(group)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>(){
                        @Override protected void initChannel(SocketChannel sc){
                        sc.pipeline().addLast(new ObjectEncoder());
                        sc.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(Data.class.getClassLoader())));
                        sc.pipeline().addLast(new ClientHandler());}
                });
                    net = startup.connect(ip,port).sync();
                    Platform.runLater(()->stat.setProgress(1));
                    net.channel().closeFuture().sync();
                }
                catch (InterruptedException e){e.printStackTrace(); disconnect0();}
                finally
                {
                    group.shutdownGracefully();
                    Platform.runLater(()->stat.setProgress(0));
                }
            }
        }
        
        public ChannelFuture getConnection(){return net;}
    }

    //Dialog box - in some instances its used for error messaging, otherwise for confirmation
    public static boolean alertMsg(String content, String subtext, Alert.AlertType type){
        Alert alert = new Alert(type,subtext,ButtonType.OK);
        alert.setTitle("Cerebrum by Albert Bregonia");
        alert.setHeaderText(content);
        Optional<ButtonType> result = alert.showAndWait();
        //If the OK button is pressed return true
        return result.filter(bt->bt==ButtonType.OK).isPresent();
    }
    
}
