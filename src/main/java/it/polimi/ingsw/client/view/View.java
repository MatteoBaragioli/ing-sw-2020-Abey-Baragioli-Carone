package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.network.CommunicationChannel;
import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.server.model.*;

import java.util.List;

public interface View {
    int askBox(List<Box> boxes);
    int askCards(List<GodCard> cards);
    boolean askConfirmation();
    String askIp();
    void askMatchType(ClientController clientController, CommunicationChannel communicationChannel);
    int askPort();
    void askUserName(ClientController clientController, CommunicationChannel communicationChannel);
    int askWorker(List<Worker> workers);
    void prepareAdditionalCommunication(CommunicationProtocol key);
    void updateMap(List<Box> boxes);
    void setPlayersInfo(List<Player> players);
}
