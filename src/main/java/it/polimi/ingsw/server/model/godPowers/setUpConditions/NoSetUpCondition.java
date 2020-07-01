package it.polimi.ingsw.server.model.godPowers.setUpConditions;

import it.polimi.ingsw.server.model.Box;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.SetUpCondition;

import java.util.ArrayList;
import java.util.List;

public class NoSetUpCondition implements SetUpCondition {

    /**
     * This method do not change anything in set up (for powers that don't have set up power)
     * @param currentPlayer Current player
     * @param players All players of the match
     */
    @Override
    public void modifySetUpOrder(Player currentPlayer, List<Player> players) {
    }

    /**
     * This method do not change anything in set up (for powers that don't have set up power)
     * @param currentPlayer Current player
     * @param freeMap Free boxes of the match
     * @return List of possible destination based on the power
     */
    @Override
    public List<Box> applySetUpCondition(Player currentPlayer, List<Box> freeMap) {
        return new ArrayList<>(freeMap);
    }
}
