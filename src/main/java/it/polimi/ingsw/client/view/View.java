package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.network.CommunicationChannel;
import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.network.objects.Cell;
import it.polimi.ingsw.network.objects.God;
import it.polimi.ingsw.network.objects.PlayerCard;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.List;

public interface View {
    int askBox(List<int[]> boxes);
    int askCards(List<God> cards);
    boolean askConfirmation();
    String askIp();
    void askMatchType(ClientController clientController, CommunicationChannel communicationChannel);
    int askPort();
    void askUserName(ClientController clientController, CommunicationChannel communicationChannel) throws IOException;
    String askUserName();
    int askWorker(List<int[]> workers);
    void prepareAdditionalCommunication(CommunicationProtocol key);
    void updateMap(List<Cell> boxes);
    void setMyPlayer(PlayerCard player);
    void setOpponentsInfo(List<PlayerCard> players);
    void connectionLost();
    void unknownHost(String host, UnknownHostException e);
    void connectionRefused(String host, ConnectException e);
}
