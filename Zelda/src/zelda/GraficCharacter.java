package zelda;

import zelda.*;
import static zelda.GameUtils.checkPosition;

class GraficCharacter{
    
    private int currentPositionX;
    private int currentPositionY;
    private GraficModel graficModel;
    
    private Command direction;
    
    GraficCharacter(final int coordinateX, final int coordinateY, GraficModel gameView) {
        currentPositionX = coordinateX;
        currentPositionY = coordinateY;
        this.graficModel = gameView;
    }
    
    public boolean move(Command direction){
        int x = currentPositionX, y = currentPositionY;
        //Condizione per spostarsi
        switch(direction){
            case Left:
                x--;
                break;
            case Right:
                x++;
                break;
            case Up:
                y--;
                break;
            case Down:
                y++;
                break;
        }
        graficModel.getTile(currentPositionX, currentPositionY).free();
        currentPositionX = x;
        currentPositionY = y;
        graficModel.getTile(currentPositionX, currentPositionY).occupieCharacter();
        return true; //confermo lo spostamento
    }
}
