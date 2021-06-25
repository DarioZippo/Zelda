package zelda;

import java.util.*;
import static zelda.GameUtils.*;

class GameCharacter{
    
    private int currentPositionX;
    private int currentPositionY;   
    private GameModel gameModel;
    
    private Command direction;
    
    GameCharacter(final int coordinateX, final int coordinateY, GameModel gameModel) {
        currentPositionX = coordinateX;
        currentPositionY = coordinateY;
        this.gameModel = gameModel;
        
        direction = Command.Down;
    }
    
    public boolean move(Command direction){
        int x = currentPositionX, y = currentPositionY;
        switch(direction){
            case Left:
                x--;
                break;
            case Right:
                x++;
                break;
            case Up:
                y--;
                break;
            case Down:
                y++;
                break;
        }
        this.direction = direction;
        boolean inMap = checkPosition(x, y);
        if(inMap == true){
            if(gameModel.getTile(x, y).occupied == true){
                System.out.println("Nemico in posizione x: " + x + " y: " + y);
                return false;
            }
            gameModel.getTile(currentPositionX, currentPositionY).changeState();//Libero    
            currentPositionX = x;
            currentPositionY = y;
            gameModel.getTile(currentPositionX, currentPositionY).changeState();//Occupo 
            return true; //confermo lo spostamento
        }
        else{
            System.out.println("Fuori mappa in posizione x: " + x + " y: " + y);
            return false;
        }
    }
    
    public void attack(){
        int x = currentPositionX, y = currentPositionY;
        switch(direction){
            case Left:
                x--;
                break;
            case Right:
                x++;
                break;
            case Up:
                y--;
                break;
            case Down:
                y++;
                break;
        }
        boolean legal = checkPosition(x, y);
        if(legal == true){
            GameTile attacked = gameModel.getTile(x, y);
            if(attacked.occupied == true){
                System.out.println("Colpito in posizione x: " + x + " y: " + y);
            }
            if(attacked.occupied == false){
                System.out.println("Mancato in posizione x: " + x + " y: " + y);
            }
        }
        else{
            System.out.println("Fuori mappa in posizione x: " + x + " y: " + y);
        }
    }
    
    public void showPosition(){
        System.out.println("x: " + currentPositionX + " y: " + currentPositionY);
    }
}