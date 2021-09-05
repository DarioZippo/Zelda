package zelda;

import java.util.*;

/**
 */
public class GameUtils {
    
    public static final int TILE_SIZE = 100;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 6;
    
    public static boolean checkPosition(final int x, final int y) {
        boolean result = true;
        
        if(x < 0 || x >= WIDTH)
            result = false;
        if(y < 0 || y >= HEIGHT)
            result = false;
        
        return result;
    }
    
    public static Command randomDirection(){
        Command direction = null;
        Random rand = new Random();
        int randInt = rand.nextInt( 4 ); //Generates a number in [0, 1, .., 3]
        
        switch(randInt){
            case 0:
                direction = Command.Down;
                break;
            case 1:
                direction = Command.Up;
                break;
            case 2:
                direction = Command.Left;
                break;
            case 3:
                direction = Command.Right;
                break;
            default:
                System.out.println("Numero imprevisto: " + randInt);
                break;
        }
        return direction;
    }
}
