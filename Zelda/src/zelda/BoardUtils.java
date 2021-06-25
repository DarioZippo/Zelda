package zelda;

/**
 */
public class BoardUtils {
    
    public static final int TILE_SIZE = 100;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    
    private BoardUtils() {
        throw new RuntimeException("You cannot instantiate me!");
    }
    
    public static boolean isValidTileCoordinate(int coordinate) {
        return coordinate >= 0 && coordinate < 64;
    }
}
