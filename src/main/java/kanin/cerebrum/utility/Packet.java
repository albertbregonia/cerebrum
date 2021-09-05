package kanin.cerebrum.utility;

import java.io.Serializable;
/**
 * Packet is a immutable object that contains the data of any input event
 */
public class Packet implements Serializable {

    private final String msg;
    private final Event event;

    public Packet(Event event, String msg) {
        this.event = event;
        this.msg = msg;
    }

    public Event getEvent() { return this.event; }
    public String getMsg() { return this.msg; }

}