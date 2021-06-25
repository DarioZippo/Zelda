package zelda;

import zelda.*;

class GraficCharacter{
    
    private int currentPositionX;
    private int currentPositionY;
    private GraficModel gameView;
    
    private Command direction;
    
    GraficCharacter(final int coordinateX, final int coordinateY, GraficModel gameView) {
        currentPositionX = coordinateX;
        currentPositionY = coordinateY;
        this.gameView = gameView;
    }
    
    public boolean move(Command direction){
        //Condizione per spostarsi
        switch(direction){
            case Left:
                gameView.getTile(currentPositionX, currentPositionY).free();
                currentPositionX--;
                gameView.getTile(currentPositionX, currentPositionY).occupieCharacter();
                break;
            case Right:
                gameView.getTile(currentPositionX, currentPositionY).free();
                currentPositionX++;
                gameView.getTile(currentPositionX, currentPositionY).occupieCharacter();
                break;
            case Up:
                gameView.getTile(currentPositionX, currentPositionY).free();
                currentPositionY--;
                gameView.getTile(currentPositionX, currentPositionY).occupieCharacter();
                break;
            case Down:
                gameView.getTile(currentPositionX, currentPositionY).free();
                currentPositionY++;
                gameView.getTile(currentPositionX, currentPositionY).occupieCharacter();
                break;
        }
        this.direction = direction;
        return true; //confermo lo spostamento
    }
}
