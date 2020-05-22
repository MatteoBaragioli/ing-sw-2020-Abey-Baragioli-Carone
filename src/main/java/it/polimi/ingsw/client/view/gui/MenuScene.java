package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.network.CommunicationChannel;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicBoolean;

public class MenuScene {

    private static final Font lillybelleFont = Font.loadFont(MenuScene.class.getResourceAsStream("/fonts/LillyBelle.ttf"), 20);
    private static final Font errorFont = Font.loadFont(MenuScene.class.getResourceAsStream("/fonts/LillyBelle.ttf"), 13);
    private final Gui gui;
    private final HBox menuPage;
    private final double screenWidth;
    private final double screenHeight;
    private final StackPane loadingPage;
    private final Text formText = new Text();
    private final Text errorMessage = new Text();
    private final Text matchTypeText = new Text("Number of players");
    private final TextField formField = new TextField();
    private VBox formView;
    private Group formGroup;
    private Group playGroup;
    private ImageView playView;
    private String nickname;
    private String ip;
    private int port;
    private int matchType;
    private ImageView loadingIcon;
    private Button confirmButton = new Button("Next");
    private ToggleGroup numberOfPlayers;
    private HBox numberOfPlayersOptions;
    private VBox matchTypeNumber = new VBox();

    public MenuScene(Gui gui, HBox menuPage, double screenWidth, double screenHeight, StackPane loadingPage) {
        this.gui = gui;
        this.menuPage = menuPage;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.loadingPage = loadingPage;
        menuPage.setPrefWidth(screenWidth);
        menuPage.setPrefHeight(screenHeight);


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

    public void setErrorMessage1(String message){
        errorMessage.setText(message);
        errorMessage.setVisible(true);
        errorMessage.setManaged(true);
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


    //_______________________________________________END GETTER__________________________________________________________


    public void setMenuScene() {
        //Menu Box Background
        StackPane menuBox = menuGroup();

        //form
        formGroup = new Group();
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
        errorMessage.setFont(errorFont);
        errorMessage.setFill(Color.DARKRED);
        errorMessage.setTextAlignment(TextAlignment.CENTER);
        Image loadingImg = new Image(MenuScene.class.getResource("/img/loadingIcon.gif").toString(), screenWidth / 20, screenHeight / 16, false, false);
        loadingIcon = new ImageView(loadingImg);

        formView = new VBox();
        formView.setAlignment(Pos.CENTER);
        formView.setSpacing(20);
        formView.setPadding(new Insets(0, 0, screenHeight / 9, 0));
        formView.getChildren().addAll(formText, formField, confirmButton, loadingIcon, errorMessage);
        formGroup.getChildren().add(formView);
        errorMessage.setVisible(false);
        errorMessage.setManaged(false);
        loadingIcon.setVisible(false);
        loadingIcon.setManaged(false);

        matchTypeText.setFont(lillybelleFont);
        RadioButton twoPlayers = new RadioButton("2");
        RadioButton threePlayers = new RadioButton("3");
        twoPlayers.setCursor(Cursor.HAND);
        threePlayers.setCursor(Cursor.HAND);
        twoPlayers.setFont(lillybelleFont);
        threePlayers.setFont(lillybelleFont);

        numberOfPlayers = new ToggleGroup();
        twoPlayers.setToggleGroup(numberOfPlayers);
        twoPlayers.setSelected(true);
        threePlayers.setToggleGroup(numberOfPlayers);
        threePlayers.setSelected(false);
        numberOfPlayersOptions = new HBox();
        numberOfPlayersOptions.getChildren().addAll(twoPlayers, threePlayers);
        numberOfPlayersOptions.setSpacing(60);
        numberOfPlayersOptions.setAlignment(Pos.CENTER);
        matchTypeNumber.setAlignment(Pos.CENTER);
        matchTypeNumber.setSpacing(20);
        matchTypeNumber.setPadding(new Insets(0, 0, screenHeight / 8, 0));
        matchTypeNumber.getChildren().addAll(matchTypeText, numberOfPlayersOptions);
        matchTypeNumber.setVisible(false);
        errorMessage.setWrappingWidth(screenWidth/8);

        Group quitGroup = quitGroup();
        playGroup = playGroup();
        menuBox.getChildren().addAll(formGroup, matchTypeNumber);




        menuPage.getChildren().addAll(quitGroup, menuBox, playGroup);
        menuPage.setBackground(background());
        menuPage.setAlignment(Pos.CENTER);
        playGroup.setVisible(false);
        FadeTransition menuFade = new FadeTransition(Duration.millis(1000), menuPage);
        menuFade.setFromValue(0.0);
        menuPage.setVisible(true);
        menuFade.setToValue(1.0);
        menuFade.play();
    }

    private Background background() {
        BackgroundImage backgroundImage = new BackgroundImage(new Image(MenuScene.class.getResource("/img/background.png").toString(), screenWidth, screenHeight, false, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        return new Background(backgroundImage);
    }

    private StackPane menuGroup() {
        Image menuBoxImg = new Image(MenuScene.class.getResource("/img/menu_box_background.png").toString(), screenWidth / 2, screenHeight / 1.1, false, false);
        ImageView menuBoxView = new ImageView(menuBoxImg);
        menuBoxView.setEffect(new DropShadow(10, Color.BLACK));
        StackPane menuBox = new StackPane();
        menuBox.setAlignment(Pos.CENTER);
        menuBox.getChildren().add(menuBoxView);
        return menuBox;
    }

    //-----------------------------------------------Form and Communication------------------------------------------------------------
    public String askIp() {
        loadingIcon.setVisible(false);
        confirmButton.setVisible(true);
        AtomicBoolean clicked = new AtomicBoolean(false);
        setIp(null);
        formText.setText("Ip Address");
        formField.clear();
        confirmButton.setOnMouseClicked(e -> {
            setIp(formField.getText());
            errorMessage.setVisible(false);
            clicked.set(true);
        });
        while(!clicked.get()){

        }

        return ip();
    }

    public int askPort() {
        setPort(0);
        AtomicBoolean clicked = new AtomicBoolean(false);
        AtomicBoolean valid = new AtomicBoolean(false);
        formText.setText("Port Number");
        while (!valid.get()){
            clicked.set(false);
            formField.clear();
            confirmButton.setOnMouseClicked(e -> {
                try {
                    setPort(Integer.parseInt(formField.getText()));
                    errorMessage.setVisible(false);
                    errorMessage.setManaged(false);
                    if(port()>1023)
                        valid.set(true);
                    else
                        setPort(0);
                } catch (Exception notInt) {
                    errorMessage.setVisible(true);
                    errorMessage.setManaged(true);
                    setErrorMessage1("Port must be a number");
                }
                clicked.set(true);
            });
            while (!clicked.get()){
            }
        }
        confirmButton.setVisible(false);
        confirmButton.setManaged(false);
        loadingIcon.setVisible(true);
        loadingIcon.setManaged(true);
        return port();
    }

    public String askNickname() {
        if(nickname!=null){
            setErrorMessage1("Nickname already used");
        }
        loadingIcon.setVisible(false);
        loadingIcon.setManaged(false);
        confirmButton.setVisible(true);
        confirmButton.setManaged(true);
        AtomicBoolean clicked = new AtomicBoolean(false);
        formText.setText("Nickname");
        formField.clear();
        confirmButton.setOnMouseClicked(e -> {
            setNickname(formField.getText());
            clicked.set(true);
            errorMessage.setVisible(false);
            errorMessage.setManaged(false);
        });
        while (!clicked.get()){
        }
        return nickname();
    }

    public int askNumberOfPlayers() {
        AtomicBoolean clicked = new AtomicBoolean(false);
        formView.setVisible(false);
        matchTypeNumber.setVisible(true);
        playGroup.setVisible(true);
        Image playClickImg = new Image(MenuScene.class.getResource("/img/play_clicked.png").toString(),screenWidth/8, screenHeight/4, false, false);
        playView.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            RadioButton selectedRadioButton = (RadioButton) numberOfPlayers.getSelectedToggle();
            String number = selectedRadioButton.getText();
            if (number.equals("2")) {
                setmatchType(1);
            } else {
                setmatchType(2);
            }
            playView.setImage(playClickImg);
            setLoadingPage();
            clicked.set(true);
            event.consume();
        });
        while (!clicked.get()){

        }
        return matchType();
    }

    //-----------------------------------------------End Form and Communication------------------------------------------------------------



    //-----------------------------------------------Loading Page------------------------------------------------------------

    private void setLoadingPage(){
        Image loadingGif = new Image(MenuScene.class.getResource("/img/loading.gif").toString(), screenWidth/4, screenHeight/4, false, false);
        ImageView loadingImageGif = new ImageView(loadingGif);
        Image loadingImg = new Image(MenuScene.class.getResource("/img/loading.png").toString(), screenWidth, screenHeight, false, false);
        ImageView loadingImageView = new ImageView(loadingImg);
        loadingPage.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        Image quitLoadingImg = new Image(MenuScene.class.getResource("/img/quit_normal.png").toString(), screenWidth/12, screenHeight/8, false, false);
        Image quitLoadingHoverImg = new Image(MenuScene.class.getResource("/img/quit_hover.png").toString(),screenWidth/12, screenHeight/8, false, false);
        Image quitLoadingClickImg = new Image(MenuScene.class.getResource("/img/quit_clicked.png").toString(),screenWidth/12, screenHeight/8, false, false);
        ImageView quitLoading = new ImageView(quitLoadingImg);

        quitLoading.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
            quitLoading.setImage(quitLoadingHoverImg);
            quitLoading.setCursor(Cursor.HAND);
            event.consume();
        });

        quitLoading.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, event -> {
            quitLoading.setImage(quitLoadingImg);
            event.consume();
        });

        quitLoading.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            quitLoading.setImage(quitLoadingClickImg);
            gui.closeProgram();
            event.consume();
        });
        StackPane quitPane = new StackPane();
        quitPane.setAlignment(Pos.TOP_LEFT);
        quitPane.getChildren().add(quitLoading);
        loadingPage.getChildren().addAll(loadingImageGif, loadingImageView, quitPane);
        gui.playTransitionClouds();
        FadeTransition menuFadeOut = new FadeTransition(Duration.millis(2000), menuPage);
        menuFadeOut.setFromValue(1);
        menuFadeOut.setToValue(0);
        menuFadeOut.play();

        Timeline loadingTimer = new Timeline(new KeyFrame(
                Duration.millis(2000),
                ae -> {
                    menuPage.setVisible(false);
                    FadeTransition loadingFade = new FadeTransition(Duration.millis(4000), loadingPage);
                    loadingFade.setFromValue(0.0);
                    loadingFade.setToValue(1.0);
                    loadingFade.play();
                    loadingPage.setVisible(true);
                }));
        loadingTimer.play();

        Timeline momentaneoTimer = new Timeline(new KeyFrame(
                Duration.millis(10000),
                ae -> {
                    gui.startMatch();
                }));
        momentaneoTimer.play();
    }

    //-----------------------------------------------End Loading Page------------------------------------------------------------



    //-----------------------------------------------Play Button------------------------------------------------------------


    private ImageView playButton(){
        Image playImg = new Image(MenuScene.class.getResource("/img/play.png").toString(), screenWidth/8, screenHeight/4, false, false);
        Image playHoverImg = new Image(MenuScene.class.getResource("/img/play_hover.png").toString(),screenWidth/8, screenHeight/4, false, false);
        playView = new ImageView(playImg);
        playView.setEffect(new DropShadow(10, Color.BLACK));
        playView.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
            playView.setImage(playHoverImg);
            playView.setCursor(Cursor.HAND);
            event.consume();
        });
        playView.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, event -> {
            playView.setImage(playImg);
            event.consume();
        });
        return playView;
    }

    private Group playGroup(){
        ImageView playView = playButton();

        Group playGroup = new Group();
        playGroup.setAutoSizeChildren(true);
        playGroup.getChildren().addAll(playView);
        playGroup.prefWidth(screenWidth/8);
        return playGroup;
    }

    //-----------------------------------------------END Play Button------------------------------------------------------------




    //-----------------------------------------------Quit Button------------------------------------------------------------

    private  ImageView quitButton(){
        Image quitImg = new Image(MenuScene.class.getResource("/img/quit_normal.png").toString(), screenWidth/8, screenHeight/4, false, false);
        Image quitHoverImg = new Image(MenuScene.class.getResource("/img/quit_hover.png").toString(),screenWidth/8, screenHeight/4, false, false);
        Image quitClickImg = new Image(MenuScene.class.getResource("/img/quit_clicked.png").toString(),screenWidth/8, screenHeight/4, false, false);
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

        quitView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            quitView.setImage(quitClickImg);
            gui.closeProgram();
            event.consume();
        });

        return quitView;
    }



    private Group quitGroup(){
        ImageView quitView = quitButton();

        quitView.setEffect(new DropShadow(10, Color.BLACK));

        Group quitGroup = new Group();
        quitGroup.setAutoSizeChildren(true);
        quitGroup.getChildren().addAll(quitView);
        quitGroup.prefWidth(screenWidth/8);
        return quitGroup;
    }

    //-----------------------------------------------END Quit Button------------------------------------------------------------

}
