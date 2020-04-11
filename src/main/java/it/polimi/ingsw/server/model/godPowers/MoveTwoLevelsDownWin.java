package it.polimi.ingsw.server.model.godPowers;

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
        if(map.levelDifference(currentPlayer.turnSequence().workersCurrentPosition(currentPlayer.turnSequence().chosenWorker()),currentPlayer.turnSequence().previousBox())<-1)
            return true;
        else
            return false;
    }
}
