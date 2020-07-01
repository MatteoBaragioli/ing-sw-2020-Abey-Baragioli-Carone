package it.polimi.ingsw.server.model.godPowers.winConditions;

import it.polimi.ingsw.server.model.Map;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.WinCondition;

import static it.polimi.ingsw.server.model.Phase.*;
import static it.polimi.ingsw.server.model.Target.*;

public class MoveTwoLevelsDownWin extends WinCondition {

    public MoveTwoLevelsDownWin(){
        super(MOVE,SELF);
    }

    /**
     * This method verifies if win condition is satisfied (if player moved two or more levels down)
     * @param currentPlayer Current player
     * @param map Map of the match
     * @return Boolean that is true if win condition is satisfied
     */
    @Override
    public boolean establishWinCondition(Player currentPlayer, Map map) {
        return map.levelDifference(currentPlayer.turnSequence().previousBox(), currentPlayer.turnSequence().workersCurrentPosition(currentPlayer.turnSequence().chosenWorker())) < -1;
    }
}
