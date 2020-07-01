package it.polimi.ingsw.server.model;

import it.polimi.ingsw.network.exceptions.*;
import it.polimi.ingsw.network.objects.MatchStory;

import java.util.List;

public abstract class MoveModifier implements TurnSequenceModifier {

    /**
     * This method adds or removes boxes from possible destinations list based on the god power
     * @param player Player that has the card
     * @param actionController Action controller
     * @param map Map of the match
     */
    public void changePossibleOptions(Player player, ActionController actionController, Map map) {
    }

    /**
     * This method executes actions before using the power
     * @param player Player that has the card
     * @param communicationController Communication controller
     * @param actionController Action controller
     * @param map Map of the match
     * @param opponents Player's opponents
     * @param winConditions List of win conditions
     * @param matchStory Last turn story
     * @throws TimeOutException Exception thrown when the time to do an action runs out
     * @throws ChannelClosedException Exception thrown when communication channel is closed
     */
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, MatchStory matchStory) throws TimeOutException, ChannelClosedException {
        //startPower-Prometheus
        boolean usePower = communicationController.chooseToUsePower(player);
        usePower(player, communicationController, actionController, map, opponents, winConditions, usePower, matchStory);

    }

    /**
     * This method executes the power
     * @param player Player that has the card
     * @param actionController Action controller
     * @param chosenBox Chosen box
     * @param matchStory Last turn story
     */
    public void executePower(Player player, ActionController actionController, Box chosenBox, MatchStory matchStory) {}

    /**
     * This method calls executePower if player chooses to use the power
     * @param player Player that has the card
     * @param communicationController Communication controller
     * @param actionController Action controller
     * @param map Map of the match
     * @param opponents Player's opponents
     * @param winConditions List of win conditions
     * @param usePower Boolean that is true if player chose to use the power
     * @param matchStory Last turn story
     * @throws TimeOutException Exception thrown when the time to do an action runs out
     * @throws ChannelClosedException Exception thrown when communication channel is closed
     */
    public void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower, MatchStory matchStory) throws TimeOutException, ChannelClosedException {}

}
