package it.polimi.ingsw.client.view.gui;

import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.List;

public class GuiBox extends Button {
    private final int x;
    private final int y;
    private final double boxWidth;
    private final double boxHeight;
    private StackPane graphics = new StackPane();
    private final ImageView building = new ImageView();
    private final ImageView worker = new ImageView();
    private final ImageView dome = new ImageView();
    private double previousOpacity;
    private int index = -1;
    private final double screenWidth;
    private final double screenHeight;

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
        dome.setOpacity(1);
        graphics.getChildren().addAll(building, worker, dome);
        graphics.maxWidth(boxWidth);
        graphics.prefWidth(boxWidth);
        graphics.maxHeight(boxHeight);
        graphics.prefHeight(boxHeight);
        graphics.setAlignment(Pos.CENTER);
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

    public void setEffects(MatchScene match){
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
            match.setChosenBox(this);
            for(int[] i : match.chosableBoxes()){
                match.map().box(i[0], i[1]).setAsNotChosable();
            }
            setSelected();
            match.playerView().activateButtons();
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

    public void setAsChosable(MatchScene match){
        setBlue();
        setDisable(false);
        previousOpacity = getOpacity();
        changeOpacity(0.4);
        setEffects(match);
    }

    public void setAsNotChosable(){
        removeColor();
        setDisable(true);
        changeOpacity(previousOpacity);
        clearEffects();
    }

    public void build(int level, boolean isThereDome){
        Image levelImg;
        if(level==0){
            building.setImage(null);
        }else {
            levelImg = new Image(PlayerView.class.getResource("/img/buildingsAndWorkers/level" + level + ".png").toString(), boxWidth - boxWidth / 5, boxHeight - boxHeight / 5, false, false);
            building.setImage(levelImg);
        }
        if(isThereDome) {
            Image domeImg = new Image(PlayerView.class.getResource("/img/buildingsAndWorkers/dome.png").toString(), boxWidth - boxWidth / 5, boxHeight - boxHeight / 5, false, false);
            dome.setImage(domeImg);
        }
        setOpacity(1);
    }

    public void moveWorker(String color, String gender){
        Image workerImg = new Image(PlayerView.class.getResource("/img/buildingsAndWorkers/worker" + gender + color + ".png").toString(), boxWidth - boxWidth / 6, boxHeight - boxHeight / 8, false, false);
        worker.setImage(workerImg);
        changeOpacity(1);
        previousOpacity = 1;
    }

    public void removeWorker(){
        worker.setImage(null);
    }

}
