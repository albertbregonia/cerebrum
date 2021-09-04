package kanin.cerebrum.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.robot.Robot;
import kanin.cerebrum.utility.Packet;

public class ClientHandler extends SimpleChannelInboundHandler<Packet> {
    
    public static Robot bot;
    public ClientHandler() { Platform.runLater(() -> bot = new Robot()); } //Platform.runLater() is used as 'Robot's have to be made on the GUI thread

    @Override //Parse incoming data and simulate mouse movement/key strokes
    public void channelRead0(ChannelHandlerContext ctx, Packet e) throws Exception {
        Platform.runLater(() -> {
            switch(e.getEvent()) {
                case MOUSE_MOVE:
                    String[] data = e.getMsg().split(",");
                    bot.mouseMove(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
                    break;
                case MOUSE_CLICK:
                    bot.mouseClick(MouseButton.valueOf(e.getMsg()));
                    break;
                case MOUSE_HOLD:
                    bot.mousePress(MouseButton.valueOf(e.getMsg()));
                    break;
                case MOUSE_RELEASE:
                    bot.mouseRelease(MouseButton.valueOf(e.getMsg()));
                    break;
                case KEY_PRESS:
                    bot.keyPress(KeyCode.valueOf(e.getMsg()));
                    break;
                case KEY_RELEASE:
                    bot.keyRelease(KeyCode.valueOf(e.getMsg()));
                    break;
                case SCROLL: //multiplied by -1 to invert motion, scroll should follow the direction of the scroll wheel
                    bot.mouseWheel(-1 * Integer.parseInt(e.getMsg()));
            }
        });
        ctx.flush(); //Flush data buffer
    }

    @Override //Disconnect on error
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception { ctx.close(); }
}
