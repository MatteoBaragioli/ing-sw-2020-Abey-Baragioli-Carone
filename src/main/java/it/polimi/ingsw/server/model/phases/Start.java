package it.polimi.ingsw.server.model.phases;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.server.model.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static it.polimi.ingsw.server.model.Phase.START;

public class Start implements TurnPhase {
    @Override
    public void executePhase(Player currentPlayer, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) throws TimeoutException, ChannelClosedException {
        int phaseIndex = 0;
        if(!actionController.canPlayerPlay(currentPlayer, opponents, map)){
            currentPlayer.playerIsOver();
            return;
        }
        Worker chosenWorker = communicationController.chooseWorker(currentPlayer, currentPlayer.turnSequence().movableWorkers());
        currentPlayer.turnSequence().setChosenWorker(chosenWorker);
        currentPlayer.godCard().actions().get(phaseIndex).executeAction(currentPlayer, communicationController, actionController, map, opponents, winConditions);
        actionController.verifyWinCondition(START,winConditions, currentPlayer,map,opponents);
    }

}
