package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.User;

import java.util.ArrayList;
import java.util.List;

public class Lobby {
    final private int nPlayers;
    private List<User> users = new ArrayList<>();

    public int nPlayers() {
        return nPlayers;
    }

    public List<User> users() {
        return users;
    }

    public Lobby(int nPlayers, User firstPlayer) {
        this.nPlayers = nPlayers;
        this.users.add(firstPlayer);
    }
}
