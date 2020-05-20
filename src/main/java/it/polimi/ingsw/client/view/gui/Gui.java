package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.network.CommunicationProtocol;
import javafx.animation.FadeTransition;
import it.polimi.ingsw.network.objects.BoxProxy;
import it.polimi.ingsw.network.objects.GodCardProxy;
import it.polimi.ingsw.network.objects.PlayerProxy;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import it.polimi.ingsw.network.objects.BoxProxy;
import it.polimi.ingsw.network.objects.GodCardProxy;
import it.polimi.ingsw.network.objects.PlayerProxy;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.List;

public class Gui extends Application implements View {

    private double screenWidth = Screen.getPrimary().getBounds().getWidth();
    private double screenHeight = Screen.getPrimary().getBounds().getHeight();
    private Font lillybelleFont = Font.loadFont(Gui.class.getResourceAsStream("/fonts/LillyBelle.ttf"), 25);
    private static final int mapRowsNumber = 5;
    private static final int mapColumnsNumber = 5;
    Client client = new Client(this);
    Stage window;
    MenuScene menuScene;



    //All pages Pane
    StackPane firstScene = new StackPane();


    StackPane openingPage = new StackPane();

    HBox menuPage = new HBox();

    StackPane loadingPage = new StackPane();

    StackPane transitionClouds = new StackPane();

    TranslateTransition translateLeftTransitionIn;
    TranslateTransition translateLeftTransitionOut;
    TranslateTransition translateRightTransitionIn;
    TranslateTransition translateRightTransitionOut;


    AnchorPane matchPage = new AnchorPane();
    private String ip;
    private int port;
    private String nickname;
    private int numberOfPlayers;


    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        windowStyle();



        firstScene.getChildren().addAll(openingPage, loadingPage, menuPage, transitionClouds);
        openingPage.setVisible(true);
        menuPage.setVisible(false);
        loadingPage.setVisible(false);
        transitionClouds.setVisible(false);


        //openingPage();

        //if not opening page todo toglierlo
        openingPage.setVisible(false);
        menuScene = new MenuScene(this, window, menuPage, screenWidth, screenHeight, loadingPage);
        menuScene.setMenuScene();
        startClient();


        createTransitionClouds();

        Scene fullScene = new Scene(firstScene);
        window.setScene(fullScene);

        window.show();
    }



    public void windowStyle(){
        //window.setMaximized(true);
        window.setWidth(1280);
        window.setHeight(720);
        screenWidth = 1280;
        screenHeight = 720;
        window.initStyle(StageStyle.UNDECORATED);
        window.setTitle("Santorini");
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram(menuScene().menuPage());
        });
        window.setResizable(true);
    }

    public void startClient(){
        client.start();
    }


    private void closeProgram(HBox fullPage){
        boolean answer = ConfirmBox.display("Quit?", "Sure you want to quit the game?", window.getWidth(), window.getHeight());
        if(answer)
            window.close();
        else
            fullPage.setEffect(new BoxBlur(0, 0, 0));
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
                    menuScene = new MenuScene(this, window, menuPage, screenWidth, screenHeight, loadingPage);
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
    }


    public MenuScene menuScene(){
        return menuScene;
    }


    @Override
    public int askBox(List<int[]> boxes) {
        return 0;
    }

    @Override
    public int askCards(List<GodCardProxy> cards) {
        return 0;
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
        return menuScene.askNumberOfPlayers();
    }

    @Override
    public int askPort() {
        return menuScene.askPort();
    }

    @Override
    public String askUserName() {
        return menuScene.askNickname();
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

    }

    @Override
    public void setOpponentsInfo(List<PlayerProxy> players) {

    }

    @Override
    public void connectionLost() {

    }

    @Override
    public void unknownHost(String host, UnknownHostException e) {
        menuScene().setErrorMessage1("Host does not exist");
    }

    @Override
    public void connectionRefused(String host, ConnectException e) {
        menuScene().setErrorMessage1("Connection refused");
    }
}
