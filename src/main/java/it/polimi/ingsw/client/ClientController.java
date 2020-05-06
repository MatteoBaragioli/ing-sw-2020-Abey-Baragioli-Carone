package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.socket.CommunicationChannel;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class ClientController extends Thread {

    final private CommunicationChannel communicationChannel;
    final private View view;

    public ClientController(CommunicationChannel communicationChannel, View view) {
        this.communicationChannel = communicationChannel;
        this.view = view;
    }

    public CommunicationChannel communicationChannel() {
        return communicationChannel;
    }

    public View view() {
        return view;
    }

    public void manageListOfCards(CommunicationChannel communicationChannel, View view) throws IOException {

        List<GodCard> cards;
        int index;
        communicationChannel().write("ok");

        String delivery = communicationChannel().read();

        Type listType = new TypeToken<List<GodCard>>() {}.getType();
        cards = new Gson().fromJson(delivery, listType);
        index= view().askCards(cards);
        communicationChannel().write(index);
    }

    public void manageListOfBoxes(CommunicationChannel communicationChannel, View view) throws IOException {
        List<Box> boxes;
        String message;
        int index;

        communicationChannel().write("ok");

        message = communicationChannel().read();

        Type listType = new TypeToken<List<Box>>() {}.getType();
        boxes = new Gson().fromJson(message, listType);
        index=view.askBox(boxes);
        communicationChannel().write(index);

    }

    public void manageMapAsListOfBoxes(CommunicationChannel communicationChannel, View view) throws IOException {
        List<Box> boxes;
        String message;
        int index;

        communicationChannel().write("ok");

        message = communicationChannel().read();

        Type listType = new TypeToken<List<Box>>() {}.getType();
        boxes= new Gson().fromJson(message, listType);

        view.updateMap(boxes);
        communicationChannel().write("ok");
    }

    public void manageListOfWorkers(CommunicationChannel communicationChannel, View view) throws IOException {
        List<Worker> workers;
        String message;
        int index;

        communicationChannel().write("ok");

        message = communicationChannel().read();

        Type listType = new TypeToken<List<Worker>>() {}.getType();
        workers= new Gson().fromJson(message, listType);
        index=view.askWorker(workers);
        communicationChannel().write(index);
    }

    public void manageConfirmation(CommunicationChannel communicationChannel, View view){
        boolean confirmation;
        confirmation = view.askConfirmation();
        communicationChannel().write(confirmation);
    }

    public void run() {
        while (!communicationChannel().isClosed()) {
            String message = null;
            try {
                message = communicationChannel().read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            view.prepareAdditionalCommunication(message);
            try {
                switch (message) {
                    case ("deck"):
                    case("choose your game card"):
                    case("card"):
                        manageListOfCards(communicationChannel(), view);
                        break;
                    case("possible destinations list of boxes"):
                    case("possible builds list of boxes"):
                    case("setup list of boxes"):
                        manageListOfBoxes(communicationChannel(), view);
                        break;
                    case("map as list of boxes"):
                        manageMapAsListOfBoxes(communicationChannel(), view);
                        break;
                    case("list of movable workers"):
                        manageListOfWorkers(communicationChannel(), view);
                        break;
                    case("undo"):
                    case("use power"):
                        manageConfirmation(communicationChannel(), view);
                        break;
                    default:
                        communicationChannel().write("WTF?!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
