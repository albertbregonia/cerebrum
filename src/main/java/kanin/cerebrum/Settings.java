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
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;

import static kanin.cerebrum.Main.disconnect0;

public class Settings implements Initializable {
    
    private static Stage panel;
    private static boolean configured=false;
    @FXML private Label ipInfo; 
    private static Label ipi;
    
    public static void startup(){
        panel = new Stage();
        try{
            panel.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(Settings.class.getClassLoader().getResource("settings.fxml")))));
            load();
            ipi.setText(ipi.getText()+Inet4Address.getLocalHost().getHostAddress());
        }catch(IOException e){e.printStackTrace();}
        panel.setTitle("Cerebrum Settings");
        panel.initModality(Modality.APPLICATION_MODAL);
        panel.setResizable(false);
        panel.initStyle(StageStyle.UTILITY);
        panel.showAndWait();
    }
    
    public static void load() throws IOException{
        File settings = new File("settings.txt");
        if(settings.exists()){
            Scanner loader = new Scanner(new FileInputStream(settings));
            int x;
            for(x=0; loader.hasNextLine(); x++)
                switch(x){
                    case 0:
                        i.setText(loader.nextLine());
                        break;
                    case 1:
                        p.setText(loader.nextLine());
                        break;
                    case 2:
                        h.setSelected(Boolean.parseBoolean(loader.nextLine()));
                        break;
                    case 3:
                        c.setValue(Double.parseDouble(loader.nextLine()));
                        configured = true;
                        break;
                    default: //in case there is an error, abort loading settings;
                        loader.close();
                }
        } 
        else System.out.println("Prior Settings Not Found.");
    }
    
    @FXML
    private CheckBox host;
    private static CheckBox h;
    
    @FXML
    private TextField ip,port;
    private static TextField i,p;
    
    @FXML
    private Slider count;
    private static Slider c;

    //Upon Invalid Input, Defaulted to Host
    public static String getIP(){ 
        if(i.getText().trim().isEmpty())
            return "Server";
        else
            return i.getText().trim();
    }

    //Upon Invalid Input, Defaulted to port 54000
    public static int getPort(){
        try { 
            return Integer.parseInt(p.getText().trim());
        } catch (Exception e) {
            return 54000;
        }
    }
    
    public static boolean isHost(){return h.isSelected();}
    
    public static int getMaxClients(){return (int)c.getValue();}

    public static boolean isConfigured(){return configured;}
    
    public static boolean isValid(){
        if(getPort()>0 && getPort()<65536)
            if(isHost())
                return true;
            else{//I understand that this freezes the main thread, but it's 1 second so it shouldn't matter
                try{return InetAddress.getByName(getIP()).isReachable(1000);}
                catch(IOException e){
                    e.printStackTrace();
                    return false;
                }
            }
        else
            return false;
    }
    
    @FXML
    private void save() throws IOException {
        disconnect0(); //Can't change settings while a network thread is alive
        FileOutputStream settings = new FileOutputStream("settings.txt");
        PrintWriter w = new PrintWriter(settings,true);
        w.println(getIP());
        w.println(getPort());
        w.println(isHost());
        w.println(getMaxClients());
        configured=true;
        settings.close();
        panel.close();
    }
    
    @FXML
    private void cancel(){panel.close();}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Pass by reference for static use
        i = ip;
        p = port;
        h = host;
        c = count;
        ipi = ipInfo;
    }
    
}
