package zelda;

import java.io.File;
import java.util.ArrayList;
import javafx.animation.*;
import javafx.event.*;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.*;
import javafx.scene.transform.*;
import javafx.stage.Screen;
import javafx.util.Duration;
import static zelda.GameUtils.HEIGHT;
import static zelda.GameUtils.TILE_SIZE;
import static zelda.GameUtils.WIDTH;
import static zelda.GameUtils.randomDirection;

public class GameView {
    private GraficTile[][] board;
    //private GraficCharacter link;
    
    //private ArrayList<GraficEnemy> enemies;
    
    private Pane root;
    private Pane boardWrapper;
    private ArrayList<ImageView> hearts;
    private Button loginButton;
    private TableView<Record> ranking;
    
    public boolean endedAnimationCharacter;
    public boolean endedAnimationEnemies;
    public boolean endedAnimationCurrentEnemy;
    
    public GameView(){
        endedAnimationCharacter = false;
        endedAnimationEnemies = false;
        endedAnimationCurrentEnemy = false;
        //enemies = new ArrayList<GraficEnemy>();
    }
    
    public Parent createContent(GameModel gameModel){
        root = new Pane();
        createBoard(gameModel);
        createCharacterStat(gameModel);
        createKeyTutorial(gameModel);
        createLogin(gameModel);
        createRanking(gameModel);
        
        return root;
    }
        
    private void createBoard(GameModel gameModel) {
        boardWrapper = new Pane();
        
        int width = gameModel.getBoard().length;
        int height = gameModel.getBoard()[0].length;
        
        board = new GraficTile[width][height];
        
        //root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        boardWrapper.getChildren().addAll(Zelda.tileGroup);
        
        System.out.println("Altezza: " + HEIGHT + " larghezza:" + WIDTH);
        
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                boolean currentPositionState = gameModel.getTile(x, y).occupied;
                
                GraficTile gTile = new GraficTile(x, y, currentPositionState);
                this.board[x][y] = gTile;
                
                Zelda.tileGroup.getChildren().add(gTile);
            }
        }
        boardWrapper.getStyleClass().add("background");
        root.getChildren().add(boardWrapper);
    }
    
    private void createCharacterStat(GameModel gameModel){
        Pane characterStat = new Pane();
        characterStat.setLayoutY(600);
        //characterStat.setPrefHeight(150.0); 
        //characterStat.setPrefWidth(300.0);
        characterStat.getStyleClass().add("characterStat");
        
        Image linkHeadImage = new Image("file:myFiles/img/linkHead.png");
        ImageView linkHead = new ImageView(linkHeadImage);
        linkHead.setFitHeight(55.0);
        linkHead.setFitWidth(60.0);
        linkHead.setLayoutX(122); 
        linkHead.setLayoutY(14);
        linkHead.setId("LinkHead");
        
        characterStat.getChildren().add(linkHead);
        
        Image fullHeartImage = new Image("file:myFiles/img/fullHeart.png");
        
        hearts = new ArrayList<ImageView>(); 
        
        ImageView heart0 = new ImageView(fullHeartImage);
        heart0.setFitHeight(50.0);
        heart0.setFitWidth(50.0);
        heart0.setLayoutX(75); 
        heart0.setLayoutY(86);
        heart0.getStyleClass().add("heart");
        //heart0.setId("Heart0");
        
        hearts.add(heart0);
        
        ImageView heart1 = new ImageView(fullHeartImage);
        heart1.setFitHeight(50.0);
        heart1.setFitWidth(50.0);
        heart1.setLayoutX(125); 
        heart1.setLayoutY(86);
        heart1.getStyleClass().add("heart");
        //heart1.setId("Heart1");
        
        hearts.add(heart1);
        
        ImageView heart2 = new ImageView(fullHeartImage);
        heart2.setFitHeight(50.0);
        heart2.setFitWidth(50.0);
        heart2.setLayoutX(176); 
        heart2.setLayoutY(86);
        heart2.getStyleClass().add("heart");
        //heart2.setId("Heart2");
        
        hearts.add(heart2);
        characterStat.getChildren().addAll(hearts);
        
        root.getChildren().add(characterStat);
    }
    
    private void createKeyTutorial(GameModel gameModel){
        Pane keyTutorial = new Pane();
        keyTutorial.setLayoutX(300);
        keyTutorial.setLayoutY(600);
        //keyTutorial.setPrefHeight(150.0); 
        //keyTutorial.setPrefWidth(500.0);
        keyTutorial.getStyleClass().add("keyTutorial");
        
        Image keyDownImage = new Image("file:myFiles/img/keyDown.png");
        ImageView keyDown = new ImageView(keyDownImage);
        keyDown.setFitHeight(35.0);
        keyDown.setFitWidth(35.0);
        keyDown.setLayoutX(233); 
        keyDown.setLayoutY(111);
        
        keyTutorial.getChildren().add(keyDown);
        
        Image keyUpImage = new Image("file:myFiles/img/keyUp.png");
        ImageView keyUp = new ImageView(keyUpImage);
        keyUp.setFitHeight(35.0);
        keyUp.setFitWidth(35.0);
        keyUp.setLayoutX(233); 
        keyUp.setLayoutY(76);
        
        keyTutorial.getChildren().add(keyUp);
        
        Image keyRightImage = new Image("file:myFiles/img/keyRight.png");
        ImageView keyRight = new ImageView(keyRightImage);
        keyRight.setFitHeight(35.0);
        keyRight.setFitWidth(35.0);
        keyRight.setLayoutX(268); 
        keyRight.setLayoutY(111);
        
        keyTutorial.getChildren().add(keyRight);
        
        Image keyLeftImage = new Image("file:myFiles/img/keyLeft.png");
        ImageView keyLeft = new ImageView(keyLeftImage);
        keyLeft.setFitHeight(35.0);
        keyLeft.setFitWidth(35.0);
        keyLeft.setLayoutX(198); 
        keyLeft.setLayoutY(111);
        
        keyTutorial.getChildren().add(keyLeft);
        
        Image keyZImage = new Image("file:myFiles/img/keyZ.png");
        ImageView keyZ = new ImageView(keyZImage);
        keyZ.setFitHeight(35.0);
        keyZ.setFitWidth(35.0);
        keyZ.setLayoutX(83); 
        keyZ.setLayoutY(5);        
        
        TextField keyZText = new TextField("SWORD");
        keyZText.setLayoutX(58);
        keyZText.setLayoutY(40);
        keyZText.setPrefHeight(25);
        keyZText.setPrefWidth(86);
        keyZText.setDisable(true);
        keyZText.getStyleClass().add("keyText");
        
        keyTutorial.getChildren().addAll(keyZ, keyZText);
        
        Image keyCImage = new Image("file:myFiles/img/keyC.png");
        ImageView keyC = new ImageView(keyCImage);
        keyC.setFitHeight(35.0);
        keyC.setFitWidth(35.0);
        keyC.setLayoutX(233); 
        keyC.setLayoutY(5);
        
        TextField keyCText = new TextField("SPECIAL");
        keyCText.setLayoutX(208);
        keyCText.setLayoutY(40);
        keyCText.setPrefHeight(25);
        keyCText.setPrefWidth(86);
        keyCText.setDisable(true);
        keyCText.getStyleClass().add("keyText");
        
        keyTutorial.getChildren().addAll(keyC, keyCText);
        
        Image keyXImage = new Image("file:myFiles/img/keyX.png");
        ImageView keyX = new ImageView(keyXImage);
        keyX.setFitHeight(35.0);
        keyX.setFitWidth(35.0);
        keyX.setLayoutX(385); 
        keyX.setLayoutY(5);
        
        TextField keyXText = new TextField("BOW");
        keyXText.setLayoutX(360);
        keyXText.setLayoutY(40);
        //keyXText.setPrefHeight(25);
        //keyXText.setPrefWidth(86);
        keyXText.setDisable(true);
        keyXText.getStyleClass().add("keyText");
        
        keyTutorial.getChildren().addAll(keyX, keyXText);
        
        root.getChildren().add(keyTutorial);
    }
    
    private void createLogin(GameModel gameModel){
        Pane login = new Pane();
        login.setLayoutX(800);
        login.setLayoutY(600);
        //login.setPrefHeight(150.0); 
        //login.setPrefWidth(300.0);
        login.getStyleClass().add("login");
        
        TextField loginTextField = new TextField();
        loginTextField.setLayoutX(76);
        loginTextField.setLayoutY(50);
        loginTextField.setPromptText("USERNAME");
        loginTextField.setFocusTraversable(false);
        loginTextField.getStyleClass().add("loginTextField");
        
        loginButton = new Button();
        loginButton.setLayoutX(101);
        loginButton.setLayoutY(75);
        loginButton.setMnemonicParsing(false);
        //loginButton.setPrefHeight(35);
        //loginButton.setPrefWidth(98);
        loginButton.setText("PLAY");
        loginButton.getStyleClass().add("loginButton");
        
        login.getChildren().addAll(loginTextField, loginButton);
        
        root.getChildren().add(login);
    }
    
    private void createRanking(GameModel gameModel){
        VBox rankingWrapper = new VBox();
        rankingWrapper.setLayoutX(800);
        rankingWrapper.setLayoutY(0);
        //ranking.setPrefHeight(600.0); 
        //ranking.setPrefWidth(300.0);
        rankingWrapper.getStyleClass().add("rankingWrapper");
        
        Label rankingTitle = new Label("Records");
        rankingTitle.getStyleClass().add("rankingTitle");
        
        TableColumn user = new TableColumn("Utente");
        user.setCellValueFactory(new PropertyValueFactory<>("user"));
        user.getStyleClass().add("column");
        
        TableColumn points = new TableColumn("Punteggio");
        points.setCellValueFactory(new PropertyValueFactory<>("points"));
        points.getStyleClass().add("column");
        
        ranking = new TableView<>();
        ranking.setItems(Zelda.records);
        ranking.getColumns().addAll(user, points);
        ranking.getStyleClass().add("ranking");
        
        rankingWrapper.getChildren().addAll(rankingTitle, ranking);
        
        root.getChildren().add(rankingWrapper);
    }
    
    public void showCharacter(GameCharacter character) {
        if(character != null){
            int x = character.getX(), y = character.getY();
            Command direction = character.getDirection();
            //link = new GraficCharacter(coordinateX, coordinateY, this);
            board[x][y].occupieCharacter(direction);
        }
    }  
    
    public void showEnemy(GameEnemy enemy){
        if(enemy != null){
            int x = enemy.getX(), y = enemy.getY();
            Command direction = enemy.getDirection();
            /*GraficEnemy temp = new GraficEnemy(coordinateX, coordinateY, direction, this);
            enemies.add(temp);*/
            board[x][y].occupieEnemy(direction);
        }
    }
    
    public void update(GameModel gameModel){
        clearBoard();
        
        ArrayList<GameEnemy> enemies = gameModel.getEnemies();
        for(int i = 0; i < enemies.size(); i++){
            showEnemy(enemies.get(i));
        }
        
        showCharacter(gameModel.getCharacter());
        
        endedAnimationCharacter = false;
        endedAnimationEnemies = false;
        endedAnimationCurrentEnemy = false;
    }
    
    public void updateLives(int lives){
        System.out.println("Aggiorno cuore: " + lives + "Mentre ho " + hearts.size() + " cuori");
        Image emptyHeart = new Image("file:myFiles/img/emptyHeart.png");
        hearts.get(lives).setImage(emptyHeart);
    }
    
    private void clearBoard(){
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                board[i][j].free();
            }
        }
    }
    
    public GraficTile getTile(final int x, final int y){
        return board[x][y];
    }
    
    public synchronized void moveAnimation(GameCharacter link, GameModel gameModel){
        int x = link.getX(), y = link.getY();
        
        switch(link.getDirection()){
            case Left:
                x++;
                break;
            case Right:
                x--;
                break;
            case Up:
                y++;
                break;
            case Down:
                y--;
                break;
        }
        
        GraficTile tile = this.getTile(x, y);
      
        Image im1 = new Image(tile.occupierPath + link.getDirection() + ".png");
        Image im2 = new Image("file:myFiles/img/rightFootLink" + link.getDirection() + ".png");
        Image im3 = new Image("file:myFiles/img/leftFootLink" + link.getDirection() + ".png");
        
        ImageView currentImage = new ImageView(im1);
        
        double cx = tile.getLayoutX() + tile.occupier.getLayoutX();
        double cy = tile.getLayoutY() + tile.occupier.getLayoutY();
        
        tile.free();
        
        currentImage.setFitHeight(70);
        currentImage.setFitWidth(55);
        
        root.getChildren().add(currentImage);
        int last = root.getChildren().size() - 1;
        root.getChildren().get(last).setLayoutX(cx);
        root.getChildren().get(last).setLayoutY(cy);
        
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(100), new KeyValue(currentImage.imageProperty(), im2)),
            new KeyFrame(Duration.millis(250), new KeyValue(currentImage.imageProperty(), im3)),
            new KeyFrame(Duration.millis(400), new KeyValue(currentImage.imageProperty(), im2)),
            new KeyFrame(Duration.millis(550), new KeyValue(currentImage.imageProperty(), im3))
        );
        timeline.play();
        
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(currentImage);
        translate.setDuration(Duration.millis(600));
        //translate.setCycleCount(TranslateTransition.INDEFINITE);
        
        switch(link.getDirection()){
            case Up:
                translate.setByY(-TILE_SIZE);
                break;
            case Down:
                translate.setByY(TILE_SIZE);
                break;
            case Left:
                translate.setByX(-TILE_SIZE);
                break;
            case Right:
                translate.setByX(TILE_SIZE);
                break;
        }
        
        //translate.setAutoReverse(true);
        translate.play();
        translate.setOnFinished((finish) -> {
            root.getChildren().remove(last); 
            endedAnimationCharacter = true; //update(gameModel); 
            System.out.println("Fine animazione");
        });
        
        System.out.println("Animato");
    }
    
    public synchronized void moveAnimation(GameEnemy enemy, GameModel gameModel){
        int x = enemy.getX(), y = enemy.getY();
        
        switch(enemy.getDirection()){
            case Left:
                x++;
                break;
            case Right:
                x--;
                break;
            case Up:
                y++;
                break;
            case Down:
                y--;
                break;
        }
        
        GraficTile tile = this.getTile(x, y);
      
        Image im = new Image(tile.occupierPath + enemy.getDirection() + ".png");
        
        ImageView currentImage = new ImageView(im);
        
        double cx = tile.getLayoutX() + tile.occupier.getLayoutX() + 15;
        double cy = tile.getLayoutY() + tile.occupier.getLayoutY() + 15;
        
        tile.free();
        
        currentImage.setFitHeight(70);
        currentImage.setFitWidth(70);
        
        root.getChildren().add(currentImage);
        int last = root.getChildren().size() - 1;
        root.getChildren().get(last).setLayoutX(cx);
        root.getChildren().get(last).setLayoutY(cy);
        
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(currentImage);
        translate.setDuration(Duration.millis(600));
        //translate.setCycleCount(TranslateTransition.INDEFINITE);
        
        switch(enemy.getDirection()){
            case Up:
                translate.setByY(-TILE_SIZE);
                break;
            case Down:
                translate.setByY(TILE_SIZE);
                break;
            case Left:
                translate.setByX(-TILE_SIZE);
                break;
            case Right:
                translate.setByX(TILE_SIZE);
                break;
        }
        
        //translate.setAutoReverse(true);
        translate.play();
        translate.setOnFinished((finish) -> {
            root.getChildren().remove(last); 
            endedAnimationCurrentEnemy = true;//update(gameModel);
            System.out.println("Fine animazione");
        });
        
        System.out.println("Animato");
    }
    
    public synchronized void attackAnimation(GameCharacter link, GameModel gameModel, boolean hasHit){
        int x = link.getX(), y = link.getY();
        GraficTile tile = this.getTile(x, y);
        
        /*  NON SUONA NE' IL MEDIAPLAYER NE' L'AUDIOCLIP
        //Media media = new Media(new File("myFiles/sounds/linkAttack1.mp3").toURI().toString());
        AudioClip audioClip = new AudioClip(new File("myFiles/sounds/linkAttack1.mp3").toURI().toString()); 
        audioClip.setCycleCount(10);
        audioClip.play();
        System.out.println(audioClip.isPlaying() + " Volume: " + audioClip.getVolume());
        */
        
        Image im0 = new Image(tile.occupierPath + link.getDirection() + ".png");
        Image im1 = new Image("file:myFiles/img/swordFirstLink" + link.getDirection() + ".png");
        Image im2 = new Image("file:myFiles/img/swordSecondLink" + link.getDirection() + ".png");
        Image im3 = new Image("file:myFiles/img/swordThirdLink" + link.getDirection() + ".png");
        
        ImageView currentImage = new ImageView(im1);
        
        double cx = tile.getLayoutX() + tile.occupier.getLayoutX();
        double cy = tile.getLayoutY() + tile.occupier.getLayoutY();
        
        tile.getChildren().clear();
        
        currentImage.setFitHeight(70);
        currentImage.setFitWidth(70);
        
        root.getChildren().add(currentImage);
        int last = root.getChildren().size() - 1;
        root.getChildren().get(last).setLayoutX(cx);
        root.getChildren().get(last).setLayoutY(cy);
                
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(100), new KeyValue(currentImage.imageProperty(), im1)),
            new KeyFrame(Duration.millis(150), new KeyValue(currentImage.imageProperty(), im2)),
            new KeyFrame(Duration.millis(300), new KeyValue(currentImage.imageProperty(), im3)),
            new KeyFrame(Duration.millis(450), new KeyValue(currentImage.imageProperty(), im0))
        );
        timeline.play();
        timeline.setOnFinished((finish) -> {
            root.getChildren().remove(last); 
            if(hasHit == false){
                System.out.println("Attacco a vuoto");
                endedAnimationCharacter = true;//update(gameModel);
            }
            System.out.println("Fine animazione attack");
        });
        
    }
    
    public synchronized void attackAnimation(GameEnemy gameEnemy, GameModel gameModel){
        int x = gameEnemy.getX(), y = gameEnemy.getY();
        GraficTile tile = this.getTile(x, y);
        
        Command direction = gameEnemy.getDirection();
        Image im0 = new Image(tile.occupierPath + direction + ".png");
        Image im1 = new Image("file:myFiles/img/swordFirstKnight" + direction + ".png");
        
        ImageView currentImage = new ImageView(im1);
        
        double cx = tile.getLayoutX() + tile.occupier.getLayoutX();
        double cy = tile.getLayoutY() + tile.occupier.getLayoutY();
        
        tile.getChildren().clear();
        
        if(direction == Command.Up || direction == Command.Down){
            currentImage.setFitHeight(90);
            currentImage.setFitWidth(60);
            cx += 15;
            cy += 15;
        }
        else if(direction == Command.Left || direction == Command.Right){
            currentImage.setFitHeight(55);
            currentImage.setFitWidth(85);
            cy += 15;
            cx += 5;
        }
        
        root.getChildren().add(currentImage);
        int last = root.getChildren().size() - 1;
        root.getChildren().get(last).setLayoutX(cx);
        root.getChildren().get(last).setLayoutY(cy);
                
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(100), new KeyValue(currentImage.imageProperty(), im1)),
            new KeyFrame(Duration.millis(400), new KeyValue(currentImage.imageProperty(), null))
        );
        timeline.play();
        timeline.setOnFinished((finish) -> {
            root.getChildren().remove(last); 
            endedAnimationCurrentEnemy = true;//update(gameModel);
            System.out.println("Fine animazione enemy attack");
        });
        
    }
    
    public synchronized void killAnimation(GameTile gameTile, GameModel gameModel){
        int x = gameTile.coordinateX, y = gameTile.coordinateY;
        GraficTile tile = this.getTile(x, y);
        
        ImageView imageView = new ImageView(tile.occupierPath + gameTile.getOccupierEnemy().getDirection() + ".png");
        
        double cx = tile.getLayoutX() + tile.occupier.getLayoutX();
        double cy = tile.getLayoutY() + tile.occupier.getLayoutY();
        
        tile.free();
        
        imageView.setFitHeight(70);
        imageView.setFitWidth(70);
        
        root.getChildren().add(imageView);
        int last = root.getChildren().size() - 1;
        root.getChildren().get(last).setLayoutX(cx);
        root.getChildren().get(last).setLayoutY(cy);
        
        FadeTransition fade = new FadeTransition();
        fade.setNode(imageView);
        fade.setDuration(Duration.millis(450));
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.play();
        fade.setOnFinished((finish) -> {
            //root.getChildren().remove(last); 
            endedAnimationCharacter = true;//update(gameModel); 
            System.out.println("Fine animazione");
        });
    }
    
    public void endGame(){
        clearBoard();
    }
    
    /*
    public synchronized void executePlayerCommand(Command command, boolean mooved) {
        //System.out.println("Grafic " + command);
        link.move(command, mooved);
    }
    */
}
