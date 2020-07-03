package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.network.CommunicationChannel;
import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.network.exceptions.TimeOutException;
import it.polimi.ingsw.network.objects.BoxProxy;
import it.polimi.ingsw.network.objects.GodCardProxy;
import it.polimi.ingsw.network.objects.PlayerProxy;

import java.lang.reflect.Type;
import java.util.List;

import static it.polimi.ingsw.network.CommunicationProtocol.*;

class ClientController {

    /**
     * This method asks the user through the view which cards
     * should be used in the upcoming match.
     * @param communicationChannel Communication Channel
     * @param view User Interface
     * @throws ChannelClosedException If the connection is lost
     */
    void manageDeck(CommunicationChannel communicationChannel, View view) throws ChannelClosedException {
        List<GodCardProxy> cards;
        int[] index;
        String content = communicationChannel.popMessage();
        Type listType = new TypeToken<List<GodCardProxy>>() {}.getType();
        cards = new Gson().fromJson(communicationChannel.getContent(content), listType);
        try {
            index = view.askDeck(cards);
            if (index[0] == -1)
                communicationChannel.writeKeyWord(QUIT);
            else
                communicationChannel.writeChoicesFromList(DECK, index);
        } catch (TimeOutException e) {
            communicationChannel.writeKeyWord(TIMEOUT);
        }
    }

    /**
     * This method asks the user to choose one card.
     * @param communicationChannel Communication Channel
     * @param view User Interface
     * @throws ChannelClosedException If the connection is lost
     */
    void manageListOfCards(CommunicationChannel communicationChannel, View view) throws ChannelClosedException {
        List<GodCardProxy> cards;
        int index;
        String content = communicationChannel.popMessage();
        Type listType = new TypeToken<List<GodCardProxy>>() {}.getType();
        cards = new Gson().fromJson(communicationChannel.getContent(content), listType);
        try {
            index = view.askCards(cards );
            if (index == -1)
                communicationChannel.writeKeyWord(QUIT);
            else
                communicationChannel.writeChoiceFromList(CARD, index);

        } catch (TimeOutException e) {
            communicationChannel.writeKeyWord(TIMEOUT);
        }
    }

    /**
     * This method notifies the view of the map update
     * @param communicationChannel Communication Channel
     * @param view User Interface
     * @throws ChannelClosedException If the connection is lost
     */
    void manageMapAsListOfBoxes(CommunicationChannel communicationChannel, View view) throws ChannelClosedException {
        List<BoxProxy> boxes;
        String message = communicationChannel.popMessage();

        Type listType = new TypeToken<List<BoxProxy>>() {}.getType();
        boxes = new Gson().fromJson(communicationChannel.getContent(message), listType);
        view.updateMap(boxes);
    }

    /**
     * This method asks the user through the view
     * to choose one position from a list.
     * @param key Position request type
     * @param communicationChannel Communication Channel
     * @param view User Interface
     * @throws ChannelClosedException If the connection is lost
     */
    void manageListOfPositions(CommunicationProtocol key, CommunicationChannel communicationChannel, View view) throws ChannelClosedException {
        List<int[]> positions;
        String message = communicationChannel.popMessage();

        Type listType = new TypeToken<List<int[]>>() {}.getType();
        positions = new Gson().fromJson(communicationChannel.getContent(message), listType);

        int index;

        try {
            if (key == WORKER)
                index = view.askWorker(positions);
            else
                index = view.askPosition(positions);
            if (index == -1)
                communicationChannel.writeKeyWord(QUIT);
            else
                communicationChannel.writeChoiceFromList(key, index);
        } catch (TimeOutException e) {
            communicationChannel.writeKeyWord(TIMEOUT);
        }
    }

    /**
     * This method notifies the view of an update regarding one player
     * @param key Player update type
     * @param communicationChannel Communication Channel
     * @param view User Interface
     * @throws ChannelClosedException If the connection is lost
     */
    void managePlayer(CommunicationProtocol key, CommunicationChannel communicationChannel, View view) throws ChannelClosedException {
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
            case WINNER:
                view.setWinner(player);
                break;
            case LOSER:
                view.setLoser(player);
                break;
            default:
        }
    }

    /**
     * This method notifies the view of an update regarding the opponent players
     * @param communicationChannel Communication Channel
     * @param view User Interface
     * @throws ChannelClosedException If the connection is lost
     */
    void manageListOfOpponents(CommunicationChannel communicationChannel, View view) throws ChannelClosedException {
        List<PlayerProxy> players;
        String message = communicationChannel.popMessage();
        Type listType = new TypeToken<List<PlayerProxy>>() {}.getType();
        players = new Gson().fromJson(communicationChannel.getContent(message), listType);
        view.setOpponentsInfo(players);
    }

    /**
     * This method asks the user through the view
     * to answer a binary question.
     * @param key Position request type
     * @param communicationChannel Communication Channel
     * @param view User Interface
     * @throws ChannelClosedException If the connection is lost
     */
    void manageConfirmation(CommunicationProtocol key, CommunicationChannel communicationChannel, View view) throws ChannelClosedException {
        try {
            communicationChannel.writeConfirmation(key, view.askConfirmation(key));
        } catch (TimeOutException e) {
            communicationChannel.writeKeyWord(TIMEOUT);
        }
    }

    /**
     * This method updates the latest in-game events.
     * @param communicationChannel Communication Channel
     * @param view User Interface
     * @throws ChannelClosedException If the connection is lost
     */
    void manageMatchStory(CommunicationChannel communicationChannel, View view) throws ChannelClosedException {
        String message = communicationChannel.popMessage();
        Type type = new TypeToken<List<String>>() {}.getType();
        view.tellStory(new Gson().fromJson(communicationChannel.getContent(message), type));
    }
}
