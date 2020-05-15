package it.polimi.ingsw.client.view.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MenuForm extends Group {
    private static final Font lillybelleFont = Font.loadFont(ConfirmBox.class.getResourceAsStream("/fonts/LillyBelle.ttf"), 25);
    TextField nicknameInput;
    VBox formBox;
    Group form;
    ToggleGroup numberOfPlayers;

    public void create(double screenWidth, double screenHeight) {

        //nickname
        Text nicknameText = new Text("Nickname");
        nicknameInput = new TextField();
        nicknameInput.setCursor(Cursor.TEXT);
        //todo aggiungere avviso limite carattere e nome già usato

        //number of players
        Text numberOfPlayersText = new Text("Numbers of players");
        RadioButton twoPlayers = new RadioButton("2");
        RadioButton threePlayers = new RadioButton("3");
        twoPlayers.setCursor(Cursor.HAND);
        threePlayers.setCursor(Cursor.HAND);

        numberOfPlayers = new ToggleGroup();
        twoPlayers.setToggleGroup(numberOfPlayers);
        twoPlayers.setSelected(true);
        twoPlayers.requestFocus();
        threePlayers.setToggleGroup(numberOfPlayers);

        //number of players options line
        HBox numberOfPlayersOptions = new HBox();
        numberOfPlayersOptions.getChildren().addAll(twoPlayers, threePlayers);
        numberOfPlayersOptions.setSpacing(60);
        numberOfPlayersOptions.setPrefWidth(200);
        numberOfPlayersOptions.setAlignment(Pos.CENTER);

        //font
        nicknameText.setFont(lillybelleFont);
        nicknameInput.setFont(lillybelleFont);
        numberOfPlayersText.setFont(lillybelleFont);
        twoPlayers.setFont(lillybelleFont);
        threePlayers.setFont(lillybelleFont);

        //form box
        VBox textBox = new VBox();
        textBox.setSpacing(10);
        textBox.getChildren().addAll(nicknameText, nicknameInput, numberOfPlayersText, numberOfPlayersOptions);
        textBox.setAlignment(Pos.CENTER);

        //space
        VBox space = spaceLine(screenWidth, screenHeight);

        //form box
        formBox = new VBox();
        formBox.setSpacing(10);
        formBox.getChildren().addAll(textBox, space);



        //form group
        form = new Group();
        form.setAutoSizeChildren(true);
        form.getChildren().addAll(formBox);
        form.prefWidth(screenWidth/4);
        form.prefHeight(screenHeight/2);
    }

    private VBox spaceLine(double screenWidth, double screenHeight){
        VBox space = new VBox();
        space.setPadding(new Insets(screenHeight/16, screenWidth/16, screenHeight/16, screenWidth/16));
        space.setSpacing(10);
        return space;
    }

    public String getNickname(){
        return nicknameInput.getText();
    }

    public int getNumberOfPlayers(){
        RadioButton selectedRadioButton = (RadioButton) numberOfPlayers.getSelectedToggle();
        String number = selectedRadioButton.getText();
        if(number.equals("2")){
            return 2;
        } else {
            return 3;
        }
    }

    public Group getForm() {
        return form;
    }
}