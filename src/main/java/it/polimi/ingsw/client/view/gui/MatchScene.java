package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.network.objects.GodCardProxy;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public class MatchScene {

    private final Font lillybelleFont = Font.loadFont(MatchScene.class.getResourceAsStream("/fonts/LillyBelle.ttf"), 25);
    private final Font godInfoFont = Font.loadFont(MatchScene.class.getResourceAsStream("/fonts/LillyBelle.ttf"), 20);
    private final Gui gui;
    private final double screenWidth;
    private final double screenHeight;
    private final StackPane matchPage;

    //number of players of the match
    private int numberOfPlayers;

    //player view (map, buttons, opponents, name,  my card)
    private PlayerView playerView;

    //map of the match
    private GuiMap guiMap;

    //current chosen worker position
    private final int[] chosenWorkerPosition = new int[2];

    //current chosen worker color
    private String chosenWorkerColor;

    //current chosen worker gender
    private String chosenWorkerGender;

    //current chosen worker new position
    private final int[] chosenWorkerNewPosition = new int[2];

    //chosen box index -> this variable will be returned as answer to server request
    private int chosenBoxIndex;

    //list of chosable boxes sent by the server
    private List<int[]> chosableBoxes = new ArrayList<>();

    //list of chosen cards by the challenger (from the list of all cards)
    private List<HBox> chosenCards = new ArrayList<>();

    //pane that is visible to the master -> the challenger has to choose the cards for the match
    private StackPane chooseCardsBox;

    //content of the chooseCardBox divided in 3 columns
    private HBox chooseCards;

    //Vertical Box that contains all chosen cards by the challenger
    private VBox choices;

    //array of names of all the chosenBoxes -> I need it to set Text when I add a card to chosen cards list
    private Text[] choicesNames;

    //array of images of all the chosenBoxes -> I need it to set Image when I add a card to chosen cards list
    private ImageView[] choicesImages;

    //array of boxes of all chosenBoxes -> I need it to set visibility to this box
    private HBox[] choicesBoxes;

    //counter that saves how many cards are chosen by the challenger
    private int choicesCounter = 0;

    //button to confirm chosen boxes by the challenger
    private ImageView confirmChoices;

    //variable that saves the currently selected card name from the entire list of all the cards
    private String selectedCard;

    //variable that saves the currently selected card box from the entire list of all the cards
    private HBox selectedCardBox;


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
        choicesNames = new Text[numberOfPlayers];
        choicesImages = new ImageView[numberOfPlayers];
        choicesBoxes = new HBox[numberOfPlayers];
        this.numberOfPlayers = numberOfPlayers;
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
        Image addGodImg = new Image(MatchScene.class.getResource("/img/buttons/addGod.png").toString(), screenWidth/12, screenHeight/12, false, false);
        Image addGodHoverImg = new Image(MatchScene.class.getResource("/img/buttons/addGodHover.png").toString(), screenWidth/12, screenHeight/12, false, false);
        Image addGodInactiveImg = new Image(MatchScene.class.getResource("/img/buttons/addGodInactive.png").toString(), screenWidth/12, screenHeight/12, false, false);
        ImageView addGod = new ImageView(addGodInactiveImg);

        Image removeGodImg = new Image(MatchScene.class.getResource("/img/buttons/removeGod.png").toString(), screenWidth/12, screenHeight/12, false, false);
        Image removeGodHoverImg = new Image(MatchScene.class.getResource("/img/buttons/removeGodHover.png").toString(), screenWidth/12, screenHeight/12, false, false);
        Image removeGodInactiveImg = new Image(MatchScene.class.getResource("/img/buttons/removeGodInactive.png").toString(), screenWidth/12, screenHeight/12, false, false);
        ImageView removeGod = new ImageView(removeGodInactiveImg);

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
        cardsList.setSpacing(60);
        cardsList.setPadding(new Insets(0,0,0,screenWidth/14));
        for(GodCardProxy card : godCards){
            Image cardImg = new Image(MatchScene.class.getResource("/img/godCards/"+card.name+".png").toString(), screenWidth/10, screenHeight/5, false, false);
            ImageView cardView = new ImageView(cardImg);
            Text godName = new Text(card.name);
            godName.setTextAlignment(TextAlignment.CENTER);
            godName.setFont(lillybelleFont);
            Text godDescription = new Text(card.description);
            godDescription.setFont(godInfoFont);
            godDescription.setWrappingWidth(screenWidth/6);
            godDescription.setTextAlignment(TextAlignment.CENTER);
            VBox cardInfo = new VBox();
            cardInfo.getChildren().addAll(godName, godDescription);
            cardInfo.setAlignment(Pos.CENTER);
            HBox cardBox = new HBox();
            cardBox.getChildren().addAll(cardView, cardInfo);
            cardBox.setSpacing(30);
            cardBox.setStyle("-fx-background-color: transparent");
            cardBox.setOnMouseEntered(e -> {
                cardBox.setStyle("-fx-border-color: black");
                cardBox.setCursor(Cursor.HAND);
            });
            cardBox.setOnMouseExited(e -> {
                cardBox.setStyle("-fx-border-color: transparent");
                cardBox.setCursor(Cursor.DEFAULT);
            });
            cardBox.setOnMouseClicked(e -> {
                if(choicesCounter<numberOfPlayers)
                    activateAddCardsButton(addGodImg, addGodHoverImg, addGodInactiveImg, addGod, removeGodImg, removeGodHoverImg, removeGodInactiveImg, removeGod);
                selectedCard = godName.getText();
                selectedCardBox = cardBox;
                cardBox.setStyle("-fx-border-color: black");
                cardBox.setCursor(Cursor.DEFAULT);
                cardBox.setOnMouseExited(f -> {
                    cardBox.setStyle("-fx-border-color: black");
                    cardBox.setCursor(Cursor.DEFAULT);
                });
                for(Node box : cardsList.getChildren()){
                    if(!box.equals(cardBox)) {
                        box.setStyle("-fx-border-color: transparent");
                        box.setOnMouseExited(k -> {
                            box.setStyle("-fx-border-color: transparent");
                            box.setCursor(Cursor.DEFAULT);
                        });
                    }
                }
            });
            cardsList.getChildren().add(cardBox);
        }

        allCards.setContent(cardsList);
        allCards.setPrefWidth(screenWidth/2.7);
        allCards.setMaxHeight(screenHeight/1.9);
        VBox buttons = new VBox();

        buttons.getChildren().addAll(addGod, removeGod);
        buttons.setPrefWidth(screenWidth/11);
        buttons.setPrefHeight(screenHeight/1.5);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(20);

        choices = new VBox();
        choices.setPrefWidth(screenWidth/2.7);
        choices.setPrefHeight(screenHeight/1.5);
        choices.setAlignment(Pos.CENTER);
        choices.setSpacing(20);
        choices.setPadding(new Insets(0, screenWidth/20, 0, 0));

        for(int i=0; i<numberOfPlayers ; i++){
            Text name = new Text();

            //save all Text -> I will be able to change Text content
            choicesNames[i] = name;

            name.setFont(lillybelleFont);
            Image choiceImg = new Image(MatchScene.class.getResource("/img/godCards/noGod.png").toString(), screenWidth/14, screenHeight/9, false, false);
            ImageView choiceView = new ImageView(choiceImg);

            //save all ImageView -> I will be able to change Image
            choicesImages[i] = choiceView;


            HBox choiceBox = new HBox();

            //save all HBox -> I will be able to set visibility
            choicesBoxes[i] = choiceBox;


            choiceBox.setPrefWidth(screenWidth/3);
            choiceBox.getChildren().addAll(choiceView, name);
            choiceBox.setSpacing(30);
            choiceBox.setVisible(false);
            choiceBox.setAlignment(Pos.CENTER_LEFT);
            choiceBox.setPadding(new Insets(0,0,0,screenWidth/50));
            choices.getChildren().add(choiceBox);
        }


        Image confirmImg = new Image(MatchScene.class.getResource("/img/buttons/confirmChoices.png").toString(), screenWidth/8, screenHeight/10, false, false);
        Image confirmHoverImg = new Image(MatchScene.class.getResource("/img/buttons/confirmChoicesHover.png").toString(), screenWidth/8, screenHeight/10, false, false);
        confirmChoices = new ImageView(confirmImg);
        confirmChoices.setOnMouseEntered(e ->{
            confirmChoices.setCursor(Cursor.HAND);
            confirmChoices.setImage(confirmHoverImg);
        });
        confirmChoices.setOnMouseExited(e ->{
            confirmChoices.setCursor(Cursor.HAND);
            confirmChoices.setImage(confirmImg);
        });
        choices.getChildren().add(confirmChoices);
        confirmChoices.setVisible(false);

        chooseCards.getChildren().addAll(allCards,  buttons, choices);
        chooseCardsBox.getChildren().addAll(chooseCardsView, chooseCards);
        chooseCardsBox.setAlignment(Pos.CENTER);
        chooseCardsBox.setVisible(true);

    }

    private void activateAddCardsButton(Image addGodImg, Image addGodHoverImg, Image addGodInactiveImg, ImageView addGod, Image removeGodImg, Image removeGodHoverImg, Image removeGodInactiveImg, ImageView removeGod){
        addGod.setImage(addGodImg);
        addGod.setOnMouseEntered(e ->{
            addGod.setImage(addGodHoverImg);
            addGod.setCursor(Cursor.HAND);
        });
        addGod.setOnMouseExited(e ->{
            addGod.setImage(addGodImg);
        });
        addGod.setOnMouseClicked(e ->{
            choicesCounter++;
            choicesNames[choicesCounter-1].setText(selectedCard);
            choicesImages[choicesCounter-1].setImage(new Image(MatchScene.class.getResource("/img/godCards/" + selectedCard + ".png").toString(), screenWidth/14, screenHeight/9, false, false));
            choicesBoxes[choicesCounter-1].setVisible(true);
            chosenCards.add(selectedCardBox);
            selectedCardBox.setVisible(false);
            selectedCardBox.setManaged(false);
            if(choicesCounter==1)
                activateRemoveCardsButton(removeGodImg, removeGodHoverImg, removeGodInactiveImg, removeGod);
            deactivateAddCardsButton(addGodInactiveImg, addGod);
            if(choicesCounter==3)
                confirmChoices.setVisible(true);
        });
    }

    private void deactivateAddCardsButton(Image addGodInactiveImg, ImageView addGod){
        addGod.setImage(addGodInactiveImg);
        addGod.setOnMouseEntered(e -> {
            addGod.setCursor(Cursor.DEFAULT);
        });
        addGod.setOnMouseExited(Event::consume);
        addGod.setOnMouseClicked(Event::consume);
    }

    private void activateRemoveCardsButton(Image removeGodImg, Image removeGodHoverImg, Image removeGodInactiveImg, ImageView removeGod){
        removeGod.setImage(removeGodImg);
        removeGod.setOnMouseEntered(e ->{
            removeGod.setImage(removeGodHoverImg);
            removeGod.setCursor(Cursor.HAND);
        });
        removeGod.setOnMouseExited(e ->{
            removeGod.setImage(removeGodImg);
        });
        removeGod.setOnMouseClicked(e ->{
            chosenCards.get(choicesCounter-1).setVisible(true);
            chosenCards.get(choicesCounter-1).setManaged(true);
            chosenCards.remove(choicesCounter-1);
            choicesBoxes[choicesCounter-1].setVisible(false);
            choicesCounter--;
            if(choicesCounter==0)
                deactivateRemoveCardsButton(removeGodInactiveImg, removeGod);
            if(choicesCounter==2)
                confirmChoices.setVisible(false);
        });
    }

    private void deactivateRemoveCardsButton(Image removeGodInactiveImg, ImageView removeGod){
        removeGod.setImage(removeGodInactiveImg);
        removeGod.setOnMouseEntered(e -> {
            removeGod.setCursor(Cursor.DEFAULT);
        });
        removeGod.setOnMouseExited(Event::consume);
        removeGod.setOnMouseClicked(Event::consume);
    }
}
