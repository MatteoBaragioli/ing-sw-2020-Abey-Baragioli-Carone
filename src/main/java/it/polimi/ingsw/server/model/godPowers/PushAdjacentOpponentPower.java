package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.*;

import java.util.List;

public class PushAdjacentOpponentPower implements MoveModifier {
    @Override
    public void changePossibleOptions(Player player, ActionController actionController, Map map) {
        Box workerBox = player.turnSequence().chosenWorker().position();
        for(Box box : map.adjacent(workerBox)){
            if(box.isOccupiedByWorkers() && !player.workers().contains(box.occupier()) && map.levelDifference(workerBox, box)<=player.turnSequence().allowedLevelDifference() && !map.boxesSameDirection(workerBox,box).isEmpty() && map.boxesSameDirection(workerBox,box).get(0).isFree())
                player.turnSequence().addPossibleDestination(box);
        }
    }

    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) {
        //movePower - Minotaur
        for(Worker worker : player.turnSequence().movedWorkers()){
            if(!player.workers().contains(worker) && player.turnSequence().workersCurrentPosition(worker).equals(player.turnSequence().workersCurrentPosition(player.turnSequence().chosenWorker()))){
                player.turnSequence().recordNewPosition(worker, map.boxesSameDirection(player.turnSequence().previousBox(), player.turnSequence().chosenBox()).get(0));
            }
        }
    }
}
