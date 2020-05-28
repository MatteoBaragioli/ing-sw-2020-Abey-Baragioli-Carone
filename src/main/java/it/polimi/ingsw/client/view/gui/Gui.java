package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.server.model.Colour;
import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import javafx.animation.FadeTransition;
import it.polimi.ingsw.network.objects.BoxProxy;
import it.polimi.ingsw.network.objects.GodCardProxy;
import it.polimi.ingsw.network.objects.PlayerProxy;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Gui extends Application implements View {

    private double screenWidth = Screen.getPrimary().getBounds().getWidth();
    private double screenHeight = Screen.getPrimary().getBounds().getHeight();
    private final Font lillybelleFont = Font.loadFont(Gui.class.getResourceAsStream("/fonts/LillyBelle.ttf"), screenWidth/80);
    private static final int mapRowsNumber = 5;
    private static final int mapColumnsNumber = 5;
    private final Client client = new Client(this);
    private Stage window;
    private MenuScene menuScene;
    private MatchScene matchScene;




    private final StackPane mainScene = new StackPane();


    private final StackPane openingPage = new StackPane();

    private final HBox menuPage = new HBox();

    private final StackPane loadingPage = new StackPane();

    private final StackPane howToPlayBox = new StackPane();

    private final StackPane transitionClouds = new StackPane();

    private final StackPane confirmPopup = new StackPane();

    private Text questionPopup;

    private Button yesPopupButton;

    private Button noPopupButton;

    private TranslateTransition translateLeftTransitionIn;
    private TranslateTransition translateRightTransitionIn;


    private StackPane matchPage = new StackPane();



    private String nickname;
    private String color;
    private int numberOfPlayers;
    private List<PlayerProxy> opponents = new ArrayList<>();


    @Override
    public void start(Stage primaryStage){
        window = primaryStage;
        windowStyle();
        confirmPopup();


        mainScene.getChildren().addAll(openingPage, menuPage, loadingPage, matchPage, transitionClouds, howToPlayBox, confirmPopup);
        openingPage.setVisible(true);
        menuPage.setVisible(false);
        loadingPage.setVisible(false);
        transitionClouds.setVisible(false);
        confirmPopup.setVisible(false);
        confirmPopup.setAlignment(Pos.CENTER);
        matchPage.setVisible(false);
        howToPlayBox.setVisible(false);



        openingPage();


        //if not opening page todo toglierlo
/*        openingPage.setVisible(false);
        menuScene = new MenuScene(this, menuPage, screenWidth, screenHeight, loadingPage, howToPlayBox);
        menuScene.setMenuScene();
        startClient();
*/
        createTransitionClouds();
        matchScene = new MatchScene(this, screenWidth, screenHeight, matchPage);

        Scene fullScene = new Scene(mainScene);
        window.setScene(fullScene);

        window.show();
    }



    public void windowStyle(){
        window.setMaximized(true);
        /*window.setWidth(1280);
        window.setHeight(720);
        screenWidth = 1280;
        screenHeight = 720;*/
        window.initStyle(StageStyle.UNDECORATED);
        window.setTitle("Santorini");
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
        window.setResizable(true);
    }

    public void startClient(){
        client.start();
    }


    public void closeProgram(){
        yesPopupButton.setOnAction(e -> {
            window.close();
            try {
                client.end();
            } catch (ChannelClosedException ex) {
                ex.printStackTrace();
                System.err.println("Connection Lost");
            }

        });
        noPopupButton.setOnAction(e -> {
            menuPage.setEffect(new BoxBlur(0, 0, 0));
            openingPage.setEffect(new BoxBlur(0, 0, 0));
            loadingPage.setEffect(new BoxBlur(0, 0, 0));
            confirmPopup.setVisible(false);
        });
        createConfirmPopup("Do you really want to quit the game?");
    }

    private void confirmPopup(){
        confirmPopup.setPrefWidth(screenWidth/4);
        confirmPopup.setPrefHeight(screenHeight/4);

        Image confirmFrame = new Image(Gui.class.getResource("/img/frame.png").toString(), screenWidth/2, screenHeight/2, false, false);
        ImageView confirmView = new ImageView(confirmFrame);

        VBox confirmBox = new VBox();

        questionPopup = new Text();
        questionPopup.setFont(lillybelleFont);

        HBox answers = new HBox();
        yesPopupButton = new Button("Yes");
        noPopupButton = new Button("No");

        yesPopupButton.setFont(lillybelleFont);
        yesPopupButton.setCursor(Cursor.HAND);
        noPopupButton.setFont(lillybelleFont);
        noPopupButton.setCursor(Cursor.HAND);



        answers.getChildren().addAll(yesPopupButton, noPopupButton);
        answers.setSpacing(50);
        answers.setAlignment(Pos.CENTER);




        confirmBox.setSpacing(10);
        confirmBox.getChildren().addAll(questionPopup, answers);

        confirmPopup.getChildren().addAll(confirmView, confirmBox);
        confirmPopup.setAlignment(Pos.CENTER);
        confirmBox.setAlignment(Pos.CENTER);

    }

    private void createConfirmPopup(String question){
        questionPopup.setText(question);
        menuPage.setEffect(new BoxBlur(5, 10, 10));
        openingPage.setEffect(new BoxBlur(5, 10, 10));
        loadingPage.setEffect(new BoxBlur(5, 10, 10));
        confirmPopup.setVisible(true);
    }


    private void openingPage(){
        Image openingImg = new Image(Gui.class.getResource("/img/opening/opening.png").toString(), screenWidth, screenHeight, false, false);
        ImageView openingView = new ImageView(openingImg);
        Image logoImg = new Image(Gui.class.getResource("/img/opening/logo.png").toString(), screenWidth/2, screenHeight/2, false, false);
        ImageView logoView = new ImageView(logoImg);

        openingPage.getChildren().addAll(openingView, logoView);
        openingPage.setAlignment(Pos.CENTER);
        openingView.setVisible(true);
        logoView.setVisible(false);


        FadeTransition openingFadeIn = new FadeTransition(Duration.millis(2000), openingPage);
        openingFadeIn.setFromValue(0.0);
        openingFadeIn.setToValue(1.0);
        FadeTransition openingFadeOut = new FadeTransition(Duration.millis(2000), openingPage);
        openingFadeOut.setFromValue(1.0);
        openingFadeOut.setToValue(0.0);


        openingFadeIn.play();

        Timeline firstTimer = new Timeline(new KeyFrame(
                Duration.millis(2000),
                ae -> {
                    openingFadeOut.play();
                }));
        firstTimer.play();

        Timeline secondTimer = new Timeline(new KeyFrame(
                Duration.millis(4000),
                ae -> {
                    openingFadeIn.play();
                    logoView.setVisible(true);
                    openingView.setVisible(false);
                }));
        secondTimer.play();

        Timeline thirdTimer = new Timeline(new KeyFrame(
                Duration.millis(6000),
                ae -> {
                    openingFadeOut.play();
                }));
        thirdTimer.play();

        Timeline menuTimer = new Timeline(new KeyFrame(
                Duration.millis(8000),
                ae -> {
                    openingPage.setVisible(false);
                    menuScene = new MenuScene(this, menuPage, screenWidth, screenHeight, loadingPage, howToPlayBox);
                    menuScene.setMenuScene();
                    client.start();
                }));
        menuTimer.play();
    }

    private void createTransitionClouds(){
        Image leftCloudImg = new Image(Gui.class.getResource("/img/transitionCloudLeft.png").toString(), screenWidth, screenHeight*2, false, false);
        ImageView leftCloud = new ImageView(leftCloudImg);
        Image rightCloudImg = new Image(Gui.class.getResource("/img/transitionCloudRight.png").toString(), screenWidth, screenHeight*2, false, false);
        ImageView rightCloud = new ImageView(rightCloudImg);

        transitionClouds.getChildren().addAll(rightCloud, leftCloud);

        translateLeftTransitionIn = new TranslateTransition(Duration.millis(2000), leftCloud);
        translateLeftTransitionIn.setFromX(-screenWidth);
        translateLeftTransitionIn.setByX(screenWidth-screenWidth/4);
        translateLeftTransitionIn.setAutoReverse(true);

        translateRightTransitionIn = new TranslateTransition(Duration.millis(2000), rightCloud);
        translateRightTransitionIn.setFromX(screenWidth);
        translateRightTransitionIn.setByX(-screenWidth+screenWidth/4);
        translateRightTransitionIn.setAutoReverse(true);
    }

    public void playTransitionClouds(){
        transitionClouds.setVisible(true);
        translateLeftTransitionIn.setCycleCount(2);
        translateLeftTransitionIn.play();
        translateRightTransitionIn.setCycleCount(2);
        translateRightTransitionIn.play();
        Timeline cloudsTimer = new Timeline(new KeyFrame(
                Duration.millis(4000),
                ae -> {
                    transitionClouds.setVisible(false);
                }));
        cloudsTimer.play();
    }

    public StackPane howToPlayBox(){
        return howToPlayBox;
    }

    public MenuScene menuScene(){
        return menuScene;
    }

    public int mapRowsNumber() {
        return mapRowsNumber;
    }

    public int mapColumnsNumber() {
        return mapColumnsNumber;
    }

    @Override
    public int askPosition(List<int[]> positions) {
        return 0;
    }

    @Override
    public int askCards(List<GodCardProxy> cards) {
        Platform.runLater(() -> matchScene.chooseCard(cards));
        return matchScene.chosenCard();
    }

    @Override
    public int[] askDeck(List<GodCardProxy> cards) {
        Platform.runLater(() -> matchScene.chooseCards(cards));
        return matchScene.chosenCards();
    }

    @Override
    public boolean askConfirmation() {
        return false;
    }

    @Override
    public String askIp() {
        return menuScene.askIp();
    }

    @Override
    public int askMatchType() {
        numberOfPlayers = menuScene.askNumberOfPlayers();
        return numberOfPlayers;
    }

    @Override
    public int askPort() {
        return menuScene.askPort();
    }

    @Override
    public String askUserName() {
        nickname = menuScene.askNickname();
        return nickname;
    }


    @Override
    public int askWorker(List<int[]> workers) {
        return 0;
    }

    @Override
    public void prepareAdditionalCommunication(CommunicationProtocol key) {

    }

    @Override
    public void updateMap(List<BoxProxy> boxes) {

    }

    @Override
    public void setMyPlayer(PlayerProxy player) {
        if(player.godCardProxy==null){
            nickname = player.name;
            if(player.colour.equals(Colour.BLUE))
                color = "Blue";
            else if(player.colour.equals(Colour.WHITE))
                color = "White";
            else if(player.colour.equals(Colour.GREY))
                color = "Grey";
        } else {
            matchScene.playerView().setMyCard(player);
        }
    }

    @Override
    public void setOpponentsInfo(List<PlayerProxy> opponents) {
        if(opponents.get(0).godCardProxy==null) {
            this.opponents = opponents;
            startMatch();
        }
        else
            matchScene.playerView().setOpponentsCards(opponents);
    }

    @Override
    public void connectionLost() {

    }

    @Override
    public void connectionFailed(String host) {
        menuScene().setErrorMessage("Connection refused");
    }

    @Override
    public void startMatch(){
        AtomicBoolean ready = new AtomicBoolean(false);
        playTransitionClouds();

        Platform.runLater(() -> matchScene.setMatchScene(nickname, numberOfPlayers, color, opponents));
        FadeTransition loadingFadeOut = new FadeTransition(Duration.millis(2000), loadingPage);
        loadingFadeOut.setFromValue(1);
        loadingFadeOut.setToValue(0);
        loadingFadeOut.play();

        Timeline matchTimer = new Timeline(new KeyFrame(
                Duration.millis(2000),
                ae -> {
                    loadingPage.setVisible(false);
                    FadeTransition matchFade = new FadeTransition(Duration.millis(3000), matchPage);
                    matchFade.setFromValue(0.0);
                    matchPage.setVisible(true);
                    matchFade.setToValue(1.0);
                    matchFade.play();
                    ready.set(true);
                }));
        matchTimer.play();

        while(!ready.get()){

        }
    }
}
