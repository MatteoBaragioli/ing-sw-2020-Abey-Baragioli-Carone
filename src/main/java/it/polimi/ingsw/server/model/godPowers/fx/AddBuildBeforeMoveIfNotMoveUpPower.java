package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.*;

import java.util.List;
import java.util.concurrent.TimeoutException;

public class AddBuildBeforeMoveIfNotMoveUpPower extends BuildModifier {
    @Override
    public void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower, MatchStory matchStory) throws TimeoutException, ChannelClosedException {
        if(usePower){
            actionController.initialisePossibleBuilds(player.turnSequence(), map);
            actionController.applyOpponentsCondition(player, opponents, 2, map);
            if(!player.turnSequence().possibleBuilds().isEmpty()) {
                Box chosenBox = communicationController.chooseBuild(player, player.turnSequence().possibleBuilds());
                if(chosenBox!=null) {
                    executePower(player, actionController, chosenBox, matchStory);
                    communicationController.updateView(player, map.createProxy());
                } else {
                    //todo errore chosenBox
                }
            } else {
                //todo comunicare all'utente che non può usare il suo potere aggiuntivo
            }
        }
    }

    @Override
    public void executePower(Player player, ActionController actionController, Box chosenBox, MatchStory matchStory) {
        player.turnSequence().setChosenBox(chosenBox);
        matchStory.addEvent(player.turnSequence().workersCurrentPosition(player.turnSequence().chosenWorker()).position(), matchStory.build, player.turnSequence().chosenBox().position());
        actionController.updateBuiltOnBox(player.turnSequence());
        player.turnSequence().setAllowedLevelDifference(0);
    }
}
