package zelda;

import java.util.*;

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
        //Condizione per spostarsi
        gameModel.getTile(currentPositionX, currentPositionY).changeState();
        switch(direction){
            case Left:
                currentPositionX--;
                break;
            case Right:
                currentPositionX++;
                break;
            case Up:
                currentPositionY--;
                break;
            case Down:
                currentPositionY++;
                break;
        }
        this.direction = direction;
        return true; //confermo lo spostamento
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
        
        GameTile attacked = gameModel.getTile(x, y);
        if(attacked.occupied == true){
            System.out.println("Colpito in posizione x: " + x + " y: " + y);
        }
        if(attacked.occupied == false){
            System.out.println("Mancato in posizione x: " + x + " y: " + y);
        }
    }
    
    public void showPosition(){
        System.out.println("x: " + currentPositionX + " y: " + currentPositionY);
    }
}