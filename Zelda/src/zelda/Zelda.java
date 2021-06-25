package zelda;

import java.net.URL;
import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.stage.*;    
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import static zelda.GameUtils.*;

    public class Zelda extends Application{       
        
        private Character link;
        
        private GraficModel gameView;
        private GameModel gameModel;
        private KeyAssociation keyAssociation;
        
        public static Group tileGroup = new Group();
        private Group pieceGroup = new Group();
        
        public void start(Stage primaryStage) throws Exception {            
            gameModel = new GameModel();
            gameView = new GraficModel(gameModel.getBoard());
            
            //keyAssociation = new KeyAssociation(39, 37);
            
            Scene scene = new Scene(gameView.graficContent);
            
            URL url = this.getClass().getResource("Style.css");
            if (url == null) {
                System.out.println("Resource not found. Aborting.");
            }
            String css = url.toExternalForm(); 
            scene.getStylesheets().add(css);
            
            scene.setOnKeyPressed((KeyEvent ev) -> {
                this.playerInput(ev.getCode());
            });
            
            primaryStage.setTitle("Zelda");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        
        private void playerInput(KeyCode key) {
            
            System.out.println("Command: " + key);
            
            //if (this.keyAssociation != null) {
                if (key == KeyCode.RIGHT){ //this.keyAssociation.rightKey) {
                    gameModel.executePlayerCommand(Command.Right);
                    gameView.executePlayerCommand(Command.Right);
                    return;
                }
                if (key == KeyCode.LEFT){ //key == this.keyAssociation.leftKey) {
                    gameModel.executePlayerCommand(Command.Left);
                    gameView.executePlayerCommand(Command.Left);
                    return;
                }
                if (key == KeyCode.UP){ //key == this.keyAssociation.leftKey) {
                    gameModel.executePlayerCommand(Command.Up);
                    gameView.executePlayerCommand(Command.Up);
                    return;
                }
                if (key == KeyCode.DOWN){ //key == this.keyAssociation.leftKey) {
                    gameModel.executePlayerCommand(Command.Down);
                    gameView.executePlayerCommand(Command.Down);
                    return;
                }
                if (key == KeyCode.Z){ //key == this.keyAssociation.swordKey) {
                    this.gameModel.executePlayerCommand(Command.Sword);
                    return;
                }
            //}
            System.out.println("Sfigato");
        }
    }