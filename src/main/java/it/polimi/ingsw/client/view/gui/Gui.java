package it.polimi.ingsw.client.view.gui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.network.exceptions.TimeOutException;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.Colour;
import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import javafx.animation.FadeTransition;
import it.polimi.ingsw.network.objects.BoxProxy;
import it.polimi.ingsw.network.objects.GodCardProxy;
import it.polimi.ingsw.network.objects.PlayerProxy;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static javafx.scene.paint.Color.*;

public class Gui extends Application implements View {

    //screen width
    private double screenWidth = Screen.getPrimary().getBounds().getWidth();

    //screen height
    private double screenHeight = Screen.getPrimary().getBounds().getHeight();

    //offset of the window relative to screen width
    private double xOffset;

    //offset of the window relative to screen height
    private double yOffset;

    //fonts
    private final Font lillybelleFont = Font.loadFont(Gui.class.getResourceAsStream("/fonts/LillyBelle.ttf"), screenWidth/80);
    private final Font settingsFont = Font.loadFont(Gui.class.getResourceAsStream("/fonts/LillyBelle.ttf"), screenWidth/110);

    //number of rows and columns of the map
    private static final int mapRowsNumber = 5;
    private static final int mapColumnsNumber = 5;

    //client
    private final Client client = new Client(this);

    //scene of the primary window
    private Scene fullScene;

    //setting window
    private Stage settingStage;

    //primary window
    private Stage window = null;

    //menu scene
    private MenuScene menuScene;

    //match scene
    private MatchScene matchScene;

    //primary window pane
    private StackPane mainScene;

    //opening page
    private final StackPane openingPage = new StackPane();

    //menu page
    private HBox menuPage;

    //match page
    private StackPane matchPage;

    //loading page
    private StackPane loadingPage = new StackPane();

    //how to play pane
    private StackPane howToPlayBox = new StackPane();

    //clouds animation pane
    private StackPane transitionClouds;

    //close popup pane
    private StackPane closePopup;

    //connection errors pane
    private StackPane connectionError;

    //yes choice of popup
    private Button yesPopupButton;

    //no choice od popup
    private Button noPopupButton;

    //animations
    private TranslateTransition translateLeftTransitionIn;
    private TranslateTransition translateRightTransitionIn;

    //connection variables
    private String ip = null;
    private int port = -1;

    //player's infos
    private String nickname;
    private String color;

    //match number of players
    private int numberOfPlayers;

    //opponents for the match
    private List<PlayerProxy> opponents = new ArrayList<>();

    //variable that tells if user is doing a new match
    private boolean secondMatch = false;

    //settings window form variables
    private Text ipForm = new Text ("Ip Address");;
    private TextField ipFormField;
    private Text portForm = new Text("Port Number");
    private TextField portFormField;
    private Button connectionButton = new Button("Connect");
    private ImageView loadingIcon;
    private Text errorMessage;
    private Text connectedText;
    private Button disconnectionButton;
    private Button enterGameButton;

    //connection error message
    private Text connectionErrorMessage;

    //variable that is true when menu page is ready
    private  AtomicBoolean gameIsReady = new AtomicBoolean(false);

    //variable that is true when match page is ready
    private  AtomicBoolean readyForTheMatch = new AtomicBoolean(false);

    //variable that is true if user clicked "connect" on setting page
    private  AtomicBoolean clicked = new AtomicBoolean(false);

    //variable that is true if user clicks on play button in the settings window
    private AtomicBoolean openGame = new AtomicBoolean(false);

    //variable that is true if user clicks on disconnect button in the settings window
    private AtomicBoolean restartConnection = new AtomicBoolean(false);

    //variable that is true if user closes settings window
    private  AtomicBoolean closeWindow = new AtomicBoolean(false);

    //variable that is true if user writes valid ip and port
    private AtomicBoolean valid = new AtomicBoolean(false);

    //window size choices
    private ComboBox screenSizeOptions;

    //text that shows screen size errors
    private Text screenSizeError;

    //variable that is true when menu scene is displayed
    private boolean inMenuPage = false;

    //variable that is true when there is a connection error
    private boolean isConnectionError = false;

    //variable that is true when second window is opened
    private boolean secondWindow;


    @Override
    public void start(Stage settingsStage){
        createSettingStage(settingsStage);
    }

    //-----------------------------------------GETTER--------------------------------------------------

    public HBox menuPage(){
        return menuPage;
    }

    public MenuScene menuScene(){
        return menuScene;
    }

    public MatchScene matchScene(){
        return matchScene;
    }

    public int mapRowsNumber() {
        return mapRowsNumber;
    }

    public int mapColumnsNumber() {
        return mapColumnsNumber;
    }

    public int port(){
        return port;
    }

    //-----------------------------------------END GETTER----------------------------------------------

    //-----------------------------------------SETTER--------------------------------------------------

    public void setIp(String ip){
        this.ip = ip;
    }

    public void setPort(int port){
        this.port = port;
    }

    /**
     * This method writes errors in settings page
     * @param message Error to show
     */
    private void setErrorMessage(String message){
        errorMessage.setVisible(true);
        errorMessage.setManaged(true);
        errorMessage.setText(message);
    }

    private void setConnectionErrorMessage(String message){
        connectionErrorMessage.setText(message);
    }

    public void setNewMatch(){
        secondMatch = true;
    }

    public void setIsConnectionError(boolean isConnectionError){
        this.isConnectionError = isConnectionError;
    }

    //-----------------------------------------END SETTER----------------------------------------------

    //-----------------------------------------SYNCHRONIZATION METHODS---------------------------------

    /**
     * This method notifies closeWindow when user closes setting window. It also closes setting window and the client
     */
    public synchronized void setCloseWindow(){
        closeWindow.set(true);
        valid.set(true); //true --> in order to exit to cycles that are waiting for responses
        setPort(0); //set port because if user quits after disconnection, last port is saved and returns creating errors
        settingStage.close();
        try {
            client.end();
        } catch (ChannelClosedException ex) {
            ex.printStackTrace();
            System.err.println("Connection Lost");
        }
        notifyAll();
    }

    /**
     * This method notifies clicked when user clicks on connect in the setting window
     */
    private synchronized void setClicked(){
        clicked.set(true);
        notifyAll();
    }

    /**
     * This method notifies restartConnection when user clicks on disconnect in the setting window. It also restarts the client
     */
    private synchronized void restartConnection(){
        restartConnection.set(true);
        notifyAll();
        restartClient();
    }

    /**
     * This method notifies openGame when user clicks on play in the setting window
     */
    private synchronized void setOpenGame(){
        openGame.set(true);
        notifyAll();
    }

    /**
     * This method notifies gameIsReady when menu scene is ready and displayed. Server is asking for username and this variable waits for menu scene to be ready to ask the username
     */
    private synchronized void setGameIsReady(){
        gameIsReady.set(true);
        notifyAll();
    }

    /**
     * This method notifies readyForTheMatch when match scene is ready and displayed. Server will ask cards to challenger and this variable waits for match scene to be ready to ask cards to challenger
     */
    private synchronized void setReadyForTheMatch(){
        readyForTheMatch.set(true);
        notifyAll();
    }

    private synchronized void setInMenuPage(boolean inMenuPage){
        this.inMenuPage = inMenuPage;
        notifyAll();
    }

    //-----------------------------------------END SYNCHRONIZATION METHODS-----------------------------

    private void createSettingStage(Stage settingsStage){
        //initialization --> I need it if we restart the setting page after a connection lost
        screenWidth = Screen.getPrimary().getBounds().getWidth();
        screenHeight = Screen.getPrimary().getBounds().getHeight();
        gameIsReady = new AtomicBoolean(false);
        readyForTheMatch = new AtomicBoolean(false);
        clicked = new AtomicBoolean(false);
        openGame = new AtomicBoolean(false);
        restartConnection = new AtomicBoolean(false);
        closeWindow = new AtomicBoolean(false);
        valid = new AtomicBoolean(false);
        secondWindow = false;

        this.settingStage = settingsStage;
        StackPane settings = new StackPane();
        setInMenuPage(false);
        createSettingsPane(settings);

        Scene settingsScene = new Scene(settings);

        settingsStage.setScene(settingsScene);
        settingsStage.setWidth(screenWidth/2);
        settingsStage.setHeight(screenHeight/2);

        if(!isConnectionError)
            client.start();

        setIsConnectionError(false);
        settingsStage.setResizable(false);
        settingsStage.initStyle(StageStyle.UNDECORATED);
        settingsStage.setX((screenWidth - settingsStage.getWidth()) / 2);
        settingsStage.setY((screenHeight - settingsStage.getHeight()) / 2);

        settingsStage.getIcons().add(new Image(Gui.class.getResourceAsStream("/img/icon.png")));
        settingsStage.show();
    }

    /**
     * This method creates primary scene of the game (after setting window)
     */
    public void primaryScene(){
        if(window==null) {
            mainScene = new StackPane();
            window = new Stage();
            if (screenWidth == Screen.getPrimary().getBounds().getWidth() && screenHeight == Screen.getPrimary().getBounds().getHeight()) {
                window.setMaximized(true);
            } else {
                window.setWidth(screenWidth);
                window.setHeight(screenHeight);
                window.setX((Screen.getPrimary().getBounds().getWidth() - window.getWidth()) / 2);
                window.setY((Screen.getPrimary().getBounds().getHeight() - window.getHeight()) / 2);

            }
            window.initStyle(StageStyle.UNDECORATED);
            window.setTitle("Santorini");
            window.setOnCloseRequest(e -> {
                e.consume();
                closeProgram();
            });
            window.setResizable(false);
            window.getIcons().add(new Image(Gui.class.getResourceAsStream("/img/icon.png")));
        }

        createTransitionClouds();
        closePopup();
        createConnectionErrorPane();

        menuPage = new HBox();

        matchPage = new StackPane();

        mainScene.getChildren().clear();
        mainScene.getChildren().addAll(openingPage, menuPage, loadingPage, matchPage, transitionClouds, howToPlayBox, closePopup, connectionError);

        if(screenWidth != Screen.getPrimary().getBounds().getWidth() && screenHeight != Screen.getPrimary().getBounds().getHeight()) {
            mainScene.setOnMousePressed(e -> {
                xOffset = window.getX() - e.getScreenX();
                yOffset = window.getY() - e.getScreenY();
            });
            mainScene.setOnMouseDragged(e -> {
                window.setX(e.getScreenX() + xOffset);
                window.setY(e.getScreenY() + yOffset);
            });
        }

        openingPage.setVisible(!secondMatch);
        menuPage.setVisible(secondMatch);

        if(!secondMatch) {
            openingPage();
            transitionClouds.setVisible(false);
        } else {
            menuScene = null;
            menuScene = new MenuScene(this, menuPage, screenWidth, screenHeight, loadingPage, howToPlayBox);
            menuScene.setMenuScene();
            setInMenuPage(true);
            Timeline waitTransitionTimer = new Timeline(new KeyFrame(
                    Duration.millis(1500),
                    ae -> {
                        transitionClouds.setVisible(false);
                    }));
            waitTransitionTimer.play();
            restartClient();

        }
        loadingPage.setVisible(false);
        closePopup.setVisible(false);
        closePopup.setAlignment(Pos.CENTER);
        matchPage.setVisible(false);
        howToPlayBox.setVisible(false);
        connectionError.setVisible(false);

        matchScene = new MatchScene(this, screenWidth, screenHeight, matchPage);

        if(!secondMatch)
            fullScene = new Scene(mainScene);
        window.setScene(fullScene);
        secondWindow = true;
        window.show();
    }



    //------------------------------------------SETTINGS PAGE----------------------------------------------------------

    /**
     * This method creates initial setting page
     * @param settings Setting window
     */
    private void createSettingsPane(StackPane settings){

        //title bar

        HBox titleBar = new HBox();
        titleBar.setMaxHeight(screenHeight/30);
        titleBar.setPrefWidth(screenWidth/2);
        titleBar.setStyle("-fx-background-color: rgba(160, 146, 134, 0.5)");


        Image iconImg = new Image(MenuScene.class.getResource("/img/icon.png").toString(), screenWidth/35, screenHeight/30, false, false);
        ImageView iconView = new ImageView(iconImg);
        Label icon = new Label();
        icon.setGraphic(iconView);
        icon.setTextAlignment(TextAlignment.CENTER);
        icon.setAlignment(Pos.TOP_LEFT);
        icon.setPadding(new Insets(screenHeight/200));

        Label exit = new Label("X");
        exit.setFont(lillybelleFont);
        exit.setTextAlignment(TextAlignment.RIGHT);
        exit.setAlignment(Pos.TOP_RIGHT);

        exit.setOnMouseEntered(e ->{
            exit.setTextFill(GREY);
            exit.setCursor(Cursor.HAND);
        });
        exit.setOnMouseExited(e ->{
            exit.setTextFill(BLACK);
            exit.setCursor(Cursor.DEFAULT);
        });
        exit.setOnMouseClicked(e -> {
            setCloseWindow();
        });

        Label minimize = new Label("-");
        minimize.setFont(Font.font("Arial", screenWidth/50));
        minimize.setTextAlignment(TextAlignment.RIGHT);
        minimize.setAlignment(Pos.TOP_RIGHT);

        minimize.setOnMouseEntered(e ->{
            minimize.setTextFill(GREY);
            minimize.setCursor(Cursor.HAND);
        });
        minimize.setOnMouseExited(e ->{
            minimize.setTextFill(BLACK);
            minimize.setCursor(Cursor.DEFAULT);
        });
        minimize.setOnMouseClicked(e -> {
            settingStage.setIconified(true);
        });

        HBox titleActions = new HBox();
        titleActions.setSpacing(screenWidth/100);
        titleActions.getChildren().addAll(minimize, exit);

        titleBar.getChildren().addAll(icon, titleActions);
        titleBar.setSpacing((screenWidth/2)-screenWidth/15);

        //end title bar

        Image settingsBackground = new Image(MenuScene.class.getResource("/img/settingsPage/background.png").toString(), screenWidth/2, screenHeight/2, false, false);
        ImageView settingsBackgroundView = new ImageView(settingsBackground);

        HBox settingsPage = new HBox();

        VBox connectionSettings = new VBox();
        connectionSettings.setSpacing(screenHeight/50);
        connectionSettings.setPrefWidth(screenWidth/4);
        connectionSettings.setPrefHeight(screenHeight/2);
        connectionSettings.setPadding(new Insets(screenHeight/20, 0, 0, 0));

        ipForm.setFont(lillybelleFont);
        ipForm.setTextAlignment(TextAlignment.CENTER);
        ipFormField = new TextField();
        ipFormField.setFont(settingsFont);
        ipFormField.setMaxWidth(screenWidth/10);

        portForm.setFont(lillybelleFont);
        portForm.setTextAlignment(TextAlignment.CENTER);
        portFormField = new TextField();
        portFormField.setFont(settingsFont);
        portFormField.setMaxWidth(screenWidth/10);

        connectionButton.setFont(lillybelleFont);
        connectionButton.setOnMouseEntered(e -> {
            connectionButton.setCursor(Cursor.HAND);
        });
        connectionButton.setOnMouseExited(e -> {
            connectionButton.setCursor(Cursor.DEFAULT);
        });

        Image loadingImg = new Image(MenuScene.class.getResource("/img/loadingAndPopups/loadingIcon.gif").toString(), screenWidth / 30, screenHeight / 25, false, false);
        loadingIcon = new ImageView(loadingImg);

        errorMessage = new Text();
        errorMessage.setFont(settingsFont);
        errorMessage.setFill(Color.DARKRED);
        errorMessage.setTextAlignment(TextAlignment.CENTER);

        connectedText = new Text("Connected");
        connectedText.setFont(lillybelleFont);
        connectedText.setFill(DARKGREEN);
        disconnectionButton = new Button("Disconnect");
        disconnectionButton.setFont(lillybelleFont);

        connectionSettings.getChildren().addAll(ipForm, ipFormField, portForm, portFormField, connectionButton, loadingIcon, errorMessage, connectedText, disconnectionButton);
        loadingIcon.setVisible(false);
        loadingIcon.setManaged(false);
        errorMessage.setVisible(false);
        errorMessage.setManaged(false);
        connectedText.setVisible(false);
        connectedText.setManaged(false);
        disconnectionButton.setVisible(false);
        disconnectionButton.setManaged(false);

        Text screenSizeText = new Text("Choose window dimension");
        screenSizeText.setFont(lillybelleFont);
        screenSizeText.setTextAlignment(TextAlignment.CENTER);
        screenSizeText.setWrappingWidth(screenWidth/5);

        screenSizeOptions = new ComboBox(FXCollections.observableArrayList(
                "Full Screen", "1920 x 1080", "1680 x 1050", "1600 x 900", "1440 x 900", "1366 x 768", "1360 x 768", "1280 x 800", "1280 x 768", "1280 x 720")
        );
        screenSizeOptions.getEditor().setAlignment(Pos.CENTER);
        screenSizeOptions.getSelectionModel().selectFirst();
        screenSizeOptions.setOnAction(e -> {
            windowSize(screenSizeOptions.getSelectionModel().getSelectedIndex());
        });

        screenSizeError = new Text();
        screenSizeError.setFont(settingsFont);
        screenSizeError.setFill(DARKRED);
        screenSizeError.setTextAlignment(TextAlignment.CENTER);
        screenSizeError.setWrappingWidth(screenWidth/4);
        screenSizeError.setVisible(false);
        screenSizeError.setManaged(true);

        VBox screenSize = new VBox();
        screenSize.getChildren().addAll(screenSizeText, screenSizeOptions, screenSizeError);
        screenSize.setSpacing(screenHeight/50);
        screenSize.setPrefHeight(screenHeight/4);
        screenSize.setAlignment(Pos.CENTER);

        enterGameButton = new Button("Play");
        enterGameButton.setFont(lillybelleFont);
        enterGameButton.setTextFill(WHITE);
        enterGameButton.setBackground(new Background(new BackgroundFill(DARKGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        enterGameButton.setEffect(new DropShadow(10, Color.BLACK));
        enterGameButton.setOnMouseEntered(e -> {
            enterGameButton.setEffect(new DropShadow(30, Color.BLACK));
            enterGameButton.setCursor(Cursor.HAND);
        });
        enterGameButton.setOnMouseExited(e -> {
            enterGameButton.setEffect(new DropShadow(10, Color.BLACK));
            enterGameButton.setCursor(Cursor.DEFAULT);
        });


        VBox windowDimensionSettings = new VBox();
        windowDimensionSettings.setPrefWidth(screenWidth/4);
        windowDimensionSettings.setPrefHeight(screenHeight/2);

        windowDimensionSettings.getChildren().addAll(screenSize, enterGameButton);
        enterGameButton.setVisible(false);
        enterGameButton.setManaged(true);

        settingsPage.getChildren().addAll(connectionSettings, windowDimensionSettings);
        connectionSettings.setAlignment(Pos.CENTER);
        windowDimensionSettings.setAlignment(Pos.CENTER);
        settingsPage.setAlignment(Pos.CENTER);
        settingsPage.setPrefWidth(screenWidth/2);
        settingsPage.setPrefHeight(screenHeight/2);

        settings.getChildren().addAll(settingsBackgroundView, settingsPage, titleBar);
        StackPane.setAlignment(titleBar, Pos.TOP_CENTER);

        //undecorated but movable
        settings.setOnMousePressed(e -> {
            xOffset = settingStage.getX() - e.getScreenX();
            yOffset = settingStage.getY() - e.getScreenY();
        });
        settings.setOnMouseDragged(e -> {
            settingStage.setX(e.getScreenX() + xOffset);
            settingStage.setY(e.getScreenY() + yOffset);
        });

    }

    //------------------------------------------END SETTINGS PAGE------------------------------------------------------

    /**
     * This method sets primary scene size based on size choice in the setting window
     * @param sizeChoice Index of size choice in the list of options
     */
    private void windowSize(int sizeChoice){
        switch (sizeChoice){
            case 0:
                break;
            case 1:
                if(Screen.getPrimary().getBounds().getWidth()<1920 || Screen.getPrimary().getBounds().getHeight()<1080){
                    screenSizeError.setText("Your device doesn't support 1920 x 1080 resolution");
                    screenSizeError.setVisible(true);
                    screenSizeOptions.getSelectionModel().selectFirst();
                } else {
                    screenSizeError.setVisible(false);
                    this.screenWidth = 1920;
                    this.screenHeight = 1080;
                }
                break;
            case 2:
                if(Screen.getPrimary().getBounds().getWidth()<1680 || Screen.getPrimary().getBounds().getHeight()<1050){
                    screenSizeError.setText("Your device doesn't support 1680 x 1050 resolution");
                    screenSizeError.setVisible(true);
                    screenSizeOptions.getSelectionModel().selectFirst();
                } else {
                    screenSizeError.setVisible(false);
                    this.screenWidth = 1680;
                    this.screenHeight = 1050;
                }
                break;
            case 3:
                if(Screen.getPrimary().getBounds().getWidth()<1600 || Screen.getPrimary().getBounds().getHeight()<900){
                    screenSizeError.setText("Your device doesn't support 1600 x 900 resolution");
                    screenSizeError.setVisible(true);
                    screenSizeOptions.getSelectionModel().selectFirst();
                } else {
                    screenSizeError.setVisible(false);
                    this.screenWidth = 1600;
                    this.screenHeight = 900;
                }
                break;
            case 4:
                if(Screen.getPrimary().getBounds().getWidth()<1440 || Screen.getPrimary().getBounds().getHeight()<900){
                    screenSizeError.setText("Your device doesn't support 1440 x 900 resolution");
                    screenSizeError.setVisible(true);
                    screenSizeOptions.getSelectionModel().selectFirst();
                } else {
                    screenSizeError.setVisible(false);
                    this.screenWidth = 1440;
                    this.screenHeight = 900;
                }
                break;
            case 5:
                if(Screen.getPrimary().getBounds().getWidth()<1366 || Screen.getPrimary().getBounds().getHeight()<768){
                    screenSizeError.setText("Your device doesn't support 1366 x 768 resolution");
                    screenSizeError.setVisible(true);
                    screenSizeOptions.getSelectionModel().selectFirst();
                } else {
                    screenSizeError.setVisible(false);
                    this.screenWidth = 1366;
                    this.screenHeight = 768;
                }
                break;
            case 6:
                if(Screen.getPrimary().getBounds().getWidth()<1360 || Screen.getPrimary().getBounds().getHeight()<768){
                    screenSizeError.setText("Your device doesn't support 1360 x 768 resolution");
                    screenSizeError.setVisible(true);
                    screenSizeOptions.getSelectionModel().selectFirst();
                } else {
                    screenSizeError.setVisible(false);
                    this.screenWidth = 1360;
                    this.screenHeight = 768;
                }
                break;
            case 7:
                if(Screen.getPrimary().getBounds().getWidth()<1280 || Screen.getPrimary().getBounds().getHeight()<800){
                    screenSizeError.setText("Your device doesn't support 1280 x 800 resolution");
                    screenSizeError.setVisible(true);
                    screenSizeOptions.getSelectionModel().selectFirst();
                } else {
                    screenSizeError.setVisible(false);
                    this.screenWidth = 1280;
                    this.screenHeight = 800;
                }
                break;
            case 8:
                if(Screen.getPrimary().getBounds().getWidth()<1280 || Screen.getPrimary().getBounds().getHeight()<768){
                    screenSizeError.setText("Your device doesn't support 1280 x 768 resolution");
                    screenSizeError.setVisible(true);
                    screenSizeOptions.getSelectionModel().selectFirst();
                } else {
                    screenSizeError.setVisible(false);
                    this.screenWidth = 1280;
                    this.screenHeight = 768;
                }
                break;
            case 9:
                if(Screen.getPrimary().getBounds().getWidth()<1280 || Screen.getPrimary().getBounds().getHeight()<720){
                    screenSizeError.setText("Your device doesn't support 1280 x 720 resolution");
                    screenSizeError.setVisible(true);
                    screenSizeOptions.getSelectionModel().selectFirst();
                } else {
                    screenSizeError.setVisible(false);
                    this.screenWidth = 1280;
                    this.screenHeight = 720;
                }
                break;
        }
    }

    /**
     * This method closes primary window and the client when user clicks on quit and confirms their action
     */
    public void closeProgram(){
        yesPopupButton.setOnAction(e -> {
            window.close();
            try {
                client.end();
                matchScene.setCloseMatch();
            } catch (ChannelClosedException ex) {
                ex.printStackTrace();
                System.err.println("Connection Lost");
            }

        });
        noPopupButton.setOnAction(e -> {
            menuPage.setEffect(new BoxBlur(0, 0, 0));
            openingPage.setEffect(new BoxBlur(0, 0, 0));
            loadingPage.setEffect(new BoxBlur(0, 0, 0));
            matchPage.setEffect(new BoxBlur(0, 0, 0));
            transitionClouds.setEffect(new BoxBlur(0, 0, 0));
            howToPlayBox.setEffect(new BoxBlur(0, 0, 0));
            closePopup.setVisible(false);
        });
        createClosePopup();
    }

    /**
     * This method creates close popup, that asks if user is sure to quit the game
     */
    private void closePopup(){
        closePopup = new StackPane();
        closePopup.setPrefWidth(screenWidth/4);
        closePopup.setPrefHeight(screenHeight/4);

        Image closeFrame = new Image(Gui.class.getResource("/img/loadingAndPopups/frame.png").toString(), screenWidth/2, screenHeight/2, false, false);
        ImageView closeView = new ImageView(closeFrame);

        VBox confirmBox = new VBox();

        Text questionPopup = new Text("Do you really want to quit the game?");
        questionPopup.setFont(Font.loadFont(Gui.class.getResourceAsStream("/fonts/LillyBelle.ttf"), screenWidth/80));
        HBox answers = new HBox();
        yesPopupButton = new Button("Yes");
        noPopupButton = new Button("No");

        yesPopupButton.setFont(Font.loadFont(Gui.class.getResourceAsStream("/fonts/LillyBelle.ttf"), screenWidth/80));
        yesPopupButton.setCursor(Cursor.HAND);
        noPopupButton.setFont(Font.loadFont(Gui.class.getResourceAsStream("/fonts/LillyBelle.ttf"), screenWidth/80));
        noPopupButton.setCursor(Cursor.HAND);



        answers.getChildren().addAll(yesPopupButton, noPopupButton);
        answers.setSpacing(50);
        answers.setAlignment(Pos.CENTER);




        confirmBox.setSpacing(10);
        confirmBox.getChildren().addAll(questionPopup, answers);

        closePopup.getChildren().addAll(closeView, confirmBox);
        closePopup.setAlignment(Pos.CENTER);
        confirmBox.setAlignment(Pos.CENTER);

    }

    /**
     * This method shows close popup
     */
    private void createClosePopup(){
        menuPage.setEffect(new BoxBlur(5, 10, 10));
        openingPage.setEffect(new BoxBlur(5, 10, 10));
        loadingPage.setEffect(new BoxBlur(5, 10, 10));
        matchPage.setEffect(new BoxBlur(5, 10, 10));
        transitionClouds.setEffect(new BoxBlur(5, 10, 10));
        howToPlayBox.setEffect(new BoxBlur(5, 10, 10));
        closePopup.setVisible(true);
    }


    /**
     * This method creates opening page and after that calls the method that creates menu scene
     */
    private void openingPage(){
        Image openingImg = new Image(Gui.class.getResource("/img/opening/opening.png").toString(), screenWidth, screenHeight, false, false);
        ImageView openingView = new ImageView(openingImg);
        Image logoImg = new Image(Gui.class.getResource("/img/opening/logo.png").toString(), screenWidth/2, screenHeight/2, false, false);
        ImageView logoView = new ImageView(logoImg);

        openingPage.getChildren().addAll(openingView, logoView);
        openingPage.setAlignment(Pos.CENTER);
        openingView.setVisible(true);
        logoView.setVisible(false);


        FadeTransition openingFadeIn = new FadeTransition(Duration.millis(2000), openingPage);
        openingFadeIn.setFromValue(0.0);
        openingFadeIn.setToValue(1.0);
        FadeTransition openingFadeOut = new FadeTransition(Duration.millis(2000), openingPage);
        openingFadeOut.setFromValue(1.0);
        openingFadeOut.setToValue(0.0);


        openingFadeIn.play();

        Timeline firstTimer = new Timeline(new KeyFrame(
                Duration.millis(2000),
                ae -> {
                    openingFadeOut.play();
                }));
        firstTimer.play();

        Timeline secondTimer = new Timeline(new KeyFrame(
                Duration.millis(4000),
                ae -> {
                    openingFadeIn.play();
                    logoView.setVisible(true);
                    openingView.setVisible(false);
                }));
        secondTimer.play();

        Timeline thirdTimer = new Timeline(new KeyFrame(
                Duration.millis(6000),
                ae -> {
                    openingFadeOut.play();
                }));
        thirdTimer.play();

        Timeline menuTimer = new Timeline(new KeyFrame(
                Duration.millis(8000),
                ae -> {
                    openingPage.setVisible(false);
                    menuScene = new MenuScene(this, menuPage, screenWidth, screenHeight, loadingPage, howToPlayBox);
                    menuScene.setMenuScene();
                    setGameIsReady();
                    setInMenuPage(true);
                }));
        menuTimer.play();
    }

    /**
     * This method creates clouds animation
     */
    private void createTransitionClouds(){
        transitionClouds = new StackPane();
        Image leftCloudImg = new Image(Gui.class.getResource("/img/loadingAndPopups/transitionCloudLeft.png").toString(), screenWidth, screenHeight*2, false, false);
        ImageView leftCloud = new ImageView(leftCloudImg);
        Image rightCloudImg = new Image(Gui.class.getResource("/img/loadingAndPopups/transitionCloudRight.png").toString(), screenWidth, screenHeight*2, false, false);
        ImageView rightCloud = new ImageView(rightCloudImg);

        transitionClouds.getChildren().addAll(rightCloud, leftCloud);

        translateLeftTransitionIn = new TranslateTransition(Duration.millis(1500), leftCloud);
        translateLeftTransitionIn.setFromX(-screenWidth);
        translateLeftTransitionIn.setByX(screenWidth-screenWidth/4);
        translateLeftTransitionIn.setAutoReverse(true);

        translateRightTransitionIn = new TranslateTransition(Duration.millis(1500), rightCloud);
        translateRightTransitionIn.setFromX(screenWidth);
        translateRightTransitionIn.setByX(-screenWidth+screenWidth/4);
        translateRightTransitionIn.setAutoReverse(true);
    }

    /**
     * This method plays clouds animation
     */
    public void playTransitionClouds(){
        transitionClouds.setVisible(true);
        translateLeftTransitionIn.setCycleCount(2);
        translateLeftTransitionIn.play();
        translateRightTransitionIn.setCycleCount(2);
        translateRightTransitionIn.play();
        Timeline cloudsTimer = new Timeline(new KeyFrame(
                Duration.millis(3000),
                ae -> {
                    transitionClouds.setVisible(false);
                }));
        cloudsTimer.play();
    }

    private void createConnectionErrorPane(){
        connectionError = new StackPane();

        Image connectionFrame = new Image(MatchScene.class.getResource("/img/loadingAndPopups/frame2.png").toString(), screenWidth/3, screenHeight/3, false, false);
        ImageView connectionErrorView = new ImageView(connectionFrame);

        VBox connectionErrorBox = new VBox();
        connectionErrorBox.setSpacing(screenHeight/30);
        connectionErrorMessage = new Text("Connection lost");
        connectionErrorMessage.setFont(lillybelleFont);
        connectionErrorMessage.setFill(DARKRED);

        Button okConnectionButton = new Button("Ok");
        okConnectionButton.setFont(lillybelleFont);

        okConnectionButton.setOnMouseEntered(e -> {
            okConnectionButton.setCursor(Cursor.HAND);
        });
        okConnectionButton.setOnMouseExited(e -> {
            okConnectionButton.setCursor(Cursor.DEFAULT);
        });
        okConnectionButton.setOnAction(e -> {
            menuScene.setClose();
            matchScene.setCloseMatch();
            setIsConnectionError(true);
            createSettingStage(new Stage());
            window.close();
            window = null;
        });

        connectionErrorBox.getChildren().addAll(connectionErrorMessage, okConnectionButton);

        connectionError.getChildren().addAll(connectionErrorView, connectionErrorBox);
        connectionErrorBox.setAlignment(Pos.CENTER);
    }

    public void showConnectionError(){
        FadeTransition paneFadeIn = new FadeTransition(Duration.millis(500), connectionError);
        paneFadeIn.setFromValue(0);
        paneFadeIn.setToValue(1);
        paneFadeIn.play();
        connectionError.setVisible(true);

        Timeline showingTimer = new Timeline(new KeyFrame(
                Duration.millis(500),
                ae -> {
                    matchScene.matchBackground().setEffect(new BoxBlur(5, 5, 5));
                    matchScene.mapView().setEffect(new BoxBlur(5, 5, 5));
                    matchScene.playerViewPane().setEffect(new BoxBlur(5, 5, 5));
                    matchScene.guiMap().setEffect(new BoxBlur(5, 5, 5));
                    matchScene.pausePane().setEffect(new BoxBlur(5, 5, 5));
                    matchScene.activePowers().setEffect(new BoxBlur(5, 5, 5));
                    matchScene.helper().setEffect(new BoxBlur(5, 5, 5));
                    matchScene.turnStory().setEffect(new BoxBlur(5, 5, 5));
                    matchScene.myTurn().setEffect(new BoxBlur(5, 5, 5));
                    matchScene.chooseCardsBox().setEffect(new BoxBlur(5, 5, 5));
                    matchScene.chooseCardPane().setEffect(new BoxBlur(5, 5, 5));
                }));
        showingTimer.play();
    }


    /**
     * This method creates all the form about connection
     */
    private synchronized void askConnection(){
        closeWindow.set(false);
        valid = new AtomicBoolean(false);
        AtomicBoolean validPort = new AtomicBoolean(false);
        ipForm.setVisible(true);
        ipFormField.setVisible(true);
        portForm.setVisible(true);
        portFormField.setVisible(true);
        connectionButton.setVisible(true);
        connectionButton.setManaged(true);
        loadingIcon.setVisible(false);
        loadingIcon.setManaged(false);
        clicked.set(false);
        setIp(null);
        ipFormField.clear();
        portFormField.clear();
        while (!valid.get() && !closeWindow.get()) {
            clicked.set(false);
            ipFormField.clear();
            portFormField.clear();
            connectionButton.setOnMouseClicked(e -> {
                loadingIcon.setVisible(true);
                loadingIcon.setManaged(true);
                connectionButton.setVisible(false);
                connectionButton.setManaged(false);
                if (ipFormField.getText().isEmpty()) {      //if ip form is empty -> valid is false and error message
                    errorMessage.setVisible(true);
                    errorMessage.setManaged(true);
                    setErrorMessage("You have to insert an Ip Address");
                    loadingIcon.setVisible(false);
                    loadingIcon.setManaged(false);
                    connectionButton.setVisible(true);
                    connectionButton.setManaged(true);
                } else if (portFormField.getText().isEmpty()) {     //if port form is empty -> valid is false and error message
                    errorMessage.setVisible(true);
                    errorMessage.setManaged(true);
                    setErrorMessage("You have to insert a port number");
                    loadingIcon.setVisible(false);
                    loadingIcon.setManaged(false);
                    connectionButton.setVisible(true);
                    connectionButton.setManaged(true);
                } else {
                    try {
                        setPort(Integer.parseInt(portFormField.getText()));     //if user insert a number>1023 validPort is true
                        errorMessage.setVisible(false);
                        errorMessage.setManaged(false);
                        if(port()>1023)
                            validPort.set(true);
                        else {
                            setPort(0);
                            setErrorMessage("Not valid port number");
                            loadingIcon.setVisible(false);
                            loadingIcon.setManaged(false);
                            connectionButton.setVisible(true);
                            connectionButton.setManaged(true);
                        }
                    } catch (Exception notInt) {
                        errorMessage.setVisible(true);
                        errorMessage.setManaged(true);
                        setErrorMessage("Port must be a number");
                        loadingIcon.setVisible(false);
                        loadingIcon.setManaged(false);
                        connectionButton.setVisible(true);
                        connectionButton.setManaged(true);
                    }
                }
                if(validPort.get()){
                    setIp(ipFormField.getText());
                    errorMessage.setVisible(false);
                    errorMessage.setManaged(false);
                    valid.set(true);
                }
                setClicked();
            });
            while (!clicked.get() && !closeWindow.get()){
                try {
                    wait();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * This method is called when user has connected to server. It shows disconnection button and play button. Firstly it wait for user choice, then, if user chooses to play, waits for menu scene to be ready and displayed
     */
    public synchronized void prepareToStart(){
        openGame = new AtomicBoolean(false);
        restartConnection = new AtomicBoolean(false);

        loadingIcon.setManaged(false);
        loadingIcon.setVisible(false);
        connectionButton.setManaged(false);
        connectionButton.setVisible(false);
        connectedText.setManaged(true);
        connectedText.setVisible(true);
        disconnectionButton.setManaged(true);
        disconnectionButton.setVisible(true);

        enterGameButton.setManaged(true);
        enterGameButton.setVisible(true);

        disconnectionButton.setOnMouseEntered(e -> {
            disconnectionButton.setCursor(Cursor.HAND);
        });

        disconnectionButton.setOnMouseExited(e -> {
            disconnectionButton.setCursor(Cursor.DEFAULT);
        });

        //if user clicks disconnect
        disconnectionButton.setOnAction(e -> {
            connectedText.setVisible(false);
            connectedText.setManaged(false);
            disconnectionButton.setVisible(false);
            disconnectionButton.setManaged(false);
            loadingIcon.setVisible(true);
            loadingIcon.setManaged(true);
            enterGameButton.setVisible(false);
            enterGameButton.setManaged(false);
            restartConnection();
        });

        //if user clicks on play
        enterGameButton.setOnAction(e -> {
            setOpenGame();
        });


        while (!openGame.get() && !restartConnection.get() && !closeWindow.get()){
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        if(openGame.get()){
            Platform.runLater(() -> settingStage.close());
            Platform.runLater(this::primaryScene);
        }
        while (!gameIsReady.get() && !restartConnection.get() && !closeWindow.get()){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method restarts client
     */
    public void restartClient(){
        try {
            client.restartClient();
        } catch (ChannelClosedException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method asks ip to user
     * If the user had already connected to server and wants to play a new match, this method returns saved ip without asking it to user again
     * @return Ip address
     */
    @Override
    public String askIp() {
        if(!secondMatch){
            askConnection();
        }
        return ip;
    }

    /**
     * This method asks port to user
     * If the user had already connected to server and wants to play a new match, this method returns saved port without asking it to user again
     * @return Port
     */
    @Override
    public int askPort() {
        return port;
    }

    /**
     * This method asks username to user
     * If the user had already connected to server and wants to play a new match, this method returns saved username without asking it to user again
     * If the user is in the setting window the method calles prepareToStart
     * If the user clicks on disconnect, this method doesn't ask username and returns null
     * @return Username
     */
    @Override
    public String askUserName(CommunicationProtocol key) {
        if(key == CommunicationProtocol.USERNAME && !secondMatch) {
            if (!inMenuPage)
                prepareToStart();
            if (!restartConnection.get() && !closeWindow.get())
                nickname = menuScene.askNickname();
        } else if (key == CommunicationProtocol.UNIQUE_USERNAME){
            if (!inMenuPage)
                prepareToStart();
            if (!restartConnection.get() && !closeWindow.get())
                nickname = menuScene.askNickname();
        }
        return nickname;
    }

    /**
     * This method asks match type to user
     * @return Match type
     */
    @Override
    public int askMatchType() {
        numberOfPlayers = menuScene.askNumberOfPlayers();
        return numberOfPlayers;
    }


    /**
     * This method asks a box from a given list. It is called for choosing workers starting position, worker for the current turn, destination of a move, a build or a remove
     * @param positions List of possible boxes
     * @return Chosen box
     */
    @Override
    public int askPosition(List<int[]> positions) throws TimeOutException {
        Platform.runLater(() -> matchScene.chooseDestination(positions));
        Platform.runLater(() -> matchScene.playerView().playTimer(false, false));
        return matchScene.chosenDestination();
    }

    /**
     * This method asks cards of the match to challenger
     * @param cards List of all cards of the game
     * @return Chosen cards indexes
     */
    @Override
    public int[] askDeck(List<GodCardProxy> cards) throws TimeOutException {
        Platform.runLater(() -> matchScene.chooseCards(cards));
        Platform.runLater(() -> matchScene.playerView().playTimer(true, false));
        return matchScene.chosenCards();
    }

    /**
     * This method asks to user to chose a card to use in this match
     * @param cards List of all cards chosen by the challenger
     * @return Chosen card
     */
    @Override
    public int askCards(List<GodCardProxy> cards) throws TimeOutException {
        Platform.runLater(() -> matchScene.chooseCard(cards));
        Platform.runLater(() -> matchScene.playerView().playTimer(false, true));
        return matchScene.chosenCard();
    }

    /**
     * This method asks confirmation to player
     * It could ask if player wants to use his power
     * It could ask if player wants to undo his turn
     * @param key Key that tells which is the request for the player
     * @return Answer by the player
     */
    @Override
    public int askConfirmation(CommunicationProtocol key) {
        switch (key){
            case UNDO:
                return matchScene.showConfirmTurnPopup();
            case GOD_POWER:
                return matchScene.playerView().askUsePower();
        }
        return -1;
    }

    /**
     * This method asks worker to move in current turn
     * @param workers List of movable workers
     * @return Chosen worker position
     */
    @Override
    public int askWorker(List<int[]> workers) throws TimeOutException {
        Platform.runLater(() -> matchScene.playerView().playTimer(false, false));
        return askPosition(workers);
    }

    /**
     * This method shows different messages in the info box in match scene
     * @param key Key that tells which message to show
     */
    @Override
    public void prepareAdditionalCommunication(CommunicationProtocol key) {
        switch (key){
            case GOD_POWER:
                Platform.runLater(() -> matchScene.playerView().changeMessage("If you want to use your power click on the Power button, otherwise click on Confirm button to go on with the match"));
                break;
            case START_POSITION:
                Platform.runLater(() -> matchScene.playerView().changeMessage("Choose starting boxes for your workers"));
                break;
            case BUILD:
                Platform.runLater(() -> matchScene.playerView().changeMessage("Choose where to build"));
                break;
            case WORKER:
                Platform.runLater(() -> matchScene.playerView().changeMessage("Choose a worker for this turn"));
                break;
            case DESTINATION:
                Platform.runLater(() -> matchScene.playerView().changeMessage("Choose where to move"));
                break;
            case REMOVAL:
                Platform.runLater(() -> matchScene.playerView().changeMessage("Choose a block to remove"));
                break;
        }


    }

    /**
     * This method updates the map with new positions and buildings
     * @param boxes List of all boxes of the map
     */
    @Override
    public void updateMap(List<BoxProxy> boxes) {
        Platform.runLater(() -> matchScene.map().clearMap());
        for(BoxProxy box : boxes){
            Platform.runLater(() -> matchScene.map().box(box.position[0], box.position[1]).build(box.level, box.dome));
            if(box.occupier!=null){
                //occupier color
                String occupierColor;
                if(box.occupier.colour.equals(Colour.BLUE))
                    occupierColor = "Blue";
                else if(box.occupier.colour.equals(Colour.WHITE))
                    occupierColor = "White";
                else
                    occupierColor = "Grey";
                //occupier gender
                String gender;
                if(box.occupier.gender)
                    gender = "Female";
                else
                    gender = "Male";
                Platform.runLater(() -> matchScene.map().box(box.position[0], box.position[1]).moveWorker(occupierColor, gender));
            }
        }
    }

    /**
     * This method sets player view
     * If it is called for the first time it sets username and color
     * If it is called for the second time it sets player's card
     * @param player Player infos
     */
    @Override
    public void setMyPlayer(PlayerProxy player) {
        if(player.godCardProxy==null){
            nickname = player.name;
            if(player.colour.equals(Colour.BLUE))
                color = "Blue";
            else if(player.colour.equals(Colour.WHITE))
                color = "White";
            else if(player.colour.equals(Colour.GREY))
                color = "Grey";
        } else {
            Platform.runLater(() ->matchScene.playerView().setMyCard(player));
        }
    }

    /**
     * This method sets opponent in player's view
     * If it is called for the first time it sets usernames and colors
     * If it is called for the second time it sets opponents' cards
     * @param opponents Opponents infos
     */
    @Override
    public void setOpponentsInfo(List<PlayerProxy> opponents) {
        if(opponents.get(0).godCardProxy==null) {
            this.opponents = opponents;
            startMatch();
        }
        else
            Platform.runLater(() -> matchScene.playerView().setOpponentsCards(opponents));
    }

    /**
     * This method shows error message of connection lost
     */
    @Override
    public void connectionLost() {
        //todo ti sei disconnesso
    }

    /**
     * This method shows error of connection refused
     * @param host Host that refused the connection
     */
    @Override
    public void connectionFailed(String host) {
        setErrorMessage("Connection refused");
    }

    /**
     * This method starts the match and shows match scene
     * It waits for the match scene to be ready and then returns
     */
    @Override
    public synchronized void startMatch() {
        readyForTheMatch.set(false);
        playTransitionClouds();

        Platform.runLater(() -> matchScene.setMatchScene(nickname, numberOfPlayers, color, opponents));
        FadeTransition loadingFadeOut = new FadeTransition(Duration.millis(2000), loadingPage);
        loadingFadeOut.setFromValue(1);
        loadingFadeOut.setToValue(0);
        loadingFadeOut.play();

        Timeline matchTimer = new Timeline(new KeyFrame(
                Duration.millis(1500),
                ae -> {
                    loadingPage.setVisible(false);
                    FadeTransition matchFade = new FadeTransition(Duration.millis(2000), matchPage);
                    matchFade.setFromValue(0.0);
                    matchFade.setToValue(1.0);
                    matchFade.play();
                    matchPage.setVisible(true);
                    setReadyForTheMatch();
                }));
        matchTimer.play();

        while (!readyForTheMatch.get()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method sets current player
     * @param player Current player
     */
    @Override
    public void setCurrentPlayer(PlayerProxy player) {
        if(player.name.equals(nickname))
            Platform.runLater(() -> matchScene.setMyTurn());
        else {
            Platform.runLater(() -> matchScene.setOpponentTurn(player.name));
            Platform.runLater(() -> matchScene.playerView().stopTimer());
        }
    }

    /**
     * This method tells what last player did in the last turn
     * @param events List of action made in the last turn
     */
    @Override
    public void tellStory(List<String> events) {

        for(String event : events){
            String[] content = event.split(MatchStory.STORY_SEPARATOR);

            String player = content[0];

            Type chosenWorkerType = new TypeToken<int[]>() {}.getType();
            int[] chosenWorker = new Gson().fromJson(content[1], chosenWorkerType);

            Type actionType = new TypeToken<CommunicationProtocol>() {}.getType();
            CommunicationProtocol action = new Gson().fromJson(content[2], actionType);

            Type destinationType = new TypeToken<int[]>() {}.getType();
            int[] destination = new Gson().fromJson(content[3], destinationType);

            Platform.runLater(() -> matchScene.playerView().writeStory(player, chosenWorker, action, destination));
        }
    }

    /**
     * This method tells who is the winner of the match
     * @param player Winner
     */
    @Override
    public void setWinner(PlayerProxy player) {
        if(player.name.equals(nickname)){
            Platform.runLater(() -> matchScene.winner(true));
            Platform.runLater(() -> matchScene.playerView().changeMessage("You won the match"));
        } else {
            Platform.runLater(() -> matchScene.winner(false));
            Platform.runLater(() -> matchScene.playerView().changeMessage(player.name + " won the match"));
        }
    }

    /**
     * This method tells if one opponent has lost the match (because of surrender or connection lost)
     * @param player Loser
     */
    @Override
    public void setLoser(PlayerProxy player) {
        if(player.name.equals(nickname)){
            Platform.runLater(() -> matchScene.winner(false));
        } else {
            Platform.runLater(() -> matchScene.playerView().setLoser(player.name));
        }
    }

    @Override
    public void timeOut() {
        matchScene.setTimeoutLoser(true);
    }

    @Override
    public synchronized void serverDisconnected() {
        if(!inMenuPage && !secondWindow) {
            connectedText.setVisible(false);
            connectedText.setManaged(false);
            disconnectionButton.setVisible(false);
            disconnectionButton.setManaged(false);
            loadingIcon.setVisible(true);
            loadingIcon.setManaged(true);
            errorMessage.setVisible(true);
            errorMessage.setManaged(true);
            enterGameButton.setVisible(false);
            enterGameButton.setManaged(false);
            restartConnection();
            setErrorMessage("Connection lost");
        } else {
            while (!inMenuPage) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            screenWidth = Screen.getPrimary().getBounds().getWidth();
            screenHeight = Screen.getPrimary().getBounds().getHeight();
            ipForm = new Text ("Ip Address");
            portForm = new Text("Port Number");
            connectionButton = new Button("Connect");
            client.setRestart(true);
            showConnectionError();
        }
    }
}
