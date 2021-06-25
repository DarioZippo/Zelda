package zelda;

class GameEnemy{
    
    private int currentPositionX;
    private int currentPositionY;   
    private GameModel gameModel;
    
    GameEnemy(final int coordinateX, final int coordinateY, GameModel gameModel) {
        currentPositionX = coordinateX;
        currentPositionY = coordinateY;
        this.gameModel = gameModel;
    }
    
    public boolean move(Command direction){
        //Condizione per spostarsi
        switch(direction){
            case Left:
                currentPositionX--;
                break;
            case Right:
                currentPositionX++;
                break;
            case Up:
                currentPositionY--;
                break;
            case Down:
                currentPositionY++;
                break;
        }
        return true; //confermo lo spostamento
    }
    
    public void showPosition(){
        System.out.println("Enemy " + "x: " + currentPositionX + " y: " + currentPositionY);
    }
}
