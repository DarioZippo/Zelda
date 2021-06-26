package zelda;

import static zelda.GameUtils.*;

class GameEnemy{
    
    private int currentPositionX;
    private int currentPositionY;   
    private GameModel gameModel;
    
    private Command direction;
    
    GameEnemy(final int coordinateX, final int coordinateY, Command direction, GameModel gameModel) {
        currentPositionX = coordinateX;
        currentPositionY = coordinateY;
        this.direction = direction;
        
        this.gameModel = gameModel;
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
                System.out.println("Protagonista in posizione x: " + x + " y: " + y);
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
    
    public void showPosition(){
        System.out.println("Enemy " + "x: " + currentPositionX + " y: " + currentPositionY);
    }
}
