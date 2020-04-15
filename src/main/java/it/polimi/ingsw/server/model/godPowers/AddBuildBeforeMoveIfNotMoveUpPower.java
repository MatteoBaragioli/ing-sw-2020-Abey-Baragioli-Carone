package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.*;

import java.util.List;

import static it.polimi.ingsw.server.model.Phase.BUILD;

public class AddBuildBeforeMoveIfNotMoveUpPower implements BuildModifier {

    @Override
    public void changePossibleOptions(Player player, ActionController actionController, Map map) {

    }

    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) {
        //startPower-Prometheus
        boolean usePower = communicationController.chooseToUsePower();
        if(usePower){
            actionController.initialisePossibleBuilds(player.turnSequence(), map);
            actionController.applyOpponentsCondition(player, opponents, 2, map);
            Box chosenBox = communicationController.chooseBox(player.turnSequence().possibleBuilds());
            if(chosenBox!=null)
                player.turnSequence().setChosenBox(chosenBox);
            actionController.updateBuiltOnBox(player.turnSequence());
            actionController.verifyWinCondition(BUILD, winConditions, player, map, opponents);
            if(actionController.currentPlayerHasWon(player)){
                return;
            }
            player.turnSequence().setAllowedLevelDifference(0);
        }
    }
}
