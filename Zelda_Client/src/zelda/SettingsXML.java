package zelda;

import java.io.Serializable;

public class SettingsXML implements Serializable {

    public KeyAssociation keyAssociation;
    public ServerAddress serverLogAddress;
    public ServerAddress dbAddress;
    public String dbUsername, dbPassword;
    public int dbLimit;
    
    public SettingsXML(KeyAssociation keyAssociation, ServerAddress serverLogAddress, ServerAddress dbAddress, String dbUsername, String dbPassword, int dbLimit) {
        this.keyAssociation = keyAssociation;
        this.serverLogAddress = serverLogAddress;
        this.dbAddress = dbAddress;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.dbLimit = dbLimit;
    }
}
