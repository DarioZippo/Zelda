package zelda;

import java.util.*;
import static zelda.BoardUtils.*;

public class GameModel{
    
    private GameTile[][] board = new GameTile[WIDTH][HEIGHT];
    
    private GameCharacter link;
    private ArrayList<GameEnemy> enemies;
    
    public GameModel(){
        createContent();
        
        enemies = new ArrayList<GameEnemy>();
        
        spawnCharacter((WIDTH / 2) - 1, (HEIGHT / 2) - 1, this);
        spawnEnemy(WIDTH - 1, HEIGHT - 1, this);
    }
    
    private void createContent() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                GameTile currentTile = new GameTile(x, y, false);
                board[x][y] = currentTile;
            }
        }
    }
    
    private void spawnCharacter(final int coordinateX, final int coordinateY, GameModel gameModel) {
        if(board[coordinateX][coordinateY].occupied == false)
        {
            link = new GameCharacter(coordinateX, coordinateY, gameModel);
            
            board[coordinateX][coordinateY].changeState();
        }
    }  
    
    private void spawnEnemy(final int coordinateX, final int coordinateY, GameModel gameModel){
        if(board[coordinateX][coordinateY].occupied == false)
        {
            GameEnemy temp = new GameEnemy(coordinateX, coordinateY, gameModel);
            enemies.add(temp);
            
            board[coordinateX][coordinateY].changeState();
        }
    }
    
    public GameTile[][] getBoard(){
        return board;
    }
    
    public GameTile getTile(final int x, final int y){
        return board[x][y];
    }
    
    public synchronized void executePlayerCommand(Command command) {
        switch(command){
            case Left: case Right: case Up: case Down:
                link.move(command);
                link.showPosition();
                break;
            case Sword:
                link.attack();
                break;
            /*case pause:
                PAUSA*/
        }
    }    
}