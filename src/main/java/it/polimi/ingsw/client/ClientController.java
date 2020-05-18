package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.network.CommunicationChannel;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import static it.polimi.ingsw.network.CommunicationProtocol.*;

public class ClientController {
    public void manageListOfCards(CommunicationChannel communicationChannel, View view) throws IOException {

        List<GodCard> cards;
        int index;
        communicationChannel.writeKeyWord(RECEIVED);

        String delivery = communicationChannel.read();

        Type listType = new TypeToken<List<GodCard>>() {}.getType();
        cards = new Gson().fromJson(delivery, listType);
        index= view.askCards(cards);
        communicationChannel.writeNumber(index);
    }

    public void manageListOfBoxes(CommunicationChannel communicationChannel, View view) throws IOException {
        List<Box> boxes;
        String message;
        int index;

        communicationChannel.writeKeyWord(RECEIVED);

        message = communicationChannel.read();

        Type listType = new TypeToken<List<Box>>() {}.getType();
        boxes = new Gson().fromJson(message, listType);
        index=view.askBox(boxes);
        communicationChannel.writeNumber(index);

    }

    public void manageMapAsListOfBoxes(CommunicationChannel communicationChannel, View view) throws IOException {
        List<Box> boxes;
        String message;

        communicationChannel.writeKeyWord(RECEIVED);

        message = communicationChannel.read();

        Type listType = new TypeToken<List<Box>>() {}.getType();
        boxes= new Gson().fromJson(message, listType);

        view.updateMap(boxes);
        communicationChannel.writeKeyWord(RECEIVED);
    }

    public void manageListOfWorkers(CommunicationChannel communicationChannel, View view) throws IOException {
        List<Worker> workers;
        String message;
        int index;

        communicationChannel.writeKeyWord(RECEIVED);

        message = communicationChannel.read();

        Type listType = new TypeToken<List<Worker>>() {}.getType();
        workers= new Gson().fromJson(message, listType);
        index=view.askWorker(workers);
        communicationChannel.writeNumber(index);
    }

    public void manageListOfPlayers(CommunicationChannel communicationChannel, View view) throws IOException {
        List<Player> players;
        String message;

        communicationChannel.writeKeyWord(RECEIVED);

        message = communicationChannel.read();

        Type listType = new TypeToken<List<Player>>() {}.getType();
        players = new Gson().fromJson(message, listType);
        view.setPlayersInfo(players);
        communicationChannel.writeKeyWord(RECEIVED);
    }

    public void manageConfirmation(CommunicationChannel communicationChannel, View view){
        boolean confirmation;
        confirmation = view.askConfirmation();
        communicationChannel.writeBoolean(confirmation);
    }

    public void askUsername(CommunicationChannel communicationChannel, View view) {
        communicationChannel.write(view.askUserName());
    }

    public void askMatchType(CommunicationChannel communicationChannel, View view) {
        communicationChannel.writeNumber(view.askMatchType());
    }

    public void manageMatchStart(CommunicationChannel communicationChannel, View view) {
        //setting of players
    }

    public void waitForPlayers(CommunicationChannel communicationChannel, View view) {

    }
}
