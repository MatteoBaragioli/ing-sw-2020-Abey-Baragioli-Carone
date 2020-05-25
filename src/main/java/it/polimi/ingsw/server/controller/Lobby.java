package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.Match;
import it.polimi.ingsw.server.model.User;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

    final private int nPlayers;
    private List<User> users = new ArrayList<>();
    private boolean readyToGo=false;
    private Match match = null;

    public Lobby(User firstPlayer, int nPlayers) {
        users.add(firstPlayer);
        this.nPlayers = nPlayers;
    }

    public int nPlayers() {
        return nPlayers;
    }

    public List<User> users() {
        return users;
    }

    public boolean isOpen() {
        return !readyToGo;
    }

    public Match match() {
        return match;
    }

    public synchronized void setReadyToGo(boolean readyToGo) {
        this.readyToGo = readyToGo;
    }

    public synchronized void setMatch(Match match) {
        this.match = match;
    }

    public boolean isReady() {
        return users().size() == nPlayers();
    }

    public synchronized void close() {
        setReadyToGo(true);
    }

    public boolean hasMatch() {
        return match() != null;
    }

    /**
     * This method adds a user to the lobby
     * @param user new user
     */
    public synchronized void addUser(User user) {
        if (!users().contains(user) && !isReady()) {
            users().add(user);
            int missing = nPlayers() - users().size();
            System.out.println("Added " + user.name() + " to lobby " + this + " with " + nPlayers() + " players\n" + missing + " missing");
            if (isReady()) {
                System.out.println("Lobby " + this + " is ready");
                close();
            }
        }
    }

    /**
     * This method removes a user from the lobby
     * @param user quitting user
     */
    public void removeUser(User user) {
        if (users().contains(user)) {
            if (hasMatch())
                match().removeUser(user);
            users().remove(user);
        }
    }

    public void beginMatch() {
        match.start();
    }

    public void createMatch() {
        setMatch(new Match(users));
        System.out.println("Match " + match() + " can start");
        beginMatch();
    }
}
