package it.polimi.ingsw.server.model;

import it.polimi.ingsw.network.exceptions.*;
import it.polimi.ingsw.network.objects.MatchStory;

import java.util.List;

public interface TurnSequenceModifier {
    void changePossibleOptions(Player player, ActionController actionController, Map map);

    void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, MatchStory matchStory) throws TimeOutException, ChannelClosedException;

    void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower, MatchStory matchStory) throws TimeOutException, ChannelClosedException;

    void executePower(Player player, ActionController actionController, Box chosenBox, MatchStory matchStory);
}
