package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.Match;
import it.polimi.ingsw.server.model.User;
import it.polimi.ingsw.server.socket.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {
    List<Match> matches = new ArrayList<>();
    List<User> activeUsers = new ArrayList<>();
    List<Lobby> lobbies = new ArrayList<>();
    Map<User, Match> userToMatch = new HashMap<>();

    public void start() {
        Server server = new Server(1024);
        server.start();
    }
}
