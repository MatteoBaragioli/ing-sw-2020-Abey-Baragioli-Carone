package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.network.objects.GodCardProxy;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MatchScene {

    private final Font lillybelleFont = Font.loadFont(MatchScene.class.getResourceAsStream("/fonts/LillyBelle.ttf"), 25);
    private final Gui gui;
    private final double screenWidth;
    private final double screenHeight;
    private final StackPane matchPage;
    private PlayerView playerView;
    private GuiMap guiMap;
    private final int[] chosenWorkerPosition = new int[2];
    private String chosenWorkerColor;
    private String chosenWorkerGender;
    private final int[] chosenWorkerNewPosition = new int[2];
    private int chosenBoxIndex;
    private List<int[]> chosableBoxes = new ArrayList<>();
    private StackPane chooseCardsBox;
    private HBox chooseCards;
    private List<ImageView> cardsView;
    private List<Text> godNames;
    private List<Text> godDescriptions;
    private List<VBox> cardsInfo;
    private List<HBox> cardBoxes;



    public MatchScene(Gui gui, double screenWidth, double screenHeight, StackPane matchPage) {
        this.gui = gui;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.matchPage = matchPage;
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

    public StackPane chooseCardsBox() {
        return chooseCardsBox;
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


        guiMap = createMap(dimension, mapDim, totalMargin);
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

        chooseCardsBox = new StackPane();


        matchPage.getChildren().addAll(matchBackground,mapView, playerViewGroup, guiMap, chooseCardsBox);
        chooseCardsBox.setVisible(false);
        matchPage.setPrefWidth(screenWidth);
        matchPage.setPrefHeight(screenHeight);

    }

    public GuiMap createMap(double dimension, double mapDim, double totalMargin){
        GuiMap guiMapCreation = new GuiMap(gui.mapRowsNumber(),gui.mapColumnsNumber(), mapDim, screenWidth, screenHeight);
        guiMapCreation.setMaxWidth(dimension);
        guiMapCreation.setMaxHeight(dimension);
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
/*
    public void createChooseCardBox(int cardsNumber){
        Image chooseCardsImg = new Image(MatchScene.class.getResource("/img/chooseCardsBackground.png").toString(), screenWidth/2, screenHeight/2, false, false);
        ImageView chooseCardsView = new ImageView(chooseCardsImg);
        ScrollPane allCards = new ScrollPane();
        allCards.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        allCards.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        VBox cardsList = new VBox();
        cardsView = new ArrayList<>();
        godNames = new ArrayList<>();
        godDescriptions = new ArrayList<>();
        cardsInfo = new ArrayList<>();
        cardBoxes = new ArrayList<>();
        for(int i = 0; i<cardsNumber; i++){
            cardsView.add(new ImageView());
            godNames.add(new Text());
            godDescriptions.add(new Text());
            cardsInfo.add(new VBox());
            cardBoxes.add(new HBox());
            cardsInfo.get(i).getChildren().addAll(godNames.get(i), godDescriptions.get(i));
            cardBoxes.get(i).getChildren().addAll(cardsView.get(i), cardsInfo.get(i));
            cardsList.getChildren().add(cardBoxes.get(i));
        }

        allCards.setContent(cardsList);
        VBox buttons = new VBox();
        Button insert = new Button();
        buttons.getChildren().add(insert);

        VBox choices = new VBox();
        //todo momentaneo
        choices.getChildren().add(new Button());

        chooseCards.getChildren().addAll(allCards,  buttons, choices);
        chooseCardsBox.getChildren().addAll(chooseCardsView);
        chooseCardsBox.setAlignment(Pos.CENTER);
        chooseCardsBox.setVisible(true);
    }

    public void chooseCards(List<GodCardProxy> godCards){
        for(int i = 0; i<godCards.size();i++){
            Image cardImg = new Image(MatchScene.class.getResource("/img/godCards/"+godCards.get(i).name+".png").toString(), screenWidth/8, screenHeight/8, false, false);
            cardsView.get(i).setImage(cardImg);
            godNames.get(i).setText(godCards.get(i).name);
            godDescriptions.get(i).setText(godCards.get(i).description);
        }

    }*/

    public void chooseCards(List<GodCardProxy> godCards){
        Image chooseCardsImg = new Image(MatchScene.class.getResource("/img/chooseCardsBackground.png").toString(), screenWidth/1.2, screenHeight/1.2, false, false);
        ImageView chooseCardsView = new ImageView(chooseCardsImg);
        ScrollPane allCards = new ScrollPane();
        allCards.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        allCards.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        allCards.getStylesheets().add(this.getClass().getResource("/css/scrollStyle.css").toExternalForm());
        VBox cardsList = new VBox();
        chooseCards = new HBox();
        chooseCards.setPrefWidth(screenWidth/1.2);
        chooseCards.setPrefHeight(screenHeight/1.2);
        chooseCards.setAlignment(Pos.CENTER);
        cardsList.setPrefWidth(screenWidth/3.2);
        cardsList.setPrefHeight(screenHeight/1.5);
        cardsList.setSpacing(20);
        for(GodCardProxy card : godCards){
            Image cardImg = new Image(MatchScene.class.getResource("/img/godCards/"+card.name+".png").toString(), screenWidth/10, screenHeight/5, false, false);
            ImageView cardView = new ImageView(cardImg);
            Text godName = new Text(card.name);
            godName.setTextAlignment(TextAlignment.CENTER);
            godName.setFont(lillybelleFont);
            Text godDescription = new Text(card.description);
            godDescription.setFont(lillybelleFont);
            godDescription.setWrappingWidth(screenWidth/5);
            VBox cardInfo = new VBox();
            cardInfo.getChildren().addAll(godName, godDescription);
            cardInfo.setAlignment(Pos.CENTER);
            HBox cardBox = new HBox();
            cardBox.getChildren().addAll(cardView, cardInfo);
            cardBox.setSpacing(20);
            cardsList.getChildren().add(cardBox);
        }

        allCards.setContent(cardsList);
        allCards.setPrefWidth(screenWidth/3.2);
        allCards.setMaxHeight(screenHeight/1.9);
        allCards.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox buttons = new VBox();
        Button insert = new Button("insert");
        buttons.getChildren().add(insert);
        buttons.setPrefWidth(screenWidth/14);
        buttons.setPrefHeight(screenHeight/1.5);

        VBox choices = new VBox();
        choices.setPrefWidth(screenWidth/3.2);
        choices.setPrefHeight(screenHeight/1.5);

        //todo momentaneo
        choices.getChildren().add(new Button());

        chooseCards.getChildren().addAll(allCards,  buttons, choices);
        chooseCardsBox.getChildren().addAll(chooseCardsView, chooseCards);
        chooseCardsBox.setAlignment(Pos.CENTER);
        chooseCardsBox.setVisible(true);

    }
}
