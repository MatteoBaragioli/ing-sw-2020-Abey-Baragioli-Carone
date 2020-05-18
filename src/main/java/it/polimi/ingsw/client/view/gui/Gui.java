package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.server.model.Box;
import it.polimi.ingsw.server.model.GodCard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Worker;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;

public class Gui extends Application implements View {

    private double screenWidth = Screen.getPrimary().getBounds().getWidth();
    private double screenHeight = Screen.getPrimary().getBounds().getHeight();
    private Font lillybelleFont = Font.loadFont(Gui.class.getResourceAsStream("/fonts/LillyBelle.ttf"), 25);
    private static final int mapRowsNumber = 5;
    private static final int mapColumnsNumber = 5;
    Client client = new Client();
    Stage window;
    Scene menu;
    Scene loadingScene;
    MenuScene menuScene;
    StackPane openingPage = new StackPane();
    StackPane menuPage = new StackPane();
    StackPane loadingPage = new StackPane();
    AnchorPane matchPage = new AnchorPane();
    private String ip;
    private int port;
    private String nickname;
    private int numberOfPlayers;





    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        windowStyle();
        //openingPage = openingPage();
        //loadingPage = loadingPage();



        menuScene = new MenuScene(window, screenWidth, screenHeight);
        menuScene.setMenuScene(this);
        menuScene.askIp();

        window.show();
    }



    public void windowStyle(){
        //window.setMaximized(true);
        window.setWidth(1280);
        window.setHeight(720);
        screenWidth = 1280;
        screenHeight = 720;
        window.initStyle(StageStyle.DECORATED);
        window.setTitle("Santorini");
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram(menuScene().menuPage());
        });
        window.setResizable(true);
    }

    public void startClient(){
        client.start(this);
    }


    private void closeProgram(StackPane fullPage){
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
    public int askBox(List<Box> boxes) {
        return 0;
    }

    @Override
    public int askCards(List<GodCard> cards) {
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
    public int askMatchType() {
        return menuScene.matchType();
    }

    @Override
    public int askPort() {
        return menuScene.port();
    }

    @Override
    public String askUserName() {
        return menuScene.nickname();
    }

    @Override
    public int askWorker(List<Worker> workers) {
        return 0;
    }

    @Override
    public void prepareAdditionalCommunication(CommunicationProtocol key) {

    }

    @Override
    public void updateMap(List<Box> boxes) {

    }

    @Override
    public void setPlayersInfo(List<Player> players) {

    }
}
