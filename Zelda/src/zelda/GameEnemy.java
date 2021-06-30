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
    
    public int getX(){
        return currentPositionX;
    }
    
    public int getY(){
        return currentPositionY;
    }
    
    public Command getDirection(){
        return direction;
    }
    
    private Command chooseDirection(){
        Command direction;
        
        int x = gameModel.getCharacter().getX();
        int y = gameModel.getCharacter().getY();
        
        int distX = Math.abs(x - this.currentPositionX);
        int distY = Math.abs(y - this.currentPositionY);
        
        if(distX >= distY){
            if(this.currentPositionX - x > 0){
                direction = Command.Left;
            }
            else{
                direction = Command.Right;
            }
        }
        else{
            if(this.currentPositionY - y > 0){
                direction = Command.Up;
            }
            else{
                direction = Command.Down;
            }
        }
        
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
                System.out.println("Protagonista in posizione x: " + x + " y: " + y);
                return false;
            }
            gameModel.getTile(currentPositionX, currentPositionY).free();//Libero    
            currentPositionX = x;
            currentPositionY = y;
            gameModel.getTile(currentPositionX, currentPositionY).occupieEnemy(this);//Occupo 
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
    
    public GameModel getGameModel(){
        return this.gameModel;
    }
    
    private boolean attackableCharacter(){
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
            if(gameModel.getTile(x, y).occupiedCharacter == true){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }
    
    public void attack(){
        gameModel.attackedCharacter();
    }
    
    public void turn(GameView gameView){
        if(attackableCharacter() == true){
            attack();
            System.out.println("Attaccabile");
            gameView.attackAnimation(this, gameModel);
        }
        else{
            System.out.println("Non attaccabile");
            boolean mooved = false;
            Command direction = chooseDirection();
            mooved = move(direction);
            if(mooved == true){
               gameView.moveAnimation(this, gameModel);
            }
            else if(attackableCharacter() == true){
                attack();
                System.out.println("Attaccabile");
                gameView.attackAnimation(this, gameModel);
            }
            else{
                gameView.endedAnimationCurrentEnemy = true;
                gameView.attackAnimation(this, gameModel);
            }
        }
    }
}
