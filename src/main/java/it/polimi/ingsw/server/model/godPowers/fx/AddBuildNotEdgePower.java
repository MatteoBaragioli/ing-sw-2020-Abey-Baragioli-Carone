package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.server.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.server.model.Phase.BUILD;

public class AddBuildNotEdgePower extends BuildModifier {


    @Override
    public void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower) throws IOException {
        if(usePower){
            actionController.verifyWinCondition(BUILD, winConditions, player, map, opponents);
            if(actionController.currentPlayerHasWon(player)){
                return;
            }
            actionController.initialisePossibleBuilds(player.turnSequence(), map);
            actionController.applyOpponentsCondition(player, opponents, 2, map);
            List<Box> edgeBoxes = new ArrayList<>();
            for(Box box : player.turnSequence().possibleBuilds()){
                if(box.isOnEdge())
                    edgeBoxes.add(box);
            }
            player.turnSequence().possibleBuilds().removeAll(edgeBoxes);
            if(!player.turnSequence().possibleBuilds().isEmpty()){
                Box chosenBox = communicationController.chooseBuild(player, player.turnSequence().possibleBuilds());
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
    }
}
