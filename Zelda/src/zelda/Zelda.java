package zelda;

import java.awt.Transparency;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.*;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.*;    
import javafx.scene.input.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.*;
import static zelda.GameUtils.*;

public class Zelda extends Application{       
    public boolean listen;
    
    private Character link;
    
    private Pane root;
    private Button loginButton;
    private TextField loginTextField;
    private TableView<Record> ranking;
    
    private Builder builder;
    private GameView gameView;
    private GameModel gameModel;
    private DBManager dbManager;
    private KeyAssociation keyAssociation;
    
    public static ObservableList<Record> records;
    public static Group tileGroup = new Group();
    
    public void start(Stage primaryStage) throws Exception {   
        builder = new Builder();
        gameView = new GameView();
        gameModel = new GameModel(gameView);
        dbManager = new DBManager();
        
        listen = false;
        
        dbManager.caricaClientiDB();
        //dbManager.caricaClientiPredefiniti();
        /*
        Record record = new Record("MasterZi", 100);
        dbManager.registraClienteDB(record);
        */
        //keyAssociation = new KeyAssociation(39, 37);
        
        root = new Pane();
        
        root.getChildren().add(gameView.createContent(gameModel));
        root.getChildren().add(builder.createContent(gameModel, gameView) );
        
        loginButton = builder.getLoginButton();
        loginTextField = builder.getLoginTextField();
        ranking = builder.getRanking();
        
        loginButton.setOnAction((ActionEvent ev) -> {
            if(loginTextField.getText() != null && loginTextField.getText().isEmpty() == false){
                gameModel.setUser(loginTextField.getText());
                gameModel.start();

                TurnHandler t = new TurnHandler(gameModel, gameView, this);
                t.setDaemon(true);
                t.start(); 
                
                loginTextField.setDisable(true);
                loginButton.setDisable(true);
            }
        });
        
        Scene scene = new Scene(root);
        
        URL url = this.getClass().getResource("Style.css");
        if (url == null) {
            System.out.println("Resource not found. Aborting.");
        }
        String css = url.toExternalForm(); 
        scene.getStylesheets().add(css);
        
        scene.setOnKeyReleased((KeyEvent ev) -> {
            try {
                this.playerInput(ev.getCode());
                ev.consume();
            } catch (InterruptedException ex) {
                Logger.getLogger(Zelda.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        scene.setOnMousePressed(event -> {
            if (!ranking.equals(event.getSource())) {
                ranking.getParent().requestFocus();
            }
            if (!loginTextField.equals(event.getSource())) {
                loginTextField.getParent().requestFocus();
            }
        });
        
        primaryStage.setTitle("Zelda");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        //gameModel.spawn();
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
    
    public void endGame(){
        listen = false;
        Record record = new Record(gameModel.getUser(), gameModel.getPoints());
        dbManager.registraClienteDB(record);
        ranking.setItems(records);
        gameModel.endGame();
        Platform.runLater(() ->{
            gameView.endGame();
        }); 
        loginTextField.setDisable(false);
        loginButton.setDisable(false);
    }
}