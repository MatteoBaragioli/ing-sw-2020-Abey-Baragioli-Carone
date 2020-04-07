package it.polimi.ingsw.server.model;

import static it.polimi.ingsw.server.model.Phase.*;
import static it.polimi.ingsw.server.model.Target.*;

public class StandardWin extends WinCondition {

    public StandardWin(){
        super(MOVE,SELF);
    }

    @Override
    public boolean establishWinCondition(Player currentPlayer, Map map) {
        return true;
    }

}