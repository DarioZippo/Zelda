package zelda;

import java.io.Serializable;
import javafx.scene.input.KeyCode;

public class KeyAssociation implements Serializable {

    public KeyCode rightKey;
    public KeyCode leftKey;
    public KeyCode upKey;
    public KeyCode downKey;
    public KeyCode swordKey;
    public KeyCode specialKey;
    public KeyCode bowKey;

    public KeyAssociation(KeyCode rightKey, KeyCode leftKey, KeyCode upKey, KeyCode downKey, KeyCode swordKey, KeyCode specialKey, KeyCode bowKey){
        this.rightKey = rightKey;
        this.leftKey = leftKey;
        this.upKey = upKey;
        this.downKey = downKey;
        this.swordKey = swordKey;
        this.specialKey = specialKey;
        this.bowKey = bowKey;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == KeyAssociation.class) {
            KeyAssociation kas = (KeyAssociation) obj;
            if (kas.rightKey == this.rightKey
                    && kas.leftKey == this.leftKey
                    && kas.rightKey == this.rightKey
                    && kas.upKey == this.upKey
                    && kas.downKey == this.downKey
                    && kas.swordKey == this.swordKey
                    && kas.specialKey == this.specialKey
                    && kas.bowKey == this.bowKey) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}

