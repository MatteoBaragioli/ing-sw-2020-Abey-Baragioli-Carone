package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBase {

    private List<String> userNames = new ArrayList<>();
    private Map<String, User> users = new HashMap<>();
    private Map<User, Lobby> lobbies = new HashMap<>();
    private List<Lobby> completeLobbies = new ArrayList<>();
    private List<Lobby> openLobbies = new ArrayList<>();

    public List<String> userNames() {
        return userNames;
    }

    public Map<String, User> users() {
        return users;
    }

    public Map<User, Lobby> lobbies() {
        return lobbies;
    }

    public List<Lobby> completeLobbies() {
        return completeLobbies;
    }

    public List<Lobby> openLobbies() {
        return openLobbies;
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
     * This method adds a new name to the list of usernames
     * @param name string
     */
    public synchronized boolean addUserName(String name) {
        boolean exists = userNameExists(name);
        if (!exists)
            userNames().add(name);
        return !exists;
    }

    /**
     * is method deletes a name from the list of usernames
     * @param name string
     */
    private synchronized void deleteUserName(String name) {
        if (userNameExists(name))
            userNames().remove(name);
    }

    /**
     * This method finds a user related to an existing userName
     * @param username String
     * @return User
     */
    public User findUser(String username) {
        if (userNameExists(username))
            return users().get(username);
        return null;
    }

    /**
     * This method adds a user to the DB
     * @param user new user
     */
    public synchronized boolean addUser(User user) {
        boolean done = false;
        if (user != null) {
            done = addUserName(user.name());
            if (done)
                users().put(user.name(), user);
        }
        return done;
    }

    /**
     * This method removes a user from his lobby
     * @param user quitting user
     */
    public void userQuitLobby(User user) {
        if (userHasLobby(user))
            findLobby(user).removeUser(user);
    }

    /**
     * This method removes a user from the DB
     * @param user leaving user
     */
    public synchronized void deleteUser(User user) {
        if (user != null) {
            userQuitLobby(user);
            users().remove(user.name());
            deleteUserName(user.name());
        }
    }

    /**
     * This method tells if a user is assigned to a Lobby
     * @param user user
     * @return boolean
     */
    public boolean userHasLobby(User user) {
        return lobbies().containsKey(user);
    }

    /**
     * This method finds the lobby the user has joined (null if the user hasn't joined a lobby yet)
     * @param user user
     * @return Lobby
     */
    public Lobby findLobby(User user) {
        if (user != null && userNameExists(user.name()) && userHasLobby(user))
            return lobbies().get(user);
        return null;
    }

    /**
     * This method adds assigns a lobby to a user in the hash-map
     * @param user joining user
     * @param lobby joined lobby
     */
    public synchronized void joinLobby(User user, Lobby lobby) {
        if (!userHasLobby(user))
            lobbies().put(user, lobby);
    }

    /**
     * This method moves the lobby from the incomplete openLobbies list to the complete ones
     * @param lobby the lobby created by this user manager
     */
    public synchronized void registerCompleteLobby(Lobby lobby) {
        openLobbies().remove(lobby);
        completeLobbies().add(lobby);
    }

    /**
     * This method puts a user in the queue chosen by him
     * @param user User
     */
    public void assignUserToLobby(User user) {
        boolean found = false;
        int nPlayers = 0;
        try {
            nPlayers = user.askTwoOrThreePlayerMatch();
        } catch (IOException e) {
            user.communicationChannel().close();
            deleteUser(user);
            e.printStackTrace();
            System.err.println("Can't ask matchtype to " + user.name());
            System.exit(0);
        }

        System.out.println("Looking for a lobby with " + nPlayers + " players for " + user.name());
        Lobby lobby;
        for (int i = 0; i < openLobbies().size() && !found; i++) {
            lobby = openLobbies().get(i);
            if (lobby.isOpen() && lobby.nPlayers()==nPlayers) {
                found = true;
                lobby.addUser(user);
                joinLobby(user, lobby);
            }
        }

        if (!found) {
            lobby = new Lobby(user, nPlayers);
            joinLobby(user, lobby);
            System.out.println("New lobby " + lobby + " with " + nPlayers + " players has been created for " + user.name());
            openLobbies().add(lobby);
            lobby.run();
            registerCompleteLobby(lobby);
        }
    }
}