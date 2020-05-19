package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.network.CommunicationChannel;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import static java.lang.Integer.parseInt;

public class MenuScene {

    private static final Font lillybelleFont = Font.loadFont(ConfirmBox.class.getResourceAsStream("/fonts/LillyBelle.ttf"), 20);
    private static final Font readyFont = Font.loadFont(ConfirmBox.class.getResourceAsStream("/fonts/LillyBelle.ttf"), 25);
    private static final Font errorFont = Font.loadFont(ConfirmBox.class.getResourceAsStream("/fonts/LillyBelle.ttf"), 15);
    private final Stage primaryWindow;
    private final HBox menuPage;
    private final double screenWidth;
    private final double screenHeight;
    private final StackPane loadingPage;
    private Text formText = new Text();
    private Text matchTypeText = new Text("Number of players");
    private TextField formField = new TextField();
    private VBox formView;
    private Group formGroup;
    private Group playGroup;
    private ImageView playView;
    private String nickname;
    private String ip;
    private int port;
    private int matchType;

    Button confirmButton = new Button("Next");
    Button nextButton = new Button("Next");
    ToggleGroup numberOfPlayers;
    HBox numberOfPlayersOptions;
    VBox matchTypeNumber = new VBox();

    public MenuScene(Stage primaryWindow, HBox menuPage, double screenWidth, double screenHeight, StackPane loadingPage) {
        this.primaryWindow = primaryWindow;
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

    public HBox menuPage() {
        return menuPage;
    }

    //_______________________________________________END GETTER__________________________________________________________


    public void setMenuScene() {
        //Menu Box Background
        StackPane menuBox = menuGroup();

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

        formView = new VBox();
        formView.setAlignment(Pos.CENTER);
        formView.setSpacing(20);
        formView.setPadding(new Insets(0, 0, screenHeight / 6, 0));
        formView.getChildren().addAll(formText, formField, confirmButton);
        formGroup.getChildren().add(formView);


        matchTypeText.setFont(lillybelleFont);
        RadioButton twoPlayers = new RadioButton("2");
        RadioButton threePlayers = new RadioButton("3");
        twoPlayers.setCursor(Cursor.HAND);
        threePlayers.setCursor(Cursor.HAND);
        twoPlayers.setFont(lillybelleFont);
        threePlayers.setFont(lillybelleFont);

        numberOfPlayers = new ToggleGroup();
        twoPlayers.setToggleGroup(numberOfPlayers);
        twoPlayers.setSelected(false);
        threePlayers.setToggleGroup(numberOfPlayers);
        threePlayers.setSelected(false);

        nextButton.setPrefWidth(screenWidth / 8);
        nextButton.setPrefHeight(screenHeight / 20);
        nextButton.setCursor(Cursor.HAND);
        nextButton.setFont(lillybelleFont);

        numberOfPlayersOptions = new HBox();
        numberOfPlayersOptions.getChildren().addAll(twoPlayers, threePlayers);
        numberOfPlayersOptions.setSpacing(60);
        numberOfPlayersOptions.setAlignment(Pos.CENTER);
        matchTypeNumber.setAlignment(Pos.CENTER);
        matchTypeNumber.setSpacing(20);
        matchTypeNumber.setPadding(new Insets(0, 0, screenHeight / 6, 0));
        matchTypeNumber.getChildren().addAll(matchTypeText, numberOfPlayersOptions, nextButton);
        matchTypeNumber.setVisible(false);

        Group quitGroup = quitGroup(menuPage);
        playGroup = playGroup();
        menuBox.getChildren().addAll(formGroup, matchTypeNumber);



        menuPage.getChildren().addAll(quitGroup, menuBox, playGroup);
        menuPage.setBackground(background());
        menuPage.setAlignment(Pos.CENTER);
        playGroup.setVisible(false);
        FadeTransition menuFade = new FadeTransition(Duration.millis(3000), menuPage);
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
        Image menuBoxImg = new Image(Gui.class.getResource("/img/menu_box_background.png").toString(), screenWidth / 2, screenHeight / 1.1, false, false);
        ImageView menuBoxView = new ImageView(menuBoxImg);
        menuBoxView.setEffect(new DropShadow(10, Color.BLACK));
        StackPane menuBox = new StackPane();
        menuBox.setAlignment(Pos.CENTER);
        menuBox.getChildren().add(menuBoxView);
        return menuBox;
    }

    //-----------------------------------------------Form and Communication------------------------------------------------------------
    public void askIp(Gui gui) {
        formText.setText("Ip Address");
        formField.clear();
        confirmButton.setOnMouseClicked(e -> {
            setIp(formField.getText());
            confirmButton.setPrefWidth(screenWidth / 16);
            askPort(gui);
        });
    }

    public void askPort(Gui gui) {
        formText.setText("Port Number");
        formField.clear();
        confirmButton.setOnMouseClicked(e -> {
            try {
                setPort(Integer.parseInt(formField.getText()));
            } catch (Exception notInt) {

            }
            gui.startClient();
        });
    }


    public void askNickname(ClientController clientController, CommunicationChannel communicationChannel) {
        formText.setText("Nickname");
        formField.clear();
        confirmButton.setOnMouseClicked(e -> {
            setNickname(formField.getText());
            formView.getChildren().remove(formField);
            clientController.writeUsername(communicationChannel, nickname);
        });
    }

    public void askNumberOfPlayers(ClientController clientController, CommunicationChannel communicationChannel) {
        formView.setVisible(false);
        matchTypeNumber.setVisible(true);
        nextButton.setOnMouseClicked(e -> {
            RadioButton selectedRadioButton = (RadioButton) numberOfPlayers.getSelectedToggle();
            String number = selectedRadioButton.getText();
            if (number.equals("2")) {
                setmatchType(1);
            } else {
                setmatchType(2);
            }
            activatePlayButton(clientController, communicationChannel);
            readyForm();
        });
    }

    public void readyForm() {
        matchTypeNumber.setVisible(false);
        formView.setVisible(true);
        formText.setText("You're ready to play!");
        confirmButton.setVisible(false);
    }


    //-----------------------------------------------End Form and Communication------------------------------------------------------------



    //-----------------------------------------------Loading Page------------------------------------------------------------

    private void setLoadingPage(){
        Image loadingGif = new Image(Gui.class.getResource("/img/loading.gif").toString(), screenWidth/4, screenHeight/4, false, false);
        ImageView loadingImageGif = new ImageView(loadingGif);
        Image loadingImg = new Image(Gui.class.getResource("/img/loading.png").toString(), screenWidth, screenHeight, false, false);
        ImageView loadingImageView = new ImageView(loadingImg);
        loadingPage.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        loadingPage.getChildren().addAll(loadingImageGif, loadingImageView);
        loadingPage.setVisible(true);
    }

    //-----------------------------------------------End Loading Page------------------------------------------------------------



    //-----------------------------------------------Play Button------------------------------------------------------------


    private void activatePlayButton(ClientController clientController, CommunicationChannel communicationChannel){
        playGroup.setVisible(true);
        Image playClickImg = new Image(Gui.class.getResource("/img/play_clicked.png").toString(),screenWidth/8, screenHeight/4, false, false);
        playView.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            playView.setImage(playClickImg);
            clientController.askMatchType(communicationChannel, matchType);
            setLoadingPage();
            event.consume();
        });
    }

    private ImageView playButton(){
        Image playImg = new Image(Gui.class.getResource("/img/play.png").toString(), screenWidth/8, screenHeight/4, false, false);
        Image playHoverImg = new Image(Gui.class.getResource("/img/play_hover.png").toString(),screenWidth/8, screenHeight/4, false, false);
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

    private  ImageView quitButton(HBox fullPage){
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



    private Group quitGroup(HBox fullPage){
        ImageView quitView = quitButton(fullPage);

        quitView.setEffect(new DropShadow(10, Color.BLACK));

        Group quitGroup = new Group();
        quitGroup.setAutoSizeChildren(true);
        quitGroup.getChildren().addAll(quitView);
        quitGroup.prefWidth(screenWidth/8);
        return quitGroup;
    }

    //-----------------------------------------------END Quit Button------------------------------------------------------------




    public void closeProgram(HBox fullPage){
        boolean answer = ConfirmBox.display("Quit?", "Sure you want to quit the game?", screenWidth, screenHeight);
        if(answer)
            primaryWindow.close();
        else
            fullPage.setEffect(new BoxBlur(0, 0, 0));
    }
}
