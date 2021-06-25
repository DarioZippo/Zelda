package zelda;

import java.util.*;

public class Tile {
    public int coordinateX;
    public int coordinateY;
    public boolean occupied;
    
    public Tile(final int coordinateX, final int coordinateY, final boolean occupied) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.occupied = occupied;
    }   
    
    public void changeState()
    {
        this.occupied = !this.occupied;
    }
}
