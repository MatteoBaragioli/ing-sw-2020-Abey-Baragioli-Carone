package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.network.exceptions.*;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.*;

import java.util.List;

import static it.polimi.ingsw.server.model.Phase.MOVE;

public class AddMoveNotStartingBoxPower extends MoveModifier{

    /**
     * This method initialises new possible destinations without starting position of the chosen worker and asks player to use power if he can do it
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
    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, MatchStory matchStory) throws TimeOutException, ChannelClosedException {
        actionController.verifyWinCondition(MOVE, winConditions, player, map, opponents);
        if(actionController.currentPlayerHasWon(player)){
            return;
        }
        actionController.initialisePossibleDestinations(player.turnSequence(), map);
        actionController.applyOpponentsCondition(player, opponents, 2, map);
        player.turnSequence().possibleDestinations().remove(player.turnSequence().previousBox()); //remove starting box
        if(!player.turnSequence().possibleDestinations().isEmpty()){
            boolean usePower = communicationController.chooseToUsePower(player);
            usePower(player, communicationController, actionController, map, opponents, winConditions, usePower, matchStory);
        }
    }

    /**
     * This method asks player to choose a box to move on, if player chose to use the power
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
    @Override
    public void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower, MatchStory matchStory) throws TimeOutException, ChannelClosedException {
        if(usePower) {
            Box chosenBox = communicationController.chooseDestination(player, player.turnSequence().possibleDestinations());
            if(chosenBox!=null) {
                executePower(player, actionController, chosenBox, matchStory);
                communicationController.updateView(player, map.createProxy());
            }
        }
    }

    /**
     * This method executes an additional move phase
     * @param player Player that has the card
     * @param actionController Action controller
     * @param chosenBox Chosen box
     * @param matchStory Last turn story
     */
    @Override
    public void executePower(Player player, ActionController actionController, Box chosenBox, MatchStory matchStory) {
        player.turnSequence().setChosenBox(chosenBox);
        matchStory.addEvent(player.turnSequence().workersCurrentPosition(player.turnSequence().chosenWorker()).position(), matchStory.move, player.turnSequence().chosenBox().position());
        actionController.updateNewPositions(player.turnSequence());
    }
}
