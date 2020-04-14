package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.Box;
import it.polimi.ingsw.server.model.Map;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.WinCondition;

import static it.polimi.ingsw.server.model.Phase.*;
import static it.polimi.ingsw.server.model.Target.*;

public class TowerCountWin extends WinCondition {
    int numOfTowers;

    public TowerCountWin(int towers) {
        super(BUILD, ALL);
        this.numOfTowers = towers;
    }

    //Chronus Power
    @Override
    public boolean establishWinCondition(Player currentPlayer, Map map) {
        int completedInTurn = 0;
        int totalCompleteTowers = map.completeTowers();
        for (Box builtOnBox:currentPlayer.turnSequence().builtOnBoxes()) {
            if (builtOnBox.isCompleteTower())
                completedInTurn++; //number of towers completed in turn ++
        }
        totalCompleteTowers = totalCompleteTowers + completedInTurn;
        if (totalCompleteTowers >= numOfTowers)
            return true;
        else
            return false;

    }
}