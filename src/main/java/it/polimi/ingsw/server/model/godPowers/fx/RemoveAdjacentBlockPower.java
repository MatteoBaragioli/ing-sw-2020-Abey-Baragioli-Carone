package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.*;

import java.util.List;
import java.util.concurrent.TimeoutException;

public class RemoveAdjacentBlockPower extends BuildModifier{

    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, MatchStory matchStory) throws TimeoutException, ChannelClosedException {
        //buildPower - Ares
        for (Worker worker : player.workers()) {
            if (!player.turnSequence().movedWorkers().contains(worker)) {
                player.turnSequence().setChosenWorker(worker);
                player.turnSequence().clearPossibleBuilds();
                for(Box box : map.adjacent(player.turnSequence().chosenWorker().position())){
                    if(box.level() > 0 && box.isFree()){
                        player.turnSequence().addPossibleBuild(box);
                    }
                }
                if(!player.turnSequence().possibleBuilds().isEmpty()) {
                    boolean usePower = communicationController.chooseToUsePower(player);
                    usePower(player, communicationController, actionController, map, opponents, winConditions, usePower, matchStory);
                }
            }
        }
    }

    @Override
    public void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower, MatchStory matchStory) throws TimeoutException, ChannelClosedException {
        if(usePower) {
            Box chosenBox = communicationController.chooseRemoval(player, player.turnSequence().possibleBuilds());
            if (chosenBox != null) {
                executePower(player, actionController, chosenBox, matchStory);
                communicationController.updateView(player, map.createProxy());
            }
        }
    }

    @Override
    public void executePower(Player player, ActionController actionController, Box chosenBox, MatchStory matchStory) {
        player.turnSequence().setChosenBox(chosenBox);
        matchStory.addEvent(player.turnSequence().workersCurrentPosition(player.turnSequence().chosenWorker()).position(), matchStory.removal, player.turnSequence().chosenBox().position());
        player.turnSequence().chosenBox().removeBlock();
        player.turnSequence().recordRemovedBlock(player.turnSequence().chosenBox());
    }
}
