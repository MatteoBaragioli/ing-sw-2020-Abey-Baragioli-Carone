package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.*;

import java.util.List;

public class OpponenentsCantMoveUpIfPlayerMovesUpPower extends MoveModifier {

    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) {
        //endPower - Athena
        if(map.levelDifference(player.turnSequence().previousBox(), player.turnSequence().workersCurrentPosition(player.turnSequence().chosenWorker()))>0){
            for(Player opponent : opponents) {
                opponent.turnSequence().setAllowedLevelDifference(0);
            }
        }
    }
}
