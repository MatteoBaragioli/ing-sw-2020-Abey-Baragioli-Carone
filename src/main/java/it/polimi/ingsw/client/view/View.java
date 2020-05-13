package it.polimi.ingsw.client.view;

import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.server.model.*;


import java.util.List;

public interface View {
    int askBox(List<Box> boxes);
    int askCards(List<GodCard> cards);
    boolean askConfirmation();
    String askIp();
    int askMatchType();
    int askPort();
    String askUserName();
    int askWorker(List<Worker> workers);
    void prepareAdditionalCommunication(CommunicationProtocol key);
    void updateMap(List<Box> boxes);
    void setPlayersInfo(List<Player> players);
}
