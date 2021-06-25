package zelda;

import java.io.Serializable;
import javafx.scene.input.KeyCode;

public class KeyAssociation implements Serializable {

    public KeyCode rightKey;
    public KeyCode leftKey;
    public KeyCode swordKey;
    public KeyCode pauseKey;

    public KeyAssociation(KeyCode rightKey, KeyCode leftKey){ //, KeyCode swordKey, KeyCode pauseKey) {
        this.rightKey = rightKey;
        this.leftKey = leftKey;
        /*this.swordKey = swordKey;
        this.pauseKey = pauseKey;*/
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == KeyAssociation.class) {
            KeyAssociation kas = (KeyAssociation) obj;
            if (kas.rightKey == this.rightKey
                    && kas.leftKey == this.leftKey
                    /*&& kas.swordKey == this.swordKey
                    && kas.pauseKey == this.pauseKey*/) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}

