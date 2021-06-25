
package zelda;

import javafx.scene.*;
import javafx.scene.layout.Pane;
import static zelda.BoardUtils.*;
import zelda.*;

public class GameView {
        
        private GraficTile[][] gBoard;
        private GraficCharacter link;
        
        public Parent graficContent;
        
        public GameView(Tile[][] board){
            gBoard = new GraficTile[WIDTH][HEIGHT];
            graficContent = createGraficContent(board);
            
            spawnCharacter((HEIGHT / 2) - 1, (WIDTH / 2) - 1, this);
        }
            
        private Parent createGraficContent(Tile[][] board) {
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
        
        private void spawnCharacter(final int coordinateX, final int coordinateY, GameView gameView) {
            link = new GraficCharacter(coordinateX, coordinateY, gameView);
            gBoard[coordinateX][coordinateY].occupie();
        }  
        
        public GraficTile getTile(final int x, final int y){
            return gBoard[x][y];
        }
        
        public synchronized void executePlayerCommand(Command command) {
            System.out.println("Grafic " + command);
            link.move(command);
        }
}
