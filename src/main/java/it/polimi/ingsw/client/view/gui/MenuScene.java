package it.polimi.ingsw.client.view.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class MenuScene {
    private static final Font lillybelleFont = Font.loadFont(ConfirmBox.class.getResourceAsStream("/fonts/LillyBelle.ttf"), 25);
    private Stage primaryWindow;
    private double screenWidth;
    private double screenHeight;
    private Scene loadingPage;
    private Group formGroup;
    private String nickname;
    private StackPane menuPage;

    public MenuScene(Stage primaryWindow, double screenWidth, double screenHeight, Scene loadingPage){
        this.primaryWindow = primaryWindow;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.loadingPage = loadingPage;
        nickname = "";
        menuPage = new StackPane();
    }




    //_________________________________________________SETTER____________________________________________________________

    private void setNickname(String nickname){
        this.nickname = nickname;
    }

    //_______________________________________________END SETTER__________________________________________________________




    //_________________________________________________GETTER____________________________________________________________

    public StackPane menuPage(){
        return menuPage;
    }

    //_______________________________________________END GETTER__________________________________________________________


    public void setMenuPage() {
        //Background
        Group background = background();

        //Menu Box Background
        Group menuBox = menuBackground();

        //form
        formGroup = new Group();
        formGroup.prefWidth(screenWidth/4);
        formGroup.prefHeight(screenHeight/2);

        //play and quit buttons

        Group playGroup = playGroup(loadingPage);
        Group quitGroup = quitGroup(menuPage);


        menuPage.getChildren().addAll(background, menuBox, quitGroup, playGroup, formGroup);
        StackPane.setAlignment(formGroup, Pos.CENTER);
        StackPane.setAlignment(menuBox, Pos.CENTER);
        StackPane.setAlignment(quitGroup, Pos.CENTER_LEFT);
        StackPane.setAlignment(playGroup, Pos.CENTER_RIGHT);

        Scene menuScene = new Scene(menuPage);
        primaryWindow.setScene(menuScene);
    }

    private Group background(){
        Image backgroundImg = new Image(GuiApp.class.getResource("/img/background.png").toString(), screenWidth, screenHeight, false, false);
        ImageView imageView = new ImageView(backgroundImg);

        Group root = new Group();
        root.getChildren().addAll(imageView);
        root.setAutoSizeChildren(true);
        return root;
    }

    private Rectangle menu(){
        //Drawing a Rectangle
        Rectangle rectangle = new Rectangle(screenWidth/2, screenHeight/1.1, screenWidth/2, screenHeight/1.1);

        Image menuBackgroundImg = new Image(GuiApp.class.getResource("/img/menu_box_background.png").toString(), screenWidth/2, screenHeight/1.1, false, false);
        rectangle.setFill(new ImagePattern(menuBackgroundImg));
        rectangle.setEffect(new DropShadow(100, Color.BLACK));
        rectangle.setArcWidth(30.0);
        rectangle.setArcHeight(30.0);

        return rectangle;
    }

    private Group menuBackground(){
        Rectangle menuRectangle = menu();
        Group menuBox = new Group();
        menuBox.setAutoSizeChildren(true);
        menuBox.getChildren().addAll(menuRectangle);
        return menuBox;
    }


    public void askIp(){
        Text ipText = new Text("Dammi sto cazzo di Ip");
        Button button = new Button("schiaccia");
        formGroup.getChildren().addAll(ipText, button);
    }

    public String askNickname(){
        Text nicknameText = new Text("Nickname");
        TextField nicknameField = new TextField();
        nicknameField.setCursor(Cursor.TEXT);
        nicknameText.setFont(lillybelleFont);
        nicknameField.setFont(lillybelleFont);
        Button send = new Button("Confirm");
        send.setCursor(Cursor.HAND);
        send.setFont(lillybelleFont);
        send.setOnMouseClicked(e -> {
            formGroup.getChildren().removeAll();
            setNickname(nicknameField.getText());
        });
        formGroup.getChildren().addAll(nicknameText, nicknameField, send);
        return nickname;
    }













    private ImageView playButton(Scene loading){
        Image playImg = new Image(GuiApp.class.getResource("/img/play.png").toString(), screenWidth/8, screenHeight/4, false, false);
        Image playHoverImg = new Image(GuiApp.class.getResource("/img/play_hover.png").toString(),screenWidth/8, screenHeight/4, false, false);
        Image playClickImg = new Image(GuiApp.class.getResource("/img/play_clicked.png").toString(),screenWidth/8, screenHeight/4, false, false);
        ImageView playView = new ImageView(playImg);
        playView.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
            playView.setImage(playHoverImg);
            playView.setCursor(Cursor.HAND);
            event.consume();
        });
        playView.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, event -> {
            playView.setImage(playImg);
            event.consume();
        });
        playView.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            playView.setImage(playClickImg);
            if(true) { //todo se il nickname non esiste già vai avanti se no richiedi
                /*nickname = form.getNickname();
                numberOfPlayers = form.getNumberOfPlayers();
                window.setScene(loading);

                MatchPage match = new MatchPage();
                match.create(screenWidth, screenHeight, nickname, numberOfPlayers);
                matchPage = match.getMatchPage();
                Scene matchScene = new Scene(matchPage);
                window.setScene(matchScene);
                match(match);*/
            }
            event.consume();
        });
        return playView;
    }

    private  ImageView quitButton(StackPane fullPage){
        Image quitImg = new Image(GuiApp.class.getResource("/img/quit_normal.png").toString(), screenWidth/8, screenHeight/4, false, false);
        Image quitHoverImg = new Image(GuiApp.class.getResource("/img/quit_hover.png").toString(),screenWidth/8, screenHeight/4, false, false);
        Image quitClickImg = new Image(GuiApp.class.getResource("/img/quit_clicked.png").toString(),screenWidth/8, screenHeight/4, false, false);
        ImageView quitView = new ImageView(quitImg);

        quitView.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
            quitView.setImage(quitHoverImg);
            quitView.setCursor(Cursor.HAND);
            event.consume();
        });

        quitView.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, event -> {
            quitView.setImage(quitImg);
            event.consume();
        });

        quitView.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            quitView.setImage(quitClickImg);
            fullPage.setEffect(new BoxBlur(5, 10, 10));
            event.consume();
        });

        quitView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            closeProgram(fullPage);
            event.consume();
        });

        return quitView;
    }

    private Group playGroup(Scene loading){
        ImageView playView = playButton(loading);

        //play box
        VBox playBox = new VBox();
        playBox.setPadding(new Insets(screenHeight/4, screenWidth/4, screenHeight/4, screenWidth/4));
        playBox.setSpacing(10);
        playBox.getChildren().addAll(playView);
        playBox.setAlignment(Pos.CENTER_LEFT);

        //play group
        Group playGroup = new Group();
        playGroup.setAutoSizeChildren(true);
        playGroup.getChildren().addAll(playBox);
        playGroup.prefWidth(screenWidth/4);
        playGroup.prefHeight(screenHeight/2);
        return playGroup;
    }

    private Group quitGroup(StackPane fullPage){
        ImageView quitView = quitButton(fullPage);

        //play box
        VBox quitBox = new VBox();
        quitBox.setPadding(new Insets(screenHeight/4, screenWidth/4, screenHeight/4, screenWidth/4));
        quitBox.setSpacing(10);
        quitBox.getChildren().addAll(quitView);
        quitBox.setAlignment(Pos.CENTER_RIGHT);

        //play group
        Group quitGroup = new Group();
        quitGroup.setAutoSizeChildren(true);
        quitGroup.getChildren().addAll(quitBox);
        quitGroup.prefWidth(screenWidth/4);
        quitGroup.prefHeight(screenHeight/2);
        return quitGroup;
    }

    public void closeProgram(StackPane fullPage){
        boolean answer = ConfirmBox.display("Quit?", "Sure you want to quit the game?", primaryWindow.getWidth(), primaryWindow.getHeight());
        if(answer)
            primaryWindow.close();
        else
            fullPage.setEffect(new BoxBlur(0, 0, 0));
    }
}
