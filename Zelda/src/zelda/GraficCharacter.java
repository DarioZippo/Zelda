package zelda;

import zelda.*;

class GraficCharacter{
    
    private int currentPositionX;
    private int currentPositionY;
    private GameView gameView;
    
    GraficCharacter(final int coordinateX, final int coordinateY, GameView gameView) {
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
                gameView.getTile(currentPositionX, currentPositionY).occupie();
                break;
            case Right:
                gameView.getTile(currentPositionX, currentPositionY).free();
                currentPositionX++;
                gameView.getTile(currentPositionX, currentPositionY).occupie();
                break;
            case Up:
                gameView.getTile(currentPositionX, currentPositionY).free();
                currentPositionY++;
                gameView.getTile(currentPositionX, currentPositionY).occupie();
                break;
            case Down:
                gameView.getTile(currentPositionX, currentPositionY).free();
                currentPositionY--;
                gameView.getTile(currentPositionX, currentPositionY).occupie();
                break;
        }
        return true; //confermo lo spostamento
    }
}
