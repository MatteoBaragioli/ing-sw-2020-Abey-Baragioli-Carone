package it.polimi.ingsw.server.model.phases;

import it.polimi.ingsw.server.model.*;

import java.util.List;

import static it.polimi.ingsw.server.model.Phase.START;

public class Start implements TurnPhase {

    @Override
    public void executePhase(Player currentPlayer, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) {
        int phaseIndex = 0;
        actionController.initialiseMovableWorker(currentPlayer, map);
        actionController.applyOpponentsCondition(currentPlayer, opponents, phaseIndex, map);
        currentPlayer.godCard().actions().get(phaseIndex).changePossibleOptions(currentPlayer, actionController, map);
        if(currentPlayer.turnSequence().movableWorkers().isEmpty()) {
            //todo eliminare il giocatore
            currentPlayer.setInGame(false);
            return;
        }
        Worker chosenWorker = communicationController.chooseWorker(currentPlayer.turnSequence().movableWorkers());
        currentPlayer.turnSequence().setChosenWorker(chosenWorker);
        currentPlayer.godCard().actions().get(phaseIndex).executeAction(currentPlayer, communicationController, actionController, map, opponents, winConditions);
        actionController.verifyWinCondition(START,winConditions, currentPlayer,map,opponents );
    }
}
