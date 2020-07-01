package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.*;

import java.util.List;

public class SwapPower extends MoveModifier {

    /**
     * This method adds an occupied box (occupied by an opponent worker) into possible destinations list if the occupied box could be a destination if it wasn't occupied
     * @param player Player that has the card
     * @param actionController Action controller
     * @param map Map of the match
     */
    @Override
    public void changePossibleOptions(Player player, ActionController actionController, Map map) {
        Box workerBox = player.turnSequence().chosenWorker().position();
        for(Box box : map.adjacent(workerBox)){
            if(box.isOccupiedByWorkers() && !player.workers().contains(box.occupier()) && map.levelDifference(workerBox, box)<=player.turnSequence().allowedLevelDifference())
                player.turnSequence().addPossibleDestination(box);
        }
    }

    /**
     * This method moves chosen worker into occupied box and moves opponent worker into chosen worker starting position, if user chose an occupied box from possible destinations list
     * @param player Player that has the card
     * @param communicationController Communication controller
     * @param actionController Action controller
     * @param map Map of the match
     * @param opponents Player's opponents
     * @param winConditions List of win conditions
     * @param matchStory Last turn story
     * @throws ChannelClosedException Exception thrown when communication channel is closed
     */
    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, MatchStory matchStory) throws ChannelClosedException {
        //movePower-Apollo
        for(Worker worker : player.turnSequence().movedWorkers()) {
            if (!player.workers().contains(worker) && player.turnSequence().workersCurrentPosition(worker).equals(player.turnSequence().workersCurrentPosition(player.turnSequence().chosenWorker()))) {
                player.turnSequence().recordNewPosition(worker, player.turnSequence().previousBox());
            }
        }
        communicationController.updateView(player, map.createProxy());
    }
}
