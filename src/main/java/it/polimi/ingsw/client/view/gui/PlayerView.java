package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.network.objects.PlayerProxy;
import it.polimi.ingsw.server.model.Colour;
import javafx.animation.*;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
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

import static javafx.geometry.Pos.*;
import static javafx.scene.paint.Color.*;

public class PlayerView extends StackPane {
    //big font
    private final Font standardFont;

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

    //pause menu
    private final StackPane pausePane;

    //helper pane
    private final StackPane helper;

    //helper page number
    private int helperPage;

    //opponents list view
    private VBox opponentsView;

    //opponents powers
    private final HashMap<String, Label> opponentsPowers = new HashMap<>();

    //active powers
    private VBox activePowersBox;

    //confirm button
    private ImageView confirmView;

    //undo button
    private ImageView undoView;

    //undo button
    private ImageView usePowerView;

    //confirm button images
    private Image confirmImg;
    private Image confirmImgHover;
    private Image confirmInactiveImg;

    //undo button images
    private Image undoImg;
    private Image undoImgHover;
    private Image undoInactiveImg;

    //usePower button images
    private Image usePowerImg;
    private Image usePowerImgHover;
    private Image usePowerInactiveImg;

    //use power answer
    private final AtomicInteger usePowerAnswer = new AtomicInteger(2);

    //timer minutes
    private String min = "02";

    //timer seconds
    private String sec = "00";

    public PlayerView(double screenWidth, double screenHeight, Gui gui, MatchScene match, StackPane pausePane, StackPane activePowers, StackPane helper) {
        standardFont = Font.loadFont(PlayerView.class.getResourceAsStream("/fonts/LillyBelle.ttf"), screenWidth/50);
        lillybelle = Font.loadFont(PlayerView.class.getResourceAsStream("/fonts/LillyBelle.ttf"), screenWidth/80);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.gui = gui;
        this.match = match;
        this.pausePane = pausePane;
        this.activePowers = activePowers;
        this.helper = helper;
        create();
    }

    //-------------------------------------GETTER----------------------------------------------

    public StackPane getPlayerViewStackPane(){
        return playerView;
    }

    public String getMessage(){
        return messagesBox.getText();
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
        Image pauseHoverImg = new Image(PlayerView.class.getResource("/img/buttons/pauseHover.png").toString(),screenWidth/12, screenHeight/8,false,false);
        ImageView pauseView = new ImageView(pauseImg);
        pauseView.setOnMouseEntered(e -> {
            pauseView.setImage(pauseHoverImg);
            pauseView.setCursor(Cursor.HAND);
        });
        pauseView.setOnMouseExited(e -> {
            pauseView.setImage(pauseImg);
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
        Image timerImg = new Image(PlayerView.class.getResource("/img/buttons/timer.png").toString(),screenWidth/4, screenHeight/5,false,false);
        ImageView timerView = new ImageView(timerImg);
        timerBackground.setGraphic(timerView);


        Label timerCounter = new Label();
        timerCounter.setText(min + " : " + sec);
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
        Image messagesImg = new Image(PlayerView.class.getResource("/img/messagesBox.png").toString(),screenWidth/4, screenHeight/4,false,false);
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
        messages.setPrefHeight(screenHeight/4);
        messages.getChildren().addAll(messagesView, messagesBox);
        messages.setPadding(new Insets(0,0,screenHeight/100, 0));


        leftView.getChildren().addAll(pause, timer, card, messages);

        //--------------------------------------------END LEFT-----------------------------


        //--------------------------------------CENTER--------------------------------------
        VBox centerView = new VBox();
        centerView.setPrefWidth(screenWidth/2);
        centerView.setPrefHeight(screenHeight);

        //center TOP
        Image turnImg = new Image(PlayerView.class.getResource("/img/turn.png").toString(),screenWidth/4, screenHeight/12,false,false);
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
        opponentsView.setPrefHeight(screenHeight/2);
        opponentsView.setPadding(new Insets(screenWidth/50, 0, 0, 0));
        opponentsView.setSpacing(screenHeight/50);
        opponentsView.setAlignment(TOP_CENTER);

        //right CENTER
        Image powersImg = new Image(PlayerView.class.getResource("/img/buttons/showPowers.png").toString(),screenWidth/6, screenHeight/4,false,false);
        Image powersHoverImg = new Image(PlayerView.class.getResource("/img/buttons/showPowersHover.png").toString(),screenWidth/6, screenHeight/4,false,false);
        ImageView powersView = new ImageView(powersImg);

        powersView.setOnMouseEntered(e -> {
            powersView.setImage(powersHoverImg);
            powersView.setCursor(Cursor.HAND);
        });
        powersView.setOnMouseExited(e -> {
            powersView.setImage(powersImg);
            powersView.setCursor(Cursor.DEFAULT);
        });
        powersView.setOnMouseClicked(e -> {
            showPane(activePowers);
        });

        StackPane powers = new StackPane(powersView);
        powers.setAlignment(CENTER);
        powers.setPrefHeight(screenHeight/4);


        rightView.getChildren().addAll(opponentsView, powers);


        //right BOTTOM
        confirmImg = new Image(PlayerView.class.getResource("/img/buttons/confirmAction.png").toString(),screenWidth/20, screenHeight/14,false,false);
        confirmImgHover = new Image(PlayerView.class.getResource("/img/buttons/confirmActionHover.png").toString(),screenWidth/20, screenHeight/14,false,false);
        confirmInactiveImg = new Image(PlayerView.class.getResource("/img/buttons/confirmActionInactive.png").toString(),screenWidth/20, screenHeight/14,false,false);

        undoImg = new Image(PlayerView.class.getResource("/img/buttons/undo.png").toString(),screenWidth/20, screenHeight/14,false,false);
        undoImgHover = new Image(PlayerView.class.getResource("/img/buttons/undoHover.png").toString(),screenWidth/20, screenHeight/14,false,false);
        undoInactiveImg = new Image(PlayerView.class.getResource("/img/buttons/undoInactive.png").toString(),screenWidth/20, screenHeight/14,false,false);


        usePowerImg = new Image(PlayerView.class.getResource("/img/buttons/usePower.png").toString(),screenWidth/20, screenHeight/14,false,false);
        usePowerImgHover = new Image(PlayerView.class.getResource("/img/buttons/usePowerHover.png").toString(),screenWidth/20, screenHeight/14,false,false);
        usePowerInactiveImg = new Image(PlayerView.class.getResource("/img/buttons/usePowerInactive.png").toString(),screenWidth/20, screenHeight/14,false,false);

        HBox buttons = new HBox();
        confirmView = new ImageView(confirmInactiveImg);
        undoView = new ImageView(undoInactiveImg);
        usePowerView = new ImageView(usePowerInactiveImg);

        buttons.getChildren().addAll(usePowerView, undoView, confirmView);
        buttons.setSpacing(screenWidth/50);
        buttons.setPrefWidth(screenWidth/4);
        buttons.setPrefHeight(screenHeight/5);
        buttons.setAlignment(BOTTOM_RIGHT);
        buttons.setPadding(new Insets(screenWidth/40, screenWidth/40, 0,0));

        rightView.getChildren().add(buttons);

        //--------------------------------------END RIGHT--------------------------------------


        //--------------------------------------ACTIVE POWERS----------------------------------

        Image activePowersImg = new Image(PlayerView.class.getResource("/img/activePowers.png").toString(),screenWidth/1.5, screenHeight/1.5,false,false);
        ImageView activePowersView = new ImageView(activePowersImg);
        activePowersBox = new VBox();
        activePowersBox.setSpacing(screenHeight/50);
        activePowersBox.setAlignment(CENTER);
        Text noPowers = new Text("No active powers");
        noPowers.setFont(standardFont);
        activePowersBox.getChildren().add(noPowers);
        Image exitImage = new Image(MenuScene.class.getResource("/img/buttons/close.png").toString(), screenWidth/15, screenHeight/10, false, false);
        Image exitHoverImage = new Image(MenuScene.class.getResource("/img/buttons/closeHover.png").toString(), screenWidth/15, screenHeight/10, false, false);
        ImageView exitButton = new ImageView(exitImage);
        exitButton.setOnMouseEntered(e -> {
            exitButton.setCursor(Cursor.HAND);
            exitButton.setImage(exitHoverImage);
        });
        exitButton.setOnMouseExited(e -> {
            exitButton.setImage(exitImage);
            exitButton.setCursor(Cursor.DEFAULT);
        });
        exitButton.setOnMouseClicked(e -> {
            hidePane(activePowers);
        });
        VBox exit = new VBox(exitButton);
        exit.setPadding(new Insets(screenHeight/10, 0, 0, screenWidth/10));

        activePowers.getChildren().addAll(activePowersView, activePowersBox, exit);
        activePowers.setPrefWidth(1.5);
        activePowers.setPrefHeight(1.5);
        setAlignment(exit, TOP_LEFT);

        //--------------------------------------END ACTIVE POWERS----------------------------------


        //--------------------------------------PAUSE----------------------------------------------
        Image pausePaneImg = new Image(PlayerView.class.getResource("/img/pauseMenu.png").toString(),screenWidth/2.5, screenHeight-screenHeight/20,false,false);
        ImageView pausePaneView = new ImageView(pausePaneImg);
        VBox pauseOptions = new VBox();

        Image howToPlayImg = new Image(PlayerView.class.getResource("/img/buttons/howToPlay.png").toString(),screenWidth/10, screenHeight/6,false,false);
        Image howToPlayHoverImg = new Image(PlayerView.class.getResource("/img/buttons/howToPlayHover.png").toString(),screenWidth/10, screenHeight/6,false,false);
        ImageView howToPlayView = new ImageView(howToPlayImg);
        howToPlayView.setOnMouseEntered(e -> {
            howToPlayView.setImage(howToPlayHoverImg);
            howToPlayView.setCursor(Cursor.HAND);
        });
        howToPlayView.setOnMouseExited(e -> {
            howToPlayView.setImage(howToPlayImg);
            howToPlayView.setCursor(Cursor.DEFAULT);
        });
        howToPlayView.setOnMouseClicked(e -> {
            showPane(gui.howToPlayBox());
        });

        Image helperImg = new Image(PlayerView.class.getResource("/img/buttons/helper.png").toString(),screenWidth/10, screenHeight/6,false,false);
        Image helperHoverImg = new Image(PlayerView.class.getResource("/img/buttons/helperHover.png").toString(),screenWidth/10, screenHeight/6,false,false);
        ImageView helperView = new ImageView(helperImg);
        helperView.setOnMouseEntered(e -> {
            helperView.setImage(helperHoverImg);
            helperView.setCursor(Cursor.HAND);
        });
        helperView.setOnMouseExited(e -> {
            helperView.setImage(helperImg);
            helperView.setCursor(Cursor.DEFAULT);
        });
        helperView.setOnMouseClicked(e ->{
            showPane(helper);
        });

        Image surrenderImg = new Image(PlayerView.class.getResource("/img/buttons/surrender.png").toString(),screenWidth/10, screenHeight/6,false,false);
        Image surrenderHoverImg = new Image(PlayerView.class.getResource("/img/buttons/surrenderHover.png").toString(),screenWidth/10, screenHeight/6,false,false);
        ImageView surrenderView = new ImageView(surrenderImg);
        surrenderView.setOnMouseEntered(e -> {
            surrenderView.setImage(surrenderHoverImg);
            surrenderView.setCursor(Cursor.HAND);
        });
        surrenderView.setOnMouseExited(e -> {
            surrenderView.setImage(surrenderImg);
            surrenderView.setCursor(Cursor.DEFAULT);
        });

        Image backToMatchImg = new Image(PlayerView.class.getResource("/img/buttons/backToMatch.png").toString(),screenWidth/10, screenHeight/6,false,false);
        Image backToMatchHoverImg = new Image(PlayerView.class.getResource("/img/buttons/backToMatchHover.png").toString(),screenWidth/10, screenHeight/6,false,false);
        ImageView backToMatchView = new ImageView(backToMatchImg);
        backToMatchView.setOnMouseEntered(e -> {
            backToMatchView.setImage(backToMatchHoverImg);
            backToMatchView.setCursor(Cursor.HAND);
        });
        backToMatchView.setOnMouseExited(e -> {
            backToMatchView.setImage(backToMatchImg);
            backToMatchView.setCursor(Cursor.DEFAULT);
        });
        backToMatchView.setOnMouseClicked((e -> {
            hidePane(pausePane);
        }));

        pauseOptions.getChildren().addAll(backToMatchView, howToPlayView, helperView, surrenderView);
        pauseOptions.setAlignment(CENTER);


        pausePane.getChildren().addAll(pausePaneView, pauseOptions);

        //---------------------------------------END PAUSE-----------------------------------------


        //---------------------------------------HELPER--------------------------------------------

        Image backHelperImg = new Image(MenuScene.class.getResource("/img/buttons/backArrow.png").toString(), screenWidth/25, screenHeight/20, false, false);
        Image backHelperHoverImg = new Image(MenuScene.class.getResource("/img/buttons/backArrowHover.png").toString(), screenWidth/25, screenHeight/20, false, false);
        Image backHelperInactiveImg = new Image(MenuScene.class.getResource("/img/buttons/backArrowInactive.png").toString(), screenWidth/25, screenHeight/20, false, false);
        ImageView backHelperView = new ImageView(backHelperInactiveImg);
        Image nextHelperImg = new Image(MenuScene.class.getResource("/img/buttons/nextArrow.png").toString(), screenWidth/25, screenHeight/20, false, false);
        Image nextHelperHoverImg = new Image(MenuScene.class.getResource("/img/buttons/nextArrowHover.png").toString(), screenWidth/25, screenHeight/20, false, false);
        Image nextHelperInactiveImg = new Image(MenuScene.class.getResource("/img/buttons/nextArrowInactive.png").toString(), screenWidth/25, screenHeight/20, false, false);
        ImageView nextHelperView = new ImageView(nextHelperImg);

        Image helperBoxImage = new Image(MenuScene.class.getResource("/img/guiTutorial/guiTutorial1.png").toString(), screenWidth/1.5, screenHeight/1.5, false, false);
        ImageView helperBoxContainerView = new ImageView(helperBoxImage);

        Image exitHelperImage = new Image(MenuScene.class.getResource("/img/buttons/close.png").toString(), screenWidth/15, screenHeight/10, false, false);
        Image exitHelperHoverImage = new Image(MenuScene.class.getResource("/img/buttons/closeHover.png").toString(), screenWidth/15, screenHeight/10, false, false);
        ImageView exitHelperButton = new ImageView(exitHelperImage);
        exitHelperButton.setOnMouseEntered(e -> {
            exitHelperButton.setCursor(Cursor.HAND);
            exitHelperButton.setImage(exitHelperHoverImage);
        });
        exitHelperButton.setOnMouseExited(e -> {
            exitHelperButton.setImage(exitHelperImage);
            exitHelperButton.setCursor(Cursor.DEFAULT);
        });
        exitHelperButton.setOnMouseClicked(e -> {
            hidePane(helper);
        });

        helperPage = 1;
        backHelperView.setOnMouseClicked(e -> {
            if(helperPage==2){
                //deactivate back button
                backHelperView.setImage(backHelperInactiveImg);
                backHelperView.setOnMouseEntered(f -> {
                    backHelperView.setCursor(Cursor.DEFAULT);
                });
                backHelperView.setOnMouseExited(Event::consume);
            } else if(helperPage==3){
                //activate next button
                nextHelperView.setImage(nextHelperImg);
                nextHelperView.setOnMouseEntered(f -> {
                    nextHelperView.setImage(nextHelperHoverImg);
                    nextHelperView.setCursor(Cursor.HAND);
                });
                nextHelperView.setOnMouseExited(f -> {
                    nextHelperView.setImage(nextHelperImg);
                    nextHelperView.setCursor(Cursor.DEFAULT);
                });
            }
            if(helperPage!=1) {
                helperPage--;
                helperBoxContainerView.setImage(new Image(MenuScene.class.getResource("/img/guiTutorial/guiTutorial" + helperPage + ".png").toString(), screenWidth / 1.5, screenHeight / 1.5, false, false));
            }
        });
        nextHelperView.setOnMouseEntered(e -> {
            nextHelperView.setImage(nextHelperHoverImg);
            nextHelperView.setCursor(Cursor.HAND);
        });
        nextHelperView.setOnMouseExited(e -> {
            nextHelperView.setImage(nextHelperImg);
            nextHelperView.setCursor(Cursor.DEFAULT);
        });
        nextHelperView.setOnMouseClicked(e -> {
            if(helperPage==1){
                //activate back button
                backHelperView.setImage(backHelperImg);
                backHelperView.setOnMouseEntered(f -> {
                    backHelperView.setImage(backHelperHoverImg);
                    backHelperView.setCursor(Cursor.HAND);
                });
                backHelperView.setOnMouseExited(f -> {
                    backHelperView.setImage(backHelperImg);
                    backHelperView.setCursor(Cursor.DEFAULT);
                });
            } else if(helperPage==2){
                //deactivate next button
                nextHelperView.setImage(nextHelperInactiveImg);
                nextHelperView.setOnMouseEntered(f -> {
                    nextHelperView.setCursor(Cursor.DEFAULT);
                });
                nextHelperView.setOnMouseExited(Event::consume);
            }
            if(helperPage!=3){
                helperPage++;
                helperBoxContainerView.setImage(new Image(MenuScene.class.getResource("/img/guiTutorial/guiTutorial" + helperPage + ".png").toString(), screenWidth/1.5, screenHeight/1.5, false, false));
            }
        });


        VBox backColumn = new VBox();
        backColumn.setAlignment(Pos.CENTER_LEFT);
        backColumn.getChildren().add(backHelperView);
        backColumn.setPrefWidth(screenWidth/3);


        VBox nextColumn = new VBox();
        nextColumn.setAlignment(Pos.CENTER_RIGHT);
        nextColumn.getChildren().add(nextHelperView);
        nextColumn.setPrefWidth(screenWidth/3);


        HBox helperRaw = new HBox();
        helperRaw.getChildren().addAll(backColumn, nextColumn);
        helperRaw.setAlignment(Pos.CENTER);
        helperRaw.setPrefWidth(screenWidth/1.5);
        helperRaw.setPrefHeight(screenHeight/1.5);

        helper.getChildren().addAll(helperBoxContainerView, helperRaw, exitHelperButton);
        helper.setPrefWidth(screenWidth/1.5);
        helper.setPrefHeight(screenHeight/1.5);
        setAlignment(exitHelperButton, TOP_LEFT);

        //---------------------------------------END HELPER------------------------------

        matchView.getChildren().addAll(leftView, centerView, rightView);
        playerView.getChildren().addAll(matchView);

    }

    /**
     * This method sets all player attributes (nickname, color, opponents) in the player view
     * @param nickname Player's nickname
     * @param color Player's color
     * @param opponents Player's opponents
     */
    public void setPage(String nickname, String color, List<PlayerProxy> opponents){
        Color fontColor = fontColor(color);

        //set god card frame with player's color
        Image cardImg = new Image(PlayerView.class.getResource("/img/godFrame"+color+".png").toString(),screenWidth/4, screenHeight/2.3,false,false);
        cardView.setImage(cardImg);

        //set card name font color
        cardName.setTextFill(fontColor);

        //set player name frame with player's color
        Image nameImg = new Image(PlayerView.class.getResource("/img/playerName"+color+".png").toString(),screenWidth/3, screenHeight/8,false,false);
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

            Image opponentImg = new Image(PlayerView.class.getResource("/img/opponentView"+opponentColor+".png").toString(),screenWidth/5, screenHeight/5,false,false);
            ImageView opponentView = new ImageView(opponentImg);

            Label opponentName = new Label(opponent.name);
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
        powerName.setFont(standardFont);
        powerInfo.setFont(lillybelle);
        powerInfo.setWrappingWidth(screenWidth/2);
        powerInfo.setTextAlignment(TextAlignment.CENTER);
        activePowersBox.getChildren().clear();

        activePowersBox.getChildren().addAll(powerName, powerInfo);

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

            powerName.setFont(standardFont);
            powerInfo.setFont(lillybelle);
            powerInfo.setWrappingWidth(screenWidth/2);
            powerInfo.setTextAlignment(TextAlignment.CENTER);
            activePowersBox.getChildren().addAll(powerName, powerInfo);

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
        FadeTransition paneFadeIn = new FadeTransition(Duration.millis(1000), pane);
        paneFadeIn.setFromValue(0);
        paneFadeIn.setToValue(1);

        ScaleTransition paneZoomIn1 = new ScaleTransition(Duration.millis(1000), pane);
        paneZoomIn1.setFromX(0);
        paneZoomIn1.setToX(1);
        paneZoomIn1.setFromY(0.01);
        paneZoomIn1.setToY(0.01);

        ScaleTransition paneZoomIn2 = new ScaleTransition(Duration.millis(1000), pane);
        paneZoomIn2.setFromY(0.01);
        paneZoomIn2.setToY(1);

        paneFadeIn.play();
        pane.setVisible(true);
        paneZoomIn1.play();

        Timeline showingTimer = new Timeline(new KeyFrame(
                Duration.millis(1000),
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
        FadeTransition paneFadeOut = new FadeTransition(Duration.millis(1000), pane);
        paneFadeOut.setFromValue(1);
        paneFadeOut.setToValue(0);

        ScaleTransition paneZoomOut1 = new ScaleTransition(Duration.millis(1000), pane);
        paneZoomOut1.setFromY(1);
        paneZoomOut1.setToY(0.01);
        paneZoomOut1.setFromX(1);
        paneZoomOut1.setToX(1);

        ScaleTransition paneZoomIn2 = new ScaleTransition(Duration.millis(1000), pane);
        paneZoomIn2.setFromX(1);
        paneZoomIn2.setToX(0);

        paneZoomOut1.play();

        Timeline hidingTimer1 = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> {
                    paneZoomIn2.play();
                    paneFadeOut.play();
                }));
        hidingTimer1.play();
        Timeline hidingTimer2 = new Timeline(new KeyFrame(
                Duration.millis(2000),
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
            confirmView.setImage(confirmImgHover);
            confirmView.setCursor(Cursor.HAND);
            e.consume();
        });
        confirmView.setOnMouseExited(e -> {
            confirmView.setImage(confirmImg);
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
            undoView.setImage(undoImgHover);
            undoView.setCursor(Cursor.HAND);
            e.consume();
        });
        undoView.setOnMouseExited(e -> {
            undoView.setImage(undoImg);
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
            usePowerView.setImage(usePowerImgHover);
            usePowerView.setCursor(Cursor.HAND);
            e.consume();
        });
        usePowerView.setOnMouseExited(e -> {
            usePowerView.setImage(usePowerImg);
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
            confirmView.setImage(confirmImgHover);
            confirmView.setCursor(Cursor.HAND);
            e.consume();
        });
        confirmView.setOnMouseExited(e -> {
            confirmView.setImage(confirmImg);
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
        usePowerView.setOnMouseEntered(e -> {
            usePowerView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
        usePowerView.setOnMouseExited(e -> {
            usePowerView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
        usePowerView.setOnMousePressed(e -> {
            usePowerView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
        usePowerView.setOnMouseReleased(e -> {
            usePowerView.setCursor(Cursor.DEFAULT);
            e.consume();
        });

        confirmView.setImage(confirmInactiveImg);
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

    private synchronized void setUsePowerAnswer(int answer){
        usePowerAnswer.set(answer);
        deactivateUsePower();
        notifyAll();
    }

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

    public void playTimer(){
        min = "02";
        sec = "00";
        AtomicInteger seconds = new AtomicInteger(0);
        AtomicInteger minutes = new AtomicInteger(2);
        final Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis( 1000 ),
                        event -> {
                            seconds.getAndDecrement();
                            if(seconds.get()<0) {
                                seconds.set(59);
                                minutes.getAndDecrement();
                                if(minutes.get()<0){
                                    //todo tempo scaduto
                                }
                            }
                            if(seconds.get()<10){
                                sec = "0"+seconds.get();
                            } else {
                                sec = "" + seconds.get();
                            }
                            min = "0" + minutes.get();
                        }
                )
        );
        timeline.setCycleCount( Animation.INDEFINITE );
        timeline.play();
    }
}
