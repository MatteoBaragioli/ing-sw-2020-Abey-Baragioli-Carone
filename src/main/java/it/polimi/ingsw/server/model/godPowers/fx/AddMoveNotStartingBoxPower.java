package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.*;

import java.util.List;
import java.util.concurrent.TimeoutException;

import static it.polimi.ingsw.server.model.Phase.MOVE;

public class AddMoveNotStartingBoxPower extends MoveModifier{
    @Override
    public void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower, MatchStory matchStory) throws TimeoutException, ChannelClosedException {
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
            Box chosenBox = communicationController.chooseDestination(player, player.turnSequence().possibleDestinations());
            if(chosenBox!=null) {
                executePower(player, actionController, chosenBox, matchStory);
                communicationController.updateView(player, map.createProxy());
            } else {
                //todo errore chosenBox
            }
        }
    }

    @Override
    public void executePower(Player player, ActionController actionController, Box chosenBox, MatchStory matchStory) {
        player.turnSequence().setChosenBox(chosenBox);
        matchStory.addEvent(player.turnSequence().workersCurrentPosition(player.turnSequence().chosenWorker()).position(), matchStory.move, player.turnSequence().chosenBox().position());
        actionController.updateNewPositions(player.turnSequence());
    }
}
