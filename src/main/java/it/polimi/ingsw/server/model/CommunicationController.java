package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.network.objects.BoxProxy;
import it.polimi.ingsw.network.objects.GodCardProxy;
import it.polimi.ingsw.network.objects.PlayerProxy;

import java.lang.reflect.Type;
import java.util.*;
import java.util.Map;
import java.util.concurrent.TimeoutException;

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

    public synchronized void removeConnection(Player player) {
        player.setInGame(false);
        removeUser(player);
    }
    /**
     * This method finds the user represented by a player
     * @param player The player I'm looking for
     * @return user
     */
    public User findUser(Player player) {
        if (playerIsUser(player))
            return playerToUser.get(player);
        return null;
    }

    /**
     * This method communicates to a user what player is representing him/her
     * @param player player avatar
     * @return boolean if everything went well
     */
    public boolean announceMyPlayer(Player player) throws ChannelClosedException {
        User user = findUser(player);
        PlayerProxy playerProxy = player.createProxy();
        Type type = new TypeToken<PlayerProxy>() {}.getType();
        return user.sendMyPlayer(new Gson().toJson(playerProxy, type));
    }

    /**
     * This method communicates to a user who are the opponents
     * @param player player avatar
     * @param players all players
     * @return boolean if everything went well
     */
    public boolean announceOpponents(Player player, List<Player> players) throws ChannelClosedException {
        User user = findUser(player);
        List<PlayerProxy> opponents = new ArrayList<>();
        for (Player opponent: players) {
            if (!player.equals(opponent) && opponent.isInGame())
                opponents.add(opponent.createProxy());
        }
        Type type = new TypeToken<List<PlayerProxy>>() {}.getType();
        return user.sendOpponents(new Gson().toJson(opponents, type));
    }

    /**
     * This method communicates to a user who are the participants
     * @param player player avatar
     * @param players all players
     * @return boolean if everything went well
     * @throws ChannelClosedException if there are network errors
     */
    public boolean announceParticipants(Player player, List<Player> players) throws ChannelClosedException {
        if (playerIsUser(player))
            return announceMyPlayer(player) && announceOpponents(player, players);
        return true;
    }

    /**
     * This method asks the user to choose one worker from a list
     * @param user User
     * @param workers List of movable workers
     * @return list index
     */
    public int askWorker(User user, List<Worker> workers) throws TimeoutException, ChannelClosedException {
        List<int[]> positions = new ArrayList<>();
        for (Worker worker: workers)
            positions.add(worker.position().position());
        Type listType = new TypeToken<List<int[]>>() {}.getType();
        return user.askWorker(new Gson().toJson(positions, listType));
    }

    /**
     * This method asks the user representing the player to choose one worker from a List
     * @param chooser Player
     * @param workers Workers
     * @return Chosen Worker
     */
    public Worker chooseWorker(Player chooser, List<Worker> workers) throws TimeoutException, ChannelClosedException {
        int index = new Random().nextInt(workers.size());
        if (playerIsUser(chooser))
            index = askWorker(findUser(chooser), workers);
        return workers.get(index);
    }

    /**
     * This method converts a Box list to a Json object
     * @param boxes List
     * @return String
     */
    public String convertBoxList(List<Box> boxes) {
        List<int[]> positions = new ArrayList<>();
        for (Box box: boxes)
            positions.add(box.position());
        Type listType = new TypeToken<List<int[]>>() {}.getType();
        return new Gson().toJson(positions, listType);
    }

    /**
     * This method asks a user to choose one location from a list
     * @param user asked user
     * @param boxes available start positions
     * @return list index
     * @throws TimeoutException if user doesn't answer
     * @throws ChannelClosedException if connection is lost
     */
    public int askStartPosition(User user, List<Box> boxes) throws TimeoutException, ChannelClosedException {
        return user.askStartPosition(convertBoxList(boxes));
    }

    /**
     * This method asks a player to choose one location from a list
     * @param player player
     * @param boxes List of choices
     * @return Chosen Position
     * @throws TimeoutException
     * @throws ChannelClosedException
     */
    public Box chooseStartPosition(Player player, List<Box> boxes) throws ChannelClosedException, TimeoutException {
        int index = new Random().nextInt(boxes.size());
        if (playerIsUser(player))
            index = askStartPosition(findUser(player), boxes);
        return boxes.get(index);
    }


    /**
     * This method asks a user to choose one move destination from a list
     * @param user asked user
     * @param boxes available destinations
     * @return list index
     * @throws TimeoutException if user doesn't answer
     * @throws ChannelClosedException if connection is lost
     */
    public int askDestination(User user, List<Box> boxes) throws TimeoutException, ChannelClosedException {
        return user.askDestination(convertBoxList(boxes));
    }

    /**
     * This method asks a player to choose one move destination from a list
     * @param chooser player
     * @param boxes List of choices
     * @return Chosen Destination
     * @throws TimeoutException if user doesn't answer
     * @throws ChannelClosedException if connection is lost
     */
    public Box chooseDestination(Player chooser, List<Box> boxes) throws TimeoutException, ChannelClosedException {
        int index = new Random().nextInt(boxes.size());
        if (playerIsUser(chooser))
            index = askDestination(findUser(chooser), boxes);
        return boxes.get(index);
    }

    /**
     * This method asks a user to choose one build location from a list
     * @param user asked user
     * @param boxes available locations
     * @return list index
     * @throws TimeoutException if user doesn't answer
     * @throws ChannelClosedException if connection is lost
     */
    public int askBuild(User user, List<Box> boxes) throws TimeoutException, ChannelClosedException {
        return user.askBuild(convertBoxList(boxes));
    }

    /**
     * This method asks a player to choose one build location from a list
     * @param chooser player
     * @param boxes List of choices
     * @return Chosen Location
     * @throws TimeoutException if user doesn't answer
     * @throws ChannelClosedException if connection is lost
     */
    public Box chooseBuild(Player chooser, List<Box> boxes) throws TimeoutException, ChannelClosedException {
        int index = new Random().nextInt(boxes.size());
        if (playerIsUser(chooser))
            index = askBuild(findUser(chooser), boxes);
        return boxes.get(index);
    }

    /**
     * This method asks a user to choose one block to remove from a list
     * @param user asked user
     * @param boxes available locations
     * @return list index
     * @throws TimeoutException if user doesn't answer
     * @throws ChannelClosedException if connection is lost
     */
    public int askRemoval(User user, List<Box> boxes) throws TimeoutException, ChannelClosedException {
        return user.askRemoval(convertBoxList(boxes));
    }

    /**
     * this method asks to the user for a confirmation to the user
     * @param user asked user
     * @param key key of communication protocol
     * @return boolean value
     * @throws TimeoutException
     * @throws ChannelClosedException
     */
    public boolean askConfirmation(User user, CommunicationProtocol key) throws TimeoutException, ChannelClosedException {
        return user.askConfirmation(key);
    }

    /**
     * this method asks to the user for a God Card
     * @param user
     * @param cards
     * @return
     * @throws TimeoutException
     * @throws ChannelClosedException
     */
    public int askCard(User user, List<GodCard> cards)throws TimeoutException, ChannelClosedException {
        List<GodCardProxy> proxyCards = new ArrayList<>();

        for (GodCard card: cards)
            proxyCards.add(card.createProxy());

        Type listType = new TypeToken<List<GodCardProxy>>() {}.getType();
        return user.askCard(new Gson().toJson(proxyCards, listType));
    }

    public GodCard chooseCard(Player chooser, List<GodCard> cards) throws TimeoutException, ChannelClosedException{
        int index = new Random().nextInt(cards.size());
        if (playerIsUser(chooser))
            index = askCard(findUser(chooser), cards);
        return cards.get(index);
    }

    /**
     * this method asks to the user for a God Card
     * @param user
     * @param deck
     * @return
     * @throws TimeoutException
     * @throws ChannelClosedException
     */
    public int askDeck(User user, List<GodCard> deck)throws TimeoutException, ChannelClosedException {
        List<GodCardProxy> proxyDeck = new ArrayList<>();

        for (GodCard card: deck)
            proxyDeck.add(card.createProxy());

        Type listType = new TypeToken<List<GodCardProxy>>() {}.getType();
        return user.askDeck(new Gson().toJson(proxyDeck, listType));
    }

    public GodCard chooseDeck(Player chooser, List<GodCard> cards) throws TimeoutException, ChannelClosedException {
        int index = new Random().nextInt(cards.size());
        if (playerIsUser(chooser))
            index = askDeck(findUser(chooser), cards);
        return cards.get(index);
    }

    /**
     * This method asks a player to choose one build location from a list
     * @param chooser player
     * @param boxes List of choices
     * @return Chosen Location
     * @throws TimeoutException if user doesn't answer
     * @throws ChannelClosedException if connection is lost
     */
    public Box chooseRemoval(Player chooser, List<Box> boxes) throws TimeoutException, ChannelClosedException {
        int index = new Random().nextInt(boxes.size());
        if (playerIsUser(chooser))
            index = askRemoval(findUser(chooser), boxes);
        return boxes.get(index);
    }

    public void youLost(Player loser){
        System.out.println("Hai perso");
    }

    public boolean chooseToUsePower(Player chooser)throws TimeoutException, ChannelClosedException {
        boolean result=new Random().nextBoolean();
        boolean answer;
        if (playerIsUser(chooser))
            result= askConfirmation(findUser(chooser), GODPOWER);
        return result;
    }

    public boolean updateView(Player player , List<BoxProxy> boxes) throws ChannelClosedException {
        User user = findUser(player);
        Type type = new TypeToken<BoxProxy>() {}.getType();
        return user.sendMap(new Gson().toJson(boxes, type));
    }

    public int chooseFirstPlayer(Player chooser, List<Player> players){
        //todo chiedere a players.get(0) chi inizia
        int index = 1; //todo index = what the Challenger chooses
        return index;
    }

    public boolean confirmPhase(Player player)throws TimeoutException, ChannelClosedException{
        boolean result=new Random().nextBoolean();
        if (playerIsUser(player))
            result= askConfirmation(findUser(player), UNDO);
        return result;
    }
}
