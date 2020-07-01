package it.polimi.ingsw.server.model.phases;

import it.polimi.ingsw.network.exceptions.*;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.*;

import java.util.List;

import static it.polimi.ingsw.server.model.Phase.END;

public class End implements TurnPhase {

    /**
     * This method executes end phase of the turn
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
        int phaseIndex = 3;
        currentPlayer.godCard().actions().get(phaseIndex).changePossibleOptions(currentPlayer, actionController, map);
        currentPlayer.godCard().actions().get(phaseIndex).executeAction(currentPlayer, communicationController, actionController, map, opponents, winConditions, matchStory);
        map.updateCompleteTowers(currentPlayer.turnSequence());
        actionController.verifyWinCondition(END, winConditions, currentPlayer,map,opponents );
        currentPlayer.turnSequence().confirmTurnSequence();
    }
}
