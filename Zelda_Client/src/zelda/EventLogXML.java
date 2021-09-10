package zelda;

import java.io.Serializable;
import java.net.*;
import java.sql.Timestamp;
import java.util.*;

public class EventLogXML implements Serializable {
    
    private String applicationName;
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
        
        applicationName = "Zelda";
        Date d = new Date();
        Timestamp t = new Timestamp(d.getTime()); //1
        this.eventTimestamp = t.toString();

        this.eventDescription = eventDescription;
    }

}

/*
1: Reference della classe Timestamp: https://docs.oracle.com/javase/8/docs/api/java/sql/Timestamp.html
*/
