package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.Map;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.WinCondition;

import static it.polimi.ingsw.server.model.Phase.*;
import static it.polimi.ingsw.server.model.Target.*;

public class CounterTowersBuiltWin extends WinCondition {

    public CounterTowersBuiltWin(){
        super(MOVE,SELF);
    }

    @Override
    public boolean establishWinCondition(Player currentPlayer, Map map) {
        //Chronus Power
        return true;
    }
}
