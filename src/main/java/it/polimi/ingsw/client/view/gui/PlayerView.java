package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.network.objects.GodCardProxy;
import it.polimi.ingsw.network.objects.PlayerProxy;
import it.polimi.ingsw.server.model.Colour;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
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
import java.util.concurrent.atomic.AtomicReference;

import static javafx.geometry.Pos.*;
import static javafx.scene.paint.Color.*;

public class PlayerView extends StackPane {
    private static final Font standardFont = Font.loadFont(PlayerView.class.getResourceAsStream("/fonts/LillyBelle.ttf"), 40);
    private static final Font messagesFont = Font.loadFont(PlayerView.class.getResourceAsStream("/fonts/LillyBelle.ttf"), 25);
    private static final Font powersNameFont = Font.loadFont(PlayerView.class.getResourceAsStream("/fonts/LillyBelle.ttf"), 25);
    private static final Font powersFont = Font.loadFont(PlayerView.class.getResourceAsStream("/fonts/LillyBelle.ttf"), 20);
    private final double screenWidth;
    private final double screenHeight;
    private StackPane playerView = new StackPane();
    private final Gui gui;

    private final GuiMap guiMap;

    //messages to show to player
    private Label messagesBox;

    //current turn
    private Label currentTurn;

    //player's god name
    private Label cardName;

    //player's god card frame
    private ImageView cardView;

    //player's god card
    private Label cardInfo;

    //player's god card image
    private ImageView godView;

    //player's name frame
    private ImageView nameView;

    //player's name
    private Label textBox;

    //active powers names
    private Text[] activePowersNames;

    //active powers infos
    private Text[] activePowersInfos;

    //active powers pane
    private StackPane activePowers;

    //pause menu
    private StackPane pausePane;

    //helper pane
    private StackPane helper;

    //helper page number
    private int helperPage;

    //opponents list view
    private VBox opponentsView;

    //opponents powers
    private HashMap<String, Label> opponentsPowers = new HashMap<>();

    private ImageView confirmView;
    private ImageView undoView;
    private ImageView usePowerView;
    private Group powers;
    private Group powersClosed;

    public PlayerView(double screenWidth, double screenHeight, GuiMap guiMap, Gui gui) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.guiMap = guiMap;
        this.gui = gui;
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
        pause.setPrefHeight(screenHeight*3/16);


        //left CENTER
        cardView = new ImageView();
        Image godImage = new Image(PlayerView.class.getResource("/img/godCards/noGod.png").toString(),(screenWidth/7), screenHeight/3.3,false,false);
        godView = new ImageView(godImage);

        cardName = new Label();
        cardName.setPrefSize(screenWidth/4, screenHeight/2);
        cardName.setFont(standardFont);
        Insets namePadding = new Insets(screenWidth/50);
        cardName.setPadding(namePadding);
        cardName.setAlignment(BOTTOM_CENTER);
        cardName.setTextAlignment(TextAlignment.CENTER);


        cardInfo = new Label();
        cardInfo.setPrefSize(screenWidth/4, screenHeight/2);
        Insets godPadding = new Insets(0, 0,screenWidth/400, 0);
        cardInfo.setPadding(godPadding);
        cardInfo.setAlignment(CENTER);
        cardInfo.setGraphic(godView);

        StackPane card = new StackPane();
        card.setAlignment(CENTER);
        card.setPrefWidth(screenWidth/4);
        card.setPrefHeight(screenHeight/2);
        card.getChildren().addAll(cardView, cardName, cardInfo);


        //left BOTTOM
        Image messagesImg = new Image(PlayerView.class.getResource("/img/messagesBox.png").toString(),screenWidth/4, screenHeight*3/16,false,false);
        ImageView messagesView = new ImageView(messagesImg);

        messagesBox = new Label("Good Luck!");
        messagesBox.setFont(messagesFont);
        messagesBox.setTextFill(WHITE);
        messagesBox.setPrefWidth(screenWidth/4);
        messagesBox.setPrefHeight(screenHeight/4);
        messagesBox.setWrapText(true);
        messagesBox.setAlignment(CENTER);
        messagesBox.setTextAlignment(TextAlignment.CENTER);

        StackPane messages = new StackPane();
        messages.setAlignment(CENTER);
        messages.setPrefWidth(screenWidth/4);
        messages.setPrefHeight(screenHeight*3/16);
        messages.getChildren().addAll(messagesView, messagesBox);


        leftView.getChildren().addAll(pause, card, messages);
        leftView.setSpacing(screenHeight/16);

        //--------------------------------------------END LEFT-----------------------------


        //--------------------------------------CENTER--------------------------------------
        VBox centerView = new VBox();
        centerView.setPrefWidth(screenWidth/2);
        centerView.setPrefHeight(screenHeight);

        //center TOP
        Image turnImg = new Image(PlayerView.class.getResource("/img/turn.png").toString(),screenWidth/4, screenHeight/12,false,false);
        ImageView turnView = new ImageView(turnImg);

        currentTurn = new Label("Your turn");
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
        opponentsView.setPrefWidth(screenHeight/2);
        opponentsView.setPadding(new Insets(screenWidth/50, 0, 0, 0));
        opponentsView.setSpacing(screenHeight/50);
        opponentsView.setAlignment(CENTER);

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
        HBox buttons = new HBox();
        Image confirmImg = new Image(PlayerView.class.getResource("/img/buttons/confirmActionInactive.png").toString(),screenWidth/15, screenHeight/10,false,false);
        confirmView = new ImageView(confirmImg);
        Image undoImg = new Image(PlayerView.class.getResource("/img/buttons/undoInactive.png").toString(),screenWidth/15, screenHeight/10,false,false);
        undoView = new ImageView(undoImg);
        Image usePowerImg = new Image(PlayerView.class.getResource("/img/buttons/usePowerInactive.png").toString(),screenWidth/15, screenHeight/10,false,false);
        usePowerView = new ImageView(usePowerImg);

        buttons.getChildren().addAll(usePowerView, undoView, confirmView);
        buttons.setSpacing(screenWidth/50);
        buttons.setPrefWidth(screenWidth/4);
        buttons.setPrefHeight(screenHeight/4);
        buttons.setAlignment(BOTTOM_RIGHT);
        buttons.setPadding(new Insets(screenWidth/40, screenWidth/40, 0,0));

        rightView.getChildren().add(buttons);

        //--------------------------------------END RIGHT--------------------------------------


        //--------------------------------------ACTIVE POWERS----------------------------------

        activePowers = new StackPane();
        Image activePowersImg = new Image(PlayerView.class.getResource("/img/activePowers.png").toString(),screenWidth/1.5, screenHeight/1.5,false,false);
        ImageView activePowersView = new ImageView(activePowersImg);
        VBox activePowersBox = new VBox();
        activePowersBox.setSpacing(screenHeight/50);
        activePowersBox.setAlignment(CENTER);
        activePowersNames = new Text[2];
        activePowersInfos = new Text[2];
        for(int i=0 ; i<2; i++){
            Text powerName = new Text();
            Text powerInfo = new Text();
            if(i==0)
                powerName.setText("No active powers");
            powerName.setFont(powersNameFont);
            powerName.setFill(MIDNIGHTBLUE);
            powerName.setTextAlignment(TextAlignment.CENTER);
            powerInfo.setFont(powersFont);
            powerInfo.setTextAlignment(TextAlignment.CENTER);
            powerInfo.setWrappingWidth(screenWidth/2.5);

            activePowersNames[i] = powerName;
            activePowersInfos[i] = powerInfo;

            activePowersBox.getChildren().addAll(powerName, powerInfo);
        }

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
        pausePane = new StackPane();
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

        helper = new StackPane();

        helper.getChildren().addAll(helperBoxContainerView, helperRaw, exitHelperButton);
        helper.setPrefWidth(screenWidth/1.5);
        helper.setPrefHeight(screenHeight/1.5);
        setAlignment(exitHelperButton, TOP_LEFT);

        //---------------------------------------END HELPER------------------------------

        matchView.getChildren().addAll(leftView, centerView, rightView);
        playerView.getChildren().addAll(matchView, activePowers, pausePane, helper);
        activePowers.setVisible(false);
        pausePane.setVisible(false);
        helper.setVisible(false);

    }

    public void setPage(String nickname, String color, List<PlayerProxy> opponents){
        Color fontColor = fontColor(color);

        //set god card frame with player's color
        Image cardImg = new Image(PlayerView.class.getResource("/img/godFrame"+color+".png").toString(),screenWidth/4, screenHeight/2,false,false);
        cardView.setImage(cardImg);

        //set card name font color
        cardName.setTextFill(fontColor);

        //set player name frame with player's color
        Image nameImg = new Image(PlayerView.class.getResource("/img/playerName"+color+".png").toString(),screenWidth/3, screenHeight/8,false,false);
        nameView.setImage(nameImg);

        //set player's name
        textBox.setText(nickname);
        textBox.setTextFill(fontColor);

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
            opponentName.setFont(messagesFont);
            opponentName.setTextFill(fontColor(opponentColor));
            opponentName.setTextAlignment(TextAlignment.CENTER);
            opponentName.setPadding(new Insets(screenHeight/70, 0, 0, 0));

            Label opponentCardName = new Label();
            opponentCardName.setFont(messagesFont);
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
     * This method assignes card to player and save the power as active power
     * @param player PlayerProxy
     */
    public void setMyCard(PlayerProxy player){
        textBox.setText(player.godCardProxy.name);
        godView.setImage(new Image(PlayerView.class.getResource("/img/godCards/" + player.godCardProxy.name + ".png").toString(),(screenWidth/7), screenHeight/3.3,false,false));
        activePowersNames[0].setText(player.godCardProxy.name);
        activePowersInfos[0].setText(player.godCardProxy.description);
    }

    /**
     * This method assignes card to opponents and save the power as active power
     * @param opponents OpponentsProxy
     */
    public void setOpponentsCards(List<PlayerProxy> opponents){
        for(PlayerProxy opponent : opponents){
            opponentsPowers.get(opponent.name).setText(opponent.godCardProxy.name);
            activePowersNames[opponents.indexOf(opponent)+1].setText(opponent.godCardProxy.name);
            activePowersInfos[opponents.indexOf(opponent)+1].setText(opponent.godCardProxy.description);
        }
    }

    /**
     * This method shows panes
     */
    private void showPane(StackPane pane){
        guiMap.setVisible(false);
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
     * This method hides panes
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




    public void changeMessage(String messageString){
        messagesBox.setText(messageString);
    }

    public void activateButtons(double screenWidth, double screenHeight, int phase, MatchScene match){
        AtomicReference<String> oldMessage = new AtomicReference<>();
        Image confirmImg = new Image(PlayerView.class.getResource("/img/buttons/confirmAction.png").toString(),screenWidth/15, screenHeight/10,false,false);
        Image confirmImgHover = new Image(PlayerView.class.getResource("/img/buttons/confirmActionHover.png").toString(),screenWidth/15, screenHeight/10,false,false);
        Image confirmImgPressed = new Image(PlayerView.class.getResource("/img/buttons/confirmActionPressed.png").toString(),screenWidth/15, screenHeight/10,false,false);
        confirmView.setImage(confirmImg);
        confirmView.setOnMouseEntered(e -> {
            oldMessage.set(getMessage());
            confirmView.setImage(confirmImgHover);
            confirmView.setCursor(Cursor.HAND);
            changeMessage("Click here to confirm your action");
            e.consume();
        });
        confirmView.setOnMouseExited(e -> {
            confirmView.setImage(confirmImg);
            confirmView.setCursor(Cursor.DEFAULT);
            changeMessage(oldMessage.toString());
            e.consume();
        });
        confirmView.setOnMousePressed(e -> {
            confirmView.setImage(confirmImgPressed);
            confirmView.setCursor(Cursor.HAND);
            e.consume();
        });
        confirmView.setOnMouseReleased(e -> {
            confirmView.setImage(confirmImg);
            confirmView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
        confirmView.setOnMouseClicked(e -> {
            if(phase==1){
                match.setChosenWorkerPosition(match.chosenWorkerNewPosition()[0], match.chosenWorkerNewPosition()[1]);
                match.setChosenBoxIndex(match.map().box(match.chosenWorkerPosition()[0], match.chosenWorkerPosition()[1]).index());
            }
            //todo manda al server la risposta
            System.out.println(match.chosenBoxIndex());

            match.clearChosableBoxes();
            deactivateButtons(screenWidth, screenHeight);
        });
        Image undoImg = new Image(PlayerView.class.getResource("/img/buttons/undo.png").toString(),screenWidth/15, screenHeight/10,false,false);
        Image undoImgHover = new Image(PlayerView.class.getResource("/img/buttons/undoHover.png").toString(),screenWidth/15, screenHeight/10,false,false);
        Image undoImgPressed = new Image(PlayerView.class.getResource("/img/buttons/undoPressed.png").toString(),screenWidth/15, screenHeight/10,false,false);
        undoView.setImage(undoImg);
        undoView.setOnMouseEntered(e -> {
            oldMessage.set(getMessage());
            undoView.setImage(undoImgHover);
            undoView.setCursor(Cursor.HAND);
            changeMessage("Click here to cancel your action");
            e.consume();
        });
        undoView.setOnMouseExited(e -> {
            undoView.setImage(undoImg);
            undoView.setCursor(Cursor.DEFAULT);
            changeMessage(oldMessage.toString());
            e.consume();
        });
        undoView.setOnMousePressed(e -> {
            undoView.setImage(undoImgPressed);
            undoView.setCursor(Cursor.HAND);
            e.consume();
        });
        undoView.setOnMouseReleased(e -> {
            undoView.setImage(undoImg);
            undoView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
        undoView.setOnMouseClicked(e -> {
            match.map().box(match.chosenWorkerNewPosition()[0], match.chosenWorkerNewPosition()[1]).removeColor();
            for (int[] i : match.chosableBoxes()) {
                match.map().box(i[0], i[1]).setAsChosable(match, match.chosableBoxes(), phase);
            }
            match.playerView().deactivateButtons(screenWidth, screenHeight);
        });
    }
    public void deactivateButtons(double screenWidth, double screenHeight) {
        Image confirmImg = new Image(PlayerView.class.getResource("/img/buttons/confirmActionInactive.png").toString(), screenWidth / 15, screenHeight / 10, false, false);
        confirmView.setImage(confirmImg);
        confirmView.setOnMouseEntered(e -> {
            confirmView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
        confirmView.setOnMouseExited(e -> {
            confirmView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
        confirmView.setOnMousePressed(e -> {
            confirmView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
        confirmView.setOnMouseReleased(e -> {
            confirmView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
        Image undoImg = new Image(PlayerView.class.getResource("/img/buttons/undoInactive.png").toString(), screenWidth / 15, screenHeight / 10, false, false);
        undoView.setImage(undoImg);
        undoView.setOnMouseEntered(e -> {
            undoView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
        undoView.setOnMouseExited(e -> {
            undoView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
        undoView.setOnMousePressed(e -> {
            undoView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
        undoView.setOnMouseReleased(e -> {
            undoView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
    }

    public void activateUsePower(double screenWidth, double screenHeight){
        AtomicReference<String> oldMessage = new AtomicReference<>();
        Image usePowerImg = new Image(PlayerView.class.getResource("/img/buttons/usePower.png").toString(),screenWidth/15, screenHeight/10,false,false);
        Image usePowerImgHover = new Image(PlayerView.class.getResource("/img/buttons/usePowerHover.png").toString(),screenWidth/15, screenHeight/10,false,false);
        Image usePowerImgPressed = new Image(PlayerView.class.getResource("/img/buttons/usePowerPressed.png").toString(),screenWidth/15, screenHeight/10,false,false);
        usePowerView.setImage(usePowerImg);
        usePowerView.setOnMouseEntered(e -> {
            oldMessage.set(getMessage());
            usePowerView.setImage(usePowerImgHover);
            usePowerView.setCursor(Cursor.HAND);
            changeMessage("Click here to use your power");
            e.consume();
        });
        usePowerView.setOnMouseExited(e -> {
            usePowerView.setImage(usePowerImg);
            usePowerView.setCursor(Cursor.DEFAULT);
            changeMessage(oldMessage.toString());
            e.consume();
        });
        usePowerView.setOnMousePressed(e -> {
            usePowerView.setImage(usePowerImgPressed);
            usePowerView.setCursor(Cursor.HAND);
            e.consume();
        });
        usePowerView.setOnMouseReleased(e -> {
            usePowerView.setImage(usePowerImg);
            usePowerView.setCursor(Cursor.DEFAULT);
            e.consume();
        });
    }
    public void deactivateUsePower(double screenWidth, double screenHeight) {
        Image usePowerImg = new Image(PlayerView.class.getResource("/img/buttons/usePowerInactive.png").toString(), screenWidth / 15, screenHeight / 10, false, false);
        usePowerView.setImage(usePowerImg);
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
    }

    /**
     * This method initialises the font color, based on player color
     * @param color
     * @return
     */
    public Color fontColor(String color){
        if(color=="White"){
            return BLACK;
        }else{
            return WHITE;
        }
    }
}
