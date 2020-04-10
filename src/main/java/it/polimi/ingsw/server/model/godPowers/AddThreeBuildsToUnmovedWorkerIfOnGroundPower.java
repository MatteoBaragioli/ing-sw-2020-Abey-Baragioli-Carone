package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.*;

import java.util.List;

import static it.polimi.ingsw.server.model.Phase.BUILD;

public class AddThreeBuildsToUnmovedWorkerIfOnGroundPower implements BuildModifier {

    @Override
    public void changePossibleOptions(Player player, ActionController actionController, Map map) {

    }

    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) {
        //endPower - Poseidon
        boolean usePower = true;
        int i;
        Worker unmovedWorker = null;
        for(Worker worker : player.workers()){
            if(!player.turnSequence().movedWorkers().contains(worker)) {
                unmovedWorker = worker;
            }
        }
        if(unmovedWorker!=null && unmovedWorker.position().level()==0){
            player.turnSequence().setChosenWorker(unmovedWorker);
            actionController.initialisePossibleBuilds(player.turnSequence(), map);
            actionController.applyOpponentsCondition(player, opponents, 2, map);
            for(i=0; i<3 && !player.turnSequence().possibleBuilds().isEmpty() && usePower; i++){
                usePower = communicationController.chooseToUsePower();
                if(usePower){
                    actionController.verifyWinCondition(BUILD, winConditions, player, map, opponents);
                    if(actionController.currentPlayerHasWon(player)){
                        return;
                    }
                    Box chosenBox = communicationController.chooseBox(map);
                    player.turnSequence().setChosenBox(chosenBox);
                    actionController.updateBuiltOnBox(player.turnSequence());
                    actionController.verifyWinCondition(BUILD, winConditions, player, map, opponents);
                    if(actionController.currentPlayerHasWon(player)){
                        return;
                    }
                    actionController.initialisePossibleBuilds(player.turnSequence(), map);
                    actionController.applyOpponentsCondition(player, opponents, 2, map);
                }
            }
            if(i<3 && usePower){
                //todo comunicare all'utente che non può più usare il suo potere
            }
        } else {
            //todo comunicare all'utente che non può usare il suo potere aggiuntivo
            return;
        }
    }
}
