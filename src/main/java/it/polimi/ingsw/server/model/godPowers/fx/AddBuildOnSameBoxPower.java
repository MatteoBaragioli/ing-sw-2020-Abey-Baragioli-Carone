package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.*;

import java.util.List;
import java.util.concurrent.TimeoutException;

import static it.polimi.ingsw.server.model.Phase.BUILD;

public class AddBuildOnSameBoxPower extends BuildModifier {

    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, MatchStory matchStory) throws TimeoutException, ChannelClosedException {
        actionController.verifyWinCondition(BUILD, winConditions, player, map, opponents);
        if(actionController.currentPlayerHasWon(player)){
            return;
        }
        if(player.turnSequence().chosenBox().level()<3) {
            boolean usePower = communicationController.chooseToUsePower(player);
            usePower(player, communicationController, actionController, map, opponents, winConditions, usePower, matchStory);
        }
    }

    @Override
    public void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower, MatchStory matchStory) throws ChannelClosedException {
        if(usePower){
            matchStory.addEvent(player.turnSequence().workersCurrentPosition(player.turnSequence().chosenWorker()).position(), matchStory.build, player.turnSequence().chosenBox().position());
            actionController.updateBuiltOnBox(player.turnSequence());
            communicationController.updateView(player, map.createProxy());
        }
    }

    @Override
    public void executePower(Player player, ActionController actionController, Box chosenBox, MatchStory matchStory) {
    }
}
