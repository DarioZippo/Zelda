package zelda;

import zelda.*;

class GraficEnemy{
    
    private int currentPositionX;
    private int currentPositionY;
    private GraficModel gameView;
    
    GraficEnemy(final int coordinateX, final int coordinateY, GraficModel gameView) {
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
                gameView.getTile(currentPositionX, currentPositionY).occupieEnemy();
                break;
            case Right:
                gameView.getTile(currentPositionX, currentPositionY).free();
                currentPositionX++;
                gameView.getTile(currentPositionX, currentPositionY).occupieEnemy();
                break;
            case Up:
                gameView.getTile(currentPositionX, currentPositionY).free();
                currentPositionY--;
                gameView.getTile(currentPositionX, currentPositionY).occupieEnemy();
                break;
            case Down:
                gameView.getTile(currentPositionX, currentPositionY).free();
                currentPositionY++;
                gameView.getTile(currentPositionX, currentPositionY).occupieEnemy();
                break;
        }
        return true; //confermo lo spostamento
    }
}