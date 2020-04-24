package it.polimi.ingsw.client;

public class ClientController {

    public static void main() {
        ClientController clientController = new ClientController();
        SocketClient socketClient = new SocketClient("127.0.0.1", 4321);
        socketClient.start();
        socketClient.write("Hi");
        while (!socketClient.closed()) {}
    }
}
