package it.polimi.ingsw.server.model.phases;

import it.polimi.ingsw.server.model.*;

import java.util.List;

import static it.polimi.ingsw.server.model.Phase.MOVE;

public class Move implements TurnPhase {

    @Override
    public void executePhase(Player currentPlayer, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) {
        int phaseIndex = 1;
        actionController.initialisePossibleDestinations(currentPlayer.turnSequence(), map);
        actionController.applyOpponentsCondition(currentPlayer, opponents, phaseIndex, map);
        currentPlayer.godCard().actions().get(phaseIndex).changePossibleOptions(currentPlayer, actionController, map);
        Box chosenBox = communicationController.chooseBox(map);
        currentPlayer.turnSequence().setChosenBox(communicationController.chooseBox(map));
        currentPlayer.godCard().actions().get(phaseIndex).executeAction(currentPlayer, communicationController, actionController, map, opponents, winConditions);
        actionController.updateNewPositions(currentPlayer.turnSequence());
        actionController.verifyWinCondition(MOVE, winConditions, currentPlayer, map);
    }
}
