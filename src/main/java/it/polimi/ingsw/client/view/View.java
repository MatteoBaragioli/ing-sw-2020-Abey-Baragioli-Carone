package it.polimi.ingsw.client.view;

import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.network.objects.BoxProxy;
import it.polimi.ingsw.network.objects.GodCardProxy;
import it.polimi.ingsw.network.objects.PlayerProxy;

import java.io.IOException;
import java.util.List;

public interface View {
    int askPosition(List<int[]> positions);
    int askCards(List<GodCardProxy> cards);
    int[] askDeck(List<GodCardProxy> cards);
    int askConfirmation(CommunicationProtocol key);
    String askIp();
    int askMatchType();
    int askPort();
    String askUserName() throws IOException;
    int askWorker(List<int[]> workers);
    void prepareAdditionalCommunication(CommunicationProtocol key);
    void updateMap(List<BoxProxy> boxes);
    void setMyPlayer(PlayerProxy player);
    void setOpponentsInfo(List<PlayerProxy> players);
    void connectionLost();
    void connectionFailed(String host);
    void startMatch();
    void setCurrentPlayer(PlayerProxy player);
}
