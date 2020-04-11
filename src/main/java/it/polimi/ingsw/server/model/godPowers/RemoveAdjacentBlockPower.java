package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.*;

import java.util.List;

public class RemoveAdjacentBlockPower implements BuildModifier {
    @Override
    public void changePossibleOptions(Player player, ActionController actionController, Map map) {

    }

    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) {
        //endPower - Ares
        boolean usePower = true;
        int i;
        Worker unmovedWorker = null;
        for(Worker worker : player.workers()){
            if(!player.turnSequence().movedWorkers().contains(worker)) {
                unmovedWorker = worker;
            }
        }
    }
}
