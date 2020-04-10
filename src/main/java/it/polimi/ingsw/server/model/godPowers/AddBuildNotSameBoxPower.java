package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.*;

import java.util.List;

import static it.polimi.ingsw.server.model.Phase.BUILD;

public class AddBuildNotSameBoxPower implements BuildModifier {
    @Override
    public void changePossibleOptions(Player player, ActionController actionController, Map map) {

    }

    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) {
        //buildPower - Demeter
        boolean usePower = communicationController.chooseToUsePower();
        if(usePower){
            actionController.verifyWinCondition(BUILD, winConditions, player, map, opponents);
            if(actionController.currentPlayerHasWon(player)){
                return;
            }
            for(Box builtOnBox : player.turnSequence().possibleBuilds()) {
                if (player.turnSequence().builtOnBoxes().contains(builtOnBox)) {
                    player.turnSequence().possibleBuilds().remove(builtOnBox);
                }
            }
            if(!player.turnSequence().possibleBuilds().isEmpty()) {
                Box chosenBox = communicationController.chooseBox(map);
                player.turnSequence().setChosenBox(chosenBox);
                actionController.updateBuiltOnBox(player.turnSequence());
            } else {
                //todo comunicare all'utente che non può usare il suo potere aggiuntivo
            }
        }
    }
}
