package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.*;

import java.util.ArrayList;
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
        usePower(player, communicationController, actionController, map, opponents, winConditions, usePower);
    }

    protected void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower){
        if(usePower){
            actionController.verifyWinCondition(BUILD, winConditions, player, map, opponents);
            if(actionController.currentPlayerHasWon(player)){
                return;
            }
            actionController.initialisePossibleBuilds(player.turnSequence(), map);
            actionController.applyOpponentsCondition(player, opponents, 2, map);
            List<Box> previousBuilds = new ArrayList<>();
            for(Box box : player.turnSequence().possibleBuilds()){
                if(player.turnSequence().builtOnBoxes().contains(box))
                    previousBuilds.add(box);
            }
            player.turnSequence().possibleBuilds().removeAll(previousBuilds);
            if(player.turnSequence().possibleBuilds().isEmpty()){
                //todo comunicare all'utente che non può usare il suo potere aggiuntivo
                return;
            }
            Box chosenBox = communicationController.chooseBox(player, player.turnSequence().possibleBuilds());
            executePower(player, actionController, chosenBox);

        }
    }

    protected void executePower(Player player, ActionController actionController, Box chosenBox){
        if(chosenBox!=null)
            player.turnSequence().setChosenBox(chosenBox);
        actionController.updateBuiltOnBox(player.turnSequence());
    }
}
