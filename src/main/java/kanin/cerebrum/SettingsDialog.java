package kanin.cerebrum;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import static kanin.cerebrum.Main.disconnectAll;

public class SettingsDialog implements Initializable {
    
    //values are static as only one SettingsDialog should ever exist
    private static Stage panel;
    private static boolean configured = false;
    @FXML private Label yourIPv4; 
    private static Label yourIPv4_;

    @FXML
    private CheckBox hosting;
    private static CheckBox hosting_;
    
    @FXML
    private TextField ipInput, portInput;
    private static TextField ipInput_, portInput_;
    
    @FXML
    private Slider connectionCount;
    private static Slider connectionCount_;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Pass by reference for static use
        ipInput_ = this.ipInput;
        portInput_ = this.portInput;
        hosting_ = this.hosting;
        connectionCount_ = this.connectionCount;
        yourIPv4_ = this.yourIPv4;
    }
    
    public static void startup() {
        panel = new Stage();
        try {
            panel.setScene(new Scene(FXMLLoader.load(
                SettingsDialog.class.getClassLoader().getResource("settings.fxml"))));
            load();
            yourIPv4_.setText(yourIPv4_.getText() + Inet4Address.getLocalHost().getHostAddress());
        } catch(IOException e) { e.printStackTrace(); }
        panel.setTitle("Cerebrum Settings");
        panel.initModality(Modality.APPLICATION_MODAL);
        panel.setResizable(false);
        panel.initStyle(StageStyle.UTILITY);
        panel.showAndWait();
    }
    
    public static void load() throws IOException {
        File settings = new File("settings.txt");
        if(settings.exists()) {
            Scanner loader = new Scanner(new FileInputStream(settings));
            try {
                for(int x=0; loader.hasNextLine(); x++)
                    switch(x) {
                        case 0: ipInput_.setText(loader.nextLine());
                            break;
                        case 1: portInput_.setText(loader.nextLine()); 
                            break;
                        case 2: hosting_.setSelected(Boolean.parseBoolean(loader.nextLine()));
                            break;
                        case 3:
                            connectionCount_.setValue(Double.parseDouble(loader.nextLine()));
                            configured = true;
                            break;
                        default:
                            throw new Exception("Error in loading settings."); //break out of loop
                    }
            } catch(Exception ignored) {} //in case there is an error, abort loading settings;
            finally { loader.close(); }
        } else
            System.out.println("Prior Settings Not Found.");
    }

    //Upon Invalid Input, Defaulted to Host
    public static String getIP() {
        String ip = ipInput_.getText().trim();
        return ip.isEmpty() ? "Server" : ip;
    }

    //Upon Invalid Input, Defaulted to port 54000
    public static int getPort() {
        try { return Integer.parseInt(portInput_.getText().trim()); } 
        catch (Exception e) { return 54000; }
    }
    
    public static boolean isHost() { return hosting_.isSelected();}
    
    public static int getMaxClients() { return (int) connectionCount_.getValue(); }

    public static boolean isConfigured() { return configured; }
    
    public static boolean hasValidAddress() {
        int port = getPort();
        if(port > 1024 && port < 65536)
            if(isHost())
                return true;
            else {//I understand that this freezes the main thread, but it's 1 second so it shouldn't matter; a fast connection is required
                try { return InetAddress.getByName(getIP()).isReachable(1000); }
                catch(IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        return false;
    }
    
    @FXML
    private void save() throws IOException {
        disconnectAll(); //Can't change settings while a network thread is alive
        File settings = new File("settings.txt");
        if(!settings.exists() && settings.createNewFile())
            System.out.println("settings.txt created");
        PrintWriter writer = new PrintWriter(new FileOutputStream("settings.txt"), true);
        writer.println(getIP());
        writer.println(getPort());
        writer.println(isHost());
        writer.println(getMaxClients());
        configured = true;
        writer.close();
        panel.close();
    }
    
    @FXML
    private void cancel() { panel.close(); }
    
}
