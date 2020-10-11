package kanin.cerebrum.utility;

import javafx.scene.input.KeyEvent;
import java.io.Serializable;

public class Data implements Serializable {
    
    private final String msg;
    private final KeyEvent key;
    private final int id;
    
    public Data(int id, String msg, KeyEvent key){
        this.id=id;
        this.msg=msg;
        this.key=key;
    }

    public int getID() {return id;}
    public KeyEvent getKey() {return key;}
    public String getMsg() {return msg;}
    
}
