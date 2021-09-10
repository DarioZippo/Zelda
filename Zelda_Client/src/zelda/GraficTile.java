
package zelda;

import javafx.geometry.Pos;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import static zelda.GameUtils.*;

public class GraficTile extends StackPane {
    public ImageView occupier;
    public String occupierPath;
    private GameView gameView;
    
    public GraficTile(int x, int y, boolean occupied) {
        
        this.getStyleClass().add("tile");
        
        if(occupied == true)
            this.getStyleClass().add("occupied");
        else
            this.getStyleClass().add("free");
        relocate(x * TILE_SIZE, y * TILE_SIZE);
    }
    
    public void free()
    {
        this.getStyleClass().clear();
        this.getStyleClass().add("tile");
        this.getStyleClass().add("free");
        
        this.getChildren().clear();
        this.occupier = null;
        
        this.occupierPath = null;
    }
    
    public void occupieCharacter(Command direction)
    {
        this.getStyleClass().clear();
        this.getStyleClass().add("tile");
        this.getStyleClass().add("occupied");
        this.getStyleClass().add("character");
        
        this.occupierPath = "file:myFiles/img/staticLink";
        
        this.occupier = new ImageView(occupierPath + direction.toString() + ".png");
        
        this.occupier.setFitHeight(70);
        this.occupier.setFitWidth(55);
        
        this.getChildren().add(this.occupier);
        this.setAlignment(Pos.CENTER);
        
    }
    
    public void occupieEnemy(Command direction)
    {
        this.getStyleClass().clear();
        this.getStyleClass().add("tile");
        this.getStyleClass().add("occupied");
        this.getStyleClass().add("enemy");
        
        this.occupierPath = "file:myFiles/img/staticKnight"; //+ direction.toString() + ".png";
        
        this.occupier = new ImageView(occupierPath + direction.toString() + ".png");
        this.occupier.setFitHeight(70);
        this.occupier.setFitWidth(70);
        
        this.getChildren().add(this.occupier);
        this.setAlignment(Pos.CENTER);
    }
}
