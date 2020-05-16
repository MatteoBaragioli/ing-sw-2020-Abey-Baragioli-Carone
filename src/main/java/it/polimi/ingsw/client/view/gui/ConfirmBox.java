package it.polimi.ingsw.client.view.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ConfirmBox {
    static boolean answer;
    static final Font lillybelleFont = Font.loadFont(ConfirmBox.class.getResourceAsStream("/fonts/LillyBelle.ttf"), 18);

    public static boolean display(String title, String message, Double primaryWidth, Double primaryHeight){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setWidth(primaryWidth/4);
        window.setHeight(primaryHeight/4);
        window.initStyle(StageStyle.UNDECORATED);

        VBox layout = layout(window, primaryHeight, primaryWidth, message);

        Image menuBackgroundImg = new Image(ConfirmBox.class.getResource("/img/frame.png").toString(), primaryWidth/4, primaryHeight/4, false, false);
        BackgroundImage backgroundImage = new BackgroundImage(menuBackgroundImg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);

        layout.setBackground(background);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }

    private static Button yesButton(Stage window){
        Button yesButton = new Button("Yes");
        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });
        yesButton.setFont(lillybelleFont);
        yesButton.setCursor(Cursor.HAND);

        return yesButton;
    }

    private static Button noButton(Stage window){
        Button noButton = new Button("No");
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });
        noButton.setFont(lillybelleFont);
        noButton.setCursor(Cursor.HAND);

        return noButton;
    }

    private static VBox layout(Stage window, double primaryHeight, double primaryWidth, String message){
        Button yesButton = yesButton(window);
        Button noButton = noButton(window);

        Label label = new Label();
        HBox space = new HBox();
        Text text = new Text(message);

        HBox box = new HBox();
        box.getChildren().addAll(yesButton, noButton);
        box.setSpacing(50);
        box.setPrefWidth(200);
        box.setAlignment(Pos.CENTER);

        text.setFont(lillybelleFont);


        VBox layout = new VBox(10);
        layout.setPadding(new Insets(primaryHeight/16, primaryWidth/16, primaryHeight/16, primaryWidth/16));
        layout.setSpacing(10);
        layout.getChildren().addAll(space, text, label, box);
        return layout;
    }
}
