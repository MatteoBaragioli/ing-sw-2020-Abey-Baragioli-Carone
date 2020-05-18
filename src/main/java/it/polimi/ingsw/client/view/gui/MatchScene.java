package it.polimi.ingsw.client.view.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MatchScene {

    private final Stage primaryWindow;
    private final double screenWidth;
    private final double screenHeight;
    private final AnchorPane matchPage = new AnchorPane();
    private PlayerView playerView;
    private GuiMap guiMap;
    private final int[] chosenWorkerPosition = new int[2];
    private String chosenWorkerColor;
    private String chosenWorkerGender;
    private final int[] chosenWorkerNewPosition = new int[2];
    private int chosenBoxIndex;
    private List<int[]> chosableBoxes = new ArrayList<>();



    public MatchScene(Stage primaryWindow, double screenWidth, double screenHeight) {
        this.primaryWindow = primaryWindow;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    //_________________________________________________SETTER____________________________________________________________


    public void setChosableBoxes(List<int[]> chosableBoxes) {
        this.chosableBoxes = chosableBoxes;
    }

    public void setChosenBoxIndex(int chosenBoxIndex) {
        this.chosenBoxIndex = chosenBoxIndex;
    }

    public void setChosenWorkerPosition(int x, int y) {
        chosenWorkerPosition[0] = x;
        chosenWorkerPosition[1] = y;
    }

    public void setChosenWorkerColor(String chosenWorkerColor) {
        this.chosenWorkerColor = chosenWorkerColor;
    }

    public void setChosenWorkerGender(String chosenWorkerGender) {
        this.chosenWorkerGender = chosenWorkerGender;
    }

    public void setChosenWorkerNewPosition(int x, int y) {
        chosenWorkerNewPosition[0] = x;
        chosenWorkerNewPosition[1] = y;
    }


    //_______________________________________________END SETTER__________________________________________________________


    //_________________________________________________GETTER____________________________________________________________


    public List<int[]> chosableBoxes() {
        return chosableBoxes;
    }

    public int chosenBoxIndex() {
        return chosenBoxIndex;
    }

    public int[] chosenWorkerPosition() {
        return chosenWorkerPosition;
    }

    public String chosenWorkerColor() {
        return chosenWorkerColor;
    }

    public String chosenWorkerGender() {
        return chosenWorkerGender;
    }

    public int[] chosenWorkerNewPosition() {
        return chosenWorkerNewPosition;
    }

    public PlayerView playerView(){
        return  playerView;
    }

    public GuiMap map(){
        return guiMap;
    }


    //_______________________________________________END GETTER__________________________________________________________





    public void clearChosableBoxes(){
        chosableBoxes = new ArrayList<>();
    }







    public void setMatchScene(String nickname, int numberOfPlayers, String color) {
        double dimension = screenHeight - screenHeight / 20;
        double marginDim = dimension / 28.05421687;
        double totalMargin = dimension / 6.504189944;
        double mapDim = dimension - (2 * marginDim);


        guiMap = createMap(dimension, mapDim, totalMargin, screenWidth, screenHeight);

        Image mapImg = new Image(MatchScene.class.getResource("/img/board.png").toString(),dimension,dimension,false,false);
        ImageView mapView = new ImageView(mapImg);

        AnchorPane.setTopAnchor(guiMap, (screenHeight-dimension)/2);
        AnchorPane.setBottomAnchor(guiMap, (screenHeight-dimension)/2);
        AnchorPane.setLeftAnchor(guiMap, (screenWidth-dimension)/2);
        AnchorPane.setRightAnchor(guiMap, (screenWidth-dimension)/2);
        AnchorPane.setTopAnchor(mapView, (screenHeight-dimension)/2);
        AnchorPane.setBottomAnchor(mapView, (screenHeight-dimension)/2);
        AnchorPane.setLeftAnchor(mapView, (screenWidth-dimension)/2);
        AnchorPane.setRightAnchor(mapView, (screenWidth-dimension)/2);

        playerView = new PlayerView();


        playerView.create(nickname, color, numberOfPlayers, screenWidth, screenHeight);
        StackPane playerViewGroup = playerView.getPlayerViewStackPane();
        setPlayerViewPosition(playerViewGroup, screenHeight);

        ImageView matchBackground = matchBackground(screenWidth, screenHeight);

        matchPage.getChildren().addAll(matchBackground,mapView, playerViewGroup, guiMap);
        matchPage.setPrefWidth(screenWidth);
        matchPage.setPrefHeight(screenHeight);

        Scene matchScene = new Scene(matchPage);
        primaryWindow.setScene(matchScene);
    }

    public GuiMap createMap(double dimension, double mapDim, double totalMargin, double screenWidth, double screenHeight){
        GuiMap guiMapCreation = new GuiMap(5,5, mapDim, screenWidth, screenHeight);
        guiMapCreation.setPrefSize(dimension, dimension);
        guiMapCreation.setAlignment(Pos.CENTER);
        Insets margin = new Insets(totalMargin);
        guiMapCreation.setPadding(margin);

        return guiMapCreation;
    }

    public void setPlayerViewPosition(StackPane playerView, double screenHeight){
        AnchorPane.setBottomAnchor(playerView, (screenHeight)/1000);
    }

    public ImageView matchBackground(double screenWidth, double screenHeight){
        Image matchBackgroundImg = new Image(MatchScene.class.getResource("/img/match_background.png").toString(),screenWidth,screenHeight,false,false);
        return new ImageView(matchBackgroundImg);
    }

    public void chooseDestination(List<int[]> possibleDestinations){

        //todo Il server mi manderà una lista e vorrà indietro l'indice della lista come risposta
        int index = 0;
        for(int[] i : possibleDestinations){
            map().box(i[0], i[1]).setIndex(index);
            map().box(i[0], i[1]).setAsChosable(this, possibleDestinations, 1);
            index++;
        }
    }
    public void chooseBuild(List<int[]> possibleDestinations){

        //todo Il server mi manderà una lista e vorrà indietro l'indice della lista come risposta
        int index = 0;
        for(int[] i : possibleDestinations){
            map().box(i[0], i[1]).setIndex(index);
            map().box(i[0], i[1]).setAsChosable(this, possibleDestinations, 2);
            index++;
        }
    }
}
