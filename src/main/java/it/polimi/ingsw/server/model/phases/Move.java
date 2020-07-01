package it.polimi.ingsw.server.model.phases;

import it.polimi.ingsw.network.exceptions.*;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.*;

import java.util.List;

import static it.polimi.ingsw.server.model.Phase.MOVE;

public class Move implements TurnPhase {

    /**
     * This method executes move phase of the turn
     * @param player Current player
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
    public void executePhase(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, MatchStory matchStory) throws TimeOutException, ChannelClosedException {
        int phaseIndex = 1;
        actionController.initialisePossibleDestinations(player.turnSequence(), map);
        actionController.applyOpponentsCondition(player, opponents, phaseIndex, map);
        player.godCard().actions().get(phaseIndex).changePossibleOptions(player, actionController, map);
        if(!player.turnSequence().possibleDestinations().isEmpty()) {
            Box chosenBox = communicationController.chooseDestination(player, player.turnSequence().possibleDestinations());
            if (chosenBox != null) {
                player.turnSequence().setChosenBox(chosenBox);
                matchStory.addEvent(player.turnSequence().workersCurrentPosition(player.turnSequence().chosenWorker()).position(), matchStory.move, player.turnSequence().chosenBox().position());
                actionController.updateNewPositions(player.turnSequence());
                communicationController.updateView(player, map.createProxy());
                player.godCard().actions().get(phaseIndex).executeAction(player, communicationController, actionController, map, opponents, winConditions, matchStory);
                actionController.verifyWinCondition(MOVE, winConditions, player, map, opponents);
            }
        }
    }
}
