package it.polimi.ingsw.client.view.gui;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicBoolean;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

public class MenuScene {

    private final Font lillybelleFont;
    private final Font pageNumberFont;
    private final Font errorFont;

    //variable that contains the Gui reference
    private final Gui gui;

    //menu page divided in 3 columns -- it's created in Gui and passed in the constructor
    private final HBox menuPage;

    //width of the menu scene
    private final double screenWidth;

    //height of the menu scene
    private final double screenHeight;

    //loading page
    private final StackPane loadingPage;

    //How to play box
    private final StackPane howToPlayBox;

    //text that says what server asks (ip, port, nickname)
    private final Text formText = new Text();

    //text that says what server asks (number of players)
    private final Text matchTypeText = new Text("Number of players");

    //error text
    private final Text errorMessage = new Text();

    //answer of the user
    private final TextField formField = new TextField();

    //next button of the form
    private final Button confirmButton = new Button("Next");

    //group that contains match type options
    private ToggleGroup numberOfPlayersGroup;

    //column that contains the match type request form
    private final VBox matchTypeNumber = new VBox();

    //column that contains the form
    private VBox formView;

    //box that contains play button
    private VBox playGroup;

    //play button
    private ImageView playView;

    //user nickname answer
    private String nickname;

    //user number of players answer
    private int numberOfPlayers;

    //loading icon (during connection to server)
    private ImageView loadingIcon;

    //page to show of the how to play
    private int howToPlayPage;

    //variable that is true if user gives an answer
    private AtomicBoolean clicked = new AtomicBoolean(false);

    //variable that is true if user quits
    private AtomicBoolean close = new AtomicBoolean(false);



    public MenuScene(Gui gui, HBox menuPage, double screenWidth, double screenHeight, StackPane loadingPage, StackPane howToPlayBox) {
        lillybelleFont = Font.loadFont(MenuScene.class.getResourceAsStream("/fonts/LillyBelle.ttf"), screenWidth/80);
        pageNumberFont = Font.loadFont(MenuScene.class.getResourceAsStream("/fonts/LillyBelle.ttf"), screenWidth/50);
        errorFont = Font.loadFont(MenuScene.class.getResourceAsStream("/fonts/LillyBelle.ttf"), screenWidth/120);
        this.gui = gui;
        this.menuPage = menuPage;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.loadingPage = loadingPage;
        this.howToPlayBox = howToPlayBox;
        menuPage.setPrefWidth(screenWidth);
        menuPage.setPrefHeight(screenHeight);
    }


    //_________________________________________________SETTER____________________________________________________________

    private void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public void setErrorMessage(String message){
        errorMessage.setText(message);
        errorMessage.setVisible(true);
        errorMessage.setManaged(true);
    }

    //_______________________________________________END SETTER__________________________________________________________


    //_________________________________________________GETTER____________________________________________________________

    public String nickname() {
        return nickname;
    }

    public int numberOfPlayers() {
        return numberOfPlayers;
    }

    //_______________________________________________END GETTER__________________________________________________________

    /**
     * This method creates the menu page
     */
    public void setMenuScene() {
        //Menu Box Background
        StackPane menuBox = menuGroup();

        //form
        Group formGroup = new Group();
        formGroup.prefHeight(screenHeight / 2);
        formText.setFont(lillybelleFont);
        formField.setCursor(Cursor.TEXT);
        formField.setPrefWidth(screenWidth / 8);
        formField.setPrefHeight(screenHeight / 30);
        formField.setFont(lillybelleFont);
        confirmButton.setPrefWidth(screenWidth / 8);
        confirmButton.setPrefHeight(screenHeight / 20);
        confirmButton.setCursor(Cursor.HAND);
        confirmButton.setFont(lillybelleFont);
        errorMessage.setFont(errorFont);
        errorMessage.setFill(Color.DARKRED);
        errorMessage.setTextAlignment(TextAlignment.CENTER);
        Image loadingImg = new Image(MenuScene.class.getResource("/img/loadingAndPopups/loadingIcon.gif").toString(), screenWidth / 20, screenHeight / 16, false, false);
        loadingIcon = new ImageView(loadingImg);

        formView = new VBox();
        formView.setAlignment(Pos.CENTER);
        formView.setSpacing(20);
        formView.setPadding(new Insets(0, 0, screenHeight / 9, 0));
        formView.getChildren().addAll(formText, formField, confirmButton, loadingIcon, errorMessage);
        formGroup.getChildren().add(formView);
        errorMessage.setVisible(false);
        errorMessage.setManaged(false);
        loadingIcon.setVisible(false);
        loadingIcon.setManaged(false);

        matchTypeText.setFont(lillybelleFont);
        RadioButton twoPlayers = new RadioButton("2");
        RadioButton threePlayers = new RadioButton("3");
        twoPlayers.setCursor(Cursor.HAND);
        threePlayers.setCursor(Cursor.HAND);
        twoPlayers.setFont(lillybelleFont);
        threePlayers.setFont(lillybelleFont);

        numberOfPlayersGroup = new ToggleGroup();
        twoPlayers.setToggleGroup(numberOfPlayersGroup);
        twoPlayers.setSelected(true);
        threePlayers.setToggleGroup(numberOfPlayersGroup);
        threePlayers.setSelected(false);
        HBox numberOfPlayersOptions = new HBox();
        numberOfPlayersOptions.getChildren().addAll(twoPlayers, threePlayers);
        numberOfPlayersOptions.setSpacing(60);
        numberOfPlayersOptions.setAlignment(Pos.CENTER);
        matchTypeNumber.setAlignment(Pos.CENTER);
        matchTypeNumber.setSpacing(20);
        matchTypeNumber.setPadding(new Insets(0, 0, screenHeight / 8, 0));
        matchTypeNumber.getChildren().addAll(matchTypeText, numberOfPlayersOptions);
        matchTypeNumber.setVisible(false);
        errorMessage.setWrappingWidth(screenWidth/8);

        VBox leftButtons = leftButtons();
        playGroup = playColumn();
        menuBox.getChildren().addAll(formGroup, matchTypeNumber);




        menuPage.getChildren().addAll(leftButtons, menuBox, playGroup);
        menuPage.setBackground(background());
        menuPage.setAlignment(Pos.CENTER);
        playGroup.setVisible(false);
        FadeTransition menuFade = new FadeTransition(Duration.millis(1000), menuPage);
        menuFade.setFromValue(0.0);
        menuFade.setToValue(1.0);
        menuFade.play();

        Timeline pageTimer = new Timeline(new KeyFrame(
                Duration.millis(100),
                ae -> {
                    menuPage.setVisible(true);
                }));
        pageTimer.play();
    }


    /**
     * This method creates and returns menu page background
     * @return Background (Menu page background)
     */
    private Background background() {
        BackgroundImage backgroundImage = new BackgroundImage(new Image(MenuScene.class.getResource("/img/menuPage/background.png").toString(), screenWidth, screenHeight, false, false), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        return new Background(backgroundImage);
    }


    /**
     * This method creates the form container
     * @return StackPane (Form container)
     */
    private StackPane menuGroup() {
        Image menuBoxImg = new Image(MenuScene.class.getResource("/img/menuPage/menu_box_background.png").toString(), screenWidth / 2, screenHeight / 1.1, false, false);
        ImageView menuBoxView = new ImageView(menuBoxImg);
        menuBoxView.setEffect(new DropShadow(10, BLACK));
        StackPane menuBox = new StackPane();
        menuBox.setAlignment(Pos.CENTER);
        menuBox.getChildren().add(menuBoxView);
        return menuBox;
    }

    //-----------------------------------------------Play Button------------------------------------------------------------

    /**
     * This method creates play button
     * @return ImageView (Play button)
     */
    private ImageView playButton(){
        Image playImg = new Image(MenuScene.class.getResource("/img/buttons/play.png").toString(), screenWidth/8, screenHeight/4, false, false);
        playView = new ImageView(playImg);
        playView.setEffect(new DropShadow(10, BLACK));
        playView.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
            playView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 5, 5, 0, 0));
            playView.setCursor(Cursor.HAND);
            event.consume();
        });
        playView.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, event -> {
            playView.setEffect(new DropShadow(10, BLACK));
            event.consume();
        });
        return playView;
    }

    /**
     * This method creates play button container
     * @return Group (Play button container)
     */
    private VBox playColumn(){
        ImageView playView = playButton();

        VBox playButton = new VBox();
        playButton.getChildren().addAll(playView);
        playButton.prefHeight(screenHeight/2);
        playButton.setAlignment(Pos.CENTER);
        return playButton;
    }

    //-----------------------------------------------END Play Button------------------------------------------------------------

    //-----------------------------------------------Quit and How to play Buttons------------------------------------------------------------
    /**
     * This method creates quit button
     * @return ImageView (Quit button)
     */
    private ImageView quitButton(){
        Image quitImg = new Image(MenuScene.class.getResource("/img/buttons/quit.png").toString(), screenWidth/8, screenHeight/4, false, false);
        ImageView quitView = new ImageView(quitImg);

        quitView.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
            quitView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 5, 5, 0, 0));
            quitView.setCursor(Cursor.HAND);
            event.consume();
        });

        quitView.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, event -> {
            quitView.setEffect(new DropShadow(10, BLACK));
            event.consume();
        });

        quitView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            setClose();
            gui.closeProgram();
            event.consume();
        });

        return quitView;
    }

    /**
     * This method creates how to play button
     * @return ImageView (How to play button)
     */
    private ImageView howToPlayButton(){
        Image howToPlayImg = new Image(MenuScene.class.getResource("/img/buttons/howToPlay.png").toString(), screenWidth/8, screenHeight/4, false, false);
        ImageView howToPlayView = new ImageView(howToPlayImg);

        howToPlayView.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
            howToPlayView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 5, 5, 0, 0));
            howToPlayView.setCursor(Cursor.HAND);
            event.consume();
        });

        howToPlayView.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, event -> {
            howToPlayView.setEffect(new DropShadow(10, BLACK));
            event.consume();
        });

        howToPlayView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            showHowToPlay();
            event.consume();
        });
        return howToPlayView;
    }


    /**
     * This method creates quit button and how to play button container
     * @return Group (Quit button and how to play container)
     */
    private VBox leftButtons(){
        ImageView quitView = quitButton();
        ImageView howToPlayView = howToPlayButton();
        quitView.setEffect(new DropShadow(10, BLACK));
        howToPlayView.setEffect(new DropShadow(10, BLACK));
        VBox buttons = new VBox();
        buttons.getChildren().addAll(howToPlayView, quitView);
        buttons.setSpacing(20);
        buttons.setPrefHeight(screenHeight/2);
        buttons.setAlignment(Pos.CENTER);
        return buttons;
    }

    /**
     * This method creates how to play box and all the rules
     */
    private void createHowToPlay(){
        Image howToPlayBackground = new Image(MenuScene.class.getResource("/img/howToPlay/howToPlayBackground.png").toString(), screenWidth/1.2, screenHeight/1.2, false, false);
        ImageView howToPlayBackgroundView = new ImageView(howToPlayBackground);

        Image backImg = new Image(MenuScene.class.getResource("/img/buttons/backArrow.png").toString(), screenWidth/25, screenHeight/20, false, false);
        Image backInactiveImg = new Image(MenuScene.class.getResource("/img/buttons/backArrowInactive.png").toString(), screenWidth/25, screenHeight/20, false, false);
        ImageView backView = new ImageView(backInactiveImg);
        Image nextImg = new Image(MenuScene.class.getResource("/img/buttons/nextArrow.png").toString(), screenWidth/25, screenHeight/20, false, false);
        Image nextInactiveImg = new Image(MenuScene.class.getResource("/img/buttons/nextArrowInactive.png").toString(), screenWidth/25, screenHeight/20, false, false);
        ImageView nextView = new ImageView(nextImg);

        Image howToPlayImage = new Image(MenuScene.class.getResource("/img/howToPlay/howToPlay1.png").toString(), screenWidth/1.2, screenHeight/1.2, false, false);
        ImageView howToPlayView = new ImageView(howToPlayImage);

        Image exitImage = new Image(MenuScene.class.getResource("/img/buttons/close.png").toString(), screenWidth/15, screenHeight/10, false, false);
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
            hideHowToPlay();
        });

        howToPlayPage = 1;
        Text pageNumber = new Text(String.valueOf(howToPlayPage));
        pageNumber.setFont(pageNumberFont);


        backView.setOnMouseClicked(e -> {
            if(howToPlayPage==2){
                //deactivate back button
                backView.setImage(backInactiveImg);
                backView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
                backView.setOnMouseEntered(f -> {
                    backView.setCursor(Cursor.DEFAULT);
                });
                backView.setOnMouseExited(Event::consume);
            } else if(howToPlayPage==4){
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
            if(howToPlayPage!=1) {
                howToPlayPage--;
                howToPlayView.setImage(new Image(MenuScene.class.getResource("/img/howToPlay/howToPlay" + howToPlayPage + ".png").toString(), screenWidth / 1.2, screenHeight / 1.2, false, false));
                pageNumber.setText(String.valueOf(howToPlayPage));
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
            if(howToPlayPage==1){
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
            } else if(howToPlayPage==3){
                //deactivate next button
                nextView.setImage(nextInactiveImg);
                nextView.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
                nextView.setOnMouseEntered(f -> {
                    nextView.setCursor(Cursor.DEFAULT);
                });
                nextView.setOnMouseExited(Event::consume);
            }
            if(howToPlayPage!=4){
                howToPlayPage++;
                howToPlayView.setImage(new Image(MenuScene.class.getResource("/img/howToPlay/howToPlay" + howToPlayPage + ".png").toString(), screenWidth/1.2, screenHeight/1.2, false, false));
                pageNumber.setText(String.valueOf(howToPlayPage));
            }
        });


        VBox back = new VBox();
        back.setAlignment(Pos.CENTER_LEFT);
        back.getChildren().add(backView);
        back.setPrefWidth(screenWidth/4.5);

        VBox page = new VBox();
        page.getChildren().add(pageNumber);
        page.setAlignment(Pos.BOTTOM_CENTER);
        page.setPrefWidth(screenWidth/3.6);
        page.setPadding(new Insets(0,0,screenHeight/10,0));




        VBox next = new VBox();
        next.setAlignment(Pos.CENTER_RIGHT);
        next.getChildren().add(nextView);
        next.setPrefWidth(screenWidth/4.5);
        HBox howToPlayRaw = new HBox();
        howToPlayRaw.getChildren().addAll(back, page, next);
        howToPlayRaw.setAlignment(Pos.CENTER);
        howToPlayBox.getChildren().addAll(howToPlayBackgroundView, howToPlayView, howToPlayRaw, exitButton);
        StackPane.setAlignment(exitButton, Pos.TOP_LEFT);
    }

    /**
     * This method shows How to play box
     */
    public void showHowToPlay(){
        createHowToPlay();
        FadeTransition howToPlayFadeIn = new FadeTransition(Duration.millis(700), howToPlayBox);
        howToPlayFadeIn.setFromValue(0);
        howToPlayFadeIn.setToValue(1);

        ScaleTransition howToPlayZoomIn1 = new ScaleTransition(Duration.millis(700), howToPlayBox);
        howToPlayZoomIn1.setFromX(0);
        howToPlayZoomIn1.setToX(1);
        howToPlayZoomIn1.setFromY(0.01);
        howToPlayZoomIn1.setToY(0.01);

        ScaleTransition howToPlayZoomIn2 = new ScaleTransition(Duration.millis(700), howToPlayBox);
        howToPlayZoomIn2.setFromY(0.01);
        howToPlayZoomIn2.setToY(1);

        howToPlayFadeIn.play();
        howToPlayBox.setVisible(true);
        howToPlayZoomIn1.play();

        Timeline showingTimer = new Timeline(new KeyFrame(
                Duration.millis(700),
                ae -> {
                    howToPlayZoomIn2.play();
                }));
        showingTimer.play();

    }

    /**
     * This method hides How to play box
     */
    private void hideHowToPlay(){
        FadeTransition howToPlayFadeOut = new FadeTransition(Duration.millis(700), howToPlayBox);
        howToPlayFadeOut.setFromValue(1);
        howToPlayFadeOut.setToValue(0);

        ScaleTransition howToPlayZoomOut1 = new ScaleTransition(Duration.millis(700), howToPlayBox);
        howToPlayZoomOut1.setFromY(1);
        howToPlayZoomOut1.setToY(0.01);
        howToPlayZoomOut1.setFromX(1);
        howToPlayZoomOut1.setToX(1);

        ScaleTransition howToPlayZoomIn2 = new ScaleTransition(Duration.millis(700), howToPlayBox);
        howToPlayZoomIn2.setFromX(1);
        howToPlayZoomIn2.setToX(0);

        howToPlayZoomOut1.play();

        Timeline hidingTimer1 = new Timeline(new KeyFrame(
                Duration.millis(700),
                ae -> {
                    howToPlayZoomIn2.play();
                    howToPlayFadeOut.play();
                }));
        hidingTimer1.play();
        Timeline hidingTimer2 = new Timeline(new KeyFrame(
                Duration.millis(1400),
                ae -> {
                    howToPlayBox.setVisible(false);
                }));
        hidingTimer2.play();
    }


    //-----------------------------------------------END Quit and How to play Buttons------------------------------------------------------------

    //-----------------------------------------------Form and Communication------------------------------------------------------------


    /**
     * This method is called by the client and shows the nickname request form. It waits for the user to insert a nickname and returns it to the client
     * @return String (User nickname)
     */
    public synchronized String askNickname() {
        if(nickname!=null){
            setErrorMessage("Nickname already used");
        }
        loadingIcon.setVisible(false);
        loadingIcon.setManaged(false);
        confirmButton.setVisible(true);
        confirmButton.setManaged(true);
        clicked.set(false);
        formText.setText("Nickname");
        formField.clear();
        confirmButton.setOnMouseClicked(e -> {
            if(formField.getText().isEmpty()){
                formField.clear();
                setErrorMessage("Write a nickname");
            } else if(formField.getText().contains(" ")){
                formField.clear();
                setErrorMessage("Space not allowed");
            } else {
                setNickname(formField.getText());
                setClicked();
                errorMessage.setVisible(false);
                errorMessage.setManaged(false);
            }
        });
        while (!clicked.get() && !close.get()){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return nickname();
    }


    /**
     * This method is called by the client and shows the match type request form. It waits for the user to insert the number of players for the match and returns the match type
     * @return int (match type -> 1 if the user chooses 2 players, 2 if the user chooses 3 players)
     */
    public synchronized int askNumberOfPlayers() {
        clicked.set(false);
        formView.setVisible(false);
        matchTypeNumber.setVisible(true);
        playGroup.setVisible(true);
        playView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            setLoadingPage();
            RadioButton selectedRadioButton = (RadioButton) numberOfPlayersGroup.getSelectedToggle();
            String number = selectedRadioButton.getText();
            setNumberOfPlayers(Integer.parseInt(number));
            Timeline readyTimer = new Timeline(new KeyFrame(
                    Duration.millis(6000),
                    ae -> {
                        setClicked();
                    }));
            readyTimer.play();
            event.consume();
        });
        while (!clicked.get() && !close.get()){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return numberOfPlayers();
    }

    private synchronized void setClicked(){
        clicked.set(true);
        notifyAll();
    }

    public synchronized void setClose(){
        close.set(true);
        notifyAll();
    }

    //-----------------------------------------------End Form and Communication------------------------------------------------------------



    //-----------------------------------------------Loading Page------------------------------------------------------------

    /**
     * This method creates and shows the loading page
     */
    private void setLoadingPage(){
        Image loadingGif = new Image(MenuScene.class.getResource("/img/loadingAndPopups/loading.gif").toString(), screenWidth/4, screenHeight/4, false, false);
        ImageView loadingImageGif = new ImageView(loadingGif);
        Image loadingImg = new Image(MenuScene.class.getResource("/img/loadingAndPopups/loading.png").toString(), screenWidth, screenHeight, false, false);
        ImageView loadingImageView = new ImageView(loadingImg);
        loadingPage.setBackground(new Background(new BackgroundFill(WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        Image quitLoadingImg = new Image(MenuScene.class.getResource("/img/buttons/quit.png").toString(), screenWidth/12, screenHeight/8, false, false);
         ImageView quitLoading = new ImageView(quitLoadingImg);

        quitLoading.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
            quitLoading.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 5, 5, 0, 0));
            quitLoading.setCursor(Cursor.HAND);
            event.consume();
        });

        quitLoading.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, event -> {
            quitLoading.setEffect(new DropShadow(BlurType.GAUSSIAN,BLACK, 0, 0, 0, 0));
            event.consume();
        });

        quitLoading.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            gui.closeProgram();
            event.consume();
        });
        StackPane quitPane = new StackPane();
        quitPane.setAlignment(Pos.TOP_LEFT);
        quitPane.getChildren().add(quitLoading);
        loadingPage.getChildren().clear();
        loadingPage.getChildren().addAll(loadingImageGif, loadingImageView, quitPane);
        gui.playTransitionClouds();
        FadeTransition menuFadeOut = new FadeTransition(Duration.millis(1500), menuPage);
        menuFadeOut.setFromValue(1);
        menuFadeOut.setToValue(0);
        menuFadeOut.play();

        Timeline loadingTimer = new Timeline(new KeyFrame(
                Duration.millis(1500),
                ae -> {
                    menuPage.setVisible(false);
                    FadeTransition loadingFade = new FadeTransition(Duration.millis(2000), loadingPage);
                    loadingFade.setFromValue(0.0);
                    loadingFade.setToValue(1.0);
                    loadingFade.play();
                    loadingPage.setVisible(true);
                }));
        loadingTimer.play();
    }

    //-----------------------------------------------End Loading Page------------------------------------------------------------
}
