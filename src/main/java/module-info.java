module it.polimi.ingsw.client.view.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens it.polimi.ingsw.client.view.gui to javafx.fxml;
    opens it.polimi.ingsw.server.model to com.google.gson;
    opens it.polimi.ingsw.network.objects to com.google.gson;
    exports it.polimi.ingsw.client.view.gui;
    exports it.polimi.ingsw.network.objects;
    exports it.polimi.ingsw.server.model;
}