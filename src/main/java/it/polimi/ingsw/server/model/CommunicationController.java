package it.polimi.ingsw.server.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Random;

import static it.polimi.ingsw.network.CommunicationProtocol.*;

public class CommunicationController {

    private Map <Player, User> playerToUser = new HashMap<>();

    public CommunicationController() {
    }

    public CommunicationController(List<User>users, List<Player> gamePlayers){
        for(int i=0; i<gamePlayers.size();i++){
            playerToUser.put(gamePlayers.get(i), users.get(i));
        }
    }

    private boolean playerIsUser(Player chooser) {
        return playerToUser.containsKey(chooser);
    }

    /**
     * This method removes the connections with a quitting player
     * @param player quitting player
     */
    public synchronized void removeUser(Player player) {
        if (playerIsUser(player))
            playerToUser.remove(player);
    }

    private User findUser(Player player) {
        if (playerIsUser(player))
            return playerToUser.get(player);
        return null;
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
            if (user.copy()) {
                //TODO
                index = user.hearNumber();
            }
        }
        return workers.get(index);
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
            index = user.hearNumber();
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
