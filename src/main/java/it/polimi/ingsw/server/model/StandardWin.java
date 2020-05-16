package it.polimi.ingsw.server.model;

import static it.polimi.ingsw.server.model.Phase.*;
import static it.polimi.ingsw.server.model.Target.*;

public class StandardWin extends WinCondition {

    public StandardWin(){
        super(MOVE,SELF);
    }

    @Override
    public boolean establishWinCondition(Player currentPlayer, Map map) {
        if(map.levelDifference(currentPlayer.turnSequence().previousBox(),currentPlayer.turnSequence().workersCurrentPosition(currentPlayer.turnSequence().chosenWorker()))==1 && currentPlayer.turnSequence().workersCurrentPosition(currentPlayer.turnSequence().chosenWorker()).level()==3)
            return true;
        else
            return false;
    }

}