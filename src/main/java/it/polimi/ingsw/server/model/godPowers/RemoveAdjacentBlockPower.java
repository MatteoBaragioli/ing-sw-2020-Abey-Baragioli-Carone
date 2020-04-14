package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.*;

import java.util.List;

public class RemoveAdjacentBlockPower implements BuildModifier {
    @Override
    public void changePossibleOptions(Player player, ActionController actionController, Map map) {

    }

    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) {
        //buildPower - Ares
        int i;
        boolean usePower = communicationController.chooseToUsePower();
        if(usePower) {
            for (Worker worker : player.workers()) {
                if (!player.turnSequence().movedWorkers().contains(worker)) {
                    player.turnSequence().setChosenWorker(worker);
                }
            }
            player.turnSequence().clearPossibleBuilds();
            for(Box box : map.adjacent(player.turnSequence().chosenWorker().position())){
                if(box.level() > 0 && box.isFree()){
                    player.turnSequence().addPossibleBuild(box);
                }
            }
            Box chosenBox = communicationController.chooseBox(player.turnSequence().possibleBuilds());
            chosenBox.removeBlock();
            player.turnSequence().recordRemovedBlock(chosenBox);
        }
    }
}
