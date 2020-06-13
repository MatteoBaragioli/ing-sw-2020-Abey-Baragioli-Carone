package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.network.exceptions.*;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.*;

import java.util.List;

import static it.polimi.ingsw.server.model.Phase.*;

public class AddThreeBuildsToUnmovedWorkerIfOnGroundPower extends BuildModifier {
    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, MatchStory matchStory) throws TimeOutException, ChannelClosedException {
        //buildPower - Poseidon
        boolean usePower = true;
        int i;
        Worker unmovedWorker = null;
        for (Worker worker : player.workers()) {
            if (!player.turnSequence().movedWorkers().contains(worker)) {
                unmovedWorker = worker;
            }
        }
        if (unmovedWorker != null && unmovedWorker.position().level() == 0) {
            player.turnSequence().setChosenWorker(unmovedWorker);
            for (i = 0; i < 3 && !player.turnSequence().possibleBuilds().isEmpty() && usePower; i++) {
                actionController.verifyWinCondition(BUILD, winConditions, player, map, opponents);
                if (actionController.currentPlayerHasWon(player)) {
                    return;
                }
                actionController.initialisePossibleBuilds(player.turnSequence(), map);
                actionController.applyOpponentsCondition(player, opponents, 2, map);

                if(!player.turnSequence().possibleBuilds().isEmpty()) {
                    usePower = communicationController.chooseToUsePower(player);
                    usePower(player, communicationController, actionController, map, opponents, winConditions, usePower, matchStory);
                }
            }

        }
        return;
    }

    @Override
    public void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower, MatchStory matchStory) throws TimeOutException, ChannelClosedException {
        if (usePower) {
            Box chosenBox = communicationController.chooseBuild(player, player.turnSequence().possibleBuilds());
            if(chosenBox!=null) {
                executePower(player, actionController, chosenBox, matchStory);
                communicationController.updateView(player, map.createProxy());
            }
        }
    }

    public void executePower(Player player, ActionController actionController, Box chosenBox, MatchStory matchStory) {
        player.turnSequence().setChosenBox(chosenBox);
        matchStory.addEvent(player.turnSequence().workersCurrentPosition(player.turnSequence().chosenWorker()).position(), matchStory.build, player.turnSequence().chosenBox().position());
        actionController.updateBuiltOnBox(player.turnSequence());
    }
}
