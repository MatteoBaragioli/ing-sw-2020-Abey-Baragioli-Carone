module it.polimi.ingsw.client.view.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens it.polimi.ingsw.client.view.gui to javafx.fxml;
    exports it.polimi.ingsw.client.view.gui;
}