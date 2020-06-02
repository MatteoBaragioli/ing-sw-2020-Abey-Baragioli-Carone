package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static it.polimi.ingsw.server.model.Phase.BUILD;

public class AddBuildNotSameBoxPower extends BuildModifier {
    @Override
    public void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower, MatchStory matchStory) throws TimeoutException, ChannelClosedException {
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
            Box chosenBox = communicationController.chooseBuild(player, player.turnSequence().possibleBuilds());
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
        matchStory.addEvent(player.turnSequence().workersCurrentPosition(player.turnSequence().chosenWorker()).position(), matchStory.build, player.turnSequence().chosenBox().position());
        actionController.updateBuiltOnBox(player.turnSequence());
    }
}
