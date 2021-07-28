package zelda;

import java.io.Serializable;
import java.net.*;
import java.sql.Timestamp;
import java.util.*;

public class EventLogXML implements Serializable {

    private String IPAddress;
    private final String eventTimestamp;
    private final String eventDescription;

    public EventLogXML(String eventDescription) {
        try {
            this.IPAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
            this.IPAddress = "sconosciuto";
        }

        Date d = new Date();
        Timestamp t = new Timestamp(d.getTime());
        this.eventTimestamp = t.toString();

        this.eventDescription = eventDescription;
    }

}
