package zelda;

/**
 */
public class GameUtils {
    
    public static final int TILE_SIZE = 100;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    
    private GameUtils() {
        throw new RuntimeException("You cannot instantiate me!");
    }
    
    public static boolean checkPosition(final int x, final int y) {
        boolean result = true;
        
        if(x < 0 || x >= WIDTH)
            result = false;
        if(y < 0 || y >= HEIGHT)
            result = false;
        
        return result;
    }
}
