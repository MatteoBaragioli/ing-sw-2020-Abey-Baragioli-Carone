package it.polimi.ingsw.client.view.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static java.lang.Integer.parseInt;

public class MenuScene {

    private static final Font lillybelleFont = Font.loadFont(ConfirmBox.class.getResourceAsStream("/fonts/LillyBelle.ttf"), 20);
    private static final Font readyFont = Font.loadFont(ConfirmBox.class.getResourceAsStream("/fonts/LillyBelle.ttf"), 25);
    private static final Font errorFont = Font.loadFont(ConfirmBox.class.getResourceAsStream("/fonts/LillyBelle.ttf"), 15);
    private final Stage primaryWindow;
    private final double screenWidth;
    private final double screenHeight;
    private Text formText = new Text();
    private TextField formField = new TextField();
    private VBox formView;
    private Group formGroup;
    private Group playGroup;
    private String nickname;
    private String ip;
    private int port;
    private int matchType;
    private StackPane menuPage;
    Button confirmButton = new Button("Next");
    Button nextButton = new Button("Next");
    Button backButton = new Button("Back");
    HBox nextBackButtons = new HBox();

    public MenuScene(Stage primaryWindow, double screenWidth, double screenHeight) {
        this.primaryWindow = primaryWindow;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        menuPage = new StackPane();


    }


    //_________________________________________________SETTER____________________________________________________________


    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private void setmatchType(int matchType) {
        this.matchType = matchType;
    }

    //_______________________________________________END SETTER__________________________________________________________


    //_________________________________________________GETTER____________________________________________________________


    public String ip() {
        return ip;
    }

    public int port() {
        return port;
    }

    public String nickname() {
        return nickname;
    }

    public int matchType() {
        return matchType;
    }

    public StackPane menuPage() {
        return menuPage;
    }

    //_______________________________________________END GETTER__________________________________________________________


    public void setMenuScene(Gui gui) {
        //Background
        Group background = background();

        //Menu Box Background
        Group menuBox = menuBackground();

        //form
        formGroup = new Group();
        formGroup.prefWidth(screenWidth / 4);
        formGroup.prefHeight(screenHeight / 2);
        formText.setFont(lillybelleFont);
        formField.setCursor(Cursor.TEXT);
        formField.setPrefWidth(screenWidth / 8);
        formField.setPrefHeight(screenHeight / 30);
        formField.setFont(lillybelleFont);
        confirmButton.setPrefWidth(screenWidth / 8);
        confirmButton.setPrefHeight(screenHeight / 20);
        confirmButton.setCursor(Cursor.HAND);
        confirmButton.setFont(lillybelleFont);
        nextButton.setPrefWidth(screenWidth / 17);
        nextButton.setPrefHeight(screenHeight / 20);
        nextButton.setCursor(Cursor.HAND);
        nextButton.setFont(lillybelleFont);
        backButton.setPrefWidth(screenWidth / 17);
        backButton.setPrefHeight(screenHeight / 20);
        backButton.setCursor(Cursor.HAND);
        backButton.setFont(lillybelleFont);
        nextBackButtons.getChildren().addAll(backButton, nextButton);
        nextBackButtons.setSpacing(20);
        formView = new VBox();
        formView.setAlignment(Pos.CENTER);
        formView.setSpacing(20);
        formView.setPadding(new Insets(0, 0, screenHeight / 6, 0));
        formView.getChildren().addAll(formText, formField, confirmButton);
        formGroup.getChildren().add(formView);

        //play and quit buttons

        playGroup = playGroup(gui);
        Group quitGroup = quitGroup(menuPage);


        menuPage.getChildren().addAll(background, menuBox, quitGroup, formGroup);
        StackPane.setAlignment(formGroup, Pos.CENTER);
        StackPane.setAlignment(menuBox, Pos.CENTER);
        StackPane.setAlignment(quitGroup, Pos.CENTER_LEFT);
        StackPane.setAlignment(playGroup, Pos.CENTER_RIGHT);

        Scene menuScene = new Scene(menuPage);
        primaryWindow.setScene(menuScene);
    }

    private Group background() {
        Image backgroundImg = new Image(Gui.class.getResource("/img/background.png").toString(), screenWidth, screenHeight, false, false);
        ImageView imageView = new ImageView(backgroundImg);

        Group root = new Group();
        root.getChildren().addAll(imageView);
        root.setAutoSizeChildren(true);
        return root;
    }

    private Rectangle menu() {
        //Drawing a Rectangle
        Rectangle rectangle = new Rectangle(screenWidth / 2, screenHeight / 1.1, screenWidth / 2, screenHeight / 1.1);

        Image menuBackgroundImg = new Image(Gui.class.getResource("/img/menu_box_background.png").toString(), screenWidth / 2, screenHeight / 1.1, false, false);
        rectangle.setFill(new ImagePattern(menuBackgroundImg));
        rectangle.setEffect(new DropShadow(100, Color.BLACK));
        rectangle.setArcWidth(30.0);
        rectangle.setArcHeight(30.0);

        return rectangle;
    }

    private Group menuBackground() {
        Rectangle menuRectangle = menu();
        Group menuBox = new Group();
        menuBox.setAutoSizeChildren(true);
        menuBox.getChildren().addAll(menuRectangle);
        return menuBox;
    }

    //-----------------------------------------------Form and Communication------------------------------------------------------------
    public void askIp() {
        formText.setText("Ip Address");
        formField.clear();
        confirmButton.setOnMouseClicked(e -> {
            setIp(formField.getText());
            confirmButton.setPrefWidth(screenWidth / 16);
            formView.getChildren().remove(confirmButton);
            formView.getChildren().add(nextBackButtons);
            askPort();
        });
    }

    public void askPort() {
        formText.setText("Port Number");
        formField.clear();
        nextButton.setOnMouseClicked(e -> {
            try {
                setPort(Integer.parseInt(formField.getText()));
            } catch (Exception notInt) {

            }
            askNickname();
        });
        backButton.setOnMouseClicked(e -> {
            formView.getChildren().remove(nextBackButtons);
            formView.getChildren().add(confirmButton);
            askIp();
        });
    }


    public void askNickname() {
        formText.setText("Nickname");
        formField.clear();
        nextButton.setOnMouseClicked(e -> {
            setNickname(formField.getText());
            formView.getChildren().remove(formField);
            askNumberOfPlayers();
        });
        backButton.setOnMouseClicked(e -> {
            askPort();
        });
    }

    public void askNumberOfPlayers() {
        formText.setText("Number of players");
        RadioButton twoPlayers = new RadioButton("2");
        RadioButton threePlayers = new RadioButton("3");
        twoPlayers.setCursor(Cursor.HAND);
        threePlayers.setCursor(Cursor.HAND);

        ToggleGroup numberOfPlayers = new ToggleGroup();
        twoPlayers.setToggleGroup(numberOfPlayers);
        twoPlayers.setSelected(false);
        threePlayers.setToggleGroup(numberOfPlayers);
        threePlayers.setSelected(false);

        HBox numberOfPlayersOptions = new HBox();
        numberOfPlayersOptions.getChildren().addAll(twoPlayers, threePlayers);
        numberOfPlayersOptions.setSpacing(60);
        numberOfPlayersOptions.setAlignment(Pos.CENTER);
        formView.getChildren().add(1, numberOfPlayersOptions);
        nextButton.setOnMouseClicked(e -> {
            RadioButton selectedRadioButton = (RadioButton) numberOfPlayers.getSelectedToggle();
            String number = selectedRadioButton.getText();
            if (number.equals("2")) {
                setmatchType(1);
            } else {
                setmatchType(2);
            }
            formView.getChildren().clear();
            readyForm();
        });
        backButton.setOnMouseClicked(e -> {
            formView.getChildren().remove(numberOfPlayersOptions);
            formView.getChildren().add(1, formField);
            askNickname();
        });
    }

    public void readyForm() {
        Text readyMessage = new Text("You're ready to play!");
        readyMessage.setFont(lillybelleFont);
        Text readyMessage2 = new Text("Click PLAY to enter lobby!");
        readyMessage2.setFont(lillybelleFont);
        Text readyMessage3 = new Text("You can click Back to change settings");
        readyMessage3.setFont(lillybelleFont);
        formView.getChildren().addAll(readyMessage, readyMessage2, readyMessage3, backButton);
        menuPage.getChildren().add(3 , playGroup);
        readyMessage2.setFont(readyFont);
        backButton.setFont(errorFont);
        backButton.setOnMouseClicked(event -> {
            formView.getChildren().clear();
            backButton.setFont(lillybelleFont);
            nextBackButtons.getChildren().clear();
            nextBackButtons.getChildren().addAll(backButton, nextButton);
            formView.getChildren().addAll(formText, nextBackButtons);
            menuPage.getChildren().remove(formGroup);
            askNumberOfPlayers();
        });
    }


    //-----------------------------------------------End Form and Communication------------------------------------------------------------



    private Scene setLoadingPage(){
        Image loadingGif = new Image(Gui.class.getResource("/img/loading.gif").toString(), screenWidth/2, screenHeight/2, false, false);
        ImageView loadingImageGif = new ImageView(loadingGif);
        Image loadingImg = new Image(Gui.class.getResource("/img/loading.png").toString(), screenWidth, screenHeight, false, false);
        ImageView loadingImageView = new ImageView(loadingImg);
        StackPane loadingPage = new StackPane();
        loadingPage.getChildren().addAll(loadingImageGif, loadingImageView);
        return new Scene(loadingPage);
    }







    private ImageView playButton(Gui gui){
        Image playImg = new Image(Gui.class.getResource("/img/play.png").toString(), screenWidth/8, screenHeight/4, false, false);
        Image playHoverImg = new Image(Gui.class.getResource("/img/play_hover.png").toString(),screenWidth/8, screenHeight/4, false, false);
        Image playClickImg = new Image(Gui.class.getResource("/img/play_clicked.png").toString(),screenWidth/8, screenHeight/4, false, false);
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
            gui.window.setScene(setLoadingPage());
            event.consume();
            gui.startClient();
        });
        return playView;
    }

    private  ImageView quitButton(StackPane fullPage){
        Image quitImg = new Image(Gui.class.getResource("/img/quit_normal.png").toString(), screenWidth/8, screenHeight/4, false, false);
        Image quitHoverImg = new Image(Gui.class.getResource("/img/quit_hover.png").toString(),screenWidth/8, screenHeight/4, false, false);
        Image quitClickImg = new Image(Gui.class.getResource("/img/quit_clicked.png").toString(),screenWidth/8, screenHeight/4, false, false);
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

    private Group playGroup(Gui gui){
        ImageView playView = playButton(gui);

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
