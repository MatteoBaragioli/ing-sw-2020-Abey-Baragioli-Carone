package it.polimi.ingsw.server.model;

import it.polimi.ingsw.network.exceptions.*;
import it.polimi.ingsw.network.objects.MatchStory;

import java.util.List;

public abstract class MoveModifier implements TurnSequenceModifier {
    public void changePossibleOptions(Player player, ActionController actionController, Map map) {

    }

    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, MatchStory matchStory) throws TimeOutException, ChannelClosedException {
        //startPower-Prometheus
        boolean usePower = communicationController.chooseToUsePower(player);
        usePower(player, communicationController, actionController, map, opponents, winConditions, usePower, matchStory);

    }

    public void executePower(Player player, ActionController actionController, Box chosenBox, MatchStory matchStory) {}

    public void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower, MatchStory matchStory) throws TimeOutException, ChannelClosedException {}

}
