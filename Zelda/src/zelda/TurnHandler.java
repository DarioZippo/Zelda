package zelda;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

class TurnHandler extends Thread{
    public boolean turnCharacter;
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
        
        turnCharacter = true;
        enemyIndex = 0;
        inputCounter = 0;
        enemyTurnCounter = 0;
    }
    public synchronized void run(){
        zelda.listen = true;
        while(true){
            try {
                if(turnCharacter == true){
                    if(gameView.endedAnimationCharacter == true){
                        Platform.runLater(() ->{
                            gameView.update(gameModel);
                        });
                        inputCounter++;
                        if(inputCounter == 3){
                            turnCharacter = false;
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
                        Platform.runLater(() ->{
                            gameView.update(gameModel);
                        });
                        enemyIndex = 0;
                        enemyTurnCounter++;
                        if(enemyTurnCounter == 2){
                            enemyTurnCounter = 0;
                            turnCharacter = true;
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

                        if(gameModel.endGame == true){
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
