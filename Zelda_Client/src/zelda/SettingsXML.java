package zelda;

import javafx.scene.input.KeyCode;
import java.io.Serializable;

public class SettingsXML implements Serializable {

    public KeyAssociation keyAssociation;
    public ServerAddress serverLogAddress;
    
    public SettingsXML(KeyAssociation keyAssociation, ServerAddress serverLogAddress) {
        this.keyAssociation = keyAssociation;
        this.serverLogAddress = serverLogAddress;
    }
}
