package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.server.model.*;

import java.io.IOException;
import java.util.List;

public class RemoveAdjacentBlockPower extends BuildModifier {

    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) throws IOException {
        //buildPower - Ares
        boolean usePower = communicationController.chooseToUsePower(player);
        for (Worker worker : player.workers()) {
            if (!player.turnSequence().movedWorkers().contains(worker)) {
                player.turnSequence().setChosenWorker(worker);
                usePower(player, communicationController, actionController, map, opponents, winConditions, usePower);
            } else {
                //todo comunicare all'utente che non può usare il suo potere aggiuntivo
            }
        }
    }

    @Override
    public void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower) throws IOException {
        if(usePower) {
            player.turnSequence().clearPossibleBuilds();
            for(Box box : map.adjacent(player.turnSequence().chosenWorker().position())){
                if(box.level() > 0 && box.isFree()){
                    player.turnSequence().addPossibleBuild(box);
                }
            }
            if(!player.turnSequence().possibleBuilds().isEmpty()) {
                Box chosenBox = communicationController.chooseRemoval(player, player.turnSequence().possibleBuilds());
                if (chosenBox != null)
                    executePower(player, actionController, chosenBox);
                else {
                    //todo errore chosenBox
                }
            } else {
                //todo comunicare all'utente che non può usare il suo potere aggiuntivo
            }
        }
    }

    @Override
    public void executePower(Player player, ActionController actionController, Box chosenBox) {
        player.turnSequence().setChosenBox(chosenBox);
        player.turnSequence().chosenBox().removeBlock();
        player.turnSequence().recordRemovedBlock(player.turnSequence().chosenBox());
    }
}
