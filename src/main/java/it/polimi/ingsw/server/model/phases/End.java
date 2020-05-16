package it.polimi.ingsw.server.model.phases;

import it.polimi.ingsw.server.model.*;

import java.io.IOException;
import java.util.List;

import static it.polimi.ingsw.server.model.Phase.BUILD;
import static it.polimi.ingsw.server.model.Phase.END;

public class End implements TurnPhase {

    @Override
    public void executePhase(Player currentPlayer, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) throws IOException {
        int phaseIndex = 3;
        currentPlayer.godCard().actions().get(phaseIndex).changePossibleOptions(currentPlayer, actionController, map);
        currentPlayer.godCard().actions().get(phaseIndex).executeAction(currentPlayer, communicationController, actionController, map, opponents, winConditions);
        map.updateCompleteTowers(currentPlayer.turnSequence());
        actionController.verifyWinCondition(END, winConditions, currentPlayer,map,opponents );
        currentPlayer.turnSequence().confirmTurnSequence();
    }
}
