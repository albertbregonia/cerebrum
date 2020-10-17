package kanin.cerebrum.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.robot.Robot;
import kanin.cerebrum.utility.Data;

import java.util.Vector;

public class ClientHandler extends SimpleChannelInboundHandler<Data> {
    
    public static Robot bot;
    public ClientHandler(){Platform.runLater(()->bot = new Robot());}

    @Override //Parse incoming data and simulate mouse movement/key strokes
    public void channelRead0(ChannelHandlerContext ctx, Data x) throws Exception {
        switch(x.getID()){ 
            case 0: //Mouse Movement
                String[] data = x.getMsg().split(",");
                Platform.runLater(()->bot.mouseMove(Integer.parseInt(data[0]),Integer.parseInt(data[1])));
                break;
            case 1: //Simple Click
                for(MouseButton btn:MouseButton.values())
                    if(btn.name().equalsIgnoreCase(x.getMsg())){
                        Platform.runLater(()->bot.mouseClick(btn));
                        break;
                    }
                break;
            case 2: //Hold or Drag
                for(MouseButton btn:MouseButton.values())
                    if(btn.name().equalsIgnoreCase(x.getMsg())){
                        Platform.runLater(()->bot.mousePress(btn));
                        break;
                    }
                break;
            case 3: //Release
                for(MouseButton btn:MouseButton.values())
                    if(btn.name().equalsIgnoreCase(x.getMsg())){
                        Platform.runLater(()->bot.mouseRelease(btn));
                        break;
                    }
                break;
            case 4: //Press
                Platform.runLater(()->bot.keyPress(x.getKey().getCode()));
                break;
            case 5: //Release
                Platform.runLater(()->bot.keyRelease(x.getKey().getCode()));
        }
        ctx.flush(); //Flush Data
    }

    @Override //Disconnect on error
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {ctx.close();}
}
