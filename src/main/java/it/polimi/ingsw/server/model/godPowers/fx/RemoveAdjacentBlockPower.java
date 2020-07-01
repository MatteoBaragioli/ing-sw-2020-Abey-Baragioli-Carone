package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.network.exceptions.*;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.*;

import java.util.List;

public class RemoveAdjacentBlockPower extends BuildModifier{

    /**
     * This method initialises possible removals for the unmoved worker of this turn and asks player to use power if he can do it
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
        //buildPower - Ares
        for (Worker worker : player.workers()) {
            if (!player.turnSequence().movedWorkers().contains(worker)) {
                player.turnSequence().setChosenWorker(worker);
                player.turnSequence().clearPossibleBuilds();
                for(Box box : map.adjacent(player.turnSequence().chosenWorker().position())){
                    if(box.level() > 0 && box.isFree()){
                        player.turnSequence().addPossibleBuild(box);
                    }
                }
                if(!player.turnSequence().possibleBuilds().isEmpty()) {
                    boolean usePower = communicationController.chooseToUsePower(player);
                    usePower(player, communicationController, actionController, map, opponents, winConditions, usePower, matchStory);
                }
            }
        }
    }

    /**
     * This method asks player to choose a box to remove a block, if player chose to use the power
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
            Box chosenBox = communicationController.chooseRemoval(player, player.turnSequence().possibleBuilds());
            if (chosenBox != null) {
                executePower(player, actionController, chosenBox, matchStory);
                communicationController.updateView(player, map.createProxy());
            }
        }
    }

    /**
     * This method removes a block on the chosen box
     * @param player Player that has the card
     * @param actionController Action controller
     * @param chosenBox Chosen box
     * @param matchStory Last turn story
     */
    @Override
    public void executePower(Player player, ActionController actionController, Box chosenBox, MatchStory matchStory) {
        player.turnSequence().setChosenBox(chosenBox);
        matchStory.addEvent(player.turnSequence().workersCurrentPosition(player.turnSequence().chosenWorker()).position(), matchStory.removal, player.turnSequence().chosenBox().position());
        player.turnSequence().chosenBox().removeBlock();
        player.turnSequence().recordRemovedBlock(player.turnSequence().chosenBox());
    }
}
