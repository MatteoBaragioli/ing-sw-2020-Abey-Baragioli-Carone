package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.server.model.*;

import java.io.IOException;
import java.util.List;

import static it.polimi.ingsw.server.model.Phase.*;

public class AddThreeBuildsToUnmovedWorkerIfOnGroundPower extends BuildModifier {
    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) throws IOException {
        //buildPower - Poseidon
        boolean usePower = true;
        int i;
        Worker unmovedWorker = null;
        for (Worker worker : player.workers()) {
            if (!player.turnSequence().movedWorkers().contains(worker)) {
                unmovedWorker = worker;
            }
        }
        if (unmovedWorker != null && unmovedWorker.position().level() == 0) {
            player.turnSequence().setChosenWorker(unmovedWorker);
            actionController.initialisePossibleBuilds(player.turnSequence(), map);
            actionController.applyOpponentsCondition(player, opponents, 2, map);
            for (i = 0; i < 3 && !player.turnSequence().possibleBuilds().isEmpty() && usePower; i++) {
                usePower = communicationController.chooseToUsePower(player);
                usePower(player,communicationController, actionController, map, opponents, winConditions,usePower);
                if (actionController.currentPlayerHasWon(player) && usePower) {
                    return;
                }
                actionController.initialisePossibleBuilds(player.turnSequence(), map);
                actionController.applyOpponentsCondition(player, opponents, 2, map);
            }

            if (i < 3 && usePower) {
                //todo comunicare all'utente che non può più usare il suo potere
            }

        } else {
            //todo comunicare all'utente che non può usare il suo potere aggiuntivo

        }
        return;
    }

    @Override
    public void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower) throws IOException {
        if (usePower) {
            actionController.verifyWinCondition(BUILD, winConditions, player, map, opponents);
            if (actionController.currentPlayerHasWon(player)) {
                return;
            }
            Box chosenBox = communicationController.chooseBuild(player, player.turnSequence().possibleBuilds());
            if(chosenBox!=null) {
                executePower(player, actionController, chosenBox);
                actionController.verifyWinCondition(BUILD, winConditions, player, map, opponents);
            }
            else { //todo errore chosenBox
            }
        }
    }

    public void executePower(Player player, ActionController actionController, Box chosenBox) {
        player.turnSequence().setChosenBox(chosenBox);
        actionController.updateBuiltOnBox(player.turnSequence());

    }
}
