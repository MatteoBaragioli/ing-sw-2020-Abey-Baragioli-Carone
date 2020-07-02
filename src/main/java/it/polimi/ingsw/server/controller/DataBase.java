package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.network.CommunicationChannel;
import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.server.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBase {

    private List<CommunicationChannel> connections = new ArrayList<>();
    private Map<CommunicationChannel, String> connectionToUser = new HashMap<>();
    private List<String> userNames = new ArrayList<>();
    private Map<String, User> users = new HashMap<>();
    private Map<User, Lobby> lobbies = new HashMap<>();
    private List<Lobby> completeLobbies = new ArrayList<>();
    private List<Lobby> openLobbies = new ArrayList<>();

    /**
     * This method tells if a connection is registered
     * @param communicationChannel Communication channel
     * @return Boolean that is true if the connection is registered
     */
    public boolean hasConnection(CommunicationChannel communicationChannel) {
        return connections.contains(communicationChannel);
    }

    /**
     * This method registers a new connection
     * @param communicationChannel Communication channel
     */
    public synchronized void addConnection(CommunicationChannel communicationChannel) {
        if (!hasConnection(communicationChannel))
            connections.add(communicationChannel);
    }

    /**
     * This method tells if a user has registered from this connection
     * @param communicationChannel Communication channel
     * @return Boolean that is true if the user has registered from this connection
     */
    public boolean connectionHasUser(CommunicationChannel communicationChannel) {
        return connectionToUser.containsKey(communicationChannel);
    }

    /**
     * This method tells if a userName is contained in the userNames List
     * @param username User username
     * @return Boolean that is true if username is contained in the userNames list
     */
    public boolean userNameExists(String username) {
        return userNames.contains(username);
    }

    /**
     * This method adds a new name to the list of usernames
     * @param name Username to add
     */
    public synchronized boolean addUserName(String name) {
        boolean exists = userNameExists(name);
        if (!exists)
            userNames.add(name);
        return !exists;
    }

    /**
     * This method deletes a name from the list of usernames
     * @param name Username to delete
     */
    private synchronized void deleteUserName(String name) {
        if (userNameExists(name))
            userNames.remove(name);
    }

    /**
     * This method finds a user related to an existing userName
     * @param username Username to find
     * @return User related to username
     */
    public User findUser(String username) {
        if (userNameExists(username))
            return users.get(username);
        return null;
    }

    /**
     * This method tells which user has registered from this connection
     * @param communicationChannel Communication channel
     * @return User that has registered from this connection
     */
    public User findUser(CommunicationChannel communicationChannel) {
        if (connectionHasUser(communicationChannel))
            return findUser(connectionToUser.get(communicationChannel));
        else
            return null;
    }

    /**
     * This method adds a user to the DB
     * @param user New user
     */
    public synchronized boolean addUser(User user) {
        boolean done = false;
        if (user != null) {
            done = addUserName(user.name());
            if (done) {
                users.put(user.name(), user);
                connectionToUser.put(user.communicationChannel(), user.name());
            }
        }
        return done;
    }

    /**
     * This method removes a user to the DB
     * @param username New user
     */
    public synchronized void removeUser(String username) {
        users.remove(username);
    }

    /**
     * This method tells if a user is assigned to a Lobby
     * @param user User
     * @return Boolean that is true if user is assigned to a lobby
     */
    public boolean userHasLobby(User user) {
        return lobbies.containsKey(user);
    }

    /**
     * This method removes a user from his lobby
     * @param user Quitting user
     */
    public void userQuitLobby(User user) {
        if (userHasLobby(user)) {
            Lobby lobby = findLobby(user);
            lobby.removeUser(user);
            if (lobby.users().isEmpty())
                removeLobby(lobby);
            lobbies.remove(user);
        }
    }

    /**
     * This method removes a lobby from the lobby lists
     * @param lobby Lobby to remove
     */
    private synchronized void removeLobby(Lobby lobby) {
        if (lobby.hasMatch())
            completeLobbies.remove(lobby);
        else
            openLobbies.remove(lobby);
    }

    /**
     * This method removes a user from the DB
     * @param user Leaving user
     */
    public synchronized void deleteUser(User user) {
        if (user != null) {
            userQuitLobby(user);
            removeUser(user.name());
            deleteUserName(user.name());
            connectionToUser.remove(user.communicationChannel());
            System.out.println("Removed " + user.name());
        }
    }

    /**
     * This method removes a user from the DB
     * @param communicationChannel Leaving user communication channel
     */
    public synchronized void deleteConnection(CommunicationChannel communicationChannel) {
        deleteUser(findUser(communicationChannel));
        connections.remove(communicationChannel);
    }

    /**
     * This method finds the lobby the user has joined (null if the user hasn't joined a lobby yet)
     * @param user User
     * @return Lobby the user has joined
     */
    public Lobby findLobby(User user) {
        if (user != null && userNameExists(user.name()) && userHasLobby(user))
            return lobbies.get(user);
        return null;
    }

    /**
     * This method adds assigns a lobby to a user in the hash-map
     * @param user Joining user
     * @param lobby Joined lobby
     */
    public synchronized void joinLobby(User user, Lobby lobby) {
        if (!userHasLobby(user)) {
            lobbies.put(user, lobby);
            lobby.addUser(user);
            if (lobby.isReady()) {
                System.out.println("We can start this match " + lobby.toString());
                registerCompleteLobby(lobby);
                lobby.createMatch();
            }
        }
    }

    /**
     * This method creates a new lobby with n players
     * @param user User that is creating the new lobby
     * @param nPlayers Number of player for this lobby
     */
    public synchronized void createNewLobby(User user, int nPlayers) {
        Lobby lobby = new Lobby(user, nPlayers);
        joinLobby(user, lobby);
        System.out.println("New lobby " + lobby + " with " + nPlayers + " players has been created for " + user.name());
        openLobbies.add(lobby);
    }

    /**
     * This method moves the lobby from the incomplete openLobbies list to the complete ones
     * @param lobby The lobby created by this user manager
     */
    public synchronized void registerCompleteLobby(Lobby lobby) {
        openLobbies.remove(lobby);
        completeLobbies.add(lobby);
    }

    /**
     * This method puts a user in the queue chosen by him
     * @param user User
     */
    public void assignUserToLobby(User user) {
        boolean found = false;
        int nPlayers = 0;
        try {
            nPlayers = user.askMatchType();
        } catch (ChannelClosedException e) {
            user.communicationChannel().close();
            deleteUser(user);
            e.printStackTrace();
            System.err.println("Can't ask matchtype to " + user.name());
            return;
        }
        System.out.println("Looking for a lobby with " + nPlayers + " players for " + user.name());
        Lobby lobby;
        for (int i = 0; i < openLobbies.size() && !found; i++) {
            lobby = openLobbies.get(i);
            if (lobby.isOpen() && lobby.nPlayers()==nPlayers) {
                found = true;
                joinLobby(user, lobby);
            }
        }
        if (!found)
            createNewLobby(user, nPlayers);
    }
}
