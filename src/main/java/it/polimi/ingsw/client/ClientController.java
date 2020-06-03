package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.network.objects.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import static it.polimi.ingsw.network.CommunicationProtocol.*;

public class ClientController {

    public void manageDeck(CommunicationChannel communicationChannel, View view) throws ChannelClosedException {
        List<GodCardProxy> cards;
        int[] index;
        String content = communicationChannel.popMessage();
        Type listType = new TypeToken<List<GodCardProxy>>() {}.getType();
        cards = new Gson().fromJson(communicationChannel.getContent(content), listType);
        index = view.askDeck(cards);

        if (index[0] == -1)
            communicationChannel.writeKeyWord(QUIT);
        else
            communicationChannel.writeChoicesFromList(DECK, index);
    }

    public void manageListOfCards(CommunicationChannel communicationChannel, View view) throws ChannelClosedException {
        List<GodCardProxy> cards;
        int index;
        String content = communicationChannel.popMessage();
        Type listType = new TypeToken<List<GodCardProxy>>() {}.getType();
        cards = new Gson().fromJson(communicationChannel.getContent(content), listType);
        index = view.askCards(cards);
        if (index == -1)
            communicationChannel.writeKeyWord(QUIT);
        else
            communicationChannel.writeChoiceFromList(CARD, index);
    }

    public void manageMapAsListOfBoxes(CommunicationChannel communicationChannel, View view) throws ChannelClosedException {
        List<BoxProxy> boxes;
        String message = communicationChannel.popMessage();

        Type listType = new TypeToken<List<BoxProxy>>() {}.getType();
        boxes = new Gson().fromJson(communicationChannel.getContent(message), listType);
        view.updateMap(boxes);
        communicationChannel.writeKeyWord(RECEIVED);
    }

    public void manageListOfPositions(CommunicationProtocol key, CommunicationChannel communicationChannel, View view) throws ChannelClosedException {
        List<int[]> positions;
        String message = communicationChannel.popMessage();

        Type listType = new TypeToken<List<int[]>>() {}.getType();
        positions = new Gson().fromJson(communicationChannel.getContent(message), listType);

        int index;

        if (key == WORKER)
            index = view.askWorker(positions);
        else
            index = view.askPosition(positions);

        if (index == -1)
            communicationChannel.writeKeyWord(QUIT);
        else {
            communicationChannel.writeChoiceFromList(key, index);
        }
    }

    public void managePlayer(CommunicationProtocol key, CommunicationChannel communicationChannel, View view) throws ChannelClosedException {
        PlayerProxy player;
        String message = communicationChannel.popMessage();
        Type listType = new TypeToken<PlayerProxy>() {}.getType();
        player = new Gson().fromJson(communicationChannel.getContent(message), listType);
        switch (key) {
            case MY_PLAYER:
                view.setMyPlayer(player);
                break;
            case CURRENT_PLAYER:
                view.setCurrentPlayer(player);
                break;
            case LOSER:
                break;
        }
        communicationChannel.writeKeyWord(RECEIVED);
    }

    public void manageListOfOpponents(CommunicationChannel communicationChannel, View view) throws ChannelClosedException {
        List<PlayerProxy> players;
        String message = communicationChannel.popMessage();
        Type listType = new TypeToken<List<PlayerProxy>>() {}.getType();
        players = new Gson().fromJson(communicationChannel.getContent(message), listType);
        view.setOpponentsInfo(players);
        communicationChannel.writeKeyWord(RECEIVED);
    }

    public void manageConfirmation(CommunicationProtocol key, CommunicationChannel communicationChannel, View view) throws ChannelClosedException {
        communicationChannel.writeConfirmation(key, view.askConfirmation(key));
    }

    public void manageMatchStart(CommunicationChannel communicationChannel, View view) {
        //setting of players
    }

    public void waitForPlayers(CommunicationChannel communicationChannel, View view) throws ChannelClosedException {
        communicationChannel.writeKeyWord(RECEIVED);
    }

    public void manageCountDown(CommunicationChannel communicationChannel, View view) throws ChannelClosedException {
        view.manageCountdown();
        communicationChannel.popMessage();
    }

    public void manageMatchStory(CommunicationChannel communicationChannel, View view) throws ChannelClosedException {
        String message = communicationChannel.popMessage();
        view.tellStory(communicationChannel.getContent(message));
        communicationChannel.writeKeyWord(RECEIVED);
    }
}
