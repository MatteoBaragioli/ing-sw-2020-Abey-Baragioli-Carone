package it.polimi.ingsw.client.view.gui;

import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.util.List;

public class GuiBox extends Button {
    private int x;
    private int y;
    private int level=0;
    private double boxWidth;
    private double boxHeight;
    private Group graphics = new Group();
    private Label building = new Label();
    private Label worker = new Label();
    private double previousOpacity;
    private int index = -1;
    private double screenWidth;
    private double screenHeight;

    public GuiBox(int x, int y, double boxWidth, double boxHeight, double screenWidth, double screenHeight) {
        this.x = x;
        this.y = y;
        this.boxWidth = boxWidth;
        this.boxHeight = boxHeight;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        setDisable(true);
        removeColor();
        setContentDisplay(ContentDisplay.CENTER);
        building.setOpacity(1);
        worker.setOpacity(1);
        graphics.getChildren().addAll(building, worker);
        graphics.autoSizeChildrenProperty();
        graphics.maxWidth(boxWidth);
        graphics.prefWidth(boxWidth);
        graphics.maxHeight(boxHeight);
        graphics.prefHeight(boxHeight);
        setGraphic(graphics);
    }

    public int boxX() {
        return x;
    }

    public int boxY() {
        return y;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public int index() {
        return index;
    }

    public void setBlue(){
        setBackground(new Background(new BackgroundFill(Color.ROYALBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    }
    public void setSelected(){
        setBackground(new Background(new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        changeOpacity(1);
        previousOpacity=1;
    }

    public void setHoverBlue(){
        setBackground(new Background(new BackgroundFill(Color.MIDNIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    }
    public void removeColor(){
        setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void changeOpacity(double opacity){
        setOpacity(opacity);
    }

    public void setEffects(MatchScene match, List<int[]> chosableBoxes, int phase){
        addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            changeOpacity(0.4);
            setHoverBlue();
            setCursor(Cursor.HAND);
            event.consume();
        });

        addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            changeOpacity(0.4);
            setBlue();
            setCursor(Cursor.DEFAULT);
            event.consume();
        });
        addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            match.setChosenWorkerNewPosition(x, y);
            for(int[] i : chosableBoxes){
                match.map().box(i[0], i[1]).setAsNotChosable();
            }
            setSelected();
            match.playerView().activateButtons(screenWidth, screenHeight, 1, match);
        });
    }

    public void clearEffects(){

        addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            changeOpacity(previousOpacity);
            setCursor(Cursor.DEFAULT);
            event.consume();
        });

        addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            changeOpacity(previousOpacity);
            setCursor(Cursor.DEFAULT);
            event.consume();
        });
        addEventHandler(MouseEvent.MOUSE_CLICKED, Event::consume);
    }

    public void setAsChosable(MatchScene match, List<int[]> chosableBoxes, int phase){
        setBlue();
        setDisable(false);
        previousOpacity = getOpacity();
        changeOpacity(0.4);
        setEffects(match, chosableBoxes, phase);
    }

    public void setAsNotChosable(){
        removeColor();
        setDisable(true);
        changeOpacity(previousOpacity);
        clearEffects();
    }

    public void build(){
        level++;
        Image levelImg;
        if(level==4){
            levelImg = new Image(PlayerView.class.getResource("/img/buildingsAndWorkers/levelDome.png").toString());
        }else {
            levelImg = new Image(PlayerView.class.getResource("/img/buildingsAndWorkers/level" + level + ".png").toString());
        }
        ImageView levelImgView = new ImageView(levelImg);
        levelImgView.setFitWidth(boxWidth - boxWidth / 5);
        levelImgView.setFitHeight(boxHeight - boxHeight / 5);
        building.setGraphic(levelImgView);
        setOpacity(1);
    }

    public void remove(){
        level--;
        if(level>0){
            Image levelImg = new Image(PlayerView.class.getResource("/img/buildingsAndWorkers/level" + level + ".png").toString());
            ImageView levelImgView = new ImageView(levelImg);
            levelImgView.setFitWidth(boxWidth - boxWidth / 5);
            levelImgView.setFitHeight(boxHeight - boxHeight / 5);
            building.setGraphic(levelImgView);
        } else {
            building.setGraphic(null);
        }
    }

    public void moveWorker(String color, String gender){
        Image workerImg = new Image(PlayerView.class.getResource("/img/buildingsAndWorkers/worker" + gender + color + ".png").toString());
        ImageView workerImgView = new ImageView(workerImg);
        workerImgView.setFitWidth(boxWidth - boxWidth / 5);
        workerImgView.setFitHeight(boxHeight - boxHeight / 5);
        worker.setGraphic(workerImgView);
        changeOpacity(1);
        previousOpacity = 1;
    }

    public void removeWorker(){
        worker.setGraphic(null);
    }

}