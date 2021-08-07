package zelda;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

class TurnHandler extends Thread{
    private boolean characterTurn;
    private int enemyIndex;
    private int inputCounter;
    private int enemyTurnCounter;
    
    private Zelda zelda;
    private GameModel gameModel;
    private GameView gameView;
    
    TurnHandler(GameModel gameModel, GameView gameView, Zelda zelda){
        super();
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.zelda = zelda;
        
        characterTurn = true;
        enemyIndex = 0;
        inputCounter = 0;
        enemyTurnCounter = 0;
    }
    
    TurnHandler(GameModel gameModel, GameView gameView, Zelda zelda, CacheData cacheData){
        super();
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.zelda = zelda;
        
        characterTurn = true;
        if(cacheData.gameCacheData.getCharacterTurn() == false){
            inputCounter = 0;
        }
        else{
            inputCounter = cacheData.gameCacheData.getInputCounter();
        }
        
        enemyIndex = 0;
        enemyTurnCounter = 0;
        /*
        characterTurn = cacheData.gameCacheData.getCharacterTurn();
        if(characterTurn == true){
            zelda.listen = true;
        }
        else{
            zelda.listen = false;
        }
        enemyIndex = cacheData.gameCacheData.getEnemyIndex();
        
        inputCounter = cacheData.gameCacheData.getInputCounter();
        enemyTurnCounter = cacheData.gameCacheData.getEnemyTurnCounter();
        
        gameView.endedAnimationCurrentEnemy = true;
        */
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
    
    public synchronized void run(){
        while(true){
            //System.out.println("characterTurn: " + characterTurn + " endedAnimationEnemies " + gameView.endedAnimationEnemies + " endedAnimationCurrentEnemy " + gameView.endedAnimationCurrentEnemy);
            try {
                if(characterTurn == true){
                    if(gameView.endedAnimationCharacter == true){
                        Platform.runLater(() ->{
                            gameView.update(gameModel);
                        });
                        inputCounter++;
                        if(inputCounter == 3){
                            characterTurn = false;
                            Platform.runLater(() ->{
                                gameModel.EnemiesTurn(enemyIndex);
                            });
                        }
                        else
                            zelda.listen = true;
                    }
                }
                else{
                    if(gameView.endedAnimationEnemies == true){
                        System.out.println("endedAnimationEnemies");
                        Platform.runLater(() ->{
                            gameView.update(gameModel);
                        });
                        enemyIndex = 0;
                        enemyTurnCounter++;
                        if(enemyTurnCounter == 2){
                            enemyTurnCounter = 0;
                            characterTurn = true;
                            Platform.runLater(() ->{
                                gameModel.updateCoolDown();
                            });
                            inputCounter = 0;
                            zelda.listen = true;
                        }
                        else
                            Platform.runLater(() ->{
                                gameModel.EnemiesTurn(enemyIndex);
                            });
                    }
                    else if(gameView.endedAnimationCurrentEnemy == true){
                        System.out.println("endedAnimationCurrentEnemy");
                        Platform.runLater(() ->{
                            gameView.update(gameModel);
                        });

                        if(gameModel.isEnded() == true){
                            System.out.println("Game Over");
                            Platform.runLater(() ->{
                                zelda.endGame();
                            });
                            break;
                        }

                        else{
                            enemyIndex++;
                            Platform.runLater(() ->{
                                gameModel.EnemiesTurn(enemyIndex);
                            });
                        }
                    }
                }
                //System.out.println("Dormo");
                sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Zelda.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //}
    }
}
