package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.server.model.*;


import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientController {

    private SocketClient setupSocket() {
        System.out.println("Dove mi collego?\n Inserire IP:");
        Scanner scanner = new Scanner(System.in);
        String ip = scanner.next();
        System.out.println("Inserire porta:");
        int port = scanner.nextInt();
        SocketClient socketClient = new SocketClient(ip, port);
        return socketClient;
    }

    public View askView() {
        System.out.println("Come te lo esco il gioco?\n 1)CLI o 2)GUI");
        try {
            int answer = System.in.read();
            if (answer == 1)
                return new Cli();
            if (answer == 2)
                return new Gui();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Non funziona scanf");
            return null;
        }
        return new Gui();
    }

    public void manageListOfCards(SocketClient socketClient, View view){

        List<GodCard> cards;
        int index;
        socketClient.write("ok");

        String delivery =socketClient.read();

        Type listType = new TypeToken<List<GodCard>>() {}.getType();
        cards = new Gson().fromJson(delivery, listType);
        index=view.askCards(cards);
        socketClient.write(index);
    }

    public void manageListOfBoxes(SocketClient socketClient, View view){
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

    public void manageMapAsListOfBoxes(SocketClient socketClient, View view){
        List<Box> boxes=new ArrayList<>();
        String message;
        int index;

        socketClient.write("ok");

        message=socketClient.read();

        Type listType = new TypeToken<List<Box>>() {}.getType();
        boxes= new Gson().fromJson(message, listType);

        view.updateMap(boxes);
        socketClient.write("ok");
    }

    public void manageListOfWorkers(SocketClient socketClient, View view){
        List<Worker> workers=new ArrayList<>();
        String message;
        int index;

        socketClient.write("ok");

        message=socketClient.read();

        Type listType = new TypeToken<List<Worker>>() {}.getType();
        workers= new Gson().fromJson(message, listType);
        index=view.askWorker(workers);
        socketClient.write(index);
    }

    public void manageConfirmation(SocketClient socketClient, View view){
        boolean confirmation;
        confirmation=view.askConfirmation();
        socketClient.write(confirmation);
    }

    public void start() {
        String message;
        View view = askView();
        SocketClient socketClient = setupSocket();
        socketClient.start();
        socketClient.write("Hi");
        while (!socketClient.isClosed()) {
            message = socketClient.read();
            view.prepareAdditionalCommunication(message);
            switch (message) {
                case ("deck"):
                case("choose your game card"):
                case("card"):
                    manageListOfCards(socketClient, view);
                    break;
                case("possible destinations list of boxes"):
                case("possible builds list of boxes"):
                case("setup list of boxes"):
                    manageListOfBoxes(socketClient, view);
                    break;
                case("map as list of boxes"):
                    manageMapAsListOfBoxes(socketClient, view);
                    break;
                case("list of movable workers"):
                    manageListOfWorkers(socketClient, view);
                    break;
                case("undo"):
                case("use power"):
                    manageConfirmation(socketClient, view);
                    break;
                default:
                    socketClient.write("WTF?!");
            }
        }
    }

    public static void main(String[] args) {
        ClientController clientController = new ClientController();
        clientController.start();
    }
}