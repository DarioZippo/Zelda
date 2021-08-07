package zelda;

import java.io.Serializable;

public class ElementCacheData implements Serializable{
    public boolean enemy;
    public int x;
    public int y;
    public Command direction;
    
    public ElementCacheData(boolean enemy, int x, int y, Command direction){
        this.enemy = enemy;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }
}
