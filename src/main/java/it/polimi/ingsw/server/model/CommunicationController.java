package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.network.objects.PlayerProxy;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Map;

import static it.polimi.ingsw.network.CommunicationProtocol.*;

public class CommunicationController {
    private Map<Player, User> playerToUser = new HashMap<>();

    public CommunicationController() {
    }

    public CommunicationController(List<User>users, List<Player> gamePlayers){
        for(int i=0; i<gamePlayers.size();i++){
            playerToUser.put(gamePlayers.get(i), users.get(i));
        }
    }

    /**
     * This method tells if a player represents a user
     * @param player
     * @return boolean
     */
    private boolean playerIsUser(Player player) {
        return playerToUser.containsKey(player);
    }

    /**
     * This method removes the connections with a quitting player
     * @param player quitting player
     */
    public synchronized void removeUser(Player player) {
        if (playerIsUser(player))
            playerToUser.remove(player);
    }

    /**
     * This method finds the user represented by a player
     * @param player The player I'm looking for
     * @return user
     */
    private User findUser(Player player) {
        if (playerIsUser(player))
            return playerToUser.get(player);
        return null;
    }

    /**
     * This method communicates to a user what player is representing him/her
     * @param player player avatar
     * @return boolean if everything went well
     * @throws IOException if there are network errors
     */
    public boolean announceMyPlayer(Player player) throws IOException {
        User user = findUser(player);
        user.tell(MYPLAYER);
        if (user.copy()) {
            PlayerProxy playerProxy = player.createProxy();
            Type type = new TypeToken<PlayerProxy>() {}.getType();
            user.tell(new Gson().toJson(playerProxy, type));
            return user.copy();
        }
        System.out.println("USER DIDN'T RECEIVE PLAYER");
        return false;
    }

    /**
     * This method communicates to a user who are the opponents
     * @param player player avatar
     * @param players all players
     * @return boolean if everything went well
     * @throws IOException if there are network errors
     */
    public boolean announceOpponents(Player player, List<Player> players) throws IOException {
        User user = findUser(player);
        user.tell(OPPONENTS);
        if (user.copy()) {
            List<PlayerProxy> opponents = new ArrayList<>();
            for (Player opponent: players) {
                if (!player.equals(opponent))
                    opponents.add(opponent.createProxy());
                Type type = new TypeToken<List<PlayerProxy>>() {}.getType();
                user.tell(new Gson().toJson(opponents, type));
                return user.copy();
            }
        }
        System.out.println("USER DIDN'T RECEIVE OPPONENTS");
        return false;
    }

    /**
     * This method communicates to a user who are the participants
     * @param player player avatar
     * @param players all players
     * @return boolean if everything went well
     * @throws IOException if there are network errors
     */
    public boolean announceParticipants(Player player, List<Player> players) throws IOException {
        if (announceMyPlayer(player))
            return announceOpponents(player, players);
        System.out.println("USER DIDN'T RECEIVE PARTICIPANTS");
        return false;
    }

    public void announceStart(List<Player> players) {
        for (Player player: players)
            if (playerIsUser(player)) {
                try {
                    announceParticipants(player, players);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println(player.name() + " lost connection");
                    player.setInGame(false);
                }
            }
    }

    private int askWorker(User user, List<Worker> workers) throws IOException {
        List<int[]> positions = new ArrayList<>();
        for (Worker worker: workers)
            positions.add(worker.position().position());
        Type listType = new TypeToken<List<int[]>>() {}.getType();
        user.tell(new Gson().toJson(positions, listType));
        return user.hearNumber();
    }

    /**
     * This method asks the user representing the player to choose one worker from a List
     * @param chooser Player
     * @param workers Workers
     * @return Chosen Worker
     * @throws IOException when a network error occurs
     */
    public Worker chooseWorker(Player chooser, List<Worker> workers) throws IOException {
        int index = new Random().nextInt(workers.size());
        if (playerIsUser(chooser)) {
            User user = findUser(chooser);
            user.tell(WORKER);
            if (user.copy())
                index = askWorker(user, workers);
        }
        return workers.get(index);
    }

    private int askBox(User user, List<Box> boxes) throws IOException {
        List<int[]> positions = new ArrayList<>();
        for (Box box: boxes)
            positions.add(box.position());
        Type listType = new TypeToken<List<int[]>>() {}.getType();
        user.tell(new Gson().toJson(positions, listType));
        return user.hearNumber();
    }

    /**
     * This method asks a player to choose one location from a list
     * @param player player
     * @param boxes List of choices
     * @return Chosen Position
     * @throws IOException when a network error occurs
     */
    public Box chooseStartPosition(Player player, List<Box> boxes) throws IOException {
        int index = new Random().nextInt(boxes.size());
        if (playerIsUser(player)) {
            User user = findUser(player);
            user.tell(STARTPOSITION);
            if (user.copy())
                index = askBox(user, boxes);
        }
        return boxes.get(index);
    }

    /**
     * This method asks a player to choose one move destination from a list
     * @param chooser player
     * @param boxes List of choices
     * @return Chosen Destination
     * @throws IOException when a network error occurs
     */
    public Box chooseDestination(Player chooser, List<Box> boxes) throws IOException {
        int index = new Random().nextInt(boxes.size());
        if (playerIsUser(chooser)) {
            User user = findUser(chooser);
            user.tell(DESTINATIONS);
            index = user.hearNumber();
        }
        return boxes.get(index);
    }

    /**
     * This method asks a player to choose one build location from a list
     * @param chooser player
     * @param boxes List of choices
     * @return Chosen Location
     * @throws IOException when a network error occurs
     */
    public Box chooseBuild(Player chooser, List<Box> boxes) throws IOException {
        int index = new Random().nextInt(boxes.size());
        if (playerIsUser(chooser)) {
            User user = findUser(chooser);
            user.tell(BUILDS);
            index = user.hearNumber();
        }
        return boxes.get(index);
    }

    public Box chooseRemoval(Player chooser, List<Box> boxes) throws IOException {
        int index = new Random().nextInt(boxes.size());
        if (playerIsUser(chooser)) {
            User user = findUser(chooser);
            user.tell(REMOVALS);
            index = user.hearNumber();
        }
        return boxes.get(index);
    }

    public void youLost(Player loser){
        System.out.println("Hai perso");
    }

    public boolean chooseToUsePower(Player chooser){
        return true;
    }

    public void updateView(List<Player> players, Map map) {
        //todo view communication to each player
    }

    public GodCard chooseCard(Player chooser, List<GodCard> cards){
        //todo chiedo a utente una carta
        int i = new Random().nextInt(cards.size());
        return cards.get(i);
    }

    public int chooseFirstPlayer(Player chooser, List<Player> players){
        //todo chiedere a players.get(0) chi inizia
        int index = 1; //todo index = what the Challenger chooses
        return index;
    }

    public boolean confirmPhase(){
        return new Random().nextBoolean();
    }
}
