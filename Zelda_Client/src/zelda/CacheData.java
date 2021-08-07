package zelda;

import java.io.Serializable;
import java.util.ArrayList;

public class CacheData implements Serializable {
    public GameCacheData gameCacheData;
    public ArrayList<ElementCacheData> elements;
    
    public CacheData(GameModel gameModel, TurnHandler turnHandler) {
        gameCacheData = new GameCacheData(gameModel, turnHandler);
        
        elements = new ArrayList<ElementCacheData>();
        elements.add( new ElementCacheData(false, 
                                            gameModel.getCharacter().getX(), 
                                            gameModel.getCharacter().getY(), 
                                            gameModel.getCharacter().getDirection() 
                    ));
        
        GameEnemy temp;
        for(int i = 0; i < gameModel.getEnemies().size(); i++){
            temp = gameModel.getEnemies().get(i);
            elements.add(new ElementCacheData(true, 
                                            temp.getX(), 
                                            temp.getY(), 
                                            temp.getDirection() 
                        ));
        }
    }
}

/*
    private int points;
    
    private GameCharacter link;
    private ArrayList<GameEnemy> enemies;
    
    private int linkLives;
    
    public CacheData(String user, 
            int points, 
            GameCharacter link,
            ArrayList<GameEnemy> enemies,
            int linkLives) {
        this.user = user;
        this.points = points;
        this.link = link;
        this.enemies = enemies;
        this.linkLives = linkLives;
    }
    */