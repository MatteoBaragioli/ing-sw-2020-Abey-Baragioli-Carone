package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.Box;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.SetUpCondition;

import java.util.ArrayList;
import java.util.List;

public class NoSetUpCondition implements SetUpCondition {
    @Override
    public void modifySetUpOrder(Player currentPlayer, List<Player> players) {

    }

    @Override
    public List<Box> applySetUpCondition(Player currentPlayer, List<Box> freeMap) {
        return new ArrayList<>(freeMap);
    }
}
