package it.polimi.ingsw.client.view.gui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;

public class GuiApp extends Application{
    private double screenWidth = Screen.getPrimary().getBounds().getWidth();
    private double screenHeight = Screen.getPrimary().getBounds().getHeight();
    private Font lillybelleFont = Font.loadFont(ConfirmBox.class.getResourceAsStream("/fonts/LillyBelle.ttf"), 25);
    private static final int mapRowsNumber = 5;
    private static final int mapColumnsNumber = 5;
    Stage window;
    StackPane openingPage = new StackPane();
    MenuScene menuScene;
    StackPane loadingPage = new StackPane();
    AnchorPane matchPage = new AnchorPane();
    private String nickname;
    private int numberOfPlayers;
    Scene menu;


    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        style();
        //openingPage = openingPage();
        loadingPage = loadingPage();
        Scene loading = new Scene(loadingPage);



        menuScene = new MenuScene(window, screenWidth, screenHeight, loading);
        menuScene.setMenuPage();

        window.show();
    }

    public void style(){
        //window.setMaximized(true);
        window.setWidth(1280);
        window.setHeight(720);
        screenWidth = 1280;
        screenHeight = 720;
        window.initStyle(StageStyle.DECORATED);
        window.setTitle("Santorini");
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram(menuScene().menuPage());
        });
        window.setResizable(true);
    }

    public void closeProgram(StackPane fullPage){
        boolean answer = ConfirmBox.display("Quit?", "Sure you want to quit the game?", window.getWidth(), window.getHeight());
        if(answer)
            window.close();
        else
            fullPage.setEffect(new BoxBlur(0, 0, 0));
    }

    public MenuScene menuScene(){
        return menuScene;
    }
/*
    private HBox firstLine(){
        HBox topLine = new HBox();

        Button fullscreenMode = new Button("Full Screen Mode");
        Button windowMode = new Button("Window Mode");


        fullscreenMode.setOnAction(e -> {
            window.setMaximized(true);
            windowMode.setVisible(true);
            fullscreenMode.setVisible(false);
        });

        windowMode.setOnAction(e -> {
            window.setMaximized(false);
            window.setWidth(screenWidth/1.5);
            window.setHeight(screenHeight/1.5);
            fullscreenMode.setVisible(true);
            windowMode.setVisible(false);
        });


        topLine.getChildren().addAll(fullscreenMode, windowMode);
        topLine.setAlignment(Pos.TOP_RIGHT);
        return topLine;
    }*/

    private StackPane loadingPage(){
        Image loadingGif = new Image(GuiApp.class.getResource("/img/loading.gif").toString(), screenWidth/2, screenHeight/2, false, false);
        ImageView loadingImageGif = new ImageView(loadingGif);
        Image loadingImg = new Image(GuiApp.class.getResource("/img/loading.png").toString(), screenWidth, screenHeight, false, false);
        ImageView loadingImageView = new ImageView(loadingImg);
        loadingPage.getChildren().addAll(loadingImageGif, loadingImageView);
        loadingPage.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        return loadingPage;
    }


    public Node getNodeByRowColumnIndex (int row, int column, GridPane map) {
        Node result = null;
        ObservableList<Node> childrens = map.getChildren();

        for (Node node : childrens) {
            if(map.getRowIndex(node) == row && map.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }
    private void match(MatchPage match){
        //todo inizio di gioco
        match.map().box(4,4).moveWorker("Blue", "Female");
        myTurn(match);

        /*match.getPlayerView().createCard("Zeus", screenWidth, screenHeight);
                match.getPlayerView().activateButtons(screenWidth, screenHeight);
                match.getPlayerView().activateUsePower(screenWidth, screenHeight);
                match.map().box(3,3).build();
                match.map().box(2,3).build();
                match.map().box(2,3).build();
                match.map().box(3,2).build();
                match.map().box(3,2).build();
                match.map().box(3,2).build();
                match.map().box(4,2).build();
                match.map().box(4,2).build();
                match.map().box(4,2).build();
                match.map().box(4,2).build();
                match.map().box(1,1).moveWorker("Blue", "Male");
                match.map().box(3,3).moveWorker("White", "Female");
                match.map().box(0,1).build();
                match.map().box(0,1).moveWorker("Grey", "Male");
                match.map().box(0,0).build();
                match.map().box(0,0).moveWorker("Blue", "Female");
                match.map().box(0,0).setAsChosable();
                match.map().box(0,3).build();
                match.map().box(0,3).setAsChosable();
                match.map().box(0,1).removeWorker();
                match.map().box(1,1).removeWorker();
                match.map().box(4,2).remove();
                match.map().box(3,2).remove();
                match.map().box(0,0).setAsNotChosable();*/
        //match.getPlayerView().deactivateButtons(screenWidth, screenHeight);
        //match.getPlayerView().deactivateUsePower(screenWidth, screenHeight);
        //match.getPlayerView().setActivePowers("Apollo", "Your Worker may move into an opponent Worker's space (using normal movement rules) and force their Worker to the space yours just vacated (swapping their position)", "Athena", "If one of your Workers moved up on your last turn, opponent Workers cannot move up this turn", "Zeus", "Your Worker may build under itself in its current space, forcing it up one level. You do not win by forcing yourself up to the third level");
    }

    private void myTurn(MatchPage match){
        //todo connect to server


        match.map().box(2,3).moveWorker("Blue", "Male");
        List<int[]> possibleDestinations = new ArrayList<>();
        int[] box1 = {1,2};
        int[] box2 = {1,3};
        int[] box3 = {1,4};
        int[] box4 = {2,2};
        int[] box5 = {2,4};
        int[] box6 = {3,2};
        int[] box7 = {3,3};
        int[] box8 = {3,4};
        possibleDestinations.add(box1);
        possibleDestinations.add(box2);
        possibleDestinations.add(box3);
        possibleDestinations.add(box4);
        possibleDestinations.add(box5);
        possibleDestinations.add(box6);
        possibleDestinations.add(box7);
        possibleDestinations.add(box8);

        match.setChosenWorkerPosition(2,3);
        match.setChosenWorkerColor("Blue");
        match.setChosenWorkerGender("Male");

        match.setChosableBoxes(possibleDestinations);
        match.chooseDestination(possibleDestinations);


    }

    private void chooseWorker(){
    }

    /*private void chooseBuild(MatchPage match, List<int[]> possibleDestinations){
        for(int[] i : possibleDestinations){
            match.map().box(i[0], i[1]).setAsChosable();
        }
    }*/

}