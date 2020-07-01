package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.network.exceptions.*;
import it.polimi.ingsw.network.objects.BoxProxy;
import it.polimi.ingsw.network.objects.GodCardProxy;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.network.objects.PlayerProxy;

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
     * @param player Player
     * @return Boolean that is true if player represent a user
     */
    public boolean playerIsUser(Player player) {
        return playerToUser.containsKey(player);
    }

    /**
     * This method removes the connections with a quitting player
     * @param player Quitting player
     */
    public synchronized void removeUser(Player player) {
        if (playerIsUser(player))
            playerToUser.remove(player);
    }

    /**
     * This method finds the user represented by a player
     * @param player The player I'm looking for
     * @return User
     */
    public User findUser(Player player) {
        if (playerIsUser(player))
            return playerToUser.get(player);
        return null;
    }

    /**
     * This method communicates to a user what player is representing him/her
     * @param player Player avatar
     * @return Boolean if everything went well
     * @throws ChannelClosedException If communicationChannel is closed
     */
    public boolean announceMyPlayer(Player player) throws ChannelClosedException {
        User user = findUser(player);
        PlayerProxy playerProxy = player.createProxy();
        Type type = new TypeToken<PlayerProxy>() {}.getType();
        return user.sendMyPlayer(new Gson().toJson(playerProxy, type));
    }

    /**
     * This method communicates to a user who are the opponents
     * @param player Player avatar
     * @param players All players
     * @return Boolean if everything went well
     * @throws ChannelClosedException If communicationChannel is closed
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
     * @param player Player avatar
     * @param players All players
     * @return Boolean if everything went well
     * @throws ChannelClosedException If there are network errors
     */
    public boolean announceParticipants(Player player, List<Player> players) throws ChannelClosedException {
        if (playerIsUser(player))
            return announceMyPlayer(player) && announceOpponents(player, players);
        return true;
    }

    /**
     * This method communicates to a user who is playing now
     * @param player Listener
     * @param currentPlayer Current player
     * @return Boolean if everything went well
     * @throws ChannelClosedException If communicationChannel is closed
     */
    public boolean announceCurrentPlayer(Player player, Player currentPlayer) throws ChannelClosedException {
        if(playerIsUser(player)) {
            User user = findUser(player);
            PlayerProxy playerProxy = currentPlayer.createProxy();
            Type type = new TypeToken<PlayerProxy>() {
            }.getType();
            return user.sendCurrentPlayer(new Gson().toJson(playerProxy, type));
        }
        return true;
    }

    /**
     * This method communicates to a user who is the winner
     * @param player Listener
     * @param winner Winner
     * @return Boolean if everything went well
     * @throws ChannelClosedException If communicationChannel is closed
     */
    public boolean announceWinner(Player player, Player winner) throws ChannelClosedException {
        if(playerIsUser(player)) {
            User user = findUser(player);
            PlayerProxy playerProxy = winner.createProxy();
            Type type = new TypeToken<PlayerProxy>() {
            }.getType();
            return user.sendWinner(new Gson().toJson(playerProxy, type));
        }
        return true;
    }

    /**
     * This method communicates to a user who is the loser, if someone loses the match
     * @param player Listener
     * @param loser Loser
     * @return Boolean if everything went well
     * @throws ChannelClosedException If communicationChannel is closed
     */
    public boolean announceLoser(Player player, Player loser) throws ChannelClosedException {
        if(playerIsUser(player)) {
            User user = findUser(player);
            PlayerProxy playerProxy = loser.createProxy();
            Type type = new TypeToken<PlayerProxy>() {
            }.getType();
            return user.sendLoser(new Gson().toJson(playerProxy, type));
        }
        return true;
    }

    /**
     * This method asks the user to choose one worker from a list
     * @param user User
     * @param workers List of movable workers
     * @return List index
     * @throws TimeOutException Exception thrown when the time to do an action runs out
     * @throws ChannelClosedException If communicationChannel is closed
     */
    public int askWorker(User user, List<Worker> workers) throws TimeOutException, ChannelClosedException {
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
     * @throws TimeOutException Exception thrown when the time to do an action runs out
     * @throws ChannelClosedException If communicationChannel is closed
     */
    public Worker chooseWorker(Player chooser, List<Worker> workers) throws TimeOutException, ChannelClosedException {
        int index = 0;
        if (workers.size()>1)
            index = new Random().nextInt(workers.size()-1);
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
     * @param user Asked user
     * @param boxes Available start positions
     * @return List index
     * @throws TimeOutException If user doesn't answer
     * @throws ChannelClosedException If connection is lost
     */
    public int askStartPosition(User user, List<Box> boxes) throws TimeOutException, ChannelClosedException {
        return user.askStartPosition(convertBoxList(boxes));
    }

    /**
     * This method asks a player to choose one location from a list
     * @param player Player
     * @param boxes List of choices
     * @return Chosen Position
     * @throws TimeOutException If user doesn't answer
     * @throws ChannelClosedException If connection is lost
     */
    public Box chooseStartPosition(Player player, List<Box> boxes) throws ChannelClosedException, TimeOutException {
        int index = 0;
        if (boxes.size()>1)
            index = new Random().nextInt(boxes.size()-1);
        if (playerIsUser(player))
            index = askStartPosition(findUser(player), boxes);
        return boxes.get(index);
    }


    /**
     * This method asks a user to choose one move destination from a list
     * @param user Asked user
     * @param boxes Available destinations
     * @return List index
     * @throws TimeOutException If user doesn't answer
     * @throws ChannelClosedException If connection is lost
     */
    public int askDestination(User user, List<Box> boxes) throws TimeOutException, ChannelClosedException {
        return user.askDestination(convertBoxList(boxes));
    }

    /**
     * This method asks a player to choose one move destination from a list
     * @param chooser Player
     * @param boxes List of choices
     * @return Chosen Destination
     * @throws TimeOutException If user doesn't answer
     * @throws ChannelClosedException If connection is lost
     */
    public Box chooseDestination(Player chooser, List<Box> boxes) throws TimeOutException, ChannelClosedException {
        int index = 0;
        if (boxes.size()>1)
            index = new Random().nextInt(boxes.size()-1);
        if (playerIsUser(chooser))
            index = askDestination(findUser(chooser), boxes);
        return boxes.get(index);
    }

    /**
     * This method asks a user to choose one build location from a list
     * @param user Asked user
     * @param boxes Available locations
     * @return List index
     * @throws TimeOutException If user doesn't answer
     * @throws ChannelClosedException If connection is lost
     */
    public int askBuild(User user, List<Box> boxes) throws TimeOutException, ChannelClosedException {
        return user.askBuild(convertBoxList(boxes));
    }

    /**
     * This method asks a player to choose one build location from a list
     * @param chooser Player
     * @param boxes List of choices
     * @return Chosen Location
     * @throws TimeOutException If user doesn't answer
     * @throws ChannelClosedException If connection is lost
     */
    public Box chooseBuild(Player chooser, List<Box> boxes) throws TimeOutException, ChannelClosedException {
        int index = 0;
        if (boxes.size()>1)
            index = new Random().nextInt(boxes.size()-1);
        if (playerIsUser(chooser))
            index = askBuild(findUser(chooser), boxes);
        return boxes.get(index);
    }

    /**
     * This method asks a user to choose one block to remove from a list
     * @param user Asked user
     * @param boxes Available locations
     * @return List index
     * @throws TimeOutException If user doesn't answer
     * @throws ChannelClosedException If connection is lost
     */
    public int askRemoval(User user, List<Box> boxes) throws TimeOutException, ChannelClosedException {
        return user.askRemoval(convertBoxList(boxes));
    }

    /**
     * This method asks to the user for a confirmation to the user
     * @param user Asked user
     * @param key Key of communication protocol
     * @return Boolean value
     * @throws TimeOutException If user doesn't answer
     * @throws ChannelClosedException If connection is lost
     */
    public boolean askConfirmation(User user, CommunicationProtocol key) throws TimeOutException, ChannelClosedException {
        return user.askConfirmation(key);
    }

    /**
     * This method asks to the user for a God Card
     * @param user Asked user
     * @param cards List of choosable cards
     * @return Index of chosen card
     * @throws TimeOutException If user doesn't answer
     * @throws ChannelClosedException If connection is lost
     */
    public int askCard(User user, List<GodCard> cards)throws TimeOutException, ChannelClosedException {
        List<GodCardProxy> proxyCards = new ArrayList<>();

        for (GodCard card: cards)
            proxyCards.add(card.createProxy());

        Type listType = new TypeToken<List<GodCardProxy>>() {}.getType();
        return user.askCard(new Gson().toJson(proxyCards, listType));
    }

    /**
     * This method calls askCard, saves chosen card index and returns corresponding card
     * @param chooser Asked user
     * @param cards List pf choosable cards
     * @return Chosen card
     * @throws TimeOutException If user doesn't answer
     * @throws ChannelClosedException If connection is lost
     */
    public GodCard chooseCard(Player chooser, List<GodCard> cards) throws TimeOutException, ChannelClosedException{
        int index = 0;
        if (cards.size()>1)
            index = new Random().nextInt(cards.size()-1);
        if (playerIsUser(chooser))
            index = askCard(findUser(chooser), cards);
        return cards.get(index);
    }

    /**
     * This method asks to the user (the challenger) for a list of cards (the cards for the current match)
     * @param user Challenger
     * @param deck List of all the cards of the game
     * @return Array of chosen cards indexes
     * @throws TimeOutException If user doesn't answer
     * @throws ChannelClosedException If connection is lost
     */
    public int[] askDeck(User user, List<GodCard> deck)throws TimeOutException, ChannelClosedException {
        List<GodCardProxy> proxyDeck = new ArrayList<>();

        for (GodCard card: deck)
            proxyDeck.add(card.createProxy());

        Type listType = new TypeToken<List<GodCardProxy>>() {}.getType();
        return user.askDeck(new Gson().toJson(proxyDeck, listType));
    }

    /**
     * This method calls askDeck, saves chosen cards indexes and returns corresponding cards
     * @param chooser Challenger
     * @param cards List of all the cards od the game
     * @return List of chosen cards
     * @throws TimeOutException If user doesn't answer
     * @throws ChannelClosedException If connection is lost
     */
    public List<GodCard> chooseDeck(Player chooser, List<GodCard> cards) throws TimeOutException, ChannelClosedException {
        List<GodCard> chosenCards = new ArrayList<>();
        int[] indexes;
        if (playerIsUser(chooser)) {
            indexes = askDeck(findUser(chooser), cards);
            for (int index : indexes) {
                chosenCards.add(cards.get(index));
            }
        }
        return chosenCards;
    }

    /**
     * This method asks a player to choose one build location from a list
     * @param chooser Player
     * @param boxes List of choices
     * @return Chosen Location
     * @throws TimeOutException If user doesn't answer
     * @throws ChannelClosedException If connection is lost
     */
    public Box chooseRemoval(Player chooser, List<Box> boxes) throws TimeOutException, ChannelClosedException {
        int index = 0;
        if (boxes.size()>1)
            index = new Random().nextInt(boxes.size()-1);
        if (playerIsUser(chooser))
            index = askRemoval(findUser(chooser), boxes);
        return boxes.get(index);
    }

    /**
     * This method asks a player if he wants to use his power
     * @param chooser Asked player
     * @return Boolean that is true if player chose to use his power
     * @throws TimeOutException If user doesn't answer
     * @throws ChannelClosedException If connection is lost
     */
    public boolean chooseToUsePower(Player chooser)throws TimeOutException, ChannelClosedException {
        boolean result=true;
        if (playerIsUser(chooser))
            result= askConfirmation(findUser(chooser), GOD_POWER);
        return result;
    }

    /**
     * This method calls updateView for each player in order to update the view map
     * @param players Players
     * @param boxes Boxes of the map
     * @return Boolean if everything went well
     * @throws ChannelClosedException If connection is lost
     */
    public boolean updateView(List<Player> players , List<BoxProxy> boxes) throws ChannelClosedException {
        for (Player player:players) {
            if (!updateView(player, boxes)) {
                System.out.println("Non ha ricevuto la mappa");
                return false;
            }
        }
        return true;
    }

    /**
     * This method send the updated map to the player
     * @param player Player that receives the updated map
     * @param boxes All boxes of the map
     * @return Boolean if everything went well
     * @throws ChannelClosedException If connection is lost
     */
    public boolean updateView(Player player , List<BoxProxy> boxes) throws ChannelClosedException {
        if(playerIsUser(player)) {
            User user = findUser(player);
            Type type = new TypeToken<List<BoxProxy>>() {
            }.getType();
            return user.sendMap(new Gson().toJson(boxes, type));
        }
        return true;
    }

    /**
     * This method asks a player if he wants to confirm his turn
     * @param player Asked player
     * @return Boolean that is true if player wants to undo his turn
     * @throws TimeOutException If user doesn't answer
     * @throws ChannelClosedException If connection is lost
     */
    public boolean confirmPhase(Player player)throws TimeOutException, ChannelClosedException{
        boolean result=new Random().nextBoolean();
        if (playerIsUser(player))
            result = askConfirmation(findUser(player), UNDO);
        return result;
    }

    /**
     * This method sends the highlights of the turn to a player
     * @param player Receiver
     * @param matchStory Last turn story
     * @return  Boolean if everything went well
     * @throws ChannelClosedException If connection is lost
     */
    public boolean tellMatchStory(Player player, MatchStory matchStory) throws ChannelClosedException {
        if(playerIsUser(player)) {
            User user = findUser(player);
            Type type = new TypeToken<List<String>>() {
            }.getType();
            return user.tellStory(new Gson().toJson(matchStory.story(), type));
        }
        return true;
    }
}
