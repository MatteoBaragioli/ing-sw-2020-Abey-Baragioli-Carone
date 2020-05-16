package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.server.model.Box;
import it.polimi.ingsw.server.model.GodCard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Worker;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

public class Gui extends Application implements View {

    private Stage stage;
    private GuiApp gui;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        gui = new GuiApp();
        gui.start(stage);
    }

    @Override
    public int askBox(List<Box> boxes) {
        return 0;
    }

    @Override
    public int askWorker(List<Worker> workers) {
        return 0;
    }

    @Override
    public int askCards(List<GodCard> cards) {
        return 0;
    }

    @Override
    public boolean askConfirmation() {
        return false;
    }

    @Override
    public void prepareAdditionalCommunication(CommunicationProtocol key) {

    }

    @Override
    public void updateMap(List<Box> boxes) {

    }

    @Override
    public void setPlayersInfo(List<Player> players) {

    }

    @Override
    public String askIp() {
        gui.menuScene().askIp();
        return "";
    }

    @Override
    public int askMatchType() {
        return 0;
    }

    @Override
    public int askPort() {
        return 1234;
    }

    @Override
    public String askUserName(){
        return gui.menuScene().askNickname();
    }


}
