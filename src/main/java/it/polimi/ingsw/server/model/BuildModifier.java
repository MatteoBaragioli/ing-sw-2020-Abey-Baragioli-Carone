package it.polimi.ingsw.server.model;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.network.objects.MatchStory;

import java.util.List;
import java.util.concurrent.TimeoutException;

public abstract class BuildModifier implements  TurnSequenceModifier{

    public void changePossibleOptions(Player player, ActionController actionController, Map map) {}

    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, MatchStory matchStory) throws TimeoutException, ChannelClosedException {
        boolean usePower = communicationController.chooseToUsePower(player);
        usePower(player, communicationController, actionController, map, opponents, winConditions, usePower, matchStory);

    }

    public void executePower(Player player, ActionController actionController, Box chosenBox, MatchStory matchStory) {}

    public void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower, MatchStory matchStory) throws TimeoutException, ChannelClosedException {}
}
