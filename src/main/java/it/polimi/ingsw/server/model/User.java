package it.polimi.ingsw.server.model;

import it.polimi.ingsw.network.*;
import it.polimi.ingsw.network.exceptions.*;

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
        try {
            return communicationChannel().askMatchType();
        } catch (ChannelClosedException e) {
            e.setName(name());
            throw e;
        }
    }

    /**
     * This method sends to the user his player and tells if it was received
     * @param player Avatar representing the user
     * @return boolean
     * @throws ChannelClosedException if connection is lost
     */
    public boolean sendMyPlayer(String player) throws ChannelClosedException {
        try {
            return communicationChannel().sendMyPlayer(player);
        } catch (ChannelClosedException e) {
            e.setName(name());
            throw e;
        }
    }

    /**
     * This method sends to the user his player and tells if it was received
     * @param opponents The user's opponents
     * @return boolean
     * @throws ChannelClosedException if connection is lost
     */
    public boolean sendOpponents(String opponents) throws ChannelClosedException {
        try {
            return communicationChannel().sendOpponents(opponents);
        } catch (ChannelClosedException e) {
            e.setName(name());
            throw e;
        }
    }

    /**
     * This method sends to the user the current player and tells if it was received
     * @param currentPlayer Avatar representing the current player
     * @return boolean
     * @throws ChannelClosedException if connection is lost
     */
    public boolean sendCurrentPlayer(String currentPlayer) throws ChannelClosedException {
        try {
            return communicationChannel().sendCurrentPlayer(currentPlayer);
        } catch (ChannelClosedException e) {
            e.setName(name());
            throw e;
        }
    }

    public boolean sendWinner(String winner) throws ChannelClosedException {
        try {
            return communicationChannel().sendWinner(winner);
        } catch (ChannelClosedException e) {
            e.setName(name());
            throw e;
        }
    }

    public boolean sendLoser(String loser) throws ChannelClosedException {
        try {
            return communicationChannel().sendLoser(loser);
        } catch (ChannelClosedException e) {
            e.setName(name());
            throw e;
        }
    }

    /**
     * This method sends to the user the map and tells if it was received
     * @param map
     * @return boolean
     * @throws ChannelClosedException
     */
    public boolean sendMap(String map) throws ChannelClosedException {
        try {
            return communicationChannel().sendMap(map);
        } catch (ChannelClosedException e) {
            e.setName(name());
            throw e;
        }
    }

    /**
     * This method sends to the user the turn story and tells if it was received
     * @param story Strings List
     * @return boolean
     * @throws ChannelClosedException
     */
    public boolean tellStory(String story) throws ChannelClosedException {
        try {
            return communicationChannel().sendStory(story);
        } catch (ChannelClosedException e) {
            e.setName(name());
            throw e;
        }
    }

    /**
     * This method sends a list of positions and waits for the users choice
     * @param positions Json object
     * @return int
     * @throws TimeOutException if user doesn't answer
     * @throws ChannelClosedException if connection is lost
     */
    public int askStartPosition(String positions) throws TimeOutException, ChannelClosedException {
        try {
            return communicationChannel().askStartPosition(positions);
        } catch (TimeOutException e) {
            e.setName(name());
            throw e;
        } catch (ChannelClosedException e) {
            e.setName(name());
            throw e;
        }
    }

    /**
     * This method sends a list of workers' positions and waits for the users choice
     * @param workers Json object
     * @return int
     * @throws TimeOutException if user doesn't answer
     * @throws ChannelClosedException if connection is lost
     */
    public int askWorker(String workers) throws TimeOutException, ChannelClosedException {
        try {
            return communicationChannel().askWorker(workers);
        } catch (TimeOutException e) {
            e.setName(name());
            throw e;
        } catch (ChannelClosedException e) {
            e.setName(name());
            throw e;
        }
    }

    /**
     * This method sends a list of possible destinations and waits for the users choice
     * @param destinations Json object
     * @return int
     * @throws TimeOutException if user doesn't answer
     * @throws ChannelClosedException if connection is lost
     */
    public int askDestination(String destinations) throws TimeOutException, ChannelClosedException {
        try {
            return communicationChannel().askDestination(destinations);
        } catch (TimeOutException e) {
            e.setName(name());
            throw e;
        } catch (ChannelClosedException e) {
            e.setName(name());
            throw e;
        }
    }

    /**
     * This method sends a list of build locations and waits for the users choice
     * @param builds Json object
     * @return int
     * @throws TimeOutException if user doesn't answer
     * @throws ChannelClosedException if connection is lost
     */
    public int askBuild(String builds) throws TimeOutException, ChannelClosedException {
        try {
            return communicationChannel().askBuild(builds);
        } catch (TimeOutException e) {
            e.setName(name());
            throw e;
        } catch (ChannelClosedException e) {
            e.setName(name());
            throw e;
        }
    }

    /**
     * This method sends a list of blocks that can be removed and waits for the users choice
     * @param removals Json object
     * @return int
     * @throws TimeOutException if user doesn't answer
     * @throws ChannelClosedException if connection is lost
     */
    public int askRemoval(String removals) throws TimeOutException, ChannelClosedException {
        try {
            return communicationChannel().askRemoval(removals);
        } catch (TimeOutException e) {
            e.setName(name());
            throw e;
        } catch (ChannelClosedException e) {
            e.setName(name());
            throw e;
        }
    }

    /**
     * this method sends a list cards to choose from
     * @param cards json object
     * @return int
     * @throws TimeOutException
     * @throws ChannelClosedException
     */
    public int askCard (String cards) throws TimeOutException, ChannelClosedException {
        try {
            return communicationChannel().askCard(cards);
        } catch (TimeOutException e) {
            e.setName(name());
            throw e;
        } catch (ChannelClosedException e) {
            e.setName(name());
            throw e;
        }
    }

    /**
     * this method sends a list cards to choose from
     * @param deck json object
     * @return int
     * @throws TimeOutException
     * @throws ChannelClosedException
     */
    public int[] askDeck (String deck) throws TimeOutException, ChannelClosedException {
        try {
            return communicationChannel().askDeck(deck);
        } catch (TimeOutException e) {
          e.setName(name());
          throw e;
        } catch (ChannelClosedException e) {
            e.setName(name());
            throw e;
        }
    }

    /**
     * this method asks for user confirmation, either to use a godpower or undo their turn, and waits for the users choice
     * @param key key of coms protocol
     * @return boolean
     * @throws TimeOutException
     * @throws ChannelClosedException
     */
    public boolean askConfirmation(CommunicationProtocol key) throws TimeOutException, ChannelClosedException {
        try {
            return communicationChannel().askConfirmation(key);
        } catch (TimeOutException e) {
            e.setName(name());
            throw e;
        } catch (ChannelClosedException e) {
            e.setName(name());
            throw e;
        }
    }
}
