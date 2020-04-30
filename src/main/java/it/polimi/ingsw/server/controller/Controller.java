package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.Match;
import it.polimi.ingsw.server.model.User;
import it.polimi.ingsw.server.socket.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {
    List<Match> matches = new ArrayList<>();
    List<User> activeUsers = new ArrayList<>();
    List<Lobby> lobbies = new ArrayList<>();
    Map<User, Match> userToMatch = new HashMap<>();
    final private int INVALIDPORT = -1;
    final private int STANDARDPORT = 1024;

    public void start() {
        System.out.println("Write port");
        int port = selectPort();
        if (port == INVALIDPORT)
            port = STANDARDPORT;
        Server server = new Server(port);
        server.start();
    }

    /**
     * This method asks which port the server should use
     * @return
     */
    private int selectPort() {
        System.out.println("Write port:");
        try {
            int port = System.in.read();
            return port;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Non funziona scanf");
        }
        return INVALIDPORT;
    }
}
