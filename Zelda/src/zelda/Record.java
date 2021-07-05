package zelda;

import javafx.beans.property.*;

public class Record {
    private final SimpleStringProperty user;
    private final SimpleIntegerProperty points;
    
    private Record(String currentUser, int currentPoints){
        user = new SimpleStringProperty(currentUser);
        points = new SimpleIntegerProperty(currentPoints);
    }
    
    public String getUser(){
        return user.get();
    }
    
    public int getPoints(){
        return points.get();
    }
}
