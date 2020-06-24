package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.network.exceptions.TimeOutException;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static javafx.scene.paint.Color.BLACK;

public class MatchScene {

    //fonts
    private final Font lillybelleFont;
    private final Font godInfoFont;

    //window dimensions
    private final double screenWidth;
    private final double screenHeight;

    //match page
    private final StackPane matchPage;

    //gui
    private final Gui gui;

    //dimension of the map
    private final double dimension;

    //margin between map image and map
    private final double totalMargin;

    //map image
    private final ImageView mapView;

    //number of players of the match
    private int numberOfPlayers;

    //player view (map, buttons, opponents, name,  my card)
    private final PlayerView playerView;

    //map of the match
    private final GuiMap guiMap;

    //pauseMenu
    private final StackPane pausePane = new StackPane();

    //active powers pane
    private final StackPane activePowers = new StackPane();

    //helper pane
    private final StackPane helper = new StackPane();

    //story pane
    private final StackPane turnStory = new StackPane();

    //match background
    private final ImageView matchBackground;

    //player view pane
    private final StackPane playerViewPane;

    //current chosen worker new position
    private GuiBox chosenBox;

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

    //list to save all cards images in order to change their border when one is selected
    private List<ImageView> cardsChosable;

    //variable that saves the currently selected card name from the entire list of all the cards
    private String selectedCard;

    //variable that saves the currently selected card box from the entire list of all the cards
    private HBox selectedCardBox;

    //variable to synchronise challenger choice
    private final AtomicBoolean confirmChallengerCards = new AtomicBoolean(false);

    //indexes of cards chosen by the challenger
    private int[] cardsIndexes;

    //pane to chose my card for the match
    private final StackPane chooseCard = new StackPane();

    //my card choice index
    private int cardIndex;

    //variable to synchronise my card choice
    private final AtomicBoolean confirmMyCards = new AtomicBoolean(false);

    //info name when a player has to choose their card
    private final Text infoGodName = new Text();

    //info description when a player has to choose their card
    private final Text infoGodDescription = new Text("Go on a card to see the power");

    //variable that is true when chosen box is ready
    private final AtomicBoolean destinationReady = new AtomicBoolean(false);

    //confirm turn popup
    private final StackPane askConfirmTurn = new StackPane();

    //turn confirmation answer
    private final AtomicInteger answer = new AtomicInteger(2);

    //variable that is true when player has given an answer to confirm turn popup
    private final AtomicBoolean clickedConfirmation = new AtomicBoolean(false);

    //my turn pane (to show in the center of the window when it's my turn)
    private final StackPane myTurn = new StackPane();

    //winner pane
    private final StackPane winner = new StackPane();

    //surrender pane
    private final StackPane surrender = new StackPane();

    //variable that is true if player has lost the match because of timeout
    private boolean timeoutLoser = false;

    //variable that is true when user quits the match
    private AtomicBoolean closeMatch = new AtomicBoolean(false);

    //variable that is true when timer finished
    private AtomicBoolean  endTimer = new AtomicBoolean(false);

    //timer that is shown in challenger pane
    private Label chooseCardsTimer = new Label();

    //timer that is shown in chooseMyCard pane
    private Label chooseCardTimer = new Label();

    public MatchScene(Gui gui, double screenWidth, double screenHeight, StackPane matchPage) {
        lillybelleFont = Font.loadFont(MatchScene.class.getResourceAsStream("/fonts/LillyBelle.ttf"), screenWidth/70);
        godInfoFont = Font.loadFont(MatchScene.class.getResourceAsStream("/fonts/LillyBelle.ttf"), screenWidth/80);
        this.gui = gui;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.matchPage = matchPage;

        dimension = screenHeight - screenHeight / 20;
        double marginDim = dimension / 28.05421687;
        totalMargin = dimension / 6.504189944;
        double mapDim = dimension - (2 * marginDim);

        guiMap = new GuiMap(gui.mapRowsNumber(),gui.mapColumnsNumber(), mapDim);

        Image mapImg = new Image(MatchScene.class.getResource("/img/matchPage/board.png").toString(),dimension,dimension,false,false);
        mapView = new ImageView(mapImg);

        Image matchBackgroundImg = new Image(MatchScene.class.getResource("/img/matchPage/match_background.png").toString(),screenWidth,screenHeight,false,false);
        matchBackground = new ImageView(matchBackgroundImg);

        playerView = new PlayerView(screenWidth, screenHeight, gui, this, pausePane, activePowers, helper, turnStory);

        playerViewPane = playerView.getPlayerViewStackPane();

        createConfirmTurnPopup();

        createMyTurnImage();

        createSurrenderPopup();

        matchPage.getChildren().addAll(matchBackground,mapView, playerViewPane, guiMap, pausePane, activePowers, helper, turnStory, chooseCardsBox, chooseCard, myTurn, askConfirmTurn, surrender, winner);
        chooseCardsBox.setVisible(false);
        chooseCard.setVisible(false);
        askConfirmTurn.setVisible(false);
        pausePane.setVisible(false);
        activePowers.setVisible(false);
        helper.setVisible(false);
        turnStory.setVisible(false);
        myTurn.setVisible(false);
        surrender.setVisible(false);
        winner.setVisible(false);
    }

    //------------------------------------------------SETTER------------------------------------------------------------


    public void setChosableBoxes(List<int[]> chosableBoxes) {
        this.chosableBoxes = chosableBoxes;
    }

    public void setChosenBoxIndex(int chosenBoxIndex) {
        this.chosenBoxIndex = chosenBoxIndex;
    }

    public void setChosenBox(GuiBox chosenBox) {
        this.chosenBox = chosenBox;
    }

    public void setTimeoutLoser(boolean isTimeoutLoser){
        timeoutLoser = isTimeoutLoser;
    }

    //-----------------------------------------------END SETTER---------------------------------------------------------


    //-------------------------------------------------GETTER-----------------------------------------------------------

    public List<int[]> chosableBoxes() {
        return chosableBoxes;
    }

    public int chosenBoxIndex() {
        return chosenBoxIndex;
    }

    public GuiBox chosenBox() {
        return chosenBox;
    }

    public PlayerView playerView(){
        return  playerView;
    }

    public GuiMap map(){
        return guiMap;
    }

    public boolean isTimeoutLoser(){
        return timeoutLoser;
    }

    public ImageView matchBackground(){
        return matchBackground;
    }

    public StackPane turnStory(){
        return turnStory;
    }

    public StackPane helper(){
        return helper;
    }

    public StackPane activePowers(){
        return activePowers;
    }

    public StackPane pausePane(){
        return pausePane;
    }

    public ImageView mapView(){
        return mapView;
    }

    public GuiMap guiMap(){
        return guiMap;
    }

    public StackPane playerViewPane(){
        return playerViewPane;
    }

    public StackPane myTurn(){
        return myTurn;
    }

    public StackPane chooseCardsBox(){
        return chooseCardsBox;
    }

    public StackPane chooseCardPane(){
        return chooseCard;
    }

    public Label chooseCardsTimer(){
        return chooseCardsTimer;
    }

    public Label chooseCardTimer(){
        return chooseCardTimer;
    }

    //-----------------------------------------------END GETTER---------------------------------------------------------


    //-----------------------------------------SYNCHRONIZATION METHODS--------------------------------------------------

    /**
     * This method notifies destinationReady when player has chosen a destination
     * @param destinationReady Chosen destination
     */
    public synchronized void setDestinationReady(boolean destinationReady) {
        this.destinationReady.set(destinationReady);
        notifyAll();
    }

    /**
     * This method notifies confirmChallengerCards when player (if he is the challenger) has chosen all the cards of the match
     */
    private synchronized void setConfirmChallengerCards(){
        confirmChallengerCards.set(true);
        notifyAll();
    }

    /**
     * This method notifies confirmMyCards when player has chosen his card for the match
     */
    public synchronized void setConfirmMyCards(){
        confirmMyCards.set(true);
        notifyAll();
    }

    /**
     * This method notifies clickedConfirmation when player chooses to undo or confirm his turn
     * It also closes confirmTurn popup
     */
    private synchronized void setClickedConfirmation(){
        clickedConfirmation.set(true);

        FadeTransition confirmTurnFadeOut = new FadeTransition(Duration.millis(500), askConfirmTurn);
        confirmTurnFadeOut.setFromValue(1);
        confirmTurnFadeOut.setToValue(0);

        confirmTurnFadeOut.play();
        Timeline hidingTimer = new Timeline(new KeyFrame(
                Duration.millis(500),
                ae -> {
                    askConfirmTurn.setVisible(false);
                }));
        hidingTimer.play();

        notifyAll();
    }

    /**
     * This method notifies closeMatch when user wants to go back to main menu, when user closes the stage or when connection is lost and user clicks on "ok" button in the connection error pane
     */
    public synchronized void setCloseMatch(){
        closeMatch.set(true);
        notifyAll();
    }

    /**
     * This method notifies when time to do actions runs out
     */
    public synchronized void setEndTimer(){
        endTimer.set(true);
        notifyAll();
    }

    //-----------------------------------------END SYNCHRONIZATION METHODS---------------------------------

    /**
     * This method clears chosable boxes list (every request has different chosable boxes)
     */
    public void clearChosableBoxes(){
        chosableBoxes = new ArrayList<>();
    }

    /**
     * This method sets nickname, opponents and colors in match page
     * @param nickname Player nickname
     * @param numberOfPlayers Number of players of the match
     * @param color Color of player
     * @param opponents Opponents (nickname and color)
     */
    public void setMatchScene(String nickname, int numberOfPlayers, String color, List<PlayerProxy> opponents) {
        this.numberOfPlayers = numberOfPlayers;

        guiMap.setMaxWidth(dimension);
        guiMap.setMaxHeight(dimension);
        guiMap.setAlignment(Pos.CENTER);
        guiMap.setPadding(new Insets(totalMargin));

        Platform.runLater(() -> playerView.setPage(nickname, color, opponents, numberOfPlayers-1));

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

    /**
     * This method sets all possible destination as chosable
     * @param possibleDestinations Possible destinations
     */
    public void chooseDestination(List<int[]> possibleDestinations){
        int index = 0;
        for(int[] i : possibleDestinations){
            map().box(i[0], i[1]).setIndex(index);
            map().box(i[0], i[1]).setAsChosable(this);
            index++;
        }
        setChosableBoxes(possibleDestinations);

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
        Image addGodInactiveImg = new Image(MatchScene.class.getResource("/img/buttons/addGodInactive.png").toString(), screenWidth/12, screenHeight/12, false, false);
        ImageView addGod = new ImageView(addGodInactiveImg);

        Image removeGodImg = new Image(MatchScene.class.getResource("/img/buttons/removeGod.png").toString(), screenWidth/12, screenHeight/12, false, false);
        Image removeGodInactiveImg = new Image(MatchScene.class.getResource("/img/buttons/removeGodInactive.png").toString(), screenWidth/12, screenHeight/12, false, false);
        ImageView removeGod = new ImageView(removeGodInactiveImg);

        Image chooseCardsImg = new Image(MatchScene.class.getResource("/img/matchPage/chooseCardsBackground.png").toString(), screenWidth/1.2, screenHeight/1.2, false, false);
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

        cardsChosable = new ArrayList<>();

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
                    activateAddCardsButton(addGodImg, addGodInactiveImg, addGod, removeGodImg, removeGodInactiveImg, removeGod, cardsIndexes, godCards);
                selectedCard = godName.getText();
                selectedCardBox = cardBox;
                cardView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0)");
                cardView.setCursor(Cursor.DEFAULT);
                cardView.setOnMouseExited(f -> {
                    cardView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0)");
                    cardView.setCursor(Cursor.DEFAULT);
                });
                for(Node box : cardsChosable){
                    if(!box.equals(cardView)) {
                        box.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0), 0, 0, 0, 0)");
                        box.setOnMouseExited(k -> {
                            box.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0), 0, 0, 0, 0)");
                            box.setCursor(Cursor.DEFAULT);
                        });
                    }
                }
            });
            cardsChosable.add(cardView);
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
        confirmChoices = new ImageView(confirmImg);
        confirmChoices.setOnMouseEntered(e ->{
            confirmChoices.setCursor(Cursor.HAND);
            confirmChoices.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 5, 5, 0, 0));
        });
        confirmChoices.setOnMouseExited(e ->{
            confirmChoices.setCursor(Cursor.HAND);
            confirmChoices.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
        });
        confirmChoices.setOnMouseClicked(e -> {
            setConfirmChallengerCards();
            hideChooseCards(chooseCardsBox);
        });
        choices.getChildren().add(confirmChoices);
        confirmChoices.setVisible(false);

        chooseCards.getChildren().addAll(allCards,  buttons, choices);

        chooseCardsTimer.setPadding(new Insets(0, 0, screenHeight/11, 0));
        StackPane.setAlignment(chooseCardsTimer, Pos.BOTTOM_CENTER);

        chooseCardsBox.getChildren().addAll(chooseCardsView, chooseCards, chooseCardsTimer);
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

        Timeline waitingReadyTimer = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> {
                    chooseCardsBoxFadeIn.play();
                    chooseCardsBoxZoomIn1.play();
                }));
        waitingReadyTimer.play();

        Timeline showingTimer1 = new Timeline(new KeyFrame(
                Duration.millis(1700),
                ae -> {
                    chooseCardsBox.setVisible(true);
                }));
        showingTimer1.play();
        Timeline showingTimer = new Timeline(new KeyFrame(
                Duration.millis(1700),
                ae -> {
                    chooseCardsBoxZoomIn2.play();
                }));
        showingTimer.play();

    }

    /**
     * This method activates add button in the Choose cards box when chosen cards are less than number of players
     * @param addGodImg Add button image
     * @param addGodInactiveImg Add button Inactive image
     * @param addGod Add button
     * @param removeGodImg Remove button image
     * @param removeGodInactiveImg Remove button inactive image
     * @param removeGod Remove button
     */

    /**
     * This method activates add button in the Choose cards box when chosen cards are less than number of players
     * @param addGodImg Add button image
     * @param addGodInactiveImg Add button Inactive image
     * @param addGod Add button
     * @param removeGodImg Remove button image
     * @param removeGodInactiveImg Remove button inactive image
     * @param removeGod Remove button
     * @param cardsIndexes Indexes of all cards -> it saves index in order to show or hide the right card in the list of all cards (left side of chooseCardsPane
     * @param godCards All god cards (list of godCardProxy)
     */
    private void activateAddCardsButton(Image addGodImg, Image addGodInactiveImg, ImageView addGod, Image removeGodImg, Image removeGodInactiveImg, ImageView removeGod, int[] cardsIndexes, List<GodCardProxy> godCards){
        addGod.setImage(addGodImg);
        addGod.setOnMouseEntered(e ->{
            addGod.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 5, 5, 0, 0));
            addGod.setCursor(Cursor.HAND);
        });
        addGod.setOnMouseExited(e ->{
            addGod.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
            addGod.setCursor(Cursor.DEFAULT);
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
                activateRemoveCardsButton(removeGodImg, removeGodInactiveImg, removeGod);
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
        addGod.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
        addGod.setOnMouseEntered(e -> {
            addGod.setCursor(Cursor.DEFAULT);
        });
        addGod.setOnMouseExited(Event::consume);
        addGod.setOnMouseClicked(Event::consume);
    }

    /**
     * This method activates remove button in the Choose cards box when chosen cards are 1 or more
     * @param removeGodImg Remove button image
     * @param removeGodInactiveImg Remove button inactive image
     * @param removeGod Remove button
     */
    private void activateRemoveCardsButton(Image removeGodImg, Image removeGodInactiveImg, ImageView removeGod){
        removeGod.setImage(removeGodImg);
        removeGod.setOnMouseEntered(e ->{
            removeGod.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 5, 5, 0, 0));
            removeGod.setCursor(Cursor.HAND);
        });
        removeGod.setOnMouseExited(e ->{
            removeGod.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
        });
        removeGod.setOnMouseClicked(e ->{
            chosenCards.get(choicesCounter-1).setVisible(true);
            chosenCards.get(choicesCounter-1).setManaged(true);
            for(ImageView card : cardsChosable)
                card.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0), 0, 0, 0, 0)");
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
        removeGod.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
        removeGod.setOnMouseEntered(e -> {
            removeGod.setCursor(Cursor.DEFAULT);
        });
        removeGod.setOnMouseExited(Event::consume);
        removeGod.setOnMouseClicked(Event::consume);
    }

    /**
     * This method asks player to choose their card
     * @param cards List of chosable cards
     */
    public void chooseCard(List<GodCardProxy> cards){
        Image chooseCardImg = new Image(MatchScene.class.getResource("/img/matchPage/chooseCard.png").toString(), screenWidth/1.2, screenHeight/1.2, false, false);
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
        ImageView confirmChoice = new ImageView(confirmChoiceImg);
        confirmChoice.setOnMouseEntered(e ->{
            confirmChoice.setCursor(Cursor.HAND);
            confirmChoice.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 5, 5, 0, 0));
        });
        confirmChoice.setOnMouseExited(e ->{
            confirmChoice.setCursor(Cursor.HAND);
            confirmChoice.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
        });
        confirmChoice.setOnMouseClicked(e -> {
            setConfirmMyCards();
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

        chooseCardTimer.setPadding(new Insets(0, 0, screenHeight/11, 0));
        StackPane.setAlignment(chooseCardTimer, Pos.BOTTOM_CENTER);

        chooseCard.getChildren().addAll(chooseCardBackground, chooseCardBox, chooseCardTimer);

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
                Duration.millis(100),
                ae -> {
                    chooseCard.setVisible(true);
                }));
        showingTimer1.play();
        Timeline showingTimer = new Timeline(new KeyFrame(
                Duration.millis(700),
                ae -> {
                    chooseCardBoxZoomIn2.play();
                }));
        showingTimer.play();
    }

    /**
     * This method hides a pane
     * @param pane Pane to hide
     */
    private void hideChooseCards(StackPane pane){
        FadeTransition chooseCardsFadeOut = new FadeTransition(Duration.millis(700), pane);
        chooseCardsFadeOut.setFromValue(1);
        chooseCardsFadeOut.setToValue(0);

        ScaleTransition chooseCardsZoomOut1 = new ScaleTransition(Duration.millis(700), pane);
        chooseCardsZoomOut1.setFromY(1);
        chooseCardsZoomOut1.setToY(0.01);
        chooseCardsZoomOut1.setFromX(1);
        chooseCardsZoomOut1.setToX(1);

        ScaleTransition chooseCardsZoomIn2 = new ScaleTransition(Duration.millis(700), pane);
        chooseCardsZoomIn2.setFromX(1);
        chooseCardsZoomIn2.setToX(0);

        chooseCardsZoomOut1.play();

        Timeline hidingTimer1 = new Timeline(new KeyFrame(
                Duration.millis(700),
                ae -> {
                    chooseCardsZoomIn2.play();
                    chooseCardsFadeOut.play();
                }));
        hidingTimer1.play();
        Timeline hidingTimer2 = new Timeline(new KeyFrame(
                Duration.millis(1400),
                ae -> {
                    pane.setVisible(false);
                }));
        hidingTimer2.play();
    }

    /**
     * This method waits for player (if he is the challenger) to choose all the cards of the match and returns them
     * @return Chosen cards indexes (it refers to list of cards)
     * @throws TimeOutException Exception thrown when the time to do an action runs out
     */
    public synchronized int[] chosenCards() throws TimeOutException{
        while(!confirmChallengerCards.get() && !closeMatch.get() && !endTimer.get()){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(closeMatch.get())
            return new int[] {-1};
        else if(endTimer.get())
            throw new TimeOutException();
        return cardsIndexes;
    }

    /**
     * This method waits for player to choose his card for the match and returns it
     * @return Chosen card index (it refers to list of cards)
     * @throws TimeOutException Exception thrown when the time to do an action runs out
     */
    public synchronized int chosenCard() throws TimeOutException{
        while(!confirmMyCards.get() && !closeMatch.get() && !endTimer.get()){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(closeMatch.get())
            return -1;
        else if(endTimer.get())
            throw new TimeOutException();
        return cardIndex;
    }

    /**
     * This method wait for player to chose a destination and returns it
     * @return Chosen destination
     * @throws TimeOutException Exception thrown when the time to do an action runs out
     */
    public synchronized int chosenDestination() throws TimeOutException {
        while(!destinationReady.get() && !closeMatch.get() && !endTimer.get()){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        destinationReady.set(false);
        if(closeMatch.get())
            return -1;
        else if(endTimer.get())
            throw new TimeOutException();
        return chosenBoxIndex();
    }

    /**
     * This method creates confirm turn popup
     */
    public void createConfirmTurnPopup(){
        askConfirmTurn.setPrefWidth(screenWidth/5);
        askConfirmTurn.setPrefHeight(screenHeight/5);

        Image confirmFrame = new Image(MatchScene.class.getResource("/img/loadingAndPopups/frame2.png").toString(), screenWidth/5, screenHeight/5, false, false);
        ImageView confirmView = new ImageView(confirmFrame);

        VBox confirmBox = new VBox();

        Text questionPopup = new Text("Do you confirm your turn?");
        questionPopup.setFont(lillybelleFont);
        HBox answers = new HBox();

        Button yesPopupButton = new Button("Yes");
        Button noPopupButton = new Button("No");

        yesPopupButton.setFont(lillybelleFont);
        yesPopupButton.setCursor(Cursor.HAND);
        noPopupButton.setFont(lillybelleFont);
        noPopupButton.setCursor(Cursor.HAND);



        answers.getChildren().addAll(yesPopupButton, noPopupButton);
        answers.setSpacing(50);
        answers.setAlignment(Pos.CENTER);

        yesPopupButton.setOnAction(e -> {
            answer.set(0);
            setClickedConfirmation();
        });
        noPopupButton.setOnAction(e -> {
            answer.set(1);
            setClickedConfirmation();
        });


        confirmBox.setSpacing(10);
        confirmBox.getChildren().addAll(questionPopup, answers);

        askConfirmTurn.getChildren().addAll(confirmView, confirmBox);
        askConfirmTurn.setAlignment(Pos.CENTER);
        confirmBox.setAlignment(Pos.CENTER);
    }


    /**
     * This method waits for player to choose to undo or confirm his turn and returns the answer
     * It shows confirmTurn popup
     * @return Undo or Confirm turn answer
     */
    public synchronized int showConfirmTurnPopup(){
        clickedConfirmation.set(false);
        FadeTransition confirmTurnFadeIn = new FadeTransition(Duration.millis(1000), askConfirmTurn);
        confirmTurnFadeIn.setFromValue(0);
        confirmTurnFadeIn.setToValue(1);

        confirmTurnFadeIn.play();
        askConfirmTurn.setVisible(true);

        while (!clickedConfirmation.get() && !closeMatch.get()){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return answer.get();
    }

    /**
     * This method sets my turn in CurrentTurn box
     */
    public void setMyTurn(){
        if(!playerView.currentTurn().getText().equals("Your Turn")) {
            playerView.setCurrentTurn("Your Turn");
            showMyTurn();
        }
    }

    /**
     * This method creates my turn announce
     */
    private void createMyTurnImage(){
        Image myTurnImg = new Image(MatchScene.class.getResource("/img/matchPage/myTurn.png").toString(),screenWidth/3, screenHeight/4,false,false);
        ImageView myTurnView = new ImageView(myTurnImg);
        myTurn.getChildren().add(myTurnView);
        myTurn.setAlignment(Pos.CENTER);
        myTurn.setPrefWidth(screenWidth/3);
        myTurn.setPrefHeight(screenHeight/4);
    }

    /**
     * This method shows my turn announce
     */
    private void showMyTurn(){
        FadeTransition myTurnFadeIn = new FadeTransition(Duration.millis(500), myTurn);
        myTurnFadeIn.setFromValue(0);
        myTurnFadeIn.setToValue(1);

        FadeTransition myTurnFadeOut = new FadeTransition(Duration.millis(500), myTurn);
        myTurnFadeOut.setFromValue(1);
        myTurnFadeOut.setToValue(0);

        ScaleTransition myTurnZoomIn = new ScaleTransition(Duration.millis(2000), myTurn);
        myTurnZoomIn.setFromX(0.5);
        myTurnZoomIn.setToX(2);
        myTurnZoomIn.setFromY(0.5);
        myTurnZoomIn.setToY(2);

        myTurnFadeIn.play();
        myTurn.setVisible(true);
        myTurnZoomIn.play();

        Timeline showingTimer = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> {
                    myTurnFadeOut.play();
                }));
        showingTimer.play();
        Timeline hidingTimer = new Timeline(new KeyFrame(
                Duration.millis(1500),
                ae -> {
                    myTurn.setVisible(false);
                }));
        hidingTimer.play();
    }

    /**
     * This method sets the name of the opponent in CurrentTurn box
     * @param playerTurnName Current player
     */
    public void setOpponentTurn(String playerTurnName){
        if(!playerView.currentTurn().getText().equals(playerTurnName + "'s Turn")) {
            playerView.setCurrentTurn(playerTurnName + "'s Turn");
        }
    }

    /**
     * This method creates and shows winner pane to winner player and loser pane to other players
     * @param isWinner This variable tells if player is a winner or a loser
     */
    public void winner(boolean isWinner){
        winner.setPrefWidth(screenWidth);
        winner.setPrefHeight(screenHeight);

        Image winnerImg;
        ImageView winnerView;

        Image winnerGoBackImg;
        ImageView winnerGoBackView;


        if(isWinner) {
            Image winnerGif = new Image(MatchScene.class.getResource("/img/matchPage/winner.gif").toString(), screenWidth, screenHeight, false, false);
            ImageView winnerGifView = new ImageView(winnerGif);

            winnerImg = new Image(MatchScene.class.getResource("/img/matchPage/winner.png").toString(), screenWidth / 1.2, screenHeight / 1.5, false, false);
            winnerView = new ImageView(winnerImg);

            winnerGoBackImg = new Image(MatchScene.class.getResource("/img/buttons/goBackWinner.png").toString(), screenWidth / 3, screenHeight / 3, false, false);
            winnerGoBackView = new ImageView(winnerGoBackImg);

            winner.getChildren().add(winnerGifView);
            StackPane.setAlignment(winnerGifView, Pos.CENTER);
        } else {
            winnerImg = new Image(MatchScene.class.getResource("/img/matchPage/loser.png").toString(), screenWidth / 1.2, screenHeight / 1.5, false, false);
            winnerView = new ImageView(winnerImg);

            winnerGoBackImg = new Image(MatchScene.class.getResource("/img/buttons/goBackLoser.png").toString(), screenWidth / 3, screenHeight / 3, false, false);
            winnerGoBackView = new ImageView(winnerGoBackImg);

            if(isTimeoutLoser()){
                Image timeoutImg = new Image(MatchScene.class.getResource("/img/matchPage/timeoutLoser.png").toString(), screenWidth / 2, screenHeight / 4, false, false);
                ImageView timeoutView = new ImageView(timeoutImg);

                winner.getChildren().add(timeoutView);
                StackPane.setAlignment(timeoutView, Pos.TOP_CENTER);
            }
        }

        winnerGoBackView.setOnMouseEntered(e -> {
            winnerGoBackView.setCursor(Cursor.HAND);
            winnerGoBackView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 5, 5, 0, 0));
        });
        winnerGoBackView.setOnMouseExited(e -> {
            winnerGoBackView.setCursor(Cursor.DEFAULT);
            winnerGoBackView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
        });
        winnerGoBackView.setOnMouseClicked(e -> {
            backToMenu();
        });


        Image exitImage = new Image(MatchScene.class.getResource("/img/buttons/close.png").toString(), screenWidth/15, screenHeight/10, false, false);
        ImageView exitButton = new ImageView(exitImage);

        exitButton.setOnMouseEntered(e -> {
            exitButton.setCursor(Cursor.HAND);
            exitButton.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 5, 5, 0, 0));
        });
        exitButton.setOnMouseExited(e -> {
            exitButton.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
            exitButton.setCursor(Cursor.DEFAULT);
        });
        exitButton.setOnMouseClicked(e -> {
            closeWinnerOrSurrender(winner);
        });


        winner.getChildren().addAll(winnerView, winnerGoBackView, exitButton);
        StackPane.setAlignment(winnerView, Pos.CENTER);
        StackPane.setAlignment(winnerGoBackView, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(exitButton, Pos.TOP_LEFT);

        playerView.endTimer();

        showWinnerOrSurrender("winner");

    }

    /**
     * This method closes match page and shows menu page
     */
    public void backToMenu(){
        setCloseMatch();
        gui.setNewMatch();
        gui.playTransitionClouds();
        FadeTransition matchFadeOut = new FadeTransition(Duration.millis(2000), matchPage);
        matchFadeOut.setFromValue(1);
        matchFadeOut.setToValue(0);
        matchFadeOut.play();

        Timeline menuTimer = new Timeline(new KeyFrame(
                Duration.millis(1500),
                ae -> {
                    matchPage.setVisible(false);
                    matchBackground.setEffect(new BoxBlur(0, 0, 0));
                    mapView.setEffect(new BoxBlur(0, 0, 0));
                    playerViewPane.setEffect(new BoxBlur(0, 0, 0));
                    guiMap.setEffect(new BoxBlur(0, 0, 0));
                    pausePane.setEffect(new BoxBlur(0, 0, 0));
                    activePowers.setEffect(new BoxBlur(0, 0, 0));
                    helper.setEffect(new BoxBlur(0, 0, 0));
                    turnStory.setEffect(new BoxBlur(0, 0, 0));
                    myTurn.setEffect(new BoxBlur(0, 0, 0));
                    chooseCardsBox.setEffect(new BoxBlur(0, 0, 0));
                    chooseCard.setEffect(new BoxBlur(0, 0, 0));

                    FadeTransition menuFadeIn = new FadeTransition(Duration.millis(2000), gui.menuPage());
                    menuFadeIn.setFromValue(0.0);
                    menuFadeIn.setToValue(1.0);
                    menuFadeIn.play();
                    Platform.runLater(gui::primaryScene);
                }));
        menuTimer.play();
    }

    /**
     * This method hides winner or surrender pane
     * @param pane Pane to hide
     */
    private void closeWinnerOrSurrender(StackPane pane){
        FadeTransition paneFadeOut = new FadeTransition(Duration.millis(500), pane);
        paneFadeOut.setFromValue(1);
        paneFadeOut.setToValue(0);
        paneFadeOut.play();

        Timeline closePaneTimer = new Timeline(new KeyFrame(
                Duration.millis(500),
                ae -> {
                    pane.setVisible(false);
                    matchBackground.setEffect(new BoxBlur(0, 0, 0));
                    mapView.setEffect(new BoxBlur(0, 0, 0));
                    playerViewPane.setEffect(new BoxBlur(0, 0, 0));
                    guiMap.setEffect(new BoxBlur(0, 0, 0));
                    pausePane.setEffect(new BoxBlur(0, 0, 0));
                    activePowers.setEffect(new BoxBlur(0, 0, 0));
                    helper.setEffect(new BoxBlur(0, 0, 0));
                    turnStory.setEffect(new BoxBlur(0, 0, 0));
                    myTurn.setEffect(new BoxBlur(0, 0, 0));
                    chooseCardsBox.setEffect(new BoxBlur(0, 0, 0));
                    chooseCard.setEffect(new BoxBlur(0, 0, 0));
                }));
        closePaneTimer.play();

        if(pane.equals(winner)) {
            playerView.pauseView().setOnMouseClicked(e -> {
                showWinnerOrSurrender("winner");
            });
        }
    }

    /**
     * This method shows a pane (winner and surrender)
     * @param paneName Pane to show
     */
    public void showWinnerOrSurrender(String paneName){
        StackPane pane = new StackPane();
        if(paneName.equals("winner"))
            pane = winner;
        else if(paneName.equals("surrender"))
            pane = surrender;
        FadeTransition paneFadeIn = new FadeTransition(Duration.millis(500), pane);
        paneFadeIn.setFromValue(0);
        paneFadeIn.setToValue(1);
        paneFadeIn.play();
        pane.setVisible(true);

        Timeline showingTimer = new Timeline(new KeyFrame(
                Duration.millis(500),
                ae -> {
                    matchBackground.setEffect(new BoxBlur(5, 5, 5));
                    mapView.setEffect(new BoxBlur(5, 5, 5));
                    playerViewPane.setEffect(new BoxBlur(5, 5, 5));
                    guiMap.setEffect(new BoxBlur(5, 5, 5));
                    pausePane.setEffect(new BoxBlur(5, 5, 5));
                    activePowers.setEffect(new BoxBlur(5, 5, 5));
                    helper.setEffect(new BoxBlur(5, 5, 5));
                    turnStory.setEffect(new BoxBlur(5, 5, 5));
                    myTurn.setEffect(new BoxBlur(5, 5, 5));
                    chooseCardsBox.setEffect(new BoxBlur(5, 5, 5));
                    chooseCard.setEffect(new BoxBlur(5, 5, 5));
                }));
        showingTimer.play();
    }

    /**
     * This method creates surrender popup
     */
    public void createSurrenderPopup(){
        surrender.setPrefWidth(screenWidth/3);
        surrender.setPrefHeight(screenHeight/3);

        Image surrenderFrame = new Image(MatchScene.class.getResource("/img/loadingAndPopups/frame2.png").toString(), screenWidth/3, screenHeight/3, false, false);
        ImageView surrenderView = new ImageView(surrenderFrame);

        VBox confirmBox = new VBox();

        Text questionPopup = new Text("Are you sure to lose and quit the match?");
        questionPopup.setFont(lillybelleFont);
        HBox answers = new HBox();

        Button yesPopupButton = new Button("Yes");
        Button noPopupButton = new Button("No");

        yesPopupButton.setFont(lillybelleFont);
        yesPopupButton.setCursor(Cursor.HAND);
        noPopupButton.setFont(lillybelleFont);
        noPopupButton.setCursor(Cursor.HAND);



        answers.getChildren().addAll(yesPopupButton, noPopupButton);
        answers.setSpacing(50);
        answers.setAlignment(Pos.CENTER);

        yesPopupButton.setOnAction(e -> {
            backToMenu();
        });
        noPopupButton.setOnAction(e -> {
            closeWinnerOrSurrender(surrender);
        });


        confirmBox.setSpacing(10);
        confirmBox.getChildren().addAll(questionPopup, answers);

        surrender.getChildren().addAll(surrenderView, confirmBox);
        surrender.setAlignment(Pos.CENTER);
        confirmBox.setAlignment(Pos.CENTER);
    }
}
