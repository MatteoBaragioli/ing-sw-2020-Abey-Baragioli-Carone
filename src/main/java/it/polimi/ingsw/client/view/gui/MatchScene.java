package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.network.objects.GodCardProxy;
import it.polimi.ingsw.network.objects.PlayerProxy;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class MatchScene {

    private final Font lillybelleFont;
    private final Font godInfoFont;
    private final Gui gui;
    private final double screenWidth;
    private final double screenHeight;
    private final StackPane matchPage;

    double dimension;
    double marginDim;
    double totalMargin;
    double mapDim;

    ImageView mapView;

    //number of players of the match
    private int numberOfPlayers;

    //player view (map, buttons, opponents, name,  my card)
    private final PlayerView playerView;

    //map of the match
    private final GuiMap guiMap;

    //current chosen worker position
    private final int[] chosenWorkerPosition = new int[2];

    //current chosen worker color
    private String chosenWorkerColor;

    //current chosen worker gender
    private String chosenWorkerGender;

    //current chosen worker new position
    private GuiBox chosenWorkerNewPosition;

    //chosen box index -> this variable will be returned as answer to server request
    private int chosenBoxIndex;

    //list of chosable boxes sent by the server
    private List<int[]> chosableBoxes = new ArrayList<>();

    //list of chosen cards by the challenger (from the list of all cards)
    private final List<HBox> chosenCards = new ArrayList<>();

    //pane that is visible to the master -> the challenger has to choose the cards for the match
    private final StackPane chooseCardsBox = new StackPane();

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

    private final AtomicBoolean confirmChallengerCards = new AtomicBoolean(false);

    private int[] cardsIndexes;

    private final StackPane chooseCard = new StackPane();

    private int cardIndex;

    private final AtomicBoolean confirmMyCards = new AtomicBoolean(false);

    //info name when a player has to choose their card
    private Text infoGodName = new Text();

    //info description when a player has to choose their card
    private Text infoGodDescription = new Text("Go on a card to see the power");

    private AtomicBoolean destinationReady = new AtomicBoolean(false);



    private List<ImageView> cardsView;
    private List<Text> godNames;
    private List<Text> godDescriptions;
    private List<VBox> cardsInfo;
    private List<HBox> cardBoxes;



    public MatchScene(Gui gui, double screenWidth, double screenHeight, StackPane matchPage) {
        lillybelleFont = Font.loadFont(MatchScene.class.getResourceAsStream("/fonts/LillyBelle.ttf"), screenWidth/70);
        godInfoFont = Font.loadFont(MatchScene.class.getResourceAsStream("/fonts/LillyBelle.ttf"), screenWidth/80);
        this.gui = gui;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.matchPage = matchPage;

        dimension = screenHeight - screenHeight / 20;
        marginDim = dimension / 28.05421687;
        totalMargin = dimension / 6.504189944;
        mapDim = dimension - (2 * marginDim);

        guiMap = new GuiMap(gui.mapRowsNumber(),gui.mapColumnsNumber(), mapDim, screenWidth, screenHeight);

        Image mapImg = new Image(MatchScene.class.getResource("/img/board.png").toString(),dimension,dimension,false,false);
        mapView = new ImageView(mapImg);
        ImageView matchBackground = matchBackground(screenWidth, screenHeight);

        playerView = new PlayerView(screenWidth, screenHeight, guiMap, gui);

        StackPane playerViewPane = playerView.getPlayerViewStackPane();

        matchPage.getChildren().addAll(matchBackground,mapView, playerViewPane, guiMap, chooseCardsBox, chooseCard);
        chooseCardsBox.setVisible(false);
        chooseCard.setVisible(false);
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

    public void setChosenWorkerNewPosition(GuiBox chosenWorkerNewPosition) {
        this.chosenWorkerNewPosition = chosenWorkerNewPosition;
    }

    public void setDestinationReady(boolean destinationReady) {
        this.destinationReady.set(destinationReady);
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

    public GuiBox chosenWorkerNewPosition() {
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







    public void setMatchScene(String nickname, int numberOfPlayers, String color, List<PlayerProxy> opponents) {
        this.numberOfPlayers = numberOfPlayers;

        guiMap.setMaxWidth(dimension);
        guiMap.setMaxHeight(dimension);
        guiMap.setAlignment(Pos.CENTER);
        guiMap.setPadding(new Insets(totalMargin));

        Platform.runLater(() -> playerView.setPage(nickname, color, opponents));

        chooseCardsBox.setVisible(false);
        matchPage.setPrefWidth(screenWidth);
        matchPage.setPrefHeight(screenHeight);

        AnchorPane.setTopAnchor(guiMap, (screenHeight-dimension)/2);
        AnchorPane.setBottomAnchor(guiMap, (screenHeight-dimension)/2);
        AnchorPane.setLeftAnchor(guiMap, (screenWidth-dimension)/2);
        AnchorPane.setRightAnchor(guiMap, (screenWidth-dimension)/2);
        AnchorPane.setTopAnchor(mapView, (screenHeight-dimension)/2);
        AnchorPane.setBottomAnchor(mapView, (screenHeight-dimension)/2);
        AnchorPane.setLeftAnchor(mapView, (screenWidth-dimension)/2);
        AnchorPane.setRightAnchor(mapView, (screenWidth-dimension)/2);

    }

    public ImageView matchBackground(double screenWidth, double screenHeight){
        Image matchBackgroundImg = new Image(MatchScene.class.getResource("/img/match_background.png").toString(),screenWidth,screenHeight,false,false);
        return new ImageView(matchBackgroundImg);
    }

    public void chooseDestination(List<int[]> possibleDestinations){
        int index = 0;
        for(int[] i : possibleDestinations){
            map().box(i[0], i[1]).setIndex(index);
            map().box(i[0], i[1]).setAsChosable(this, possibleDestinations, 1);
            index++;
        }
        setChosableBoxes(possibleDestinations);

    }

    public int chosenDestination(){
        while(!destinationReady.get()){

        }
        destinationReady.set(false);
        return chosenBoxIndex();
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

    /**
     * This method creates and shows the box to choose the match cards (only to challenger)
     * @param godCards List of GodCardProxy (all game's cards)
     */
    public void chooseCards(List<GodCardProxy> godCards){
        cardsIndexes = new int[numberOfPlayers];
        choicesNames = new Text[numberOfPlayers];
        choicesImages = new ImageView[numberOfPlayers];
        choicesBoxes = new HBox[numberOfPlayers];
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
        //content of the chooseCardBox divided in 3 columns
        HBox chooseCards = new HBox();
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
            Text godDescription = new Text();
            if(card.description!=null)
                godDescription.setText(card.description);
            else if(card.winDescription!=null)
                godDescription.setText(card.winDescription);
            else if(card.setUpDescription!=null)
                godDescription.setText(card.setUpDescription);
            else if(card.opponentsFxDescription!=null)
                godDescription.setText(card.opponentsFxDescription);
            godDescription.setFont(godInfoFont);
            godDescription.setWrappingWidth(screenWidth/7);
            godDescription.setTextAlignment(TextAlignment.CENTER);
            VBox cardInfo = new VBox();
            cardInfo.getChildren().addAll(godName, godDescription);
            cardInfo.setAlignment(Pos.CENTER);
            HBox cardBox = new HBox();
            cardBox.getChildren().addAll(cardView, cardInfo);
            cardBox.setSpacing(30);
            cardBox.setAlignment(Pos.CENTER);
            cardBox.setStyle("-fx-background-color: transparent");
            cardView.setOnMouseEntered(e -> {
                cardView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0)");
                cardView.setCursor(Cursor.HAND);
            });
            cardView.setOnMouseExited(e -> {
                cardView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0), 0, 0, 0, 0)");
                cardView.setCursor(Cursor.DEFAULT);
            });
            cardView.setOnMouseClicked(e -> {
                if(choicesCounter<numberOfPlayers)
                    activateAddCardsButton(addGodImg, addGodHoverImg, addGodInactiveImg, addGod, removeGodImg, removeGodHoverImg, removeGodInactiveImg, removeGod, cardsIndexes, godCards);
                selectedCard = godName.getText();
                selectedCardBox = cardBox;
                cardView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0)");
                cardView.setCursor(Cursor.DEFAULT);
                cardView.setOnMouseExited(f -> {
                    cardView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0)");
                    cardView.setCursor(Cursor.DEFAULT);
                });
                for(Node box : cardsList.getChildren()){
                    if(!box.equals(cardBox)) {
                        box.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0), 0, 0, 0, 0)");
                        box.setOnMouseExited(k -> {
                            box.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0), 0, 0, 0, 0)");
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

        //Vertical Box that contains all chosen cards by the challenger
        VBox choices = new VBox();
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
        confirmChoices.setOnMouseClicked(e -> {
            confirmChallengerCards.set(true);
            hideChooseCards(chooseCardsBox);
        });
        choices.getChildren().add(confirmChoices);
        confirmChoices.setVisible(false);

        chooseCards.getChildren().addAll(allCards,  buttons, choices);
        chooseCardsBox.getChildren().addAll(chooseCardsView, chooseCards);
        chooseCardsBox.setAlignment(Pos.CENTER);



        FadeTransition chooseCardsBoxFadeIn = new FadeTransition(Duration.millis(1000), chooseCardsBox);
        chooseCardsBoxFadeIn.setFromValue(0);
        chooseCardsBoxFadeIn.setToValue(1);

        ScaleTransition chooseCardsBoxZoomIn1 = new ScaleTransition(Duration.millis(1000), chooseCardsBox);
        chooseCardsBoxZoomIn1.setFromX(0);
        chooseCardsBoxZoomIn1.setToX(1);
        chooseCardsBoxZoomIn1.setFromY(0.01);
        chooseCardsBoxZoomIn1.setToY(0.01);

        ScaleTransition chooseCardsBoxZoomIn2 = new ScaleTransition(Duration.millis(1000), chooseCardsBox);
        chooseCardsBoxZoomIn2.setFromY(0.01);
        chooseCardsBoxZoomIn2.setToY(1);

        chooseCardsBoxFadeIn.play();
        chooseCardsBoxZoomIn1.play();

        Timeline showingTimer1 = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> {
                    chooseCardsBox.setVisible(true);
                }));
        showingTimer1.play();
        Timeline showingTimer = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> {
                    chooseCardsBoxZoomIn2.play();
                }));
        showingTimer.play();

    }

    /**
     * This method activates add button in the Choose cards box when chosen cards are less than number of players
     * @param addGodImg Add button image
     * @param addGodHoverImg Add button hover image
     * @param addGodInactiveImg Add button Inactive image
     * @param addGod Add button
     * @param removeGodImg Remove button image
     * @param removeGodHoverImg Remove button hover image
     * @param removeGodInactiveImg Remove button inactive image
     * @param removeGod Remove button
     */
    private void activateAddCardsButton(Image addGodImg, Image addGodHoverImg, Image addGodInactiveImg, ImageView addGod, Image removeGodImg, Image removeGodHoverImg, Image removeGodInactiveImg, ImageView removeGod, int[] cardsIndexes, List<GodCardProxy> godCards){
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
            choicesImages[choicesCounter-1].setImage(new Image(MatchScene.class.getResource("/img/godCards/" + selectedCard + ".png").toString(), screenWidth/14, screenHeight/8, false, false));
            choicesBoxes[choicesCounter-1].setVisible(true);
            for(GodCardProxy godCard : godCards){
                if(godCard.name.equals(selectedCard)){
                    cardsIndexes[choicesCounter-1] = godCards.indexOf(godCard);
                }
            }
            chosenCards.add(selectedCardBox);
            selectedCardBox.setVisible(false);
            selectedCardBox.setManaged(false);
            if(choicesCounter==1)
                activateRemoveCardsButton(removeGodImg, removeGodHoverImg, removeGodInactiveImg, removeGod);
            deactivateAddCardsButton(addGodInactiveImg, addGod);
            if(choicesCounter==numberOfPlayers)
                confirmChoices.setVisible(true);
        });
    }

    /**
     * This method deactivates add button in the Choose cards box when chosen cards are as many as number of players
     * @param addGodInactiveImg Add button inactive image
     * @param addGod Add button
     */
    private void deactivateAddCardsButton(Image addGodInactiveImg, ImageView addGod){
        addGod.setImage(addGodInactiveImg);
        addGod.setOnMouseEntered(e -> {
            addGod.setCursor(Cursor.DEFAULT);
        });
        addGod.setOnMouseExited(Event::consume);
        addGod.setOnMouseClicked(Event::consume);
    }

    /**
     * This method activates remove button in the Choose cards box when chosen cards are 1 or more
     * @param removeGodImg Remove button image
     * @param removeGodHoverImg Remove button hover image
     * @param removeGodInactiveImg Remove button inactive image
     * @param removeGod Remove button
     */
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
            if(choicesCounter<numberOfPlayers)
                confirmChoices.setVisible(false);
        });
    }

    /**
     * This method deactivates remove button in the Choose cards box when chosen cards are 0
     * @param removeGodInactiveImg Remove button inactive image
     * @param removeGod Remove button
     */
    private void deactivateRemoveCardsButton(Image removeGodInactiveImg, ImageView removeGod){
        removeGod.setImage(removeGodInactiveImg);
        removeGod.setOnMouseEntered(e -> {
            removeGod.setCursor(Cursor.DEFAULT);
        });
        removeGod.setOnMouseExited(Event::consume);
        removeGod.setOnMouseClicked(Event::consume);
    }

    /**
     * This method asks player to choose their card
     */
    public void chooseCard(List<GodCardProxy> cards){
        Image chooseCardImg = new Image(MatchScene.class.getResource("/img/chooseCard.png").toString(), screenWidth/1.2, screenHeight/1.2, false, false);
        ImageView chooseCardBackground = new ImageView(chooseCardImg);

        List<ImageView> cardChoiceBoxes = new ArrayList<>();
        AtomicReference<ImageView> selected = new AtomicReference<>(new ImageView());

        VBox chooseCardBox = new VBox();

        HBox cardImages = new HBox();
        cardImages.setAlignment(Pos.CENTER);
        cardImages.setSpacing(screenWidth/20);


        infoGodName.setFont(lillybelleFont);
        infoGodName.setTextAlignment(TextAlignment.CENTER);
        infoGodDescription.setFont(lillybelleFont);
        infoGodDescription.setTextAlignment(TextAlignment.CENTER);
        infoGodDescription.setWrappingWidth(screenWidth/1.6);

        VBox infoBox = new VBox();
        infoBox.getChildren().addAll(infoGodName, infoGodDescription);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setPrefHeight(screenHeight/6);

        Image confirmChoiceImg = new Image(MatchScene.class.getResource("/img/buttons/confirmChoices.png").toString(), screenWidth/8, screenHeight/10, false, false);
        Image confirmChoiceHoverImg = new Image(MatchScene.class.getResource("/img/buttons/confirmChoicesHover.png").toString(), screenWidth/8, screenHeight/10, false, false);
        ImageView confirmChoice = new ImageView(confirmChoiceImg);
        confirmChoice.setOnMouseEntered(e ->{
            confirmChoice.setCursor(Cursor.HAND);
            confirmChoice.setImage(confirmChoiceHoverImg);
        });
        confirmChoice.setOnMouseExited(e ->{
            confirmChoice.setCursor(Cursor.HAND);
            confirmChoice.setImage(confirmChoiceImg);
        });
        confirmChoice.setOnMouseClicked(e -> {
            confirmMyCards.set(true);
            hideChooseCards(chooseCard);
        });

        confirmChoice.setVisible(false);


        for(GodCardProxy card : cards){

            Image chosableCardImg = new Image(MatchScene.class.getResource("/img/godCards/" + card.name +".png").toString(), screenWidth/10, screenHeight/5, false, false);
            ImageView chosableCardView = new ImageView(chosableCardImg);

            cardImages.getChildren().add(chosableCardView);


            chosableCardView.setOnMouseEntered(e -> {
                chosableCardView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0)");
                infoGodName.setText(card.name);

                if(card.description!=null)
                    infoGodDescription.setText(card.description);
                else if(card.winDescription!=null)
                    infoGodDescription.setText(card.winDescription);
                else if(card.setUpDescription!=null)
                    infoGodDescription.setText(card.setUpDescription);
                else if(card.opponentsFxDescription!=null)
                    infoGodDescription.setText(card.opponentsFxDescription);

                chosableCardView.setCursor(Cursor.HAND);
            });
            chosableCardView.setOnMouseExited(e -> {
                chosableCardView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0), 0, 0, 0, 0)");
                chosableCardView.setCursor(Cursor.DEFAULT);
                infoGodName.setText("");
                infoGodDescription.setText("Go on a card to see the power");
            });
            chosableCardView.setOnMouseClicked(e -> {
                selected.set(chosableCardView);
                confirmChoice.setVisible(true);
                cardIndex = cards.indexOf(card);
                chosableCardView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0)");
                chosableCardView.setCursor(Cursor.DEFAULT);
                chosableCardView.setOnMouseExited(f -> {
                    chosableCardView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0)");
                    chosableCardView.setCursor(Cursor.DEFAULT);
                    infoGodName.setText("");
                    infoGodDescription.setText("Go on a card to see the power");
                });
                for(Node box : cardChoiceBoxes){
                    if(!box.toString().equals(selected.toString())) {
                        box.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0), 0, 0, 0, 0)");
                        box.setOnMouseExited(k -> {
                            box.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0), 0, 0, 0, 0)");
                            box.setCursor(Cursor.DEFAULT);
                            infoGodName.setText("");
                            infoGodDescription.setText("Go on a card to see the power");
                        });
                    }
                }
            });
            cardChoiceBoxes.add(chosableCardView);

        }

        chooseCardBox.getChildren().addAll(cardImages, infoBox, confirmChoice);
        chooseCardBox.setPrefWidth(screenWidth/3);
        chooseCardBox.setAlignment(Pos.CENTER);
        chooseCardBox.setSpacing(screenHeight/50);
        chooseCard.getChildren().addAll(chooseCardBackground, chooseCardBox);

        FadeTransition chooseCardBoxFadeIn = new FadeTransition(Duration.millis(1000), chooseCard);
        chooseCardBoxFadeIn.setFromValue(0);
        chooseCardBoxFadeIn.setToValue(1);

        ScaleTransition chooseCardBoxZoomIn1 = new ScaleTransition(Duration.millis(1000), chooseCard);
        chooseCardBoxZoomIn1.setFromX(0);
        chooseCardBoxZoomIn1.setToX(1);
        chooseCardBoxZoomIn1.setFromY(0.01);
        chooseCardBoxZoomIn1.setToY(0.01);

        ScaleTransition chooseCardBoxZoomIn2 = new ScaleTransition(Duration.millis(1000), chooseCard);
        chooseCardBoxZoomIn2.setFromY(0.01);
        chooseCardBoxZoomIn2.setToY(1);

        chooseCardBoxFadeIn.play();
        chooseCardBoxZoomIn1.play();

        Timeline showingTimer1 = new Timeline(new KeyFrame(
                Duration.millis(200),
                ae -> {
                    chooseCard.setVisible(true);
                }));
        showingTimer1.play();
        Timeline showingTimer = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> {
                    chooseCardBoxZoomIn2.play();
                }));
        showingTimer.play();
    }

    /**
     * This method hides choose cards box
     */
    private void hideChooseCards(StackPane pane){
        FadeTransition chooseCardsFadeOut = new FadeTransition(Duration.millis(1000), pane);
        chooseCardsFadeOut.setFromValue(1);
        chooseCardsFadeOut.setToValue(0);

        ScaleTransition chooseCardsZoomOut1 = new ScaleTransition(Duration.millis(1000), pane);
        chooseCardsZoomOut1.setFromY(1);
        chooseCardsZoomOut1.setToY(0.01);
        chooseCardsZoomOut1.setFromX(1);
        chooseCardsZoomOut1.setToX(1);

        ScaleTransition chooseCardsZoomIn2 = new ScaleTransition(Duration.millis(1000), pane);
        chooseCardsZoomIn2.setFromX(1);
        chooseCardsZoomIn2.setToX(0);

        chooseCardsZoomOut1.play();

        Timeline hidingTimer1 = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> {
                    chooseCardsZoomIn2.play();
                    chooseCardsFadeOut.play();
                }));
        hidingTimer1.play();
        Timeline hidingTimer2 = new Timeline(new KeyFrame(
                Duration.millis(2000),
                ae -> {
                    pane.setVisible(false);
                }));
        hidingTimer2.play();
    }

    public int[] chosenCards(){
        while(!confirmChallengerCards.get()){

        }
        return cardsIndexes;
    }

    public int chosenCard(){
        while(!confirmMyCards.get()){

        }
        return cardIndex;
    }
}
