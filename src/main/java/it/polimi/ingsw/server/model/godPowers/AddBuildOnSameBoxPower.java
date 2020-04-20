package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.*;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.server.model.Phase.BUILD;

public class AddBuildOnSameBoxPower implements BuildModifier {
    @Override
    public void changePossibleOptions(Player player, ActionController actionController, Map map) {
    }

    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) {
        //buildPower - Hephaestus
        boolean usePower = communicationController.chooseToUsePower();
        usePower(player, communicationController, actionController, map, opponents, winConditions, usePower);

    }

    @Override
    public void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower) {
        if(usePower){
            actionController.verifyWinCondition(BUILD, winConditions, player, map, opponents);
            if(actionController.currentPlayerHasWon(player)){
                return;
            }
            if(player.turnSequence().chosenBox().level()<3) {
                actionController.updateBuiltOnBox(player.turnSequence());
            } else {
                //todo comunicare all'utente che non può usare il suo potere aggiuntivo
            }
        }
    }

    @Override
    public void executePower(Player player, ActionController actionController, Box chosenBox) {
    }
}
