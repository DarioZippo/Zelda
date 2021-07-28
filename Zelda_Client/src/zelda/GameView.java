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
    
    public void setHearts(ArrayList<ImageView> hearts){
        this.hearts = hearts;
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
    
    public void resetLives(){
        Image fullHeart = new Image("file:myFiles/img/fullHeart.png");
        for(int i = 0; i < hearts.size(); i++){
            hearts.get(i).setImage(fullHeart);
        }
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
    
    /*
        special animation
        
        Image im0 = new Image(tile.occupierPath + link.getDirection() + ".png");
        Image im1 = new Image("file:myFiles/img/firstSpecial.png");
        Image im2 = new Image("file:myFiles/img/secondSpecial.png");
        Image im3 = new Image("file:myFiles/img/thirdSpecial.png");
        Image im4 = new Image("file:myFiles/img/fourthSpecial.png");
        Image im5 = new Image("file:myFiles/img/fifthSpecial.png");
    
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(100), new KeyValue(currentImage.imageProperty(), im1)),
            new KeyFrame(Duration.millis(150), new KeyValue(currentImage.imageProperty(), im2)),
            new KeyFrame(Duration.millis(300), new KeyValue(currentImage.imageProperty(), im3)),
            new KeyFrame(Duration.millis(450), new KeyValue(currentImage.imageProperty(), im4)),
            new KeyFrame(Duration.millis(600), new KeyValue(currentImage.imageProperty(), im5)),
            new KeyFrame(Duration.millis(750), new KeyValue(currentImage.imageProperty(), im0))
        );
    
    */
    
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
