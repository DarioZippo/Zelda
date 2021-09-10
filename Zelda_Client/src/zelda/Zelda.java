package zelda;

import java.net.URL;
import java.util.logging.*;
import javafx.application.*;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;    
import javafx.scene.input.*;
import javafx.scene.layout.*;

public class Zelda extends Application{       
    public boolean listen;
    
    private Pane root;
    private TextField specialTextField;
    private TextField bowTextField;
    private Button loginButton;
    private TextField loginTextField;
    private TableView<Record> ranking;
    
    private InterfaceBuilder interfaceBuilder;
    private GameView gameView;
    private GameModel gameModel;
    private DBManager dbManager;
    private KeyAssociation keyAssociation;
    
    public static ObservableList<Record> records;
    public static Group tileGroup = new Group();
    
    private TurnHandler t;
    
    public void start(Stage primaryStage) throws Exception {   
        interfaceBuilder = new InterfaceBuilder();
        gameView = new GameView();
        gameModel = new GameModel(gameView);
        
        loadSettings();
        
        listen = false;
        
        dbManager.loadRecordsDB();
        
        root = new Pane();
        
        root.getChildren().add(gameView.createContent(gameModel));
        root.getChildren().add(interfaceBuilder.createContent(gameView) );
        
        loginButton = interfaceBuilder.getLoginButton();
        loginTextField = interfaceBuilder.getLoginTextField();
        specialTextField = interfaceBuilder.getSpecialTextField();
        bowTextField = interfaceBuilder.getBowTextField();
        ranking = interfaceBuilder.getRanking();
        
        gameView.setSpecialTextField(specialTextField);
        gameView.setBowTextField(bowTextField);
        
        LocalCacheOperations.restoreCache(this);
        
        loginButton.setOnAction((ActionEvent ev) -> {
            EventLoggerXML.recordEvent(EventLoggerXML.eventDescriptionButton);
            
            if(loginTextField.getText() != null && loginTextField.getText().isEmpty() == false){                
                gameModel.setUser(loginTextField.getText());
                
                EventLoggerXML.recordEvent(EventLoggerXML.eventDescriptionUsername);
                
                gameModel.start();

                t = new TurnHandler(gameModel, gameView, this);
                t.setDaemon(true);
                t.start(); 
                
                listen = true;
                
                loginTextField.setDisable(true);
                loginButton.setDisable(true);
            }
        });
        
        Scene scene = new Scene(root);
        
        URL url = this.getClass().getResource("Style.css");
        if (url == null) {
            System.out.println("Resource not found");
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
            LocalCacheOperations.saveCache(gameModel, t);
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
        dbManager = new DBManager(settingsXML.dbAddress.IPAddress, settingsXML.dbAddress.port, settingsXML.dbUsername, settingsXML.dbPassword);
        
        EventLoggerXML.recordEvent(EventLoggerXML.eventDescriptionStart);
        
        if (!settingsXML.keyAssociation.equals(ReaderSettingsXML.defaultSettings.keyAssociation)) {   
            String eventDescription = EventLoggerXML.eventDescriptionKeyAssociation.replaceFirst(EventLoggerXML.placeholderDescription, settingsXML.keyAssociation.rightKey.getName());
            eventDescription = eventDescription.replaceFirst(EventLoggerXML.placeholderDescription, settingsXML.keyAssociation.leftKey.getName());
            eventDescription = eventDescription.replaceFirst(EventLoggerXML.placeholderDescription, settingsXML.keyAssociation.upKey.getName());
            eventDescription = eventDescription.replaceFirst(EventLoggerXML.placeholderDescription, settingsXML.keyAssociation.downKey.getName());
            eventDescription = eventDescription.replaceFirst(EventLoggerXML.placeholderDescription, settingsXML.keyAssociation.swordKey.getName());
            eventDescription = eventDescription.replaceFirst(EventLoggerXML.placeholderDescription, settingsXML.keyAssociation.specialKey.getName());
            eventDescription = eventDescription.replaceFirst(EventLoggerXML.placeholderDescription, settingsXML.keyAssociation.bowKey.getName());
            EventLoggerXML.recordEvent(eventDescription);
        }
    }
    
    private synchronized void playerInput(KeyCode key) throws InterruptedException {
        if(listen == true){
            listen = false;
            //System.out.println("Command: " + key);
            boolean mooved;
            boolean hasHit;
            if (this.keyAssociation != null) {
                if (key == this.keyAssociation.rightKey) {
                    mooved = gameModel.executePlayerCommand(Command.Right);
                    return;
                }
                if (key == this.keyAssociation.leftKey) {
                    mooved = gameModel.executePlayerCommand(Command.Left);
                    return;
                }
                if (key == this.keyAssociation.upKey) {
                    mooved = gameModel.executePlayerCommand(Command.Up);
                    return;
                }
                if (key == this.keyAssociation.downKey) {
                    mooved = gameModel.executePlayerCommand(Command.Down);
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
                if (key == this.keyAssociation.bowKey) {
                    hasHit = gameModel.executePlayerCommand(Command.Bow);
                    return;
                }
            }
            //System.out.println("Non è un comando");
            listen = true;
        }
        /*
        else
            System.out.println("Aspetta");
        */
    }
    
    public void endGame(){
        listen = false;
        
        Record record = new Record(gameModel.getUser(), gameModel.getPoints());
        dbManager.registerRecordDB(record);
        ranking.setItems(records);
        
        gameModel.endGame();
        Platform.runLater(() ->{
            gameView.endGame();
        }); 
        
        loginTextField.setDisable(false);
        loginButton.setDisable(false);
    }
    
    public void rebuildFromCache(CacheData cacheData){
        gameModel.rebuildFromCache(cacheData);        
                
        loginTextField.setText(cacheData.gameCacheData.getUser());
        loginTextField.setDisable(true);
        loginButton.setDisable(true);
        
        t = new TurnHandler(gameModel, gameView, this, cacheData);
        t.setDaemon(true);
        t.start(); 
        
        listen = true;
    }
}