package zelda;

import static java.lang.Thread.sleep;
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
    
    private int linkLives;
    private int coolDown;
    private boolean readySpecial;
    private int arrows;
    
    public boolean ended;
    
    public GameModel(GameView gameView){
        createContent();
        
        linkLives = 3;
        
        coolDown = 0;
        readySpecial = true;
        
        enemies = new ArrayList<GameEnemy>();
        
        this.gameView = gameView;
        ended = false;
    }
    
    private void createContent() {
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
    
    public void start(){
        points = 0;
        waveMagnitude = 1;
        spawn();
        resetLives();
        resetArrows();
        EventLoggerXML.recordEvent(EventLoggerXML.eventDescriptionStartGame);
    }
    
    public void spawn(){
        spawnCharacter((WIDTH / 2) - 1, (HEIGHT / 2) - 1, this);
        for(int i = 0; i < waveMagnitude; i++){
            randomSpawnEnemy(this);
        }
    }
    
    public void spawnWave(){
        for(int i = 0; i < waveMagnitude; i++){
            randomSpawnEnemy(this);
        }
    }
    
    private void spawnCharacter(final int coordinateX, final int coordinateY, GameModel gameModel) {
        if(board[coordinateY][coordinateX].occupied == false)
        {
            link = new GameCharacter(coordinateX, coordinateY, gameModel);
            
            board[coordinateY][coordinateX].occupieCharacter(link);
            
            gameView.showCharacter(link);
        }
        else
            System.out.println("Spawn personaggio occupato");
    }  
    
    private boolean spawnEnemy(final int coordinateX, final int coordinateY, GameModel gameModel){
        if(board[coordinateY][coordinateX].occupied == false)
        {
            Command direction = randomDirection();
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
            spawned = spawnEnemy(x, y, gameModel);
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
                System.out.println("Spostamento avvenuto, mooved = " + mooved);
                if(mooved == true){
                    gameView.moveAnimation(link, this);
                    return true;
                }
                link.showPosition();
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
            case Arrow:
                if(arrows != 0){
                    updateArrows();
                    attacked = link.arrow();
                    gameView.bowAnimation(link, this, attacked != null);
                    if(attacked != null){
                        combat(link, attacked);
                    }
                    return true;
                }
                break;
        }
        gameView.endedAnimationCharacter = true;//gameView.update(this);
        return result;
    }
    
    public void resetCoolDown(){
        readySpecial = false;
        coolDown = 1;
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
    
    public void resetArrows(){
        arrows = 2;
        gameView.updateArrows(arrows);
        gameView.readyBow();
    }
    
    public void updateArrows(){
        arrows--;
        gameView.updateArrows(arrows);
        if(arrows == 0){
            System.out.println("A FACC RO CAZZ");
            gameView.notReadyBow();
        }
    }
    
    //Versione in cui Link attacca
    public void combat(GameCharacter attacker, GameTile attackedTile){
        kill(attackedTile.getOccupierEnemy());
        gameView.killAnimation(attackedTile, this);
        attackedTile.free();
        //attacked.free();
        //GameUpdate
        //gameView.update(this);
        //DropOggetto
    }
    
    public void kill(GameEnemy attacked){
        enemies.remove(attacked);
        points += 10;
        /*
        for(int i = 0; i < 2; i++){
            randomSpawnEnemy(this);
        }
        */
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
        linkLives--;
        gameView.updateLives(linkLives);
        if(linkLives == 0){
            System.out.println("Hai perso");
            ended = true;
        }
    }
    
    public void resetLives(){
        linkLives = 3;
        gameView.resetLives();
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
        ended = false;
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
}