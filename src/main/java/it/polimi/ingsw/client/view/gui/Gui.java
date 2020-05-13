package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.server.model.Box;
import it.polimi.ingsw.server.model.GodCard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Worker;

import java.util.List;

public class Gui implements View {

    private String[] args;

    public Gui(String[] args) {
        this.args = args;
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
        return null;
    }

    @Override
    public int askMatchType() {
        return 0;
    }

    @Override
    public int askPort() {
        return 0;
    }

    @Override
    public String askUserName() {
        return null;
    }

    public static void main(String[] args) {

    }
}
