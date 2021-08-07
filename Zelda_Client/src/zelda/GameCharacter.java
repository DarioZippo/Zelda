package zelda;

import java.util.*;
import static zelda.GameUtils.*;

class GameCharacter{
    
    private int currentPositionX;
    private int currentPositionY;   
    private GameModel gameModel;
    
    private Command direction;
    
    GameCharacter(final int coordinateX, final int coordinateY, Command direction, GameModel gameModel) {
        currentPositionX = coordinateX;
        currentPositionY = coordinateY;
        this.gameModel = gameModel;
        
        this.direction = direction;
    }
    
    public int getX(){
        return currentPositionX;
    }
    
    public int getY(){
        return currentPositionY;
    }
    
    public Command getDirection(){
        return direction;
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
            gameModel.getTile(currentPositionX, currentPositionY).free();//Libero    
            currentPositionX = x;
            currentPositionY = y;
            gameModel.getTile(currentPositionX, currentPositionY).occupieCharacter(this);//Occupo 
            return true; //confermo lo spostamento
        }
        else{
            System.out.println("Fuori mappa in posizione x: " + x + " y: " + y);
            return false;
        }
    }
    
    public GameTile attack(){
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
        boolean inMap = checkPosition(x, y);
        if(inMap == true){
            GameTile attacked = gameModel.getTile(x, y);
            if(attacked.occupiedEnemy == true){
                System.out.println("Colpito in posizione x: " + x + " y: " + y);
                return attacked;//Gestici combattimento
            }
            if(attacked.occupiedEnemy == false){
                System.out.println("Mancato in posizione x: " + x + " y: " + y);
            }
        }
        else{
            System.out.println("Fuori mappa in posizione x: " + x + " y: " + y);
        }
        return null;
    }
    
    public ArrayList<GameTile> special(){
        int x = currentPositionX, y = currentPositionY;
        int up = y - 1, down = y + 1;
        int left = x - 1, right = x + 1;
        
        System.out.println("Link position: " + x + ", " + y);
        
        GameTile attacked = null;
        ArrayList<GameTile> attackedPeople = new ArrayList<GameTile>();
        
        boolean inMap = false;
        for(int i = 0; i < 4; i++){
            switch(i){
                case 0:
                    y--;
                    break;
                case 1:
                    x++;
                    break;
                case 2:
                    y++;
                    break;
                case 3:
                    x--;
                    break;
            }
            inMap = checkPosition(x, y);
            if(inMap == true){
                attacked = gameModel.getTile(x, y);
                if(attacked.occupiedEnemy == true){
                    System.out.println("Colpito in posizione x: " + x + " y: " + y);
                    attackedPeople.add(attacked); //Aggiungo una piastrella coinvolta
                }
                else{
                    System.out.println("Mancato in posizione x: " + x + " y: " + y);
                }
            }
            else{
                System.out.println("Fuori mappa in posizione x: " + x + " y: " + y);
            }
            x = currentPositionX; y = currentPositionY;
        }
        if(attackedPeople.size() == 0)
            return null;
        return attackedPeople;
    }
    
    public GameTile bow(){
        return gameModel.firstEncounterOnAxis(currentPositionX, currentPositionY, direction);
    }
    
    public void showPosition(){
        System.out.println("x: " + currentPositionX + " y: " + currentPositionY);
    }
}