package zelda;

import java.io.Serializable;

public class GameCacheData implements Serializable{
    private String user;
    private int lives;
    private int waveMagnitude;
    private int points;
    private int coolDown;
    private boolean readySpecial;
    private int arrows;
    
    private boolean characterTurn;
    private int enemyIndex;
    private int inputCounter;
    private int enemyTurnCounter;
    
    public GameCacheData(GameModel gameModel, TurnHandler turnHandler){
        this.user = gameModel.getUser();
        this.lives = gameModel.getLives();
        this.waveMagnitude = gameModel.getWaveMagnitude();
        this.points = gameModel.getPoints();
        
        this.arrows = gameModel.getArrows();
        this.coolDown = gameModel.getCoolDown();
        this.readySpecial = gameModel.getReadySpecial();
        
        characterTurn = turnHandler.getCharacterTurn();
        inputCounter = turnHandler.getInputCounter();
    }
    
    public String getUser(){
        return user;
    }
    
    public int getLives(){
        return lives;
    }
    
    public int getWaveMagnitude(){
        return waveMagnitude;
    }
    
    public int getPoints(){
        return points;
    }
    
    public boolean getCharacterTurn(){
        return characterTurn;
    }
    
    public int getEnemyIndex(){
        return enemyIndex;
    }
    
    public int getInputCounter(){
        return inputCounter;
    }
    
    public int getEnemyTurnCounter(){
        return enemyTurnCounter;
    }
    
    public int getArrows(){
        return arrows;
    }
    
    public int getCoolDown(){
        return coolDown;
    }
    
    public boolean getReadySpecial(){
        return readySpecial;
    }
}
