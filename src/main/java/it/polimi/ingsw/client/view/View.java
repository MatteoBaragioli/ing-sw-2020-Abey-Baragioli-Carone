package it.polimi.ingsw.client.view;

import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.network.exceptions.TimeOutException;
import it.polimi.ingsw.network.objects.BoxProxy;
import it.polimi.ingsw.network.objects.GodCardProxy;
import it.polimi.ingsw.network.objects.PlayerProxy;

import java.io.IOException;
import java.util.List;

public interface View {

    /**
     * This method asks a box from a given list. It is called for choosing workers starting position, worker for the current turn, destination of a move, a build or a remove
     * @param positions List of possible boxes
     * @return Chosen box
     * @throws TimeOutException Exception thrown when the time to do an action runs out
     */
    int askPosition(List<int[]> positions) throws TimeOutException;

    /**
     * This method asks to user to chose a card to use in this match
     * @param cards List of all cards chosen by the challenger
     * @return Chosen card
     * @throws TimeOutException Exception thrown when the time to do an action runs out
     */
    int askCards(List<GodCardProxy> cards) throws TimeOutException;

    /**
     * This method asks cards of the match to challenger
     * @param cards List of all cards of the game
     * @return Chosen cards indexes
     * @throws TimeOutException Exception thrown when the time to do an action runs out
     */
    int[] askDeck(List<GodCardProxy> cards) throws TimeOutException;

    /**
     * This method asks confirmation to player
     * @param key Key that tells which is the request for the player
     * @return Answer by the player
     * @throws TimeOutException Exception thrown when the time to do an action runs out
     */
    int askConfirmation(CommunicationProtocol key) throws TimeOutException;

    /**
     * This method asks ip to user
     * @return Ip address
     */
    String askIp();

    /**
     * This method asks match type to user
     * @return Match type
     * returns 1 for 2 players
     * returns 2 for 3 players
     */
    int askMatchType();

    /**
     * This method asks port to user
     * @return Port number
     */
    int askPort();

    /**
     * This method asks username to user
     * @param key Key that could be USERNAME or UNIQUE_USERNAME
     * @return Username
     * @throws IOException Needed for cli view
     */
    String askUserName(CommunicationProtocol key) throws IOException;

    /**
     * This method asks worker to move in current turn
     * @param workers List of movable workers
     * @return Chosen worker position
     * @throws TimeOutException Exception thrown when the time to do an action runs out
     */
    int askWorker(List<int[]> workers)throws TimeOutException;

    /**
     * This method allows views to show different messages base on the key
     * @param key Key that identifies different messages
     */
    void prepareAdditionalCommunication(CommunicationProtocol key);

    /**
     * This method updates the map with new positions and buildings
     * @param boxes List of all boxes of the map
     */
    void updateMap(List<BoxProxy> boxes);

    /**
     * This method sets player's infos
     * @param player Player infos
     */
    void setMyPlayer(PlayerProxy player);

    /**
     * This method sets opponents infos
     * @param players Opponents infos
     */
    void setOpponentsInfo(List<PlayerProxy> players);

    /**
     * This method shows error of connection refused
     * @param host Host that refused the connection
     */
    void connectionFailed(String host);

    /**
     * This method sets current player
     * @param player Current player
     */
    void setCurrentPlayer(PlayerProxy player);

    /**
     * This method tells what last player did in the last turn
     * @param events List of action made in the last turn
     */
    void tellStory(List<String> events);

    /**
     * This method tells who is the winner of the match
     * @param player Winner
     */
    void setWinner(PlayerProxy player);

    /**
     * This method tells if an opponent has lost the match
     * @param player Loser
     */
    void setLoser(PlayerProxy player);

    /**
     * This method shows error message of connection lost
     */
    void connectionLost();

    /**
     * This method tells if player's time to do his action runs out
     */
    void timeOut();
}
