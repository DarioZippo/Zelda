package zelda;

import java.net.URL;
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
            boolean mooved;
            //if (this.keyAssociation != null) {
                if (key == KeyCode.RIGHT){ //this.keyAssociation.rightKey) {
                    mooved = gameModel.executePlayerCommand(Command.Right);
                    graficModel.executePlayerCommand(Command.Right, mooved);
                    return;
                }
                if (key == KeyCode.LEFT){ //key == this.keyAssociation.leftKey) {
                    mooved = gameModel.executePlayerCommand(Command.Left);
                    graficModel.executePlayerCommand(Command.Left, mooved);
                    return;
                }
                if (key == KeyCode.UP){ //key == this.keyAssociation.leftKey) {
                    mooved = gameModel.executePlayerCommand(Command.Up);
                    graficModel.executePlayerCommand(Command.Up, mooved);
                    return;
                }
                if (key == KeyCode.DOWN){ //key == this.keyAssociation.leftKey) {
                    mooved = gameModel.executePlayerCommand(Command.Down);
                    graficModel.executePlayerCommand(Command.Down, mooved);
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