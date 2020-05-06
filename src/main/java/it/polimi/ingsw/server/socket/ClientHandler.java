package it.polimi.ingsw.server.socket;

import it.polimi.ingsw.server.controller.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    final private Controller controller;
    final private Socket socket;

    public ClientHandler(Controller controller, Socket socket) {
        this.controller = controller;
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            CommunicationChannel communicationChannel = new CommunicationChannel(in, out);
            String message = communicationChannel.read();
            if (message.equals("HI"))
                new UserManager(controller, communicationChannel).run();
            while (!communicationChannel.isClosed()) {
                communicationChannel.read();
            }
            // Chiudo gli stream e il socket
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
