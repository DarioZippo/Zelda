package zelda;

import java.util.*;
import static zelda.GameUtils.*;

public class GameModel{
    private GameView gameView;
    private String user;
    private int points;
    
    private GameTile[][] board = new GameTile[HEIGHT][WIDTH];
    
    private GameCharacter link;
    private ArrayList<GameEnemy> enemies;
    private int waveMagnitude; 
    
    private int lives;
    private int coolDown;
    private boolean readySpecial;
    private int arrows;
    
    private boolean ended;
    
    public GameModel(GameView gameView){
        createBoard();
        
        lives = 3;
        
        coolDown = 0;
        readySpecial = true;
        
        enemies = new ArrayList<GameEnemy>();
        
        this.gameView = gameView;
        ended = true;
    }
    
    private void createBoard() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                GameTile currentTile = new GameTile(x, y, false);
                board[y][x] = currentTile;
            }
        }
    }
    
    public void setUser(String user){
        this.user = new String(user);
    }
    
    public String getUser(){
        return user;
    }
    
    public void setPoints(int points){
        this.points = points;
    }
    
    public int getPoints(){
        return points;
    }
    
    public int getWaveMagnitude(){
        return waveMagnitude;
    }
    
    public void start(){
        ended = false;
        points = 0;
        waveMagnitude = 1;
        
        spawn();
        resetLives();
        resetArrows();
        
        EventLoggerXML.recordEvent(EventLoggerXML.eventDescriptionStartGame);
    }
    
    public void spawn(){
        spawnCharacter((WIDTH / 2) - 1, (HEIGHT / 2) - 1, Command.Down, this);
        for(int i = 0; i < waveMagnitude; i++){
            randomSpawnEnemy(this);
        }
    }
    
    public void spawnWave(){
        for(int i = 0; i < waveMagnitude; i++){
            randomSpawnEnemy(this);
        }
    }
    
    private void spawnCharacter(final int coordinateX, final int coordinateY, Command direction, GameModel gameModel) {
        if(board[coordinateY][coordinateX].occupied == false)
        {
            link = new GameCharacter(coordinateX, coordinateY, direction, gameModel);
            
            board[coordinateY][coordinateX].occupieCharacter(link);
            
            gameView.showCharacter(link);
        }
        else
            System.out.println("Spawn personaggio occupato");
    }  
    
    private boolean spawnEnemy(final int coordinateX, final int coordinateY, Command direction, GameModel gameModel){
        if(board[coordinateY][coordinateX].occupied == false)
        {
            GameEnemy temp = new GameEnemy(coordinateX, coordinateY, direction, gameModel);
            enemies.add(temp);
            
            board[coordinateY][coordinateX].occupieEnemy(temp);
            
            gameView.showEnemy(temp);
            
            return true;
        }
        return false;
    }
    
    private void randomSpawnEnemy(GameModel gameModel){
        Random rand = new Random();
        int x = rand.nextInt(WIDTH);
        int y = rand.nextInt(HEIGHT);
        
        boolean spawned = false;
        while(spawned == false){
            spawned = spawnEnemy(x, y, randomDirection(), gameModel);
        }
    }
    
    public GameTile[][] getBoard(){
        return board;
    }
    
    public GameTile getTile(final int x, final int y){
        return board[y][x];
    }
    
    public GameCharacter getCharacter(){
        return link;
    }
    
    public ArrayList<GameEnemy> getEnemies(){
        return enemies;
    }
    
    public synchronized boolean executePlayerCommand(Command command) throws InterruptedException {
        boolean result = false;
        boolean mooved;
        GameTile attacked;
        switch(command){
            case Left: case Right: case Up: case Down:
                mooved = link.move(command);
                //System.out.println("Spostamento avvenuto, mooved = " + mooved);
                if(mooved == true){
                    gameView.moveAnimation(link, this);
                    return true;
                }
                //link.showPosition();
                break;
            case Sword:
                attacked = link.attack();
                gameView.attackAnimation(link, this, attacked != null);
                if(attacked != null){
                    combat(link, attacked);
                }
                return true;
            case Special:
                if(readySpecial == true){
                    ArrayList<GameTile> attackedPeople = link.special();
                    gameView.specialAnimation(link, this, attackedPeople != null);
                    if(attackedPeople != null){
                        for(int i = 0; i < attackedPeople.size(); i++){
                            combat(link, attackedPeople.get(i));
                        }
                    }
                    resetCoolDown();
                    return true;
                }
                System.out.println("Non disponibile, cooldown: " + coolDown);
                break;
            case Bow:
                if(arrows != 0){
                    updateArrows();
                    attacked = link.bow();
                    gameView.bowAnimation(link, this, attacked != null);
                    if(attacked != null){
                        combat(link, attacked);
                    }
                    return true;
                }
                break;
        }
        gameView.endedAnimationCharacter = true;
        return result;
    }
    
    public void resetCoolDown(){
        readySpecial = false;
        coolDown = 2;
        gameView.notReadySpecial();
        gameView.updateCoolDown(coolDown);
    }
    
    public void updateCoolDown(){
        if(readySpecial == false){
            coolDown--;
            if(coolDown == 0){
                readySpecial = true;
                gameView.removeCoolDown();
                gameView.readySpecial();
                return;
            }
            gameView.updateCoolDown(coolDown);
        }
    }
    
    public boolean getReadySpecial(){
        return readySpecial;
    }
    
    public int getCoolDown(){
        return coolDown;
    }
    
    public void resetArrows(){
        arrows = 2;
        gameView.updateArrows(arrows);
        gameView.readyBow();
    }
    
    public void updateArrows(){
        arrows--;
        gameView.updateArrows(arrows);
        if(arrows == 0){
            gameView.notReadyBow();
        }
    }
    
    public int getArrows(){
        return arrows;
    }
    
    //Versione in cui Link attacca
    public void combat(GameCharacter attacker, GameTile attackedTile){
        kill(attackedTile.getOccupierEnemy());
        gameView.killAnimation(attackedTile, this);
        attackedTile.free();
    }
    
    public void kill(GameEnemy attacked){
        enemies.remove(attacked);
        points += 10;
        if(enemies.isEmpty() || enemies.size() == 0){
            waveMagnitude++;
            spawnWave();
        }
    }
    
    public void EnemiesTurn(int i){
        if(i >= enemies.size()) //Finisce il turno avversario
        {
            gameView.endedAnimationEnemies = true;
            return;
        }
        else
        {
            System.out.println("Nemico numero: " + i);
            enemies.get(i).turn(gameView);
        }
    }
    
    public void attackedCharacter(){
        lives--;
        gameView.updateLives(lives);
        if(lives == 0){
            System.out.println("Hai perso");
            ended = true;
        }
    }
    
    public void resetLives(){
        lives = 3;
        gameView.resetLives();
    }
    
    public int getLives(){
        return lives;
    }
    
    private void clearBoard(){
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                board[i][j].free();
            }
        }
    }
    
    private void clear(){
        link = null;
        enemies.clear();
        clearBoard();
    }
    
    public void endGame(){
        ended = true;
        clear();
        
        gameView.readyBow();
        gameView.removeArrows();
        
        gameView.readySpecial();
        gameView.removeCoolDown();
        
        EventLoggerXML.recordEvent(EventLoggerXML.eventDescriptionEndGame);
    }
    
    public boolean isEnded(){
        return this.ended;
    }
    
    public GameTile firstEncounterOnAxis(int x, int y, Command direction){
        int yModifier = 0, xModifier = 0;
        switch(direction){
            case Left:
                xModifier = -1;
                break;
            case Right:
                xModifier = 1;
                break;
            case Up:
                yModifier = -1;
                break;
            case Down:
                yModifier = 1;
        }
        
        GameTile target = null;
        while(true){
            x += xModifier; y += yModifier;
            
            if(x < 0 || x >= WIDTH)
                break;
            if(y < 0 || y >= HEIGHT)
                break;
            
            if(board[y][x].occupiedEnemy == true){
                target = board[y][x];
                break;
            }
        }
        return target;
    }
    
    public void rebuildFromCache(CacheData cacheData){
        ended = false;
        
        user = cacheData.gameCacheData.getUser();
        points = cacheData.gameCacheData.getPoints();
        
        lives = cacheData.gameCacheData.getLives();
        for(int i = 3; i > lives; i--){
            gameView.updateLives(i - 1);
        }
        
        waveMagnitude = cacheData.gameCacheData.getWaveMagnitude();
        
        ElementCacheData temp;
        for(int i = 0; i < cacheData.elements.size(); i++){
            temp = cacheData.elements.get(i);
            if(temp.enemy == false){
                spawnCharacter(temp.x, temp.y, temp.direction, this);
            }
            else{
                spawnEnemy(temp.x, temp.y, temp.direction, this);
            }
        }
        
        arrows = cacheData.gameCacheData.getArrows();
        coolDown = cacheData.gameCacheData.getCoolDown();
        readySpecial = cacheData.gameCacheData.getReadySpecial();
        
        gameView.updateArrows(arrows);
        if(arrows == 0)
            gameView.notReadyBow();
        if(coolDown > 0){
            gameView.notReadySpecial();
            gameView.updateCoolDown(coolDown);
        }
    }
}