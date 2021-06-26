
package zelda;

import java.util.*;
import javafx.scene.*;
import javafx.scene.layout.Pane;
import static zelda.GameUtils.*;
import zelda.*;

public class GraficModel {
        
        private GraficTile[][] gBoard;
        private GraficCharacter link;
        
        private ArrayList<GraficEnemy> enemies;
        
        public Parent graficContent;
        
        public GraficModel(GameTile[][] board){
            gBoard = new GraficTile[WIDTH][HEIGHT];
            graficContent = createGraficContent(board);
            
            enemies = new ArrayList<GraficEnemy>();
            
            spawnCharacter((HEIGHT / 2) - 1, (WIDTH / 2) - 1, this);
            spawnEnemy(WIDTH - 1, HEIGHT - 1, this);
        }
            
        private Parent createGraficContent(GameTile[][] board) {
            Pane root = new Pane();
            root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
            root.getChildren().addAll(Zelda.tileGroup);
            
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    boolean currentPositionState = board[x][y].occupied;
                    
                    GraficTile gTile = new GraficTile(x, y, currentPositionState);
                    gBoard[x][y] = gTile;
                    
                    Zelda.tileGroup.getChildren().add(gTile);
                }
            }
                                  
            return root;
        }
        
        private void spawnCharacter(final int coordinateX, final int coordinateY, GraficModel gameView) {
            link = new GraficCharacter(coordinateX, coordinateY, gameView);
            gBoard[coordinateX][coordinateY].occupieCharacter(Command.Down);
        }  
        
        private void spawnEnemy(final int coordinateX, final int coordinateY, GraficModel gameView){
            GraficEnemy temp = new GraficEnemy(coordinateX, coordinateY, gameView);
            enemies.add(temp);
            gBoard[coordinateX][coordinateY].occupieEnemy();
        }
        
        public GraficTile getTile(final int x, final int y){
            return gBoard[x][y];
        }
        
        public synchronized void executePlayerCommand(Command command, boolean mooved) {
            //System.out.println("Grafic " + command);
            link.move(command, mooved);
        }
}
