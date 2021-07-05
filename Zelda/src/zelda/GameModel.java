package zelda;

import static java.lang.Thread.sleep;
import java.util.*;
import static zelda.GameUtils.*;

public class GameModel{
    private GameView gameView;
    
    private GameTile[][] board = new GameTile[WIDTH][HEIGHT];
    
    private GameCharacter link;
    private ArrayList<GameEnemy> enemies;
    
    private int linkLives;
    public boolean endGame;
    
    public GameModel(GameView gameView){
        createContent();
        
        linkLives = 3;
        
        enemies = new ArrayList<GameEnemy>();
        
        this.gameView = gameView;
        endGame = false;
    }
    
    private void createContent() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                GameTile currentTile = new GameTile(x, y, false);
                board[x][y] = currentTile;
            }
        }
    }
    
    public void spawn(){
        spawnCharacter((WIDTH / 2) - 1, (HEIGHT / 2) - 1, this);
        spawnEnemy(WIDTH - 1, HEIGHT - 1, this);
        spawnEnemy(WIDTH - 2, HEIGHT - 2, this);
        spawnEnemy(2, 2, this);
        spawnEnemy(WIDTH - 1, 1, this);
    }
    
    private void spawnCharacter(final int coordinateX, final int coordinateY, GameModel gameModel) {
        if(board[coordinateX][coordinateY].occupied == false)
        {
            link = new GameCharacter(coordinateX, coordinateY, gameModel);
            
            board[coordinateX][coordinateY].occupieCharacter(link);
            
            gameView.showCharacter(link);
        }
    }  
    
    private void spawnEnemy(final int coordinateX, final int coordinateY, GameModel gameModel){
        if(board[coordinateX][coordinateY].occupied == false)
        {
            Command direction = randomDirection();
            GameEnemy temp = new GameEnemy(coordinateX, coordinateY, direction, gameModel);
            enemies.add(temp);
            
            board[coordinateX][coordinateY].occupieEnemy(temp);
            
            gameView.showEnemy(temp);
        }
    }
    
    public GameTile[][] getBoard(){
        return board;
    }
    
    public GameTile getTile(final int x, final int y){
        return board[x][y];
    }
    
    public GameCharacter getCharacter(){
        return link;
    }
    
    public ArrayList<GameEnemy> getEnemies(){
        return enemies;
    }
    
    public synchronized boolean executePlayerCommand(Command command) throws InterruptedException {
        boolean result = false;
        boolean mooved;
        GameTile attacked;
        switch(command){
            case Left: case Right: case Up: case Down:
                mooved = link.move(command);
                System.out.println("Spostamento avvenuto, mooved = " + mooved);
                if(mooved == true){
                    gameView.moveAnimation(link, this);
                    return true;
                }
                link.showPosition();
                break;
            case Sword:
                attacked = link.attack();
                gameView.attackAnimation(link, this, attacked != null);
                if(attacked != null){
                    combat(link, attacked);
                }
                return true;
                //break;
            /*case pause:
                PAUSA*/ 
        }
        gameView.endedAnimationCharacter = true;//gameView.update(this);
        return result;
    }
    
    //Versione in cui Link attacca
    public void combat(GameCharacter attacker, GameTile attackedTile){
        kill(attackedTile.getOccupierEnemy());
        gameView.killAnimation(attackedTile, this);
        attackedTile.free();
        //attacked.free();
        //GameUpdate
        //gameView.update(this);
        //DropOggetto
    }
    
    public void kill(GameEnemy attacked){
        enemies.remove(attacked);
    }
    
    public void EnemiesTurn(int i){
        if(i >= enemies.size()) //Finisce il turno avversario
        {
            gameView.endedAnimationEnemies = true;
            return;
        }
        else
        {
            System.out.println("Nemico numero: " + i);
            enemies.get(i).turn(gameView);
        }
    }
    
    public void attackedCharacter(){
        linkLives--;
        gameView.updateLives(linkLives);
        if(linkLives == 0){
            System.out.println("Hai perso");
            endGame = true;
        }
    }
    
    private void clear(){
        link = null;
        enemies.clear();
    }
    
    public void endGame(){
        clear();
    }
}