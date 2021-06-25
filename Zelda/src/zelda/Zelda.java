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
        
        private GraficModel graficModel;
        private GameModel gameModel;
        private KeyAssociation keyAssociation;
        
        public static Group tileGroup = new Group();
        private Group pieceGroup = new Group();
        
        public void start(Stage primaryStage) throws Exception {            
            gameModel = new GameModel();
            graficModel = new GraficModel(gameModel.getBoard());
            
            //keyAssociation = new KeyAssociation(39, 37);
            
            Scene scene = new Scene(graficModel.graficContent);
            
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
            boolean done;
            //if (this.keyAssociation != null) {
                if (key == KeyCode.RIGHT){ //this.keyAssociation.rightKey) {
                    done = gameModel.executePlayerCommand(Command.Right);
                    if(done == true)
                        graficModel.executePlayerCommand(Command.Right);
                    return;
                }
                if (key == KeyCode.LEFT){ //key == this.keyAssociation.leftKey) {
                    done = gameModel.executePlayerCommand(Command.Left);
                    if(done == true)
                        graficModel.executePlayerCommand(Command.Left);
                    return;
                }
                if (key == KeyCode.UP){ //key == this.keyAssociation.leftKey) {
                    done = gameModel.executePlayerCommand(Command.Up);
                    if(done == true)
                        graficModel.executePlayerCommand(Command.Up);
                    return;
                }
                if (key == KeyCode.DOWN){ //key == this.keyAssociation.leftKey) {
                    done = gameModel.executePlayerCommand(Command.Down);
                    if(done == true)
                        graficModel.executePlayerCommand(Command.Down);
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