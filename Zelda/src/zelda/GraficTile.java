
package zelda;

import static java.time.Clock.system;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import static zelda.BoardUtils.*;

public class GraficTile extends Region {
/*
    private Piece piece;

    public boolean hasPiece() {
        return piece != null;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
*/
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
    
    public void occupie()
    {
        this.getStyleClass().clear();
        this.getStyleClass().add("tile");
        this.getStyleClass().add("occupied");
    }
}
