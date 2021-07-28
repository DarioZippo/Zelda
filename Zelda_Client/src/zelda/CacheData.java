package zelda;

import java.io.Serializable;
import java.util.ArrayList;

public class CacheData implements Serializable {
    private String user;
    
    public CacheData(String user){
        this.user = user;
    }
    
    public String getUser(){
        return user;
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