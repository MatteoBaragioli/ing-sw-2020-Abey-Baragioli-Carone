package it.polimi.ingsw.server.model;

import static it.polimi.ingsw.server.model.Phase.MOVE;
import static it.polimi.ingsw.server.model.Target.SELF;

public class StandardWin extends WinCondition {

    public StandardWin(){
        super(MOVE,SELF);
    }

    /**
     * This method verifies if standard win is satisfied for the current player
     * @param currentPlayer Current player
     * @param map Map of the match
     * @return Boolean that is true if standard win is satisfied
     */
    @Override
    public boolean establishWinCondition(Player currentPlayer, Map map) {
        return map.levelDifference(currentPlayer.turnSequence().previousBox(), currentPlayer.turnSequence().workersCurrentPosition(currentPlayer.turnSequence().chosenWorker())) == 1 && currentPlayer.turnSequence().workersCurrentPosition(currentPlayer.turnSequence().chosenWorker()).level() == 3;
    }
}
