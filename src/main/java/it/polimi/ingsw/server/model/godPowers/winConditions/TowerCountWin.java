package it.polimi.ingsw.server.model.godPowers.winConditions;

import it.polimi.ingsw.server.model.Box;
import it.polimi.ingsw.server.model.Map;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.WinCondition;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.server.model.Phase.*;
import static it.polimi.ingsw.server.model.Target.*;

public class TowerCountWin extends WinCondition {
    private int numOfTowers;

    public TowerCountWin(int towers) {
        super(BUILD, ALL);
        this.numOfTowers = towers;
    }

    //Chronus Power

    /**
     * This method verifies if win condition is satisfied (if there is a specific number of complete towers in the map)
     * @param currentPlayer Current player
     * @param map Map of the match
     * @return Boolean that is true if win condition is satisfied
     */
    @Override
    public boolean establishWinCondition(Player currentPlayer, Map map) {
        List<Box> countedTowers = new ArrayList<>();

        for (Box builtOnBox:currentPlayer.turnSequence().builtOnBoxes()) {
            if (!countedTowers.contains(builtOnBox) && builtOnBox.isCompleteTower())
                countedTowers.add(builtOnBox); //number of towers completed in turn ++
        }

        int totalCompleteTowers = map.completeTowers() + countedTowers.size();

        return totalCompleteTowers >= numOfTowers;
    }
}