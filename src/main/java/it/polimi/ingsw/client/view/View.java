package it.polimi.ingsw.client.view;

import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.network.exceptions.TimeOutException;
import it.polimi.ingsw.network.objects.BoxProxy;
import it.polimi.ingsw.network.objects.GodCardProxy;
import it.polimi.ingsw.network.objects.PlayerProxy;

import java.io.IOException;
import java.util.List;

public interface View {
    int askPosition(List<int[]> positions) throws TimeOutException;
    int askCards(List<GodCardProxy> cards) throws TimeOutException;
    int[] askDeck(List<GodCardProxy> cards) throws TimeOutException;
    int askConfirmation(CommunicationProtocol key) throws TimeOutException;
    String askIp();
    int askMatchType();
    int askPort();
    String askUserName(CommunicationProtocol key) throws IOException;
    int askWorker(List<int[]> workers)throws TimeOutException;
    void prepareAdditionalCommunication(CommunicationProtocol key);
    void updateMap(List<BoxProxy> boxes);
    void setMyPlayer(PlayerProxy player);
    void setOpponentsInfo(List<PlayerProxy> players);
    void connectionLost();
    void connectionFailed(String host);
    void startMatch();
    void setCurrentPlayer(PlayerProxy player);
    void tellStory(List<String> events);
    void setWinner(PlayerProxy player);
    void setLoser(PlayerProxy player);
    void serverDisconnected();
    void timeOut();
}
