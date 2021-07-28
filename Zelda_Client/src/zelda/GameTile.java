package zelda;

import java.util.*;

public class GameTile {
    public int coordinateX;
    public int coordinateY;
    
    public boolean occupied;
    public boolean occupiedCharacter;
    public boolean occupiedEnemy;
    
    private GameCharacter occupierCharacter;
    private GameEnemy occupierEnemy;
    
    public GameTile(final int coordinateX, final int coordinateY, final boolean occupied) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.occupied = occupied;
    }   
    
    public void occupieCharacter(GameCharacter occupier){
        occupied = true;
        occupiedCharacter = true;
        occupierCharacter = occupier;
    }
    
    public void occupieEnemy(GameEnemy occupier){
        occupied = true;
        occupiedEnemy = true;
        occupierEnemy = occupier;
    }
    
    public void free(){
        occupied = occupiedEnemy = occupiedCharacter = false;
        occupierCharacter = null;
        occupierEnemy = null;
    }
    
    public GameCharacter getOccupierCharacter(){
        return occupierCharacter;
    }
    
    public GameEnemy getOccupierEnemy(){
        return occupierEnemy;
    }
}
