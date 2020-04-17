package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.*;

import java.util.List;

public class AddBuildBeforeMoveIfNotMoveUpPower implements BuildModifier {

    @Override
    public void changePossibleOptions(Player player, ActionController actionController, Map map) {

    }

    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) {
        //startPower-Prometheus
        boolean usePower = communicationController.chooseToUsePower();
        usePower(player, communicationController, actionController, map, opponents, winConditions, usePower);
    }

    protected void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower){
        if(usePower){
            actionController.initialisePossibleBuilds(player.turnSequence(), map);
            actionController.applyOpponentsCondition(player, opponents, 2, map);
            if(!player.turnSequence().possibleBuilds().isEmpty()) {
                Box chosenBox = communicationController.chooseBox(player, player.turnSequence().possibleBuilds());
                executePower(player, actionController, chosenBox);
            } else {
                //todo comunicare all'utente che non può usare il suo potere aggiuntivo
            }
        }
    }

    protected void executePower(Player player, ActionController actionController, Box chosenBox){
        if(chosenBox!=null)
            player.turnSequence().setChosenBox(chosenBox);
        actionController.updateBuiltOnBox(player.turnSequence());
        player.turnSequence().setAllowedLevelDifference(0);
    }
}
