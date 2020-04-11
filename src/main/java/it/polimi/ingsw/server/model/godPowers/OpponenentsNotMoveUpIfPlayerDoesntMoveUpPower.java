package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.*;

import java.util.List;

public class OpponenentsNotMoveUpIfPlayerDoesntMoveUpPower implements MoveModifier {
    @Override
    public void changePossibleOptions(Player player, ActionController actionController, Map map) {

    }

    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) {
        //movePower - Athena
        if(map.levelDifference(player.turnSequence().chosenBox(), player.turnSequence().newPositions().get(player.turnSequence().chosenWorker()))>0){
            for(Player opponent : opponents) {
                opponent.turnSequence().setAllowedLevelDifference(0);
            }
        }
    }
}
