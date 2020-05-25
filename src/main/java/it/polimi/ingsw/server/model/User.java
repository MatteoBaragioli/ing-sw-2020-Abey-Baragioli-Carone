package it.polimi.ingsw.server.model;

import it.polimi.ingsw.network.CommunicationChannel;
import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.network.exceptions.ChannelClosedException;

import java.util.concurrent.TimeoutException;

public class User {
    final private CommunicationChannel communicationChannel;
    final private String name;

    public User(String name, CommunicationChannel communicationChannel){
        this.communicationChannel = communicationChannel;
        this.name = name;
    }

    public CommunicationChannel communicationChannel() {
        return communicationChannel;
    }

    public String name(){
        return name;
    }

    public boolean hasChannel() {
        return communicationChannel() != null;
    }

    /**
     * This method tells what matchtype was chosen by the user
     * @return 1 or 2 (-1 if an error occured)
     * @throws ChannelClosedException if connection is lost
     */
    public int askMatchType() throws ChannelClosedException {
        return communicationChannel().askMatchType();
    }

    /**
     * This method sends to the user his player and tells if it was received
     * @param player Avatar representing the user
     * @return boolean
     * @throws ChannelClosedException if connection is lost
     */
    public boolean sendMyPlayer(String player) throws ChannelClosedException {
        return communicationChannel().sendMyPlayer(player);
    }

    /**
     * This method sends to the user his player and tells if it was received
     * @param opponents The user's opponents
     * @return boolean
     * @throws ChannelClosedException if connection is lost
     */
    public boolean sendOpponents(String opponents) throws ChannelClosedException {
        return communicationChannel().sendOpponents(opponents);
    }

    /**
     * This method sends a list of positions and waits for the users choice
     * @param positions Json object
     * @return int
     * @throws TimeoutException if user doesn't answer
     * @throws ChannelClosedException if connection is lost
     */
    public int askStartPosition(String positions) throws TimeoutException, ChannelClosedException {
        return communicationChannel().askStartPosition(positions);
    }

    /**
     * This method sends a list of workers' positions and waits for the users choice
     * @param workers Json object
     * @return int
     * @throws TimeoutException if user doesn't answer
     * @throws ChannelClosedException if connection is lost
     */
    public int askWorker(String workers) throws TimeoutException, ChannelClosedException {
        return communicationChannel().askWorker(workers);
    }

    /**
     * This method sends a list of possible destinations and waits for the users choice
     * @param destinations Json object
     * @return int
     * @throws TimeoutException if user doesn't answer
     * @throws ChannelClosedException if connection is lost
     */
    public int askDestination(String destinations) throws TimeoutException, ChannelClosedException {
        return communicationChannel().askDestination(destinations);
    }

    /**
     * This method sends a list of build locations and waits for the users choice
     * @param builds Json object
     * @return int
     * @throws TimeoutException if user doesn't answer
     * @throws ChannelClosedException if connection is lost
     */
    public int askBuild(String builds) throws TimeoutException, ChannelClosedException {
        return communicationChannel().askBuild(builds);
    }

    /**
     * This method sends a list of blocks that can be removed and waits for the users choice
     * @param removals Json object
     * @return int
     * @throws TimeoutException if user doesn't answer
     * @throws ChannelClosedException if connection is lost
     */
    public int askRemoval(String removals) throws TimeoutException, ChannelClosedException {
        return communicationChannel().askRemoval(removals);
    }
}
