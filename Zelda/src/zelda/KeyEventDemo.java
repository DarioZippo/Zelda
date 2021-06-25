/*package zelda;

import java.net.URL;
import javafx.application.*;
import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import static zelda.BoardUtils.HEIGHT;
import static zelda.BoardUtils.WIDTH;

public class KeyEventDemo extends Application implements EventHandler<KeyEvent>{
    Text text = new Text();
    
    @Override
    public void start(Stage window) throws Exception {            
        StackPane root = new StackPane();
        root.getChildren().add(text);
        
        Scene scene = new Scene(root, 400, 400);
        scene.setOnKeyPressed(this);
        
        window.setScene(scene);
        window.show();
    }
    
    @Override
    public void handle(KeyEvent event){
        text.setText(event.getCode().toString());
    }
}*/