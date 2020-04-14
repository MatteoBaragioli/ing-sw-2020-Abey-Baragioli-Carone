package it.polimi.ingsw.server.model.phases;

import it.polimi.ingsw.server.model.*;

import java.util.List;

import static it.polimi.ingsw.server.model.Phase.BUILD;

public class Build  implements TurnPhase {

    @Override
    public void executePhase(Player currentPlayer, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) {
        int phaseIndex = 2;
        actionController.initialisePossibleBuilds(currentPlayer.turnSequence(), map);
        actionController.applyOpponentsCondition(currentPlayer, opponents, phaseIndex, map);
        currentPlayer.godCard().actions().get(phaseIndex).changePossibleOptions(currentPlayer, actionController, map);
        Box chosenBox = communicationController.chooseBox(currentPlayer.turnSequence().possibleDestinations());
        currentPlayer.turnSequence().setChosenBox(chosenBox);
        actionController.updateBuiltOnBox(currentPlayer.turnSequence());
        currentPlayer.godCard().actions().get(phaseIndex).executeAction(currentPlayer, communicationController, actionController, map, opponents, winConditions);
        actionController.verifyWinCondition(BUILD, winConditions, currentPlayer, map, opponents);
    }
}
