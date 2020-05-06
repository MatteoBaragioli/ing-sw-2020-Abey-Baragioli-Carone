package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.Match;
import it.polimi.ingsw.server.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller extends Thread {
    private List<Match> matches = new ArrayList<>();
    private List<User> activeUsers = new ArrayList<>();
    private Map<User, Match> userToMatch = new HashMap<>();
    private List<String> userNames = new ArrayList<>();
    private Map<String, User> users = new HashMap<>();
    private List<Lobby> completeLobbies = new ArrayList<>();
    private List<Lobby> lobbies = new ArrayList<>();

    public List<String> userNames() {
        return userNames;
    }

    public Map<String, User> users() {
        return users;
    }

    public List<Lobby> lobbies() {
        return lobbies;
    }

    public List<Lobby> completeLobbies() {
        return completeLobbies;
    }

    /**
     * This method tells if a userName is contained in the userNames List
     * @param username String
     * @return boolean
     */
    public boolean userNameExists(String username) {
        return userNames().contains(username);
    }

    /**
     * This method finds a user related to an existing userName
     * @param username String
     * @return User
     */
    public User findUser(String username) {
        if (users().containsKey(username))
            return users().get(username);
        return null;
    }

    /**
     * This method moves the lobby from the incomplete lobbies list to the complete ones
     * @param lobby the lobby created by this user manager
     */
    private synchronized void registerCompleteLobby(Lobby lobby) {
        lobbies().remove(lobby);
        completeLobbies().add(lobby);
    }

    /**
     * This method puts a user in the queue chosen by him
     * @param user User
     */
    public synchronized void assignUserToLobby(User user) throws IOException {
        boolean found = false;
        int nPlayers = user.askTwoOrThreePlayerMatch();
        for (int i = 0; i < lobbies().size() && !found; i++) {
            Lobby lobby = lobbies().get(i);
            if (lobby.isFree() && lobby.nPlayers()==nPlayers) {
                found = true;
                lobby.addUser(user);
            }
        }
        if (!found) {
            Lobby lobby = new Lobby(user, nPlayers);
            lobbies().add(lobby);
            lobby.start();
            try {
                lobby.join();
                registerCompleteLobby(lobby);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("La lobby non joina");
            }
        }
    }


    public void start() {
        while (true) {}
    }
}
