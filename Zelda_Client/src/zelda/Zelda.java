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
    private TextField specialTextField;
    private TextField bowTextField;
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
        
        loadSettings();
        
        listen = false;
        
        dbManager.caricaClientiDB();
        //dbManager.caricaClientiPredefiniti();
        /*
        Record record = new Record("MasterZi", 100);
        dbManager.registraClienteDB(record);
        */
        
        root = new Pane();
        
        root.getChildren().add(gameView.createContent(gameModel));
        root.getChildren().add(builder.createContent(gameModel, gameView) );
        
        loginButton = builder.getLoginButton();
        loginTextField = builder.getLoginTextField();
        specialTextField = builder.getKeyCTextField();
        bowTextField = builder.getBowTextField();
        ranking = builder.getRanking();
        
        LocalCacheOperations.ripristinaCache(loginTextField);
        gameView.setSpecialTextField(specialTextField);
        gameView.setBowTextField(bowTextField);
        
        loginButton.setOnAction((ActionEvent ev) -> {
            EventLoggerXML.recordEvent(EventLoggerXML.eventDescriptionButton);
            
            if(loginTextField.getText() != null && loginTextField.getText().isEmpty() == false){                
                gameModel.setUser(loginTextField.getText());
                
                EventLoggerXML.recordEvent(EventLoggerXML.eventDescriptionUsername);
                
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
        
        primaryStage.setOnCloseRequest((WindowEvent ev) -> {
            LocalCacheOperations.salvaCache(gameModel);
            EventLoggerXML.recordEvent(EventLoggerXML.eventDescriptionClose);
            Platform.exit();
        });
        
        primaryStage.setTitle("Zelda");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void loadSettings() {
        SettingsXML settingsXML = ReaderSettingsXML.readSettings();
        this.keyAssociation = settingsXML.keyAssociation;
        
        EventLoggerXML.setServerLogAddress(settingsXML.serverLogAddress.IPAddress, settingsXML.serverLogAddress.port);
        //OperazioniDatabase.impostaIndirizzoDatabase(impostazioniXml.indirizzoDatabase.indirizzoIP, impostazioniXml.indirizzoDatabase.porta);
        
        EventLoggerXML.recordEvent(EventLoggerXML.eventDescriptionStart);
        
        if (!settingsXML.keyAssociation.equals(ReaderSettingsXML.defaultSettings.keyAssociation)) {      //1
            String descrizioneEvento = EventLoggerXML.eventDescriptionKeyAssociation.replaceFirst(EventLoggerXML.placeholderDescription, settingsXML.keyAssociation.rightKey.getName());
            descrizioneEvento = descrizioneEvento.replaceFirst(EventLoggerXML.placeholderDescription, settingsXML.keyAssociation.leftKey.getName());
            descrizioneEvento = descrizioneEvento.replaceFirst(EventLoggerXML.placeholderDescription, settingsXML.keyAssociation.upKey.getName());
            descrizioneEvento = descrizioneEvento.replaceFirst(EventLoggerXML.placeholderDescription, settingsXML.keyAssociation.downKey.getName());
            descrizioneEvento = descrizioneEvento.replaceFirst(EventLoggerXML.placeholderDescription, settingsXML.keyAssociation.swordKey.getName());
            EventLoggerXML.recordEvent(descrizioneEvento);
        }
    }
    
    private synchronized void playerInput(KeyCode key) throws InterruptedException {
        if(listen == true){
            listen = false;
            System.out.println("Command: " + key);
            boolean mooved; //per il momento è inutile
            boolean hasHit; //per il momento è inutile
            if (this.keyAssociation != null) {
                if (key == this.keyAssociation.rightKey) {
                    mooved = gameModel.executePlayerCommand(Command.Right);
                    //gameView.executePlayerCommand(Command.Right, mooved);
                    return;
                }
                if (key == this.keyAssociation.leftKey) {
                    mooved = gameModel.executePlayerCommand(Command.Left);
                    //gameView.executePlayerCommand(Command.Left, mooved);
                    return;
                }
                if (key == this.keyAssociation.upKey) {
                    mooved = gameModel.executePlayerCommand(Command.Up);
                    //gameView.executePlayerCommand(Command.Up, mooved);
                    return;
                }
                if (key == this.keyAssociation.downKey) {
                    mooved = gameModel.executePlayerCommand(Command.Down);
                    //gameView.executePlayerCommand(Command.Down, mooved);
                    return;
                }
                if (key == this.keyAssociation.swordKey) {
                    hasHit = gameModel.executePlayerCommand(Command.Sword);
                    return;
                }
                if (key == this.keyAssociation.specialKey) {
                    hasHit = gameModel.executePlayerCommand(Command.Special);
                    return;
                }
                if (key == this.keyAssociation.arrowKey) {
                    hasHit = gameModel.executePlayerCommand(Command.Arrow);
                    return;
                }
            }
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