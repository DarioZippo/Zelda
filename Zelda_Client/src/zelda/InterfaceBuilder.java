package zelda;

import java.util.ArrayList;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class InterfaceBuilder {
    private Pane root;
    private ArrayList<ImageView> hearts;
    private TextField specialTextField;
    private TextField bowTextField;
    private TextField loginTextField;
    private Button loginButton;
    private TableView<Record> ranking;
    
    public TextField getSpecialTextField(){
        return specialTextField;
    }
    
    public TextField getBowTextField(){
        return bowTextField;
    }
    
    public Button getLoginButton(){
        return loginButton;
    }
    
    public TextField getLoginTextField(){
        return loginTextField;
    }
    
    public TableView<Record> getRanking(){
        return ranking;
    }
    
    public Parent createContent(GameView gameView){
        root = new Pane();
        createCharacterStat();
        gameView.setHearts(hearts);
        createKeyTutorial();
        createLogin();
        createRanking();
        
        return root;
    }
    
    private void createCharacterStat(){
        Pane characterStat = new Pane();
        characterStat.setLayoutY(600);
        characterStat.getStyleClass().add("characterStat");
        
        Image linkHeadImage = new Image("file:myFiles/img/linkHead.png");
        ImageView linkHead = new ImageView(linkHeadImage);
        linkHead.setFitHeight(55.0);
        linkHead.setFitWidth(60.0);
        linkHead.setLayoutX(122); 
        linkHead.setLayoutY(14);
        linkHead.setId("LinkHead");
        
        characterStat.getChildren().add(linkHead);
        
        Image fullHeartImage = new Image("file:myFiles/img/fullHeart.png");
        
        hearts = new ArrayList<ImageView>(); 
        
        ImageView heart0 = new ImageView(fullHeartImage);
        heart0.setFitHeight(50.0);
        heart0.setFitWidth(50.0);
        heart0.setLayoutX(75); 
        heart0.setLayoutY(86);
        heart0.getStyleClass().add("heart");
        
        hearts.add(heart0);
        
        ImageView heart1 = new ImageView(fullHeartImage);
        heart1.setFitHeight(50.0);
        heart1.setFitWidth(50.0);
        heart1.setLayoutX(125); 
        heart1.setLayoutY(86);
        heart1.getStyleClass().add("heart");
        
        hearts.add(heart1);
        
        ImageView heart2 = new ImageView(fullHeartImage);
        heart2.setFitHeight(50.0);
        heart2.setFitWidth(50.0);
        heart2.setLayoutX(176); 
        heart2.setLayoutY(86);
        heart2.getStyleClass().add("heart");
        
        hearts.add(heart2);
        characterStat.getChildren().addAll(hearts);
        
        root.getChildren().add(characterStat);
    }
    
    private void createKeyTutorial(){
        Pane keyTutorial = new Pane();
        keyTutorial.setLayoutX(300);
        keyTutorial.setLayoutY(600);
        keyTutorial.getStyleClass().add("keyTutorial");
        
        Image keyDownImage = new Image("file:myFiles/img/keyDown.png");
        ImageView keyDown = new ImageView(keyDownImage);
        keyDown.setFitHeight(35.0);
        keyDown.setFitWidth(35.0);
        keyDown.setLayoutX(233); 
        keyDown.setLayoutY(111);
        
        keyTutorial.getChildren().add(keyDown);
        
        Image keyUpImage = new Image("file:myFiles/img/keyUp.png");
        ImageView keyUp = new ImageView(keyUpImage);
        keyUp.setFitHeight(35.0);
        keyUp.setFitWidth(35.0);
        keyUp.setLayoutX(233); 
        keyUp.setLayoutY(76);
        
        keyTutorial.getChildren().add(keyUp);
        
        Image keyRightImage = new Image("file:myFiles/img/keyRight.png");
        ImageView keyRight = new ImageView(keyRightImage);
        keyRight.setFitHeight(35.0);
        keyRight.setFitWidth(35.0);
        keyRight.setLayoutX(268); 
        keyRight.setLayoutY(111);
        
        keyTutorial.getChildren().add(keyRight);
        
        Image keyLeftImage = new Image("file:myFiles/img/keyLeft.png");
        ImageView keyLeft = new ImageView(keyLeftImage);
        keyLeft.setFitHeight(35.0);
        keyLeft.setFitWidth(35.0);
        keyLeft.setLayoutX(198); 
        keyLeft.setLayoutY(111);
        
        keyTutorial.getChildren().add(keyLeft);
        
        Image keyZImage = new Image("file:myFiles/img/keyZ.png");
        ImageView keyZ = new ImageView(keyZImage);
        keyZ.setFitHeight(35.0);
        keyZ.setFitWidth(35.0);
        keyZ.setLayoutX(83); 
        keyZ.setLayoutY(5);        
        
        TextField keyZText = new TextField("SWORD");
        keyZText.setLayoutX(58);
        keyZText.setLayoutY(40);
        keyZText.setDisable(true);
        keyZText.getStyleClass().add("keyText");
        
        keyTutorial.getChildren().addAll(keyZ, keyZText);
        
        Image keyCImage = new Image("file:myFiles/img/keyC.png");
        ImageView keyC = new ImageView(keyCImage);
        keyC.setFitHeight(35.0);
        keyC.setFitWidth(35.0);
        keyC.setLayoutX(233); 
        keyC.setLayoutY(5);
        
        specialTextField = new TextField("SPECIAL");
        specialTextField.setLayoutX(208);
        specialTextField.setLayoutY(40);
        specialTextField.setPrefHeight(25);
        specialTextField.setPrefWidth(86);
        specialTextField.setDisable(true);
        specialTextField.getStyleClass().add("keyText");
        
        keyTutorial.getChildren().addAll(keyC, specialTextField);
        
        Image keyXImage = new Image("file:myFiles/img/keyX.png");
        ImageView keyX = new ImageView(keyXImage);
        keyX.setFitHeight(35.0);
        keyX.setFitWidth(35.0);
        keyX.setLayoutX(385); 
        keyX.setLayoutY(5);
        
        bowTextField = new TextField("BOW");
        bowTextField.setLayoutX(360);
        bowTextField.setLayoutY(40);
        bowTextField.setDisable(true);
        bowTextField.getStyleClass().add("keyText");
        
        keyTutorial.getChildren().addAll(keyX, bowTextField);
        
        root.getChildren().add(keyTutorial);
    }
    
    private void createLogin(){
        Pane login = new Pane();
        login.setLayoutX(800);
        login.setLayoutY(600);
        login.getStyleClass().add("login");
        
        loginTextField = new TextField();
        loginTextField.setLayoutX(76);
        loginTextField.setLayoutY(50);
        loginTextField.setPromptText("USERNAME");
        loginTextField.setFocusTraversable(false);
        loginTextField.getStyleClass().add("loginTextField");
        
        loginButton = new Button();
        loginButton.setLayoutX(101);
        loginButton.setLayoutY(75);
        loginButton.setMnemonicParsing(false);
        loginButton.setText("PLAY");
        loginButton.getStyleClass().add("loginButton");
        
        login.getChildren().addAll(loginTextField, loginButton);
        
        root.getChildren().add(login);
    }
    
    private void createRanking(){
        VBox rankingWrapper = new VBox();
        rankingWrapper.setLayoutX(800);
        rankingWrapper.setLayoutY(0);
        rankingWrapper.getStyleClass().add("rankingWrapper");
        
        Label rankingTitle = new Label("Records");
        rankingTitle.getStyleClass().add("rankingTitle");
        
        TableColumn user = new TableColumn("Utente");
        user.setCellValueFactory(new PropertyValueFactory<>("user"));
        user.getStyleClass().add("column");
        
        TableColumn points = new TableColumn("Punteggio");
        points.setCellValueFactory(new PropertyValueFactory<>("points"));
        points.getStyleClass().add("column");
        
        ranking = new TableView<>();
        ranking.setItems(Zelda.records);
        ranking.getColumns().addAll(user, points);
        ranking.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); 
        ranking.getStyleClass().add("ranking");
        
        rankingWrapper.getChildren().addAll(rankingTitle, ranking);
        
        root.getChildren().add(rankingWrapper);
    }
    
}
