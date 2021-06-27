package zelda;

import java.util.ArrayList;
import javafx.animation.*;
import javafx.event.*;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
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
    
    public GameView(){
        //enemies = new ArrayList<GraficEnemy>();
    }
        
    public Parent showBoard(GameModel gameModel) {
        int width = gameModel.getBoard().length;
        int height = gameModel.getBoard()[0].length;
        
        board = new GraficTile[width][height];
        
        Pane root = new Pane();
        root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        root.getChildren().addAll(Zelda.tileGroup);
        
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                boolean currentPositionState = gameModel.getTile(x, y).occupied;
                
                GraficTile gTile = new GraficTile(x, y, currentPositionState);
                this.board[x][y] = gTile;
                
                Zelda.tileGroup.getChildren().add(gTile);
            }
        }
        this.root = root;                      
        return root;
    }
    
    public void showCharacter(GameCharacter character) {
        int x = character.getX(), y = character.getY();
        Command direction = character.getDirection();
        //link = new GraficCharacter(coordinateX, coordinateY, this);
        board[x][y].occupieCharacter(direction);
    }  
    
    public void showEnemy(GameEnemy enemy){
        int x = enemy.getX(), y = enemy.getY();
        Command direction = enemy.getDirection();
        /*GraficEnemy temp = new GraficEnemy(coordinateX, coordinateY, direction, this);
        enemies.add(temp);*/
        board[x][y].occupieEnemy(direction);
    }
    
    public void update(GameModel gameModel){
        clearBoard();
        
        ArrayList<GameEnemy> enemies = gameModel.getEnemies();
        for(int i = 0; i < enemies.size(); i++){
            showEnemy(enemies.get(i));
        }
        
        showCharacter(gameModel.getCharacter());
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
            new KeyFrame(Duration.millis(300), new KeyValue(currentImage.imageProperty(), im3)),
            new KeyFrame(Duration.millis(500), new KeyValue(currentImage.imageProperty(), im2)),
            new KeyFrame(Duration.millis(700), new KeyValue(currentImage.imageProperty(), im3))
        );
        timeline.play();
        
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(currentImage);
        translate.setDuration(Duration.millis(800));
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
            update(gameModel); 
            System.out.println("Fine animazione");
        });
        
        System.out.println("Animato");
    }
    
    /*
    public synchronized void executePlayerCommand(Command command, boolean mooved) {
        //System.out.println("Grafic " + command);
        link.move(command, mooved);
    }
    */
}
