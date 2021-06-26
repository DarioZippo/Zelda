
package zelda;

import static java.time.Clock.system;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import static zelda.GameUtils.*;

public class GraficTile extends Region {
    public GraficTile(int x, int y, boolean occupied) {
        //setWidth(TILE_SIZE);
        //setHeight(TILE_SIZE);
        
        this.getStyleClass().add("tile");
        
        if(occupied == true)
            this.getStyleClass().add("occupied");
        else
            this.getStyleClass().add("free");
        System.out.println("x:" + x + " y:" + y);
        relocate(x * TILE_SIZE, y * TILE_SIZE);
    }
    
    public void free()
    {
        this.getStyleClass().clear();
        this.getStyleClass().add("tile");
        this.getStyleClass().add("free");
    }
    
    public void occupieCharacter(Command direction)
    {
        this.getStyleClass().clear();
        this.getStyleClass().add("tile");
        this.getStyleClass().add("occupied");
        this.getStyleClass().add("character");
        this.getStyleClass().add(direction.toString().toLowerCase());
    }
    
    public void occupieEnemy(Command direction)
    {
        this.getStyleClass().clear();
        this.getStyleClass().add("tile");
        this.getStyleClass().add("occupied");
        this.getStyleClass().add("enemy");
        this.getStyleClass().add(direction.toString().toLowerCase());
    }
}
