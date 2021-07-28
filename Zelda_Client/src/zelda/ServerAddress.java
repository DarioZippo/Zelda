package zelda;

import java.io.Serializable;

public class ServerAddress implements Serializable {

    public String IPAddress;
    public int port;

    public ServerAddress(String IPAddress, int port) {
        this.IPAddress = IPAddress;
        this.port = port;
    }

}
