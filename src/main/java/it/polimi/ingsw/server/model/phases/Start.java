package it.polimi.ingsw.server.model.phases;

import it.polimi.ingsw.server.model.*;

import java.util.List;

import static it.polimi.ingsw.server.model.Phase.START;

public class Start implements TurnPhase {

    @Override
    public void executePhase(Player currentPlayer, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) {
        int phaseIndex = 0;

        for(Worker playerWorker : currentPlayer.workers()){
            currentPlayer.turnSequence().setChosenWorker(playerWorker); //salvo il worker nel chosenWorker
            actionController.initialisePossibleDestinations(currentPlayer.turnSequence(), map); //chiamo inzitialisePossibleDestination che segna dove può andare il chosenWorker della turnSequence
            actionController.applyOpponentsCondition(currentPlayer, opponents, 1, map);
            currentPlayer.godCard().actions().get(phaseIndex).changePossibleOptions(currentPlayer, actionController, map);
            if(!currentPlayer.turnSequence().possibleDestinations().isEmpty()){
                currentPlayer.turnSequence().addMovableWorker(playerWorker);
            }
        }
        actionController.applyOpponentsCondition(currentPlayer, opponents, phaseIndex, map);

        if(currentPlayer.turnSequence().movableWorkers().isEmpty()){
            communicationController.youLost();
            currentPlayer.playerIsOver();
        }

        currentPlayer.turnSequence().clearPossibleDestinations();
        currentPlayer.turnSequence().setChosenWorker(communicationController.chooseWorker(map));
        currentPlayer.godCard().actions().get(phaseIndex).changePossibleOptions(currentPlayer, actionController, map);
        currentPlayer.godCard().actions().get(phaseIndex).executeAction(currentPlayer, communicationController, actionController, map, opponents, winConditions);
        actionController.verifyWinCondition(START,winConditions, currentPlayer, map);
    }
}
