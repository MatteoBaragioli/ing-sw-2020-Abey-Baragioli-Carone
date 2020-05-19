package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.network.objects.Cell;
import it.polimi.ingsw.network.objects.God;
import it.polimi.ingsw.network.objects.PlayerCard;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.network.CommunicationChannel;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import static it.polimi.ingsw.network.CommunicationProtocol.*;

public class ClientController {

    public void manageListOfCards(CommunicationChannel communicationChannel, View view) throws IOException {
        List<God> cards;
        int index;
        communicationChannel.writeKeyWord(RECEIVED);

        String delivery = communicationChannel.read();

        Type listType = new TypeToken<List<God>>() {}.getType();
        cards = new Gson().fromJson(delivery, listType);
        index= view.askCards(cards);
        communicationChannel.writeNumber(index);
    }

    public void manageMapAsListOfBoxes(CommunicationChannel communicationChannel, View view) throws IOException {
        List<Cell> boxes;
        String message;

        communicationChannel.writeKeyWord(RECEIVED);

        message = communicationChannel.read();

        Type listType = new TypeToken<List<Cell>>() {}.getType();
        boxes= new Gson().fromJson(message, listType);

        view.updateMap(boxes);
        communicationChannel.writeKeyWord(RECEIVED);
    }

    public void manageListOfBoxes(CommunicationChannel communicationChannel, View view) throws IOException {
        List<int[]> boxes;
        String message;
        int index;

        communicationChannel.writeKeyWord(RECEIVED);

        message = communicationChannel.read();

        Type listType = new TypeToken<List<int[]>>() {}.getType();
        boxes = new Gson().fromJson(message, listType);
        index=view.askBox(boxes);
        communicationChannel.writeNumber(index);
    }

    public void manageListOfWorkers(CommunicationChannel communicationChannel, View view) throws IOException {
        List<int[]> workers;
        String message;
        int index;

        communicationChannel.writeKeyWord(RECEIVED);

        message = communicationChannel.read();

        Type listType = new TypeToken<List<int[]>>() {}.getType();
        workers= new Gson().fromJson(message, listType);
        index=view.askWorker(workers);
        communicationChannel.writeNumber(index);
    }

    public void manageMyPlayer(CommunicationChannel communicationChannel, View view) throws IOException {
        PlayerCard player;
        String message;

        communicationChannel.writeKeyWord(RECEIVED);

        message = communicationChannel.read();

        Type listType = new TypeToken<List<PlayerCard>>() {}.getType();
        player = new Gson().fromJson(message, listType);
        view.setMyPlayer(player);
        System.out.println(message);
        communicationChannel.writeKeyWord(RECEIVED);
    }

    public void manageListOfOpponents(CommunicationChannel communicationChannel, View view) throws IOException {
        List<PlayerCard> players;
        String message;

        communicationChannel.writeKeyWord(RECEIVED);

        message = communicationChannel.read();

        Type listType = new TypeToken<List<PlayerCard>>() {}.getType();
        players = new Gson().fromJson(message, listType);
        view.setOpponentsInfo(players);
        communicationChannel.writeKeyWord(RECEIVED);
    }

    public void manageConfirmation(CommunicationChannel communicationChannel, View view){
        boolean confirmation;
        confirmation = view.askConfirmation();
        communicationChannel.writeBoolean(confirmation);
    }

    public void writeUsername(CommunicationChannel communicationChannel, String userName) {
        communicationChannel.write(userName);
    }

    public void askMatchType(CommunicationChannel communicationChannel, int matchType) {
        communicationChannel.writeNumber(matchType);
    }

    public void manageMatchStart(CommunicationChannel communicationChannel, View view) {
        //setting of players
    }

    public void waitForPlayers(CommunicationChannel communicationChannel, View view) {

    }
}
