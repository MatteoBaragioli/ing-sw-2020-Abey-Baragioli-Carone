package it.polimi.ingsw.server.model.phases;

import it.polimi.ingsw.network.exceptions.*;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.*;

import java.util.List;

import static it.polimi.ingsw.server.model.Phase.START;

public class Start implements TurnPhase {

    /**
     * This method executes start phase of the turn
     * @param currentPlayer Current player
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
    public void executePhase(Player currentPlayer, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, MatchStory matchStory) throws TimeOutException, ChannelClosedException {
        int phaseIndex = 0;
        if(!actionController.canPlayerPlay(currentPlayer, opponents, map)){
            currentPlayer.playerIsOver();
            return;
        }
        Worker chosenWorker = communicationController.chooseWorker(currentPlayer, currentPlayer.turnSequence().movableWorkers());
        currentPlayer.turnSequence().setChosenWorker(chosenWorker);
        currentPlayer.godCard().actions().get(phaseIndex).executeAction(currentPlayer, communicationController, actionController, map, opponents, winConditions, matchStory);
        actionController.verifyWinCondition(START,winConditions, currentPlayer,map,opponents);
    }

}
