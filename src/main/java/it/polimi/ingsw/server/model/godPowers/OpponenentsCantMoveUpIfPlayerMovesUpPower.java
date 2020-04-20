package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.*;

import java.util.List;

public class OpponenentsCantMoveUpIfPlayerMovesUpPower implements MoveModifier {
    @Override
    public void changePossibleOptions(Player player, ActionController actionController, Map map) {

    }

    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) {
        //endPower - Athena
        if(map.levelDifference(player.turnSequence().previousBox(), player.turnSequence().newPositions().get(player.turnSequence().chosenWorker()))>0){
            for(Player opponent : opponents) {
                opponent.turnSequence().setAllowedLevelDifference(0);
            }
        }
    }

    @Override
    public void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower) {

    }

    @Override
    public void executePower(Player player, ActionController actionController, Box chosenBox) {

    }
}
