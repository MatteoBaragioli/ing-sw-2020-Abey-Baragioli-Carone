package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.ProtoCard;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ClientController {

    public static void main() {
        ClientController clientController = new ClientController();
        String message = new String();
        View view = new Cli();
        SocketClient socketClient = new SocketClient("127.0.0.1", 4321);
        socketClient.start();
        socketClient.write("Hi");
        while (!socketClient.isClosed()) {
            message = socketClient.read();
 //toDo finire switch
            switch (message) {
                case ("deck"):
                    manageListOfCards(socketClient, view);

                case("choose your game card"):
                    //dire all'utente che deve scegliere la sua carta
                    manageListOfCards(socketClient, view);

                case("card"):
                    //per visualizzare una carta
                    manageListOfCards(socketClient, view);

                case("possible destinations list of boxes"):
                    //toDo dire all'utente che deve scegliere la destinazione della move
                    manageListOfBoxes(socketClient, view);

                case("possible builds list of boxes"):
                    //toDo dire all'utente che deve scegliere la box per la build
                    manageListOfBoxes(socketClient, view);

                case("setup list of boxes"):
                    //todo dire all'utente di scegliere le posizioni dei worker per il setUp
                    manageListOfBoxes(socketClient, view);

                case("map as list of boxes"):
                    //per visualizzare la mappa
                    manageMapAsListOfBoxes(socketClient, view);

                case("list of movable workers"):
                    //dire all'utente di scegliere un worker
                    manageListOfWorkers(socketClient, view);

                case("undo"):
                    //chiedere all'utente se vuole fare undo

                    manageConfirmation(socketClient, view);

                case("use power"):
                    //chiedere all'utente se vuole usare il suo potere
                    manageConfirmation(socketClient, view);
            }

        }
    }


    public static void manageListOfCards(SocketClient socketClient, View view){

        List<GodCard> cards=new ArrayList<>();
        String message;
        int index;

        socketClient.write("ok");

        message=socketClient.read();

        Type listType = new TypeToken<List<GodCard>>() {}.getType();
        cards = new Gson().fromJson(message, listType);
        index=view.askCards(cards);
        socketClient.write(index);

    }

    public static void manageListOfBoxes(SocketClient socketClient, View view){
        List<Box> boxes=new ArrayList<>();
        String message;
        int index;

        socketClient.write("ok");

        message=socketClient.read();

        Type listType = new TypeToken<List<Box>>() {}.getType();
        boxes= new Gson().fromJson(message, listType);
        index=view.askBox(boxes);
        socketClient.write(index);

    }

    public static void manageMapAsListOfBoxes(SocketClient socketClient, View view){
        List<Box> boxes=new ArrayList<>();
        String message;
        int index;

        socketClient.write("ok");

        message=socketClient.read();

        socketClient.write("ok");

        Type listType = new TypeToken<List<Box>>() {}.getType();
        boxes= new Gson().fromJson(message, listType);


    }

    public static void manageListOfWorkers(SocketClient socketClient, View view){
        List<Worker> workers=new ArrayList<>();
        String message;
        int index;

        socketClient.write("ok");

        message=socketClient.read();

        Type listType = new TypeToken<List<Box>>() {}.getType();
        workers= new Gson().fromJson(message, listType);
        index=view.askWorker(workers);
        socketClient.write(index);

    }

    public static void manageConfirmation(SocketClient socketClient, View view){
        boolean confirmation;
        confirmation=view.askConfirmation("string");
        socketClient.write(confirmation);
    }

    public void manageUndo(SocketClient socketClient, View view){
        //chiedere all'utente se vuole fare undo
        boolean confirmation;
        confirmation=view.askConfirmation("undo");

    }

    public void manageUsePower(SocketClient socketClient, View view){
        //chiedere all'utente se vuole usare il suo potere
        boolean confirmation;
        confirmation=view.askConfirmation("use power");

    }



}