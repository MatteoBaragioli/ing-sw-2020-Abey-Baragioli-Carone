package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.network.objects.BoxProxy;
import it.polimi.ingsw.network.objects.GodCardProxy;
import it.polimi.ingsw.network.objects.PlayerProxy;
import it.polimi.ingsw.network.CommunicationChannel;

import java.lang.reflect.Type;
import java.util.List;

import static it.polimi.ingsw.network.CommunicationProtocol.*;

public class ClientController {

    public void manageListOfCards(CommunicationChannel communicationChannel, View view) throws ChannelClosedException {
        List<GodCardProxy> cards;
        int index;

        String content = communicationChannel.nextMessage();
        CommunicationProtocol key = communicationChannel.getKey(content);
        if (key == CARD || key == DECK) {
            Type listType = new TypeToken<List<GodCardProxy>>() {}.getType();
            cards = new Gson().fromJson(communicationChannel.getContent(content), listType);
            index = view.askCards(cards);
            if (index == -1)
                communicationChannel.writeKeyWord(QUIT);
            else
                communicationChannel.writeChoiceFromList(key, index);
        }
    }

    public void manageMapAsListOfBoxes(CommunicationChannel communicationChannel, View view) throws ChannelClosedException {
        List<BoxProxy> boxes;
        String message = communicationChannel.nextMessage();

        if (communicationChannel.getKey(message) == MAP) {
            Type listType = new TypeToken<List<BoxProxy>>() {}.getType();
            boxes = new Gson().fromJson(communicationChannel.getContent(message), listType);
            view.updateMap(boxes);
            communicationChannel.writeKeyWord(RECEIVED);
        }
        else
            communicationChannel.writeKeyWord(INVALID);
    }

    public void manageListOfPositions(CommunicationChannel communicationChannel, View view) throws ChannelClosedException {
        List<int[]> positions;
        String message = communicationChannel.nextMessage();

        CommunicationProtocol key = communicationChannel.getKey(message);
        if (key == BUILD || key == DESTINATION || key == REMOVAL || key == STARTPOSITION || key == WORKER) {
            Type listType = new TypeToken<List<int[]>>() {}.getType();
            positions = new Gson().fromJson(message, listType);
            int index;

            if (key == WORKER)
                index = view.askWorker(positions);
            else
                index = view.askPosition(positions);

            if (index == -1)
                communicationChannel.writeKeyWord(QUIT);
            else
                communicationChannel.writeChoiceFromList(key, index);
        }
        else
            communicationChannel.writeKeyWord(INVALID);
    }

    public void manageMyPlayer(CommunicationChannel communicationChannel, View view) throws ChannelClosedException {
        PlayerProxy player;
        String message = communicationChannel.nextMessage();

        if (communicationChannel.getKey(message) == MYPLAYER) {
            Type listType = new TypeToken<List<PlayerProxy>>() {}.getType();
            player = new Gson().fromJson(communicationChannel.getContent(message), listType);
            view.setMyPlayer(player);
            communicationChannel.writeKeyWord(RECEIVED);
        }
        else
            communicationChannel.writeKeyWord(INVALID);
    }

    public void manageListOfOpponents(CommunicationChannel communicationChannel, View view) throws ChannelClosedException {
        List<PlayerProxy> players;
        String message = communicationChannel.nextMessage();

        if (communicationChannel.getKey(message) == OPPONENTS) {
            Type listType = new TypeToken<List<PlayerProxy>>() {}.getType();
            players = new Gson().fromJson(communicationChannel.getContent(message), listType);
            view.setOpponentsInfo(players);
            communicationChannel.writeKeyWord(RECEIVED);
        }
        else
            communicationChannel.writeKeyWord(INVALID);
    }

    public void manageConfirmation(CommunicationChannel communicationChannel, View view) throws ChannelClosedException {
        String message = communicationChannel.nextMessage();
        CommunicationProtocol key = communicationChannel.getKey(message);

        if (key == GODPOWER || key == UNDO) {
            Type type = new TypeToken<Boolean>() {}.getType();
            communicationChannel.writeConfirmation(key, new Gson().toJson(view.askConfirmation(), type));
        }
        else
            communicationChannel.writeKeyWord(INVALID);
    }

    public void manageMatchStart(CommunicationChannel communicationChannel, View view) {
        //setting of players
    }

    public void waitForPlayers(CommunicationChannel communicationChannel, View view) throws ChannelClosedException {
        String message = communicationChannel.nextMessage();
        if (communicationChannel.getKey(message) == WAITFORPLAYERS) {
            //notify view
            communicationChannel.writeKeyWord(RECEIVED);
        }
    }
}
