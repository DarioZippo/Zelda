package zelda;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.stage.*;    
import javafx.scene.input.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import static zelda.GameUtils.*;
    
class TurnHandler extends Thread{
    public boolean turnCharacter;
    private int enemyIndex;
    private int inputCounter;
    
    private GameModel gameModel;
    private GameView gameView;
    
    TurnHandler(GameModel gameModel, GameView gameView){
        super();
        this.gameModel = gameModel;
        this.gameView = gameView;
        
        turnCharacter = true;
        enemyIndex = 0;
        inputCounter = 0;
        
        Zelda.listen = true;
    }
    public void run(){
        while(true){
            try {
                if(turnCharacter == true){
                    if(gameView.endedAnimationCharacter == true){
                        Platform.runLater(() ->{
                            gameView.update(gameModel);
                        });
                        inputCounter++;
                        if(inputCounter == 2){
                            turnCharacter = false;
                            Platform.runLater(() ->{
                                gameModel.EnemiesTurn(enemyIndex);
                            });
                        }
                        else
                            Zelda.listen = true;
                    }
                }
                else{
                    if(gameView.endedAnimationEnemies == true){
                        Platform.runLater(() ->{
                            gameView.update(gameModel);
                        });
                        enemyIndex = 0;
                        turnCharacter = true;
                        inputCounter = 0;
                        Zelda.listen = true;
                    }
                    else if(gameView.endedAnimationCurrentEnemy == true){
                        System.out.println("endedAnimationCurrentEnemy");
                        Platform.runLater(() ->{
                            gameView.update(gameModel);
                        });
                        
                        if(gameModel.endGame == true){
                            System.out.println("Game Over");
                            Zelda.listen = false;
                            gameModel.endGame();
                            Platform.runLater(() ->{
                                gameView.endGame();
                            });
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
                this.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Zelda.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

public class Zelda extends Application{       
    static boolean listen;
    
    private Character link;
    
    private GameView gameView;
    private GameModel gameModel;
    private KeyAssociation keyAssociation;
    
    public static Group tileGroup = new Group();
    
    public void start(Stage primaryStage) throws Exception {            
        gameView = new GameView();
        gameModel = new GameModel(gameView);
        
        //keyAssociation = new KeyAssociation(39, 37);
        
        Scene scene = new Scene(gameView.showBoard(gameModel));
        
        URL url = this.getClass().getResource("Style.css");
        if (url == null) {
            System.out.println("Resource not found. Aborting.");
        }
        String css = url.toExternalForm(); 
        scene.getStylesheets().add(css);
        
        TurnHandler t1 = new TurnHandler(gameModel, gameView);
        t1.setDaemon(true);
        t1.start();
        
        scene.setOnKeyReleased((KeyEvent ev) -> {
            try {
                this.playerInput(ev.getCode());
            } catch (InterruptedException ex) {
                Logger.getLogger(Zelda.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        primaryStage.setTitle("Zelda");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        gameModel.spawn();
    }
    
    private synchronized void playerInput(KeyCode key) throws InterruptedException {
        if(listen == true){
            listen = false;
            System.out.println("Command: " + key);
            boolean mooved; //per il momento è inutile
            boolean hasHit; //per il momento è inutile
            //if (this.keyAssociation != null) {
                if (key == KeyCode.RIGHT){ //this.keyAssociation.rightKey) {
                    mooved = gameModel.executePlayerCommand(Command.Right);
                    //gameView.executePlayerCommand(Command.Right, mooved);
                    return;
                }
                if (key == KeyCode.LEFT){ //key == this.keyAssociation.leftKey) {
                    mooved = gameModel.executePlayerCommand(Command.Left);
                    //gameView.executePlayerCommand(Command.Left, mooved);
                    return;
                }
                if (key == KeyCode.UP){ //key == this.keyAssociation.leftKey) {
                    mooved = gameModel.executePlayerCommand(Command.Up);
                    //gameView.executePlayerCommand(Command.Up, mooved);
                    return;
                }
                if (key == KeyCode.DOWN){ //key == this.keyAssociation.leftKey) {
                    mooved = gameModel.executePlayerCommand(Command.Down);
                    //gameView.executePlayerCommand(Command.Down, mooved);
                    return;
                }
                if (key == KeyCode.Z){ //key == this.keyAssociation.swordKey) {
                    hasHit = gameModel.executePlayerCommand(Command.Sword);
                    return;
                }
            //}
            System.out.println("Sfigato");
            listen = true;
        }
        else
            System.out.println("Aspetta");
    }
}