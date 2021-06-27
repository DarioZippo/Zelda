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

    public class Zelda extends Application{       
        
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
            
            scene.setOnKeyPressed((KeyEvent ev) -> {
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
        }
    }