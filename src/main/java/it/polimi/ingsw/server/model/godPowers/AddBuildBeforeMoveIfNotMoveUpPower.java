package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.*;

import java.util.List;

public class AddBuildBeforeMoveIfNotMoveUpPower extends BuildModifier {

    @Override
    public void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower) {
        if(usePower){
            actionController.initialisePossibleBuilds(player.turnSequence(), map);
            actionController.applyOpponentsCondition(player, opponents, 2, map);
            if(!player.turnSequence().possibleBuilds().isEmpty()) {
                Box chosenBox = communicationController.chooseBox(player, player.turnSequence().possibleBuilds());
                if(chosenBox!=null)
                    executePower(player, actionController, chosenBox);
                else{
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
        actionController.updateBuiltOnBox(player.turnSequence());
        player.turnSequence().setAllowedLevelDifference(0);
    }
}
