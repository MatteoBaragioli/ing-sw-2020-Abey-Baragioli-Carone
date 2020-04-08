package it.polimi.ingsw.server.model.phases;

import it.polimi.ingsw.server.model.*;

import java.util.List;

import static it.polimi.ingsw.server.model.Phase.MOVE;

public class Move implements TurnPhase {

    @Override
    public void executePhase(Player currentPlayer, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) {
        int phaseIndex = 2;
        actionController.initialisePossibleDestinations(currentPlayer.turnSequence(), map);
        actionController.applyOpponentsCondition(currentPlayer, opponents, phaseIndex, map);
        currentPlayer.godCard().actions().get(phaseIndex).changePossibleOptions(currentPlayer, actionController, map);
        Box targetBox = communicationController.chooseBox();
        currentPlayer.godCard().actions().get(phaseIndex).executeAction(currentPlayer, communicationController, actionController, map);
        actionController.updateNewPositions(currentPlayer.turnSequence(), targetBox);
        actionController.verifyWinCondition(MOVE, winConditions, currentPlayer, map, opponents);
    }
}
