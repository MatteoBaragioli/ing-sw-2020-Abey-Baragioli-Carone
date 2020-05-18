package it.polimi.ingsw.client.view.gui;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static javafx.geometry.Pos.*;
import static javafx.geometry.Pos.TOP_RIGHT;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

public class PlayerView extends StackPane {
    private static final Font standardFont = Font.loadFont(PlayerView.class.getResourceAsStream("/fonts/LillyBelle.ttf"), 40);
    private static final Font messagesFont = Font.loadFont(PlayerView.class.getResourceAsStream("/fonts/LillyBelle.ttf"), 25);
    private static final Font powersFont = Font.loadFont(PlayerView.class.getResourceAsStream("/fonts/MinionPro-SemiboldIt.otf"), 20);
    private StackPane playerView;
    private Label messagesBox;
    private Label powersBox;
    private Label cardName;
    private Label cardInfo;
    private Image godImage;
    ImageView godView;
    ImageView confirmView;
    ImageView undoView;
    ImageView usePowerView;
    Group powers;
    Group powersClosed;

    public void create(String nickname, String color, int numberOfPlayers, double screenWidth, double screenHeight){ //todo servirà probabilmente la lista di players
        playerView = new StackPane();
        Color fontColor = fontColor(color);

        //bottom-center
        Image nameImg = new Image(PlayerView.class.getResource("/img/playerName"+color+".png").toString(),screenWidth/3, screenHeight/8,false,false);
        ImageView nameView = new ImageView(nameImg);

        Label textBox = new Label(nickname);
        textBox.setFont(standardFont);
        textBox.setTextFill(fontColor);
        textBox.setPrefWidth(screenWidth/3);
        textBox.setPrefHeight(screenHeight/8);
        textBox.setAlignment(CENTER);



        //bottom-left
        Image messagesImg = new Image(PlayerView.class.getResource("/img/messagesBox.png").toString(),screenWidth/4, screenHeight/4,false,false);
        ImageView messagesView = new ImageView(messagesImg);

        messagesBox = new Label("Good Luck!");
        messagesBox.setFont(messagesFont);
        messagesBox.setTextFill(WHITE);
        messagesBox.setPrefWidth(screenWidth/4);
        messagesBox.setPrefHeight(screenHeight/4);
        Insets padding = new Insets(screenWidth/500, screenWidth/70, screenWidth/500, screenWidth/70);
        messagesBox.setPadding(padding);
        messagesBox.setWrapText(true);
        messagesBox.setAlignment(CENTER);
        messagesBox.setTextAlignment(TextAlignment.CENTER);



        //bottom-right
        HBox buttons = new HBox();
        Image confirmImg = new Image(PlayerView.class.getResource("/img/buttons/confirmActionInactive.png").toString(),screenWidth/15, screenHeight/10,false,false);
        confirmView = new ImageView(confirmImg);
        Image undoImg = new Image(PlayerView.class.getResource("/img/buttons/undoInactive.png").toString(),screenWidth/15, screenHeight/10,false,false);
        undoView = new ImageView(undoImg);
        Image usePowerImg = new Image(PlayerView.class.getResource("/img/buttons/usePowerInactive.png").toString(),screenWidth/15, screenHeight/10,false,false);
        usePowerView = new ImageView(usePowerImg);

        buttons.getChildren().addAll(usePowerView, undoView, confirmView);
        buttons.setSpacing(screenWidth/50);
        buttons.setPrefWidth(screenWidth/3);
        buttons.setPrefHeight(screenHeight/8);
        buttons.setAlignment(BOTTOM_RIGHT);
        Insets buttonsPadding = new Insets(screenWidth/50, screenWidth/40, screenWidth/50, screenWidth/50);
        buttons.setPadding(buttonsPadding);



        //center-left
        Image cardImg = new Image(PlayerView.class.getResource("/img/godFrame"+color+".png").toString(),screenWidth/4, screenHeight/2,false,false);
        ImageView cardView = new ImageView(cardImg);
        godImage = new Image(PlayerView.class.getResource("/img/godCards/noGod.png").toString(),(screenWidth/7), screenHeight/3.3,false,false);
        godView = new ImageView(godImage);

        cardName = new Label();
        cardName.setPrefSize(screenWidth/4, screenHeight/2);
        cardName.setFont(standardFont);
        cardName.setTextFill(fontColor);
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



        //center-right
        Image powersImg = new Image(PlayerView.class.getResource("/img/powersFrame.png").toString(),screenWidth/3, screenHeight/1.2,false,false);
        ImageView powersView = new ImageView(powersImg);

        powersBox = new Label();
        powersBox.setPrefWidth(screenWidth/3);
        powersBox.setPrefHeight(screenHeight/1.5);
        Insets powersPadding = new Insets(screenWidth/10, screenWidth/15, 0, screenWidth/15);
        powersBox.setPadding(powersPadding);
        powersBox.setWrapText(true);
        powersBox.setAlignment(CENTER);
        powersBox.setTextAlignment(TextAlignment.CENTER);
        powersBox.setTextFill(BLACK);
        powersBox.setFont(powersFont);

        /*Image closePowersImg = new Image(PlayerView.class.getResource("/img/buttons/close.png").toString(),screenWidth/20, screenHeight/15,false,false);
        Image closePowersImgHover = new Image(PlayerView.class.getResource("/img/buttons/closeHover.png").toString(),screenWidth/20, screenHeight/15,false,false);
        Image closePowersImgPressed = new Image(PlayerView.class.getResource("/img/buttons/closePressed.png").toString(),screenWidth/20, screenHeight/15,false,false);
        ImageView closePowersView = new ImageView(closePowersImg);
        closePowersView.setOnMouseEntered(e -> {
            closePowersView.setCursor(Cursor.HAND);
            closePowersView.setImage(closePowersImgHover);
        });
        closePowersView.setOnMouseExited(e -> {
            closePowersView.setCursor(Cursor.DEFAULT);
            closePowersView.setImage(closePowersImg);
        });
        closePowersView.setOnMousePressed(e -> {
            closePowersView.setImage(closePowersImgPressed);
        });
        closePowersView.setOnMouseReleased(e -> {
            closePowersView.setImage(closePowersImg);
        });
        closePowersView.setOnMouseClicked(e -> {
            playerView.getChildren().remove(9);
            playerView.getChildren().add(powersClosed);
        });*/



        powers = new Group();
        powers.getChildren().addAll(powersView, powersBox /*closePowersView*/);
        powers.setAutoSizeChildren(true);
        powers.prefWidth(screenWidth/3);
        powers.prefHeight(screenHeight/1.2);


        Image showPowersImg = new Image(PlayerView.class.getResource("/img/buttons/showPowers.png").toString(),screenWidth/5, screenHeight/2,false,false);
        Image showPowersImgHover = new Image(PlayerView.class.getResource("/img/buttons/showPowersHover.png").toString(),screenWidth/5, screenHeight/2,false,false);
        Image showPowersImgPressed = new Image(PlayerView.class.getResource("/img/buttons/showPowersPressed.png").toString(),screenWidth/5, screenHeight/2,false,false);
        ImageView showPowersView = new ImageView(showPowersImg);

        showPowersView.setOnMouseEntered(e -> {
            showPowersView.setCursor(Cursor.HAND);
            showPowersView.setImage(showPowersImgHover);
        });
        showPowersView.setOnMouseExited(e -> {
            showPowersView.setCursor(Cursor.DEFAULT);
            showPowersView.setImage(showPowersImg);
        });
        showPowersView.setOnMousePressed(e -> {
            showPowersView.setImage(showPowersImgPressed);
        });
        showPowersView.setOnMouseReleased(e -> {
            showPowersView.setImage(showPowersImg);
        });
        showPowersView.setOnMouseClicked(e -> {
            playerView.getChildren().remove(8);
            playerView.getChildren().add(powers);
        });

        powersClosed = new Group();
        powersClosed.getChildren().addAll(showPowersView);
        powersClosed.prefWidth(screenWidth/5);
        powersClosed.prefHeight(screenHeight/2);



        //top-left
        Image surrenderImg = new Image(PlayerView.class.getResource("/img/buttons/surrender.png").toString(),screenWidth/12, screenHeight/8,false,false);
        Image surrenderImgHover = new Image(PlayerView.class.getResource("/img/buttons/surrenderHover.png").toString(),screenWidth/12, screenHeight/8,false,false);
        Image surrenderImgPressed = new Image(PlayerView.class.getResource("/img/buttons/surrenderPressed.png").toString(),screenWidth/12, screenHeight/8,false,false);
        ImageView surrenderView = new ImageView(surrenderImg);

        surrenderView.setOnMouseEntered(e -> {
            surrenderView.setCursor(Cursor.HAND);
            surrenderView.setImage(surrenderImgHover);
        });
        surrenderView.setOnMouseExited(e -> {
            surrenderView.setCursor(Cursor.DEFAULT);
            surrenderView.setImage(surrenderImg);
        });
        surrenderView.setOnMousePressed(e -> {
            surrenderView.setImage(surrenderImgPressed);
        });
        surrenderView.setOnMouseReleased(e -> {
            surrenderView.setImage(surrenderImg);
        });
        surrenderView.setOnMouseClicked(e -> {
            //todo disconnettere client
        });

        Image helperImg = new Image(PlayerView.class.getResource("/img/buttons/helper.png").toString(),(screenWidth/12), screenHeight/8,false,false);
        Image helperImgHover = new Image(PlayerView.class.getResource("/img/buttons/helperHover.png").toString(),screenWidth/12, screenHeight/8,false,false);
        Image helperImgPressed = new Image(PlayerView.class.getResource("/img/buttons/helperPressed.png").toString(),screenWidth/12, screenHeight/8,false,false);
        ImageView helperView = new ImageView(helperImg);

        helperView.setOnMouseEntered(e -> {
            helperView.setCursor(Cursor.HAND);
            helperView.setImage(helperImgHover);
        });
        helperView.setOnMouseExited(e -> {
            helperView.setCursor(Cursor.DEFAULT);
            helperView.setImage(helperImg);
        });
        helperView.setOnMousePressed(e -> {
            helperView.setImage(helperImgPressed);
        });
        helperView.setOnMouseReleased(e -> {
            helperView.setImage(helperImg);
        });
        helperView.setOnMouseClicked(e -> {
            //todo aprire tutorial
        });

        StackPane upperButtons = new StackPane();
        upperButtons.getChildren().addAll(surrenderView, helperView);
        upperButtons.setMaxWidth(screenWidth/5);
        upperButtons.setMaxHeight(screenHeight/16);
        setAlignment(surrenderView, CENTER_LEFT);
        setAlignment(helperView, CENTER_RIGHT);


        //top-center
        //todo cartello del turno corrente



        //top-right
        //todo rifare con lista di opponents
        VBox opponents = new VBox();
        Insets opponentPadding = new Insets(screenWidth/50, screenWidth/50, 0, 0);
        opponents.setPadding(opponentPadding);
        opponents.setAlignment(TOP_RIGHT);

        List<String> nicknamesMomentanei = new ArrayList<>();
        nicknamesMomentanei.add("opponent1");
        nicknamesMomentanei.add("opponent2");

        List<String> coloriMomentanei = new ArrayList<>();
        coloriMomentanei.add("White");
        coloriMomentanei.add("Grey");

        List<String> deiMomentanei = new ArrayList<>();
        deiMomentanei.add("GodCard1");
        deiMomentanei.add("GodCard2");

        String opponentColor;
        String opponentNickName;
        String opponentCard;

        for(int i=0; i<numberOfPlayers-1; i++){
            opponentColor = coloriMomentanei.get(i);
            opponentNickName = nicknamesMomentanei.get(i);
            opponentCard = deiMomentanei.get(i);

            Image opponentImg = new Image(PlayerView.class.getResource("/img/opponentView"+opponentColor+".png").toString(),screenWidth/5, screenHeight/5,false,false);
            ImageView opponentView = new ImageView(opponentImg);


            Label opponentName = new Label(opponentNickName);
            opponentName.setPrefWidth(screenWidth/5);
            opponentName.setPrefHeight(screenHeight/5);
            opponentName.setFont(messagesFont);
            Insets opponentNamePadding = new Insets(screenWidth/150, 0, 0, 0);
            opponentName.setPadding(opponentNamePadding);
            opponentName.setTextFill(fontColor(opponentColor));
            opponentName.setAlignment(TOP_CENTER);
            opponentName.setTextAlignment(TextAlignment.CENTER);

            Label opponentCardName = new Label(opponentCard);
            opponentCardName.setPrefWidth(screenWidth/5);
            opponentCardName.setPrefHeight(screenHeight/5);
            opponentCardName.setFont(messagesFont);
            opponentCardName.setTextFill(fontColor(opponentColor));
            opponentCardName.setAlignment(CENTER);
            opponentCardName.setTextAlignment(TextAlignment.CENTER);




            Group opponent = new Group();
            opponent.getChildren().addAll(opponentView, opponentName, opponentCardName);


            opponents.getChildren().add(opponent);
        }





        playerView.getChildren().addAll(opponents, buttons, messagesView, messagesBox, nameView, textBox, cardView, cardName, cardInfo, upperButtons, powersClosed);
        playerView.setPrefWidth(screenWidth);
        playerView.setPrefHeight(screenHeight);
        setAlignment(messagesBox, BOTTOM_LEFT);
        setAlignment(messagesView, BOTTOM_LEFT);
        setAlignment(nameView, BOTTOM_CENTER);
        setAlignment(textBox, BOTTOM_CENTER);
        setAlignment(cardView, CENTER_LEFT);
        setAlignment(cardName, CENTER_LEFT);
        setAlignment(cardInfo, CENTER_LEFT);
        setAlignment(buttons, BOTTOM_RIGHT);
        setAlignment(powers, CENTER_RIGHT);
        setAlignment(powersClosed, CENTER_RIGHT);
        setAlignment(upperButtons, TOP_LEFT);
        setAlignment(opponents, TOP_RIGHT);




    }

    public StackPane getPlayerViewStackPane(){
        return playerView;
    }

    public void changeMessage(String messageString){
        messagesBox.setText(messageString);
    }

    public String getMessage(){
        return messagesBox.getText();
    }

    public void createCard(String cardName, double screenWidth, double screenHeight){
        String oldText = getMessage();
        godImage = new Image(PlayerView.class.getResource("/img/godCards/"+cardName+".png").toString(),(screenWidth/7), screenHeight/3.3,false,false);
        godView = new ImageView(godImage);
        cardInfo.setGraphic(godView);

        this.cardName.setText(cardName);
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

    public void setActivePowers(String firstPowerName, String firstPower, String secondPowerName, String secondPower, String thirdPowerName, String thirdPower){
        String powers;
        if(thirdPowerName!=null&&thirdPower!=null) {
            powers = firstPowerName + System.lineSeparator() +firstPower + System.lineSeparator() + System.lineSeparator() + secondPowerName + System.lineSeparator() + secondPower + System.lineSeparator() + System.lineSeparator() + thirdPowerName + System.lineSeparator() + thirdPower ;
        }
        else{
            powers =firstPowerName + System.lineSeparator() +firstPower + System.lineSeparator() + System.lineSeparator() + secondPowerName + System.lineSeparator() + secondPower + System.lineSeparator();
        }
        powersBox.setText(powers);
    }

    public Color fontColor(String color){
        if(color=="White"){
            return BLACK;
        }else{
            return WHITE;
        }
    }
}
