package it.polimi.ingsw.server.model.phases;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.server.model.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static it.polimi.ingsw.server.model.Phase.MOVE;

public class Move implements TurnPhase {
    @Override
    public void executePhase(Player currentPlayer, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) throws TimeoutException, ChannelClosedException {
        int phaseIndex = 1;
        actionController.initialisePossibleDestinations(currentPlayer.turnSequence(), map);
        actionController.applyOpponentsCondition(currentPlayer, opponents, phaseIndex, map);
        currentPlayer.godCard().actions().get(phaseIndex).changePossibleOptions(currentPlayer, actionController, map);
        if(!currentPlayer.turnSequence().possibleDestinations().isEmpty()) {
            Box chosenBox = communicationController.chooseDestination(currentPlayer, currentPlayer.turnSequence().possibleDestinations());
            if (chosenBox != null) {
                currentPlayer.turnSequence().setChosenBox(chosenBox);
                actionController.updateNewPositions(currentPlayer.turnSequence());
                communicationController.updateView(currentPlayer, map.createProxy());
                currentPlayer.godCard().actions().get(phaseIndex).executeAction(currentPlayer, communicationController, actionController, map, opponents, winConditions);
                actionController.verifyWinCondition(MOVE, winConditions, currentPlayer, map, opponents);
            }
        }
    }
}
