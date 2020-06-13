package it.polimi.ingsw.server.model.phases;

import it.polimi.ingsw.network.exceptions.*;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.*;

import java.util.List;

import static it.polimi.ingsw.server.model.Phase.START;

public class Start implements TurnPhase {
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
