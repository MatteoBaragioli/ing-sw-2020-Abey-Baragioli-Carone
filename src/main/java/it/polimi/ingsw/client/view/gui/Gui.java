package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.network.CommunicationChannel;
import it.polimi.ingsw.network.CommunicationProtocol;
import javafx.animation.FadeTransition;
import it.polimi.ingsw.network.objects.BoxProxy;
import it.polimi.ingsw.network.objects.GodCardProxy;
import it.polimi.ingsw.network.objects.PlayerProxy;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

    // fade in and out transition of the menu Scene
    FadeTransition fadeMenu;

    //All pages Pane
    StackPane scene = new StackPane();


    StackPane openingPage = new StackPane();
    HBox menuPage = new HBox();
    StackPane loadingPage = new StackPane();
    AnchorPane matchPage = new AnchorPane();
    private String ip;
    private int port;
    private String nickname;
    private int numberOfPlayers;


    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        windowStyle();
        //openingPage = openingPage();
        //loadingPage = loadingPage();



        menuScene = new MenuScene(window, menuPage, screenWidth, screenHeight, loadingPage);

        scene.getChildren().addAll(openingPage, menuPage);
        openingPage.setVisible(false);
        menuPage.setVisible(false);
        menuScene.setMenuScene();
        menuScene.askIp(this);

        Scene fullScene = new Scene(scene);
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
        return menuScene.ip();
    }

    @Override
    public void askMatchType(ClientController clientController, CommunicationChannel communicationChannel) {
        menuScene.askNumberOfPlayers(clientController, communicationChannel);
    }

    @Override
    public int askPort() {
        return menuScene.port();
    }

    @Override
    public void askUserName(ClientController clientController, CommunicationChannel communicationChannel) {
        menuScene.askNickname(clientController, communicationChannel);
    }

    @Override
    public String askUserName() {
        return null;
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

    }

    @Override
    public void connectionRefused(String host, ConnectException e) {

    }
}
