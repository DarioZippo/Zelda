package zelda;

import static zelda.GameUtils.*;

class GameEnemy{
    
    private int x;
    private int y;   
    private GameModel gameModel;
    
    private Command direction;
    
    GameEnemy(final int coordinateX, final int coordinateY, Command direction, GameModel gameModel) {
        x = coordinateX;
        y = coordinateY;
        this.direction = direction;
        
        this.gameModel = gameModel;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public Command getDirection(){
        return direction;
    }
    
    private Command chooseDirection(){
        Command direction;
        
        int x = gameModel.getCharacter().getX();
        int y = gameModel.getCharacter().getY();
        
        int distX = Math.abs(x - this.x);
        int distY = Math.abs(y - this.y);
        
        if(distX >= distY){
            if(this.x - x > 0){
                direction = Command.Left;
            }
            else{
                direction = Command.Right;
            }
        }
        else{
            if(this.y - y > 0){
                direction = Command.Up;
            }
            else{
                direction = Command.Down;
            }
        }
        
        return direction;
    }   
    
    public boolean move(Command direction){
        int x = this.x, y = this.y;
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
            gameModel.getTile(this.x, this.y).free();//Libero    
            this.x = x;
            this.y = y;
            gameModel.getTile(this.x, this.y).occupieEnemy(this);//Occupo 
            return true; //confermo lo spostamento
        }
        else{
            System.out.println("Fuori mappa in posizione x: " + x + " y: " + y);
            return false;
        }
    }
    
    public void showPosition(){
        System.out.println("Enemy " + "x: " + x + " y: " + y);
    }
    
    private boolean attackableCharacter(){
        int x = this.x, y = this.y;
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
            }
        }
    }
}
