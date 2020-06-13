package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.network.exceptions.*;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.*;

import java.util.List;

public class AddBuildBeforeMoveIfNotMoveUpPower extends BuildModifier {
    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions,  MatchStory matchStory) throws TimeOutException, ChannelClosedException {
        actionController.initialisePossibleBuilds(player.turnSequence(), map);
        actionController.applyOpponentsCondition(player, opponents, 2, map);
        if(!player.turnSequence().possibleBuilds().isEmpty()) {
            boolean usePower=communicationController.chooseToUsePower(player);
            usePower(player, communicationController, actionController,map,opponents, winConditions, usePower, matchStory);
        }

    }

    @Override
    public void executePower(Player player, ActionController actionController, Box chosenBox, MatchStory matchStory) {
        player.turnSequence().setChosenBox(chosenBox);
        matchStory.addEvent(player.turnSequence().workersCurrentPosition(player.turnSequence().chosenWorker()).position(), matchStory.build, player.turnSequence().chosenBox().position());
        actionController.updateBuiltOnBox(player.turnSequence());
        player.turnSequence().setAllowedLevelDifference(0);
    }

    @Override
    public void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower, MatchStory matchStory) throws TimeOutException, ChannelClosedException {
       if (usePower){
           Box chosenBox = communicationController.chooseBuild(player, player.turnSequence().possibleBuilds());
           if(chosenBox!=null) {
               executePower(player, actionController, chosenBox, matchStory);
               communicationController.updateView(player, map.createProxy());
           }

    }
}

}
