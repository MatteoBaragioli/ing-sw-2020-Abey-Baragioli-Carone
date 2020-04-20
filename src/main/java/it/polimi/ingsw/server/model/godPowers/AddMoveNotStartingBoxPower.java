package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.*;

import java.util.List;

import static it.polimi.ingsw.server.model.Phase.MOVE;

public class AddMoveNotStartingBoxPower implements MoveModifier {
    @Override
    public void changePossibleOptions(Player player, ActionController actionController, Map map) {
    }

    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) {
        //movePower-Artemis
        boolean usePower = communicationController.chooseToUsePower();
        usePower(player, communicationController, actionController, map, opponents, winConditions, usePower);
    }

    protected void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower){
        if(usePower) {
            actionController.verifyWinCondition(MOVE, winConditions, player, map, opponents);
            if(actionController.currentPlayerHasWon(player)){
                return;
            }
            actionController.initialisePossibleDestinations(player.turnSequence(), map);
            actionController.applyOpponentsCondition(player, opponents, 2, map);
            player.turnSequence().possibleDestinations().remove(player.turnSequence().previousBox()); //remove starting box
            if(player.turnSequence().possibleDestinations().isEmpty()){
                //todo comunicare all'utente che non può usare il suo potere aggiuntivo
                return;
            }
            Box chosenBox = communicationController.chooseBox(player, player.turnSequence().possibleDestinations());
            executePower(player, actionController, chosenBox);
        }
    }

    protected void executePower(Player player, ActionController actionController, Box chosenBox){
        if(chosenBox!=null)
            player.turnSequence().setChosenBox(chosenBox);
        actionController.updateNewPositions(player.turnSequence());
    }
}
