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

    @Override
    public boolean establishWinCondition(Player currentPlayer, Map map) {
        if(map.levelDifference(currentPlayer.turnSequence().previousBox(), currentPlayer.turnSequence().workersCurrentPosition(currentPlayer.turnSequence().chosenWorker()))<-1)
            return true;
        else
            return false;
    }
}
