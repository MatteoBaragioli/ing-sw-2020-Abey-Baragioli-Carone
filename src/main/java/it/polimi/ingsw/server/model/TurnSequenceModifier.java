package it.polimi.ingsw.server.model;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public interface TurnSequenceModifier {
    void changePossibleOptions(Player player, ActionController actionController, Map map);

    void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) throws TimeoutException, ChannelClosedException;

    void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower) throws TimeoutException, ChannelClosedException;

    void executePower(Player player, ActionController actionController, Box chosenBox);
}
