package zelda;

import static zelda.BoardUtils.*;

public class GameModel{
    
    private Tile[][] board = new Tile[WIDTH][HEIGHT];
    private GameCharacter link;
    
    public GameModel(){
        createContent();
        
        spawnCharacter((HEIGHT / 2) - 1, (WIDTH / 2) - 1, this);
    }
    
    private void createContent() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile currentTile = new Tile(x, y, false);
                board[x][y] = currentTile;
            }
        }
    }
    
    private void spawnCharacter(final int coordinateX, final int coordinateY, GameModel gameModel) {
            link = new GameCharacter(coordinateX, coordinateY, gameModel);
            //gBoard[coordinateX][coordinateY].occupie();
        }  
    
    public Tile[][] getBoard(){
        return board;
    }
    
    public synchronized void executePlayerCommand(Command command) {
        link.move(command);
        link.showPosition();
    }
}