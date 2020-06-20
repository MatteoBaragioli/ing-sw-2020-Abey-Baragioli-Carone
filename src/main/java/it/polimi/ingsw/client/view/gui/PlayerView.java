package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.network.objects.PlayerProxy;
import it.polimi.ingsw.server.model.Colour;
import static it.polimi.ingsw.client.view.Coordinates.*;
import javafx.animation.*;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static javafx.geometry.Pos.*;
import static javafx.scene.paint.Color.*;

public class PlayerView extends StackPane {
    //big font
    private final Font standardFont;

    //active powers names font
    private final Font activePowersFont;

    //medium font
    private final Font lillybelle;

    //window width
    private final double screenWidth;

    //window height
    private final double screenHeight;

    //player view pane
    private StackPane playerView = new StackPane();

    //reference to gui
    private final Gui gui;

    //Match scene
    private final MatchScene match;

    //messages to show to player
    private Label messagesBox;

    //current turn
    private Label currentTurn;

    //player's god name
    private Label cardName;

    //player's god card frame
    private ImageView cardView;

    //player's god card image
    private ImageView godView;

    //player's name frame
    private ImageView nameView;

    //player's name
    private Label textBox;

    //active powers pane
    private final StackPane activePowers;

    //turn story
    private final StackPane turnStory;

    //pause menu
    private final StackPane pausePane;

    //helper pane
    private final StackPane helper;

    //helper page number
    private int helperPage;

    //opponents list view
    private VBox opponentsView;

    //opponents view background images list (I need it to set loser background)
    private final HashMap<String, ImageView> opponentsViews = new HashMap<>();

    //opponents view boxes list (I need it to set loser background)
    private final HashMap<String, StackPane> opponentsBoxes = new HashMap<>();

    //opponents powers
    private final HashMap<String, Label> opponentsPowers = new HashMap<>();

    //opponentsNames
    private final HashMap<String, Label> opponentsNames = new HashMap<>();

    //active powers
    private VBox activePowersContent;

    //confirm button
    private ImageView confirmView;

    //undo button
    private ImageView undoView;

    //undo button
    private ImageView usePowerView;

    //confirm button images
    private Image confirmImg;
    private Image confirmInactiveImg;

    //undo button images
    private Image undoImg;
    private Image undoInactiveImg;

    //usePower button images
    private Image usePowerImg;
    private Image usePowerInactiveImg;

    //use power answer
    private final AtomicInteger usePowerAnswer = new AtomicInteger(2);

    //timer
    private Label timerCounter;

    //timer timeline
    private Timeline timerTimeLine;

    //story content
    private VBox storyContent;

    //story player name
    private Label playerStory;

    //pause menu button
    private ImageView pauseView;


    public PlayerView(double screenWidth, double screenHeight, Gui gui, MatchScene match, StackPane pausePane, StackPane activePowers, StackPane helper, StackPane turnStory) {
        standardFont = Font.loadFont(PlayerView.class.getResourceAsStream("/fonts/LillyBelle.ttf"), screenWidth/50);
        activePowersFont = Font.loadFont(PlayerView.class.getResourceAsStream("/fonts/LillyBelle.ttf"), screenWidth/70);
        lillybelle = Font.loadFont(PlayerView.class.getResourceAsStream("/fonts/LillyBelle.ttf"), screenWidth/80);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.gui = gui;
        this.match = match;
        this.pausePane = pausePane;
        this.activePowers = activePowers;
        this.helper = helper;
        this.turnStory = turnStory;
        create();
    }

    //-------------------------------------GETTER----------------------------------------------

    public StackPane getPlayerViewStackPane(){
        return playerView;
    }

    public Label currentTurn(){
        return currentTurn;
    }

    public ImageView pauseView(){
        return pauseView;
    }

    //-------------------------------------END GETTER----------------------------------------------

    /**
     * This method creates all border player view
     */
    public void create(){
        playerView = new StackPane();

        HBox matchView = new HBox();
        matchView.setAlignment(CENTER);
        matchView.setPrefWidth(screenWidth);
        matchView.setPrefHeight(screenHeight);

        //--------------------------------------LEFT--------------------------------------
        VBox leftView = new VBox();
        leftView.setPrefWidth(screenWidth/4);
        leftView.setPrefHeight(screenHeight);


        //left TOP

        //pause
        Image pauseImg = new Image(PlayerView.class.getResource("/img/buttons/pause.png").toString(),screenWidth/12, screenHeight/8,false,false);
        pauseView = new ImageView(pauseImg);
        pauseView.setOnMouseEntered(e -> {
            pauseView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 5, 5, 0, 0));
            pauseView.setCursor(Cursor.HAND);
        });
        pauseView.setOnMouseExited(e -> {
            pauseView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
            pauseView.setCursor(Cursor.DEFAULT);
        });
        pauseView.setOnMouseClicked(e -> {
            showPane(pausePane);
        });
        StackPane pause = new StackPane();
        pause.getChildren().add(pauseView);
        pause.setPrefWidth(screenWidth/4);
        pause.setPrefHeight(screenHeight/8);
        pause.setAlignment(TOP_LEFT);

        //timer
        StackPane timer = new StackPane();
        timer.setPrefWidth(screenWidth/4);
        timer.setPrefHeight(screenHeight/5);

        Label timerBackground = new Label();
        Image timerImg = new Image(PlayerView.class.getResource("/img/matchPage/timer.png").toString(),screenWidth/4, screenHeight/5,false,false);
        ImageView timerView = new ImageView(timerImg);
        timerBackground.setGraphic(timerView);

        timerCounter = new Label();
        timerCounter.setFont(standardFont);
        timerCounter.setTextAlignment(TextAlignment.CENTER);
        timerCounter.setPadding(new Insets(screenHeight/23, 0, 0, 0));

        timer.getChildren().addAll(timerBackground, timerCounter);
        timerCounter.setAlignment(BOTTOM_CENTER);



        //left CENTER
        cardView = new ImageView();
        Image godImg = new Image(PlayerView.class.getResource("/img/godCards/noGod.png").toString(),(screenWidth/7), screenHeight/3.3,false,false);
        godView = new ImageView(godImg);


        cardName = new Label();
        cardName.setPrefSize(screenWidth/4, screenHeight/2.4);
        cardName.setFont(standardFont);
        cardName.setAlignment(BOTTOM_CENTER);
        cardName.setTextAlignment(TextAlignment.CENTER);
        cardName.setPadding(new Insets(0, 0, screenHeight/33, 0));


        Label cardInfo = new Label();
        cardInfo.setPrefSize(screenWidth/4, screenHeight/3.3);
        cardInfo.setPadding(new Insets(0, 0,screenWidth/26, 0));
        cardInfo.setAlignment(CENTER);
        cardInfo.setGraphic(godView);

        StackPane card = new StackPane();
        card.setAlignment(CENTER);
        card.setPrefWidth(screenWidth/4);
        card.setPrefHeight(screenHeight/2.5);
        card.getChildren().addAll(cardView, cardInfo, cardName);


        //left BOTTOM
        Image messagesImg = new Image(PlayerView.class.getResource("/img/matchPage/messagesBox.png").toString(),screenWidth/4, screenHeight/4-screenHeight/100,false,false);
        ImageView messagesView = new ImageView(messagesImg);

        messagesBox = new Label("Good Luck!");
        messagesBox.setFont(lillybelle);
        messagesBox.setTextFill(WHITE);
        messagesBox.setPrefWidth(screenWidth/5);
        messagesBox.setPrefHeight(screenHeight/4);
        messagesBox.setWrapText(true);
        messagesBox.setAlignment(CENTER);
        messagesBox.setTextAlignment(TextAlignment.CENTER);

        StackPane messages = new StackPane();
        messages.setAlignment(CENTER);
        messages.setPrefWidth(screenWidth/4);
        messages.setPrefHeight(screenHeight/4-screenHeight/100);
        messages.getChildren().addAll(messagesView, messagesBox);


        leftView.getChildren().addAll(pause, timer, card, messages);

        //--------------------------------------------END LEFT-----------------------------


        //--------------------------------------CENTER--------------------------------------
        VBox centerView = new VBox();
        centerView.setPrefWidth(screenWidth/2);
        centerView.setPrefHeight(screenHeight);

        //center TOP
        Image turnImg = new Image(PlayerView.class.getResource("/img/matchPage/turn.png").toString(),screenWidth/4, screenHeight/12,false,false);
        ImageView turnView = new ImageView(turnImg);

        currentTurn = new Label("Challenger's turn");
        currentTurn.setFont(standardFont);
        currentTurn.setTextFill(WHITE);
        currentTurn.setAlignment(CENTER);
        currentTurn.setTextAlignment(TextAlignment.CENTER);
        currentTurn.setPrefWidth(screenWidth/4);
        currentTurn.setPrefHeight(screenHeight/12);

        StackPane turn = new StackPane();
        turn.setPrefWidth(screenWidth/2);
        turn.setPrefHeight(screenHeight/2);
        turn.getChildren().addAll(turnView, currentTurn);
        setAlignment(turnView, TOP_CENTER);
        setAlignment(currentTurn, TOP_CENTER);


        //center BOTTOM
        nameView = new ImageView();

        textBox = new Label();
        textBox.setFont(standardFont);
        textBox.setPrefWidth(screenWidth/3);
        textBox.setPrefHeight(screenHeight/8);
        textBox.setTextAlignment(TextAlignment.CENTER);
        textBox.setAlignment(CENTER);
        textBox.setPadding(new Insets(0, 0, screenHeight/50, 0));

        StackPane playerName = new StackPane();
        playerName.setPrefWidth(screenWidth/2);
        playerName.setPrefHeight(screenHeight/2);
        playerName.getChildren().addAll(nameView, textBox);
        setAlignment(nameView, BOTTOM_CENTER);
        setAlignment(textBox, BOTTOM_CENTER);


        centerView.getChildren().addAll(turn, playerName);


        //--------------------------------------END CENTER--------------------------------------


        //--------------------------------------RIGHT--------------------------------------
        VBox rightView = new VBox();
        rightView.setPrefWidth(screenWidth/4);
        rightView.setPrefHeight(screenHeight);

        //right TOP
        opponentsView = new VBox();
        opponentsView.setPrefWidth(screenWidth/4);
        opponentsView.setPrefHeight(screenHeight/2.2);
        opponentsView.setPadding(new Insets(screenWidth/50, 0, 0, 0));
        opponentsView.setSpacing(screenHeight/50);
        opponentsView.setAlignment(TOP_CENTER);

        //right CENTER

        //story
        Image storyImg = new Image(PlayerView.class.getResource("/img/buttons/story.png").toString(),screenWidth/10, screenHeight/6,false,false);
        ImageView storyView = new ImageView(storyImg);

        storyView.setOnMouseEntered(e -> {
            storyView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 5, 5, 0, 0));
            storyView.setCursor(Cursor.HAND);
        });
        storyView.setOnMouseExited(e -> {
            storyView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
            storyView.setCursor(Cursor.DEFAULT);
        });
        storyView.setOnMouseClicked(e -> {
            showPane(turnStory);
        });

        //show powers
        Image powersImg = new Image(PlayerView.class.getResource("/img/buttons/showPowers.png").toString(),screenWidth/8, screenHeight/6,false,false);
        ImageView powersView = new ImageView(powersImg);

        powersView.setOnMouseEntered(e -> {
            powersView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 5, 5, 0, 0));
            powersView.setCursor(Cursor.HAND);
        });
        powersView.setOnMouseExited(e -> {
            powersView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
            powersView.setCursor(Cursor.DEFAULT);
        });
        powersView.setOnMouseClicked(e -> {
            showPane(activePowers);
        });

        VBox powersAndStory = new VBox();
        powersAndStory.setAlignment(CENTER);
        powersAndStory.setPrefHeight(screenHeight/4);
        powersAndStory.setSpacing(screenHeight/50);
        powersAndStory.getChildren().addAll(storyView, powersView);


        rightView.getChildren().addAll(opponentsView, powersAndStory);


        //right BOTTOM
        confirmImg = new Image(PlayerView.class.getResource("/img/buttons/confirmAction.png").toString(),screenWidth/20, screenHeight/14,false,false);
        confirmInactiveImg = new Image(PlayerView.class.getResource("/img/buttons/confirmActionInactive.png").toString(),screenWidth/20, screenHeight/14,false,false);

        undoImg = new Image(PlayerView.class.getResource("/img/buttons/undo.png").toString(),screenWidth/20, screenHeight/14,false,false);
        undoInactiveImg = new Image(PlayerView.class.getResource("/img/buttons/undoInactive.png").toString(),screenWidth/20, screenHeight/14,false,false);


        usePowerImg = new Image(PlayerView.class.getResource("/img/buttons/usePower.png").toString(),screenWidth/20, screenHeight/14,false,false);
        usePowerInactiveImg = new Image(PlayerView.class.getResource("/img/buttons/usePowerInactive.png").toString(),screenWidth/20, screenHeight/14,false,false);

        HBox buttons = new HBox();
        confirmView = new ImageView(confirmInactiveImg);
        undoView = new ImageView(undoInactiveImg);
        usePowerView = new ImageView(usePowerInactiveImg);

        buttons.getChildren().addAll(usePowerView, undoView, confirmView);
        buttons.setSpacing(screenWidth/50);
        buttons.setPrefWidth(screenWidth/4);
        buttons.setPrefHeight(screenHeight/6);
        buttons.setAlignment(BOTTOM_RIGHT);
        buttons.setPadding(new Insets(screenWidth/40, screenWidth/40, 0,0));

        rightView.getChildren().add(buttons);

        //--------------------------------------END RIGHT--------------------------------------


        //--------------------------------------ACTIVE POWERS----------------------------------

        Image activePowersImg = new Image(PlayerView.class.getResource("/img/matchPage/activePowers.png").toString(),screenWidth/1.5, screenHeight/1.5,false,false);
        ImageView activePowersView = new ImageView(activePowersImg);
        activePowersContent = new VBox();
        activePowersContent.setSpacing(screenHeight/50);
        activePowersContent.setAlignment(CENTER);
        Text noPowers = new Text("No active powers");
        noPowers.setFont(standardFont);
        activePowersContent.getChildren().add(noPowers);

        Image closeActivePowersImage = new Image(PlayerView.class.getResource("/img/buttons/okButton.png").toString(), screenWidth/15, screenHeight/13, false, false);
        ImageView closeActivePowersButton = new ImageView(closeActivePowersImage);
        closeActivePowersButton.setOnMouseEntered(e -> {
            closeActivePowersButton.setCursor(Cursor.HAND);
            closeActivePowersButton.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 5, 5, 0, 0));
        });
        closeActivePowersButton.setOnMouseExited(e -> {
            closeActivePowersButton.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
            closeActivePowersButton.setCursor(Cursor.DEFAULT);
        });
        closeActivePowersButton.setOnMouseClicked(e -> {
            hidePane(activePowers);
        });

        VBox activePowersBox = new VBox();
        activePowersBox.setSpacing(screenHeight/30);
        activePowersBox.setAlignment(CENTER);
        activePowersBox.getChildren().addAll(activePowersContent, closeActivePowersButton);


        activePowers.getChildren().addAll(activePowersView, activePowersBox);
        activePowers.setPrefWidth(1.5);
        activePowers.setPrefHeight(1.5);

        //--------------------------------------END ACTIVE POWERS----------------------------------


        //--------------------------------------PAUSE----------------------------------------------
        Image pausePaneImg = new Image(PlayerView.class.getResource("/img/matchPage/pauseMenu.png").toString(),screenWidth/2.5, screenHeight-screenHeight/20,false,false);
        ImageView pausePaneView = new ImageView(pausePaneImg);
        VBox pauseOptions = new VBox();

        Image howToPlayImg = new Image(PlayerView.class.getResource("/img/buttons/howToPlay.png").toString(),screenWidth/10, screenHeight/6,false,false);
        ImageView howToPlayView = new ImageView(howToPlayImg);
        howToPlayView.setOnMouseEntered(e -> {
            howToPlayView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 5, 5, 0, 0));
            howToPlayView.setCursor(Cursor.HAND);
        });
        howToPlayView.setOnMouseExited(e -> {
            howToPlayView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
            howToPlayView.setCursor(Cursor.DEFAULT);
        });
        howToPlayView.setOnMouseClicked(e -> {
            gui.menuScene().showHowToPlay();
        });

        Image helperImg = new Image(PlayerView.class.getResource("/img/buttons/helper.png").toString(),screenWidth/10, screenHeight/6,false,false);
        ImageView helperView = new ImageView(helperImg);
        helperView.setOnMouseEntered(e -> {
            helperView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 5, 5, 0, 0));
            helperView.setCursor(Cursor.HAND);
        });
        helperView.setOnMouseExited(e -> {
            helperView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
            helperView.setCursor(Cursor.DEFAULT);
        });
        helperView.setOnMouseClicked(e ->{
            createHelper();
            showPane(helper);
        });

        Image surrenderImg = new Image(PlayerView.class.getResource("/img/buttons/surrender.png").toString(),screenWidth/10, screenHeight/6,false,false);
        ImageView surrenderView = new ImageView(surrenderImg);
        surrenderView.setOnMouseEntered(e -> {
            surrenderView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 5, 5, 0, 0));
            surrenderView.setCursor(Cursor.HAND);
        });
        surrenderView.setOnMouseExited(e -> {
            surrenderView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
            surrenderView.setCursor(Cursor.DEFAULT);
        });
        surrenderView.setOnMouseClicked(e -> {
            gui.matchScene().showWinnerOrSurrender("surrender");
        });

        Image backToMatchImg = new Image(PlayerView.class.getResource("/img/buttons/backToMatch.png").toString(),screenWidth/10, screenHeight/6,false,false);
        ImageView backToMatchView = new ImageView(backToMatchImg);
        backToMatchView.setOnMouseEntered(e -> {
            backToMatchView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 5, 5, 0, 0));
            backToMatchView.setCursor(Cursor.HAND);
        });
        backToMatchView.setOnMouseExited(e -> {
            backToMatchView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
            backToMatchView.setCursor(Cursor.DEFAULT);
        });
        backToMatchView.setOnMouseClicked((e -> {
            hidePane(pausePane);
        }));

        pauseOptions.getChildren().addAll(backToMatchView, howToPlayView, helperView, surrenderView);
        pauseOptions.setAlignment(CENTER);


        pausePane.getChildren().addAll(pausePaneView, pauseOptions);

        //---------------------------------------END PAUSE-----------------------------------------


        //---------------------------------------STORY------------------------------

        Image storyImage = new Image(PlayerView.class.getResource("/img/matchPage/activePowers.png").toString(), screenWidth/2, screenHeight/2, false, false);
        ImageView storyBackground = new ImageView(storyImage);

        storyContent = new VBox();
        storyContent.setAlignment(CENTER);
        storyContent.setSpacing(screenHeight/100);
        storyContent.setPrefWidth(screenWidth/2.5);

        playerStory = new Label();
        playerStory.setFont(standardFont);
        playerStory.setTextAlignment(TextAlignment.CENTER);
        playerStory.setAlignment(CENTER);


        Label storyContentDefault = new Label("No one has player yet");
        storyContentDefault.setFont(lillybelle);
        storyContentDefault.setTextAlignment(TextAlignment.CENTER);
        storyContentDefault.setAlignment(CENTER);

        storyContent.getChildren().add(storyContentDefault);

        Image closeStoryImage = new Image(PlayerView.class.getResource("/img/buttons/okButton.png").toString(), screenWidth/15, screenHeight/13, false, false);
        ImageView closeStoryButton = new ImageView(closeStoryImage);
        closeStoryButton.setOnMouseEntered(e -> {
            closeStoryButton.setCursor(Cursor.HAND);
            closeStoryButton.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 5, 5, 0, 0));
        });
        closeStoryButton.setOnMouseExited(e -> {
            closeStoryButton.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
            closeStoryButton.setCursor(Cursor.DEFAULT);
        });
        closeStoryButton.setOnMouseClicked(e -> {
            hidePane(turnStory);
        });

        VBox story = new VBox();
        story.getChildren().addAll(storyContent, closeStoryButton);
        story.setSpacing(0);
        story.setAlignment(CENTER);
        turnStory.getChildren().addAll(storyBackground, story);



        //---------------------------------------END STORY------------------------------

        matchView.getChildren().addAll(leftView, centerView, rightView);
        playerView.getChildren().addAll(matchView);

    }

    /**
     * This method creates helper pane
     */
    private void createHelper(){
        Image helperBackground = new Image(PlayerView.class.getResource("/img/helper/helperBackground.png").toString(), screenWidth/1.2, screenHeight/1.2, false, false);
        ImageView helperBackgroundView = new ImageView(helperBackground);

        Image backImg = new Image(PlayerView.class.getResource("/img/buttons/backArrow.png").toString(), screenWidth/25, screenHeight/20, false, false);
        Image backInactiveImg = new Image(PlayerView.class.getResource("/img/buttons/backArrowInactive.png").toString(), screenWidth/25, screenHeight/20, false, false);
        ImageView backView = new ImageView(backInactiveImg);
        Image nextImg = new Image(PlayerView.class.getResource("/img/buttons/nextArrow.png").toString(), screenWidth/25, screenHeight/20, false, false);
        Image nextInactiveImg = new Image(PlayerView.class.getResource("/img/buttons/nextArrowInactive.png").toString(), screenWidth/25, screenHeight/20, false, false);
        ImageView nextView = new ImageView(nextImg);

        Image helperImage = new Image(PlayerView.class.getResource("/img/helper/helper1.png").toString(), screenWidth/1.2, screenHeight/1.2, false, false);
        ImageView helperView = new ImageView(helperImage);

        Image exitImage = new Image(PlayerView.class.getResource("/img/buttons/close.png").toString(), screenWidth/15, screenHeight/10, false, false);
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
            hidePane(helper);
        });

        helperPage = 1;

        backView.setOnMouseClicked(e -> {
            if(helperPage==2){
                //deactivate back button
                backView.setImage(backInactiveImg);
                backView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
                backView.setOnMouseEntered(f -> {
                    backView.setCursor(Cursor.DEFAULT);
                });
                backView.setOnMouseExited(Event::consume);
            } else if(helperPage==3){
                //activate next button
                nextView.setImage(nextImg);
                nextView.setOnMouseEntered(f -> {
                    nextView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 5, 5, 0, 0));
                    nextView.setCursor(Cursor.HAND);
                });
                nextView.setOnMouseExited(f -> {
                    nextView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
                    nextView.setCursor(Cursor.DEFAULT);
                });
            }
            if(helperPage!=1) {
                helperPage--;
                helperView.setImage(new Image(PlayerView.class.getResource("/img/helper/helper" + helperPage + ".png").toString(), screenWidth / 1.2, screenHeight / 1.2, false, false));
            }
        });
        nextView.setOnMouseEntered(e -> {
            nextView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 5, 5, 0, 0));
            nextView.setCursor(Cursor.HAND);
        });
        nextView.setOnMouseExited(e -> {
            nextView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
            nextView.setCursor(Cursor.DEFAULT);
        });
        nextView.setOnMouseClicked(e -> {
            if(helperPage==1){
                //activate back button
                backView.setImage(backImg);
                backView.setOnMouseEntered(f -> {
                    backView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 5, 5, 0, 0));
                    backView.setCursor(Cursor.HAND);
                });
                backView.setOnMouseExited(f -> {
                    backView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
                    backView.setCursor(Cursor.DEFAULT);
                });
            } else if(helperPage==2){
                //deactivate next button
                nextView.setImage(nextInactiveImg);
                nextView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
                nextView.setOnMouseEntered(f -> {
                    nextView.setCursor(Cursor.DEFAULT);
                });
                nextView.setOnMouseExited(Event::consume);
            }
            if(helperPage!=3){
                helperPage++;
                helperView.setImage(new Image(PlayerView.class.getResource("/img/helper/helper" + helperPage + ".png").toString(), screenWidth/1.2, screenHeight/1.2, false, false));
            }
        });


        VBox back = new VBox();
        back.setAlignment(Pos.CENTER_LEFT);
        back.getChildren().add(backView);
        back.setPrefWidth(screenWidth/2.7);




        VBox next = new VBox();
        next.setAlignment(Pos.CENTER_RIGHT);
        next.getChildren().add(nextView);
        next.setPrefWidth(screenWidth/2.7);
        HBox helperRaw = new HBox();
        helperRaw.getChildren().addAll(back, next);
        helperRaw.setAlignment(Pos.CENTER);
        helper.getChildren().addAll(helperBackgroundView, helperView, helperRaw, exitButton);
        StackPane.setAlignment(exitButton, Pos.TOP_LEFT);
    }

    /**
     * This method sets all player attributes (nickname, color, opponents) in the player view
     * @param nickname Player's nickname
     * @param color Player's color
     * @param opponents Player's opponents
     */
    public void setPage(String nickname, String color, List<PlayerProxy> opponents, int numberOfOpponents){
        Color fontColor = fontColor(color);

        //set god card frame with player's color
        Image cardImg = new Image(PlayerView.class.getResource("/img/matchPage/godFrame"+color+".png").toString(),screenWidth/4, screenHeight/2.3,false,false);
        cardView.setImage(cardImg);

        //set card name font color
        cardName.setTextFill(fontColor);

        //set player name frame with player's color
        Image nameImg = new Image(PlayerView.class.getResource("/img/matchPage/playerName"+color+".png").toString(),screenWidth/3, screenHeight/8,false,false);
        nameView.setImage(nameImg);

        //set player's name
        textBox.setText(nickname);
        textBox.setTextFill(fontColor);
        if(nickname.length()>20)
            textBox.setFont(lillybelle);

        //set opponents attributes
        for(PlayerProxy opponent : opponents){
            String opponentColor;
            if(opponent.colour.equals(Colour.BLUE))
                opponentColor = "Blue";
            else if(opponent.colour.equals(Colour.WHITE))
                opponentColor = "White";
            else
                opponentColor = "Grey";

            Image opponentImg = new Image(PlayerView.class.getResource("/img/matchPage/opponentView"+opponentColor+".png").toString(),screenWidth/5, screenHeight/5,false,false);
            ImageView opponentView = new ImageView(opponentImg);

            opponentsViews.put(opponent.name, opponentView);

            Label opponentName = new Label(opponent.name);
            opponentsNames.put(opponent.name, opponentName);
            if(opponent.name.length()>20)
                opponentName.setFont(Font.loadFont(PlayerView.class.getResourceAsStream("/fonts/LillyBelle.ttf"), screenWidth/100));
            else
                opponentName.setFont(lillybelle);
            if(opponent.name.length()>10)
                currentTurn.setFont(lillybelle);
            opponentName.setTextFill(fontColor(opponentColor));
            opponentName.setTextAlignment(TextAlignment.CENTER);
            opponentName.setPadding(new Insets(screenHeight/70, 0, 0, 0));

            Label opponentCardName = new Label();
            opponentCardName.setFont(lillybelle);
            opponentCardName.setTextAlignment(TextAlignment.CENTER);
            opponentCardName.setTextFill(fontColor(opponentColor));

            opponentsPowers.put(opponent.name, opponentCardName);

            StackPane opponentBox = new StackPane();
            opponentBox.getChildren().addAll(opponentView, opponentName, opponentCardName);
            setAlignment(opponentName, TOP_CENTER);
            opponentBox.setPrefHeight(screenHeight/5);

            opponentsBoxes.put(opponent.name, opponentBox);

            opponentsView.getChildren().add(opponentBox);
        }
        if(opponents.size()<numberOfOpponents){
            Image loserImg = new Image(PlayerView.class.getResource("/img/matchPage/opponentViewLoser.png").toString(),screenWidth/5, screenHeight/5,false,false);
            ImageView loserView = new ImageView(loserImg);

            Image loser2Img = new Image(PlayerView.class.getResource("/img/matchPage/opponentViewLoser2.png").toString(),screenWidth/5, screenHeight/5,false,false);
            ImageView loserView2 = new ImageView(loser2Img);
            Label opponentName = new Label("Player disconnected");
            opponentName.setFont(lillybelle);
            opponentName.setTextFill(WHITE);
            opponentName.setTextAlignment(TextAlignment.CENTER);
            opponentName.setPadding(new Insets(screenHeight/70, 0, 0, 0));
            StackPane opponentBox = new StackPane();
            opponentBox.getChildren().addAll(loserView, opponentName, loserView2);
            setAlignment(opponentName, TOP_CENTER);
            opponentBox.setPrefHeight(screenHeight/5);

            opponentsView.getChildren().add(opponentBox);
        }
    }

    /**
     * This method assigns card to player and save the power as active power
     * @param player PlayerProxy
     */
    public void setMyCard(PlayerProxy player){
        cardName.setVisible(false);
        cardName.setText(player.godCardProxy.name);
        godView.setVisible(false);
        godView.setImage(new Image(PlayerView.class.getResource("/img/godCards/" + player.godCardProxy.name + ".png").toString(),(screenWidth/7), screenHeight/3.3,false,false));
        Text powerName = new Text(player.godCardProxy.name);
        Text powerInfo = new Text();
        if(player.godCardProxy.description!=null)
            powerInfo.setText(player.godCardProxy.description);
        else if(player.godCardProxy.winDescription!=null)
            powerInfo.setText(player.godCardProxy.winDescription);
        else if(player.godCardProxy.setUpDescription!=null)
            powerInfo.setText(player.godCardProxy.setUpDescription);
        else if(player.godCardProxy.opponentsFxDescription!=null)
            powerInfo.setText(player.godCardProxy.opponentsFxDescription);
        powerName.setFont(activePowersFont);
        powerInfo.setFont(lillybelle);
        powerInfo.setWrappingWidth(screenWidth/2);
        powerInfo.setTextAlignment(TextAlignment.CENTER);
        activePowersContent.getChildren().clear();

        activePowersContent.getChildren().addAll(powerName, powerInfo);

        FadeTransition cardImageFadeIn = new FadeTransition(Duration.millis(1000), godView);
        cardImageFadeIn.setFromValue(0);
        cardImageFadeIn.setToValue(1);
        FadeTransition cardNameFadeIn = new FadeTransition(Duration.millis(1000), cardName);
        cardNameFadeIn.setFromValue(0);
        cardNameFadeIn.setToValue(1);

        cardImageFadeIn.play();
        cardNameFadeIn.play();
        cardName.setVisible(true);
        godView.setVisible(true);
    }

    /**
     * This method assigns card to opponents and save the power as active power
     * @param opponents OpponentsProxy
     */
    public void setOpponentsCards(List<PlayerProxy> opponents){
        for(PlayerProxy opponent : opponents){
            opponentsPowers.get(opponent.name).setVisible(false);
            opponentsPowers.get(opponent.name).setText(opponent.godCardProxy.name);
            Text powerName = new Text(opponent.godCardProxy.name);
            Text powerInfo = new Text();
            if(opponent.godCardProxy.description!=null)
                powerInfo.setText(opponent.godCardProxy.description);
            else if(opponent.godCardProxy.winDescription!=null)
                powerInfo.setText(opponent.godCardProxy.winDescription);
            else if(opponent.godCardProxy.setUpDescription!=null)
                powerInfo.setText(opponent.godCardProxy.setUpDescription);
            else if(opponent.godCardProxy.opponentsFxDescription!=null)
                powerInfo.setText(opponent.godCardProxy.opponentsFxDescription);

            powerName.setFont(activePowersFont);
            powerInfo.setFont(lillybelle);
            powerInfo.setWrappingWidth(screenWidth/2);
            powerInfo.setTextAlignment(TextAlignment.CENTER);
            activePowersContent.getChildren().addAll(powerName, powerInfo);

            FadeTransition opponentCardFadeIn = new FadeTransition(Duration.millis(1000), opponentsPowers.get(opponent.name));
            opponentCardFadeIn.setFromValue(0);
            opponentCardFadeIn.setToValue(1);

            opponentCardFadeIn.play();
            opponentsPowers.get(opponent.name).setVisible(true);
        }
    }

    /**
     * This method shows panes
     * @param pane Pane to show
     */
    private void showPane(StackPane pane){
        FadeTransition paneFadeIn = new FadeTransition(Duration.millis(700), pane);
        paneFadeIn.setFromValue(0);
        paneFadeIn.setToValue(1);

        ScaleTransition paneZoomIn1 = new ScaleTransition(Duration.millis(700), pane);
        paneZoomIn1.setFromX(0);
        paneZoomIn1.setToX(1);
        paneZoomIn1.setFromY(0.01);
        paneZoomIn1.setToY(0.01);

        ScaleTransition paneZoomIn2 = new ScaleTransition(Duration.millis(700), pane);
        paneZoomIn2.setFromY(0.01);
        paneZoomIn2.setToY(1);

        paneFadeIn.play();
        pane.setVisible(true);
        paneZoomIn1.play();

        Timeline showingTimer = new Timeline(new KeyFrame(
                Duration.millis(700),
                ae -> {
                    paneZoomIn2.play();
                }));
        showingTimer.play();
    }

    /**
     *  This method hides panes and re-shows the map if parameter showMap is true
     * @param pane Pane to hide
     */
    private void hidePane(StackPane pane){
        FadeTransition paneFadeOut = new FadeTransition(Duration.millis(700), pane);
        paneFadeOut.setFromValue(1);
        paneFadeOut.setToValue(0);

        ScaleTransition paneZoomOut1 = new ScaleTransition(Duration.millis(700), pane);
        paneZoomOut1.setFromY(1);
        paneZoomOut1.setToY(0.01);
        paneZoomOut1.setFromX(1);
        paneZoomOut1.setToX(1);

        ScaleTransition paneZoomIn2 = new ScaleTransition(Duration.millis(700), pane);
        paneZoomIn2.setFromX(1);
        paneZoomIn2.setToX(0);

        paneZoomOut1.play();

        Timeline hidingTimer1 = new Timeline(new KeyFrame(
                Duration.millis(700),
                ae -> {
                    paneZoomIn2.play();
                    paneFadeOut.play();
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
     * This method change the message inside the info box
     * @param messageString Message to show
     */
    public void changeMessage(String messageString){
        messagesBox.setFont(lillybelle);
        messagesBox.setText(messageString);
    }

    /**
     * This method activated undo and confirm buttons
     */
    public void activateButtons(){
        confirmView.setImage(confirmImg);
        confirmView.setOnMouseEntered(e -> {
            confirmView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 3, 3, 0, 0));
            confirmView.setCursor(Cursor.HAND);
            e.consume();
        });
        confirmView.setOnMouseExited(e -> {
            confirmView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
            confirmView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
        confirmView.setOnMouseClicked(e -> {
            match.setChosenBoxIndex(match.chosenBox().index());
            match.chosenBox().removeColor();
            match.clearChosableBoxes();
            match.setDestinationReady(true);
            deactivateButtons();
        });

        undoView.setImage(undoImg);
        undoView.setOnMouseEntered(e -> {
            undoView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 3, 3, 0, 0));
            undoView.setCursor(Cursor.HAND);
            e.consume();
        });
        undoView.setOnMouseExited(e -> {
            undoView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
            undoView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
        undoView.setOnMouseClicked(e -> {
            match.chosenBox().removeColor();
            for (int[] i : match.chosableBoxes()) {
                match.map().box(i[0], i[1]).setAsChosable(match);
            }
            match.playerView().deactivateButtons();
        });
    }

    /**
     * This method deactivates undo and confirm button
     */
    public void deactivateButtons() {
        confirmView.setImage(confirmInactiveImg);
        confirmView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
        confirmView.setOnMouseEntered(e -> {
            confirmView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
        confirmView.setOnMouseExited(e -> {
            confirmView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
        confirmView.setOnMouseClicked(e -> {
            confirmView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
        undoView.setImage(undoInactiveImg);
        undoView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
        undoView.setOnMouseEntered(e -> {
            undoView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
        undoView.setOnMouseExited(e -> {
            undoView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
        undoView.setOnMouseClicked(e -> {
            undoView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
    }

    /**
     * This method activates use power button
     */
    public void activateUsePower(){
        usePowerAnswer.set(2);
        usePowerView.setImage(usePowerImg);
        usePowerView.setOnMouseEntered(e -> {
            usePowerView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 3, 3, 0, 0));
            usePowerView.setCursor(Cursor.HAND);
            e.consume();
        });
        usePowerView.setOnMouseExited(e -> {
            usePowerView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
            usePowerView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
        usePowerView.setOnMouseClicked(e -> {
            setUsePowerAnswer(0);
            usePowerView.setCursor(Cursor.HAND);
            e.consume();
        });

        confirmView.setImage(confirmImg);
        confirmView.setOnMouseEntered(e -> {
            confirmView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 3, 3, 0, 0));
            confirmView.setCursor(Cursor.HAND);
            e.consume();
        });
        confirmView.setOnMouseExited(e -> {
            confirmView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
            confirmView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
        confirmView.setOnMouseClicked(e -> {
            setUsePowerAnswer(1);
            deactivateUsePower();
        });
    }

    /**
     * This method deactivates use power button
     */
    public void deactivateUsePower() {
        usePowerView.setImage(usePowerInactiveImg);
        usePowerView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
        usePowerView.setOnMouseEntered(e -> {
            usePowerView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
        usePowerView.setOnMouseExited(e -> {
            usePowerView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
        usePowerView.setOnMouseClicked(e -> {
            usePowerView.setCursor(Cursor.DEFAULT);
            e.consume();
        });

        confirmView.setImage(confirmInactiveImg);
        confirmView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
        confirmView.setOnMouseEntered(e -> {
            confirmView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
        confirmView.setOnMouseExited(e -> {
            confirmView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
        confirmView.setOnMouseClicked(e -> {
            confirmView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
    }

    /**
     * This method sets answer to send to server and notify synchronized method that waits for sending it
     * @param answer Answer to send to user
     */
    private synchronized void setUsePowerAnswer(int answer){
        usePowerAnswer.set(answer);
        deactivateUsePower();
        notifyAll();
    }

    /**
     * This method waits for player to chose to use power or not and sends the answer to server
     * @return Answer chosen by the player and sent to server
     */
    public synchronized int askUsePower(){
        activateUsePower();
        while (usePowerAnswer.get()==2){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return usePowerAnswer.get();
    }

    /**
     * This method initialises the font color, based on player color
     * @param color Player's color
     * @return Font color
     */
    public Color fontColor(String color){
        if(color.equals("White")){
            return BLACK;
        }else{
            return WHITE;
        }
    }

    /**
     * This method shows current turn
     * @param currentTurnMessage Message to show
     */
    public void setCurrentTurn(String currentTurnMessage){
        FadeTransition currentTurnFadeOut = new FadeTransition(Duration.millis(500), currentTurn);
        currentTurnFadeOut.setFromValue(1);
        currentTurnFadeOut.setToValue(0);
        currentTurnFadeOut.play();
        FadeTransition currentTurnFadeIn = new FadeTransition(Duration.millis(500), currentTurn);
        currentTurnFadeIn.setFromValue(0);
        currentTurnFadeIn.setToValue(1);

        Timeline hidingTimer = new Timeline(new KeyFrame(
                Duration.millis(500),
                ae -> {
                    currentTurnFadeIn.play();
                    currentTurn.setText(currentTurnMessage);
                }));
        hidingTimer.play();
        changeMessage("Wait for other players to finish their turns");
    }

    /**
     * This method starts the timer
     */
    public void playTimer(){
        if(timerTimeLine!=null)
            timerTimeLine.stop();
        //AtomicReference<String> min = new AtomicReference<>("02");
        //AtomicReference<String> sec = new AtomicReference<>("00");
        AtomicReference<String> min = new AtomicReference<>("00");
        AtomicReference<String> sec = new AtomicReference<>("30");
        timerCounter.setFont(standardFont);
        timerCounter.setText(min.toString() + " : " + sec.toString());
        //AtomicInteger seconds = new AtomicInteger(0);
        //AtomicInteger minutes = new AtomicInteger(2);
        AtomicInteger seconds = new AtomicInteger(30);
        AtomicInteger minutes = new AtomicInteger(0);
        timerTimeLine = new Timeline(
                new KeyFrame(
                        Duration.millis( 1000 ),
                        event -> {
                            seconds.getAndDecrement();
                            if(seconds.get()<0) {
                                seconds.set(59);
                                minutes.getAndDecrement();
                                if(minutes.get()<0){
                                    timerTimeLine.stop();
                                    seconds.set(0);
                                    minutes.set(0);
                                    gui.matchScene().setEndTimer();
                                }
                            }
                            if(seconds.get()<10){
                                sec.set("0" + seconds.toString());
                            } else {
                                sec.set(seconds.toString());
                            }
                            min.set("0" + minutes.toString());
                            timerCounter.setText(min.toString() + " : " + sec.toString());
                            if(minutes.get() == 0 && seconds.get() <= 20){
                                if(timerCounter.getTextFill().equals(DARKRED)){
                                    timerCounter.setTextFill(BLACK);
                                } else {
                                    timerCounter.setTextFill(DARKRED);
                                }
                            }
                        }
                )
        );
        timerTimeLine.setCycleCount(Animation.INDEFINITE);
        timerTimeLine.play();
    }

    /**
     * This method stops the timer and sets "Wait"
     */
    public void stopTimer(){
        if(timerTimeLine!=null)
            timerTimeLine.stop();
        timerCounter.setText("Wait");
        timerCounter.setFont(lillybelle);
        timerTimeLine = new Timeline(
                new KeyFrame(
                        Duration.millis( 800 ),
                        event -> {
                            if(timerCounter.getText().equals("Wait")){
                                timerCounter.setText("Wait.");
                            } else if(timerCounter.getText().equals("Wait.")){
                                timerCounter.setText("Wait..");
                            } else if(timerCounter.getText().equals("Wait..")){
                                timerCounter.setText("Wait...");
                            } else {
                                timerCounter.setText("Wait");
                            }
                        }
                )
        );
        timerTimeLine.setCycleCount(Animation.INDEFINITE);
        timerTimeLine.play();
    }

    /**
     * This method stops the timer and hides texts
     */
    public void endTimer(){
        if(timerTimeLine!=null)
            timerTimeLine.stop();
        timerCounter.setText("");
    }


    /**
     * This method writes last turn story in the story pane
     * @param player Name of last turn player
     * @param chosenWorker Position of chosen worker
     * @param action Action made by the player
     * @param destination Destination of the action
     */
    public void writeStory(String player, int[] chosenWorker, CommunicationProtocol action, int[] destination){
        if(!playerStory.getText().equals(player + " played")){
            storyContent.getChildren().clear();
            playerStory.setText(player + " played");
            storyContent.getChildren().add(playerStory);
        }

        switch (action){
            case DESTINATION: {
                Label destinationStory = new Label("Moved from box (" + getChessCoordinates(chosenWorker) + ") to box (" + getChessCoordinates(destination) + ")");
                destinationStory.setFont(lillybelle);
                destinationStory.setTextAlignment(TextAlignment.CENTER);
                destinationStory.setAlignment(CENTER);
                destinationStory.setMaxWidth(screenWidth/2.5);
                destinationStory.setWrapText(true);
                storyContent.getChildren().add(destinationStory);
            }
            break;
            case BUILD:{
                Label buildStory = new Label("Built on box (" + getChessCoordinates(destination) + ") with worker in box (" + getChessCoordinates(chosenWorker) + ")");
                buildStory.setFont(lillybelle);
                buildStory.setTextAlignment(TextAlignment.CENTER);
                buildStory.setAlignment(CENTER);
                buildStory.setMaxWidth(screenWidth/2.5);
                buildStory.setWrapText(true);
                storyContent.getChildren().add(buildStory);
            }
            break;
            case REMOVAL:{
                Label removalStory = new Label("Removed block on box (" + getChessCoordinates(destination) + ") with worker in box (" + getChessCoordinates(chosenWorker) + ")");
                removalStory.setFont(lillybelle);
                removalStory.setTextAlignment(TextAlignment.CENTER);
                removalStory.setAlignment(CENTER);
                removalStory.setMaxWidth(screenWidth/2.5);
                removalStory.setWrapText(true);
                storyContent.getChildren().add(removalStory);
            }
        }
    }

    /**
     * This method sets an opponent as loser when he loses the match
     * @param loser Opponent that loses the match
     */
    public void setLoser(String loser){
        if(opponentsNames.containsKey(loser)) {
            Image loserImg = new Image(PlayerView.class.getResource("/img/matchPage/opponentViewLoser.png").toString(), screenWidth / 5, screenHeight / 5, false, false);
            opponentsViews.get(loser).setImage(loserImg);

            Image loser2Img = new Image(PlayerView.class.getResource("/img/matchPage/opponentViewLoser2.png").toString(), screenWidth / 5, screenHeight / 5, false, false);
            ImageView loserView = new ImageView(loser2Img);

            opponentsBoxes.get(loser).getChildren().add(loserView);

            opponentsNames.get(loser).setTextFill(WHITE);

            opponentsPowers.get(loser).setVisible(false);
            opponentsPowers.get(loser).setManaged(false);

            Label loserStory = new Label(loser + " lost the match");
            loserStory.setFont(lillybelle);
            loserStory.setTextAlignment(TextAlignment.CENTER);
            loserStory.setAlignment(CENTER);
            loserStory.setMaxWidth(screenWidth / 2.5);
            loserStory.setWrapText(true);
            storyContent.getChildren().add(loserStory);
        }
    }
}
