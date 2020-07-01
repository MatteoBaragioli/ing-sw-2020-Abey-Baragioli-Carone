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
     * This method tells what match type was chosen by the user
     * @return 1 or 2 (-1 if an error occurred)
     * @throws ChannelClosedException If connection is lost
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
     * @return Boolean that is true if user received his player
     * @throws ChannelClosedException If connection is lost
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
     * This method sends to the user his opponents and tells if they were received
     * @param opponents The user's opponents
     * @return Boolean that is true if user received his opponents
     * @throws ChannelClosedException If connection is lost
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
     * @return Boolean that is true if user received the current player
     * @throws ChannelClosedException If connection is lost
     */
    public boolean sendCurrentPlayer(String currentPlayer) throws ChannelClosedException {
        try {
            return communicationChannel().sendCurrentPlayer(currentPlayer);
        } catch (ChannelClosedException e) {
            e.setName(name());
            throw e;
        }
    }

    /**
     * This method sends to the user the winner and tells if it was received
     * @param winner Avatar representing the winner
     * @return Boolean that is true if user received the winner
     * @throws ChannelClosedException If connection is lost
     */
    public boolean sendWinner(String winner) throws ChannelClosedException {
        try {
            return communicationChannel().sendWinner(winner);
        } catch (ChannelClosedException e) {
            e.setName(name());
            throw e;
        }
    }

    /**
     * This method sends to the user the loser and tells if it was received
     * @param loser Avatar representing the loser
     * @return Boolean that is true if user received the loser
     * @throws ChannelClosedException If connection is lost
     */
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
     * @param map Map of the match
     * @return Boolean that is true if user received the map
     * @throws ChannelClosedException If connection is lost
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
     * @param story Strings List (turn story)
     * @return Boolean that is true if user received the turn story
     * @throws ChannelClosedException If connection is lost
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
     * @param positions Json object (choosable positions)
     * @return Index of the chosen position
     * @throws TimeOutException If user doesn't answer
     * @throws ChannelClosedException If connection is lost
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
     * @param workers Json object (choosable workers)
     * @return Index of the chosen worker
     * @throws TimeOutException If user doesn't answer
     * @throws ChannelClosedException If connection is lost
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
     * @param destinations Json object (choosable destinations)
     * @return Index of the chosen destination
     * @throws TimeOutException If user doesn't answer
     * @throws ChannelClosedException If connection is lost
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
     * @param builds Json object (choosable locations)
     * @return Index of the chosen location
     * @throws TimeOutException If user doesn't answer
     * @throws ChannelClosedException If connection is lost
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
     * @param removals Json object (removable blocks)
     * @return Index of the chosen block
     * @throws TimeOutException If user doesn't answer
     * @throws ChannelClosedException If connection is lost
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
     * This method sends a list cards to choose from
     * @param cards Json object (choosable cards)
     * @return Index of the chosen card
     * @throws TimeOutException If user doesn't answer
     * @throws ChannelClosedException If connection is lost
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
     * This method sends the list of all cards to choose from
     * @param deck Json object (cards of the game)
     * @return Index of the chosen card
     * @throws TimeOutException If user doesn't answer
     * @throws ChannelClosedException If connection is lost
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
     * This method asks for user confirmation, either to use a godpower or undo their turn, and waits for the users choice
     * @param key Key of communication protocol, that specifies which is the request (use god power or undo the turn)
     * @return Answer of the player
     * @throws TimeOutException If user doesn't answer
     * @throws ChannelClosedException If connection is lost
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
