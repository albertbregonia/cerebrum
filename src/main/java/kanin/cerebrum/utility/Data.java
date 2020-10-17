package kanin.cerebrum.utility;

import javafx.scene.input.KeyCode;
import java.io.Serializable;

public class Data implements Serializable {
    
    private final String msg;
    private final KeyCode key;
    private final int id;
    
    public Data(int id, String msg, KeyCode key){
        this.id=id;
        this.msg=msg;
        this.key=key;
    }

    public int getID() {return id;}
    public KeyCode getKey() {return key;}
    public String getMsg() {return msg;}
    
}
