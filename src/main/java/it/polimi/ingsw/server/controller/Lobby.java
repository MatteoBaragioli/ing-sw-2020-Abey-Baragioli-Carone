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

    Lobby(User firstPlayer, int nPlayers) {
        users.add(firstPlayer);
        this.nPlayers = nPlayers;
    }

    int nPlayers() {
        return nPlayers;
    }

    List<User> users() {
        return users;
    }

    boolean isOpen() {
        return !readyToGo;
    }

    public Match match() {
        return match;
    }

    private synchronized void setReadyToGo(boolean readyToGo) {
        this.readyToGo = readyToGo;
    }

    public synchronized void setMatch(Match match) {
        this.match = match;
    }

    boolean isReady() {
        return users().size() == nPlayers();
    }

    public synchronized void close() {
        setReadyToGo(true);
    }

    boolean hasMatch() {
        return match() != null;
    }

    /**
     * This method adds a user to the lobby
     * @param user new user
     */
    synchronized void addUser(User user) {
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
    void removeUser(User user) {
        if (users().contains(user)) {
            if (hasMatch())
                match().removeUser(user);
            users().remove(user);
        }
    }

    /**
     * This method starts the match as a thread
     */
    private void beginMatch() {
        match.start();
    }

    /**
     * This method create a match using the users
     */
    void createMatch() {
        setMatch(new Match(users));
        System.out.println("Match " + match() + " can start");
        beginMatch();
    }
}
