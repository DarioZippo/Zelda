package zelda;

import java.util.ArrayList;
import javafx.animation.*;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import static zelda.GameUtils.*;

public class GameView {
    private GraficTile[][] board;
    
    private Pane root;
    private Pane boardWrapper;
    private ArrayList<ImageView> hearts;
    private TextField specialTextField;
    private TextField bowTextField;
    
    public boolean endedAnimationCharacter;
    public boolean endedAnimationEnemies;
    public boolean endedAnimationCurrentEnemy;
    
    public GameView(){
        endedAnimationCharacter = false;
        endedAnimationEnemies = false;
        endedAnimationCurrentEnemy = false;
    }
    
    public Parent createContent(GameModel gameModel){
        root = new Pane();
        createBoard(gameModel);
        
        return root;
    }
    
    private void createBoard(GameModel gameModel) {
        boardWrapper = new Pane();
        
        int height = gameModel.getBoard().length;
        int width = gameModel.getBoard()[0].length;
        
        board = new GraficTile[height][width];
        
        boardWrapper.getChildren().addAll(Zelda.tileGroup);
        
        System.out.println("Altezza: " + HEIGHT + " larghezza:" + WIDTH);
        
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                boolean currentPositionState = gameModel.getTile(x, y).occupied;
                
                GraficTile gTile = new GraficTile(x, y, currentPositionState);
                this.board[y][x] = gTile;
                
                Zelda.tileGroup.getChildren().add(gTile);
            }
        }
        boardWrapper.getStyleClass().add("background");
        root.getChildren().add(boardWrapper);
    }
    
    public void setHearts(ArrayList<ImageView> hearts){
        this.hearts = hearts;
    }
    
    public void setSpecialTextField(TextField specialTextField){
        this.specialTextField = specialTextField;
    }
    
    public void setBowTextField(TextField bowTextField){
        this.bowTextField = bowTextField;
    }
    
    public void readySpecial(){
        specialTextField.getStyleClass().remove("notReady");
    }
    
    public void notReadySpecial(){
        specialTextField.getStyleClass().add("notReady");
    }
    
    public void updateCoolDown(int coolDown){
        specialTextField.setText("SPECIAL: " + coolDown);
    }
    
    public void removeCoolDown(){
        specialTextField.setText("SPECIAL");
    }
    
    public void showCharacter(GameCharacter character) {
        if(character != null){
            int x = character.getX(), y = character.getY();
            Command direction = character.getDirection();
            //link = new GraficCharacter(coordinateX, coordinateY, this);
            board[y][x].occupieCharacter(direction);
        }
    }  
    
    public void showEnemy(GameEnemy enemy){
        if(enemy != null){
            int x = enemy.getX(), y = enemy.getY();
            Command direction = enemy.getDirection();
            /*GraficEnemy temp = new GraficEnemy(coordinateX, coordinateY, direction, this);
            enemies.add(temp);*/
            board[y][x].occupieEnemy(direction);
        }
    }
    
    public void updateBoard(GameModel gameModel){
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
    
    public void readyBow(){
        bowTextField.getStyleClass().remove("notReady");
    }
    
    public void notReadyBow(){
        bowTextField.getStyleClass().add("notReady");
    }
    
    public void updateArrows(int arrows){
        bowTextField.setText("BOW: " + arrows);
    }
    
    public void removeArrows(){
        bowTextField.setText("BOW");
    }
    
    private void clearBoard(){
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                board[i][j].free();
            }
        }
    }
    
    public GraficTile getTile(final int x, final int y){
        return board[y][x];
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
        
        translate.play();
        translate.setOnFinished((finish) -> {
            root.getChildren().remove(last); 
            endedAnimationCharacter = true;
            //System.out.println("Fine animazione");
        });
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
        
        translate.play();
        translate.setOnFinished((finish) -> {
            root.getChildren().remove(last); 
            endedAnimationCurrentEnemy = true;
            //System.out.println("Fine animazione");
        });
    }
    
    public synchronized void attackAnimation(GameCharacter link, GameModel gameModel, boolean hasHit){
        int x = link.getX(), y = link.getY();
        GraficTile tile = this.getTile(x, y);
        
        Image im0 = new Image(tile.occupierPath + link.getDirection() + ".png");
        Image im1 = new Image("file:myFiles/img/swordFirstLink" + link.getDirection() + ".png");
        Image im2 = new Image("file:myFiles/img/swordSecondLink" + link.getDirection() + ".png");
        Image im3 = new Image("file:myFiles/img/swordThirdLink" + link.getDirection() + ".png");
        
        ImageView currentImage = new ImageView(im1);
        
        double cx = tile.getLayoutX() + tile.occupier.getLayoutX();
        double cy = tile.getLayoutY() + tile.occupier.getLayoutY();
        
        tile.getChildren().clear();
        
        currentImage.setFitHeight(85);
        currentImage.setFitWidth(85);
        
        root.getChildren().add(currentImage);
        int last = root.getChildren().size() - 1;
        root.getChildren().get(last).setLayoutX(cx - 15);
        root.getChildren().get(last).setLayoutY(cy - 15);
                
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
                //System.out.println("Attacco a vuoto");
                endedAnimationCharacter = true;
            }
            //System.out.println("Fine animazione attack");
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
            endedAnimationCurrentEnemy = true;
            //System.out.println("Fine animazione enemy attack");
        });
        
    }
    
    public synchronized void specialAnimation(GameCharacter link, GameModel gameModel, boolean hasHit){
        int x = link.getX(), y = link.getY();
        GraficTile tile = this.getTile(x, y);
        
        Image im0 = new Image(tile.occupierPath + link.getDirection() + ".png");
        Image im1 = new Image("file:myFiles/img/firstSpecial.png");
        Image im2 = new Image("file:myFiles/img/secondSpecial.png");
        Image im3 = new Image("file:myFiles/img/thirdSpecial.png");
        Image im4 = new Image("file:myFiles/img/fourthSpecial.png");
        Image im5 = new Image("file:myFiles/img/fifthSpecial.png");
        
        ImageView currentImage = new ImageView(im1);
        
        double cx = tile.getLayoutX() + tile.occupier.getLayoutX();
        double cy = tile.getLayoutY() + tile.occupier.getLayoutY();
        
        tile.getChildren().clear();
        
        currentImage.setFitHeight(85);
        currentImage.setFitWidth(85);
        
        root.getChildren().add(currentImage);
        int last = root.getChildren().size() - 1;
        root.getChildren().get(last).setLayoutX(cx - 15);
        root.getChildren().get(last).setLayoutY(cy - 15);
        
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(75), new KeyValue(currentImage.imageProperty(), im1)),
            new KeyFrame(Duration.millis(150), new KeyValue(currentImage.imageProperty(), im2)),
            new KeyFrame(Duration.millis(225), new KeyValue(currentImage.imageProperty(), im3)),
            new KeyFrame(Duration.millis(300), new KeyValue(currentImage.imageProperty(), im4)),
            new KeyFrame(Duration.millis(375), new KeyValue(currentImage.imageProperty(), im5)),
            new KeyFrame(Duration.millis(450), new KeyValue(currentImage.imageProperty(), im0))
        );
        
        timeline.play();
        timeline.setOnFinished((finish) -> {
            root.getChildren().remove(last); 
            if(hasHit == false){
                //System.out.println("Attacco a vuoto");
                endedAnimationCharacter = true;
            }
            //System.out.println("Fine animazione special");
        });
        
    }
    
    public synchronized void bowAnimation(GameCharacter link, GameModel gameModel, boolean hasHit){
        int x = link.getX(), y = link.getY();
        GraficTile tile = this.getTile(x, y);
        
        Image im0 = new Image(tile.occupierPath + link.getDirection() + ".png");
        Image im1 = new Image("file:myFiles/img/firstArrow" + link.getDirection() + ".png");
        
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
            new KeyFrame(Duration.millis(400), new KeyValue(currentImage.imageProperty(), im1)),
            new KeyFrame(Duration.millis(450), new KeyValue(currentImage.imageProperty(), im0))
        );
        
        timeline.play();
        timeline.setOnFinished((finish) -> {
            root.getChildren().remove(last); 
            if(hasHit == false){
                //System.out.println("Attacco a vuoto");
                endedAnimationCharacter = true;
            }
            //System.out.println("Fine animazione special");
        });
        
    }
    
    public synchronized void killAnimation(GameTile gameTile, GameModel gameModel){
        int x = gameTile.x, y = gameTile.y;
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
            endedAnimationCharacter = true; 
            //System.out.println("Fine animazione");
        });
    }
    
    public void endGame(){
        clearBoard();
    }
}
