package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.network.exceptions.*;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.*;

import java.util.List;

public class BuildDomeEverywherePower extends BuildModifier{
    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, MatchStory matchStory) throws TimeOutException, ChannelClosedException {
        //buildPower - Atlas
        if(!player.turnSequence().chosenBox().hasDome()) {
            boolean usePower = communicationController.chooseToUsePower(player);
            usePower(player, communicationController, actionController, map, opponents, winConditions, usePower, matchStory);
            communicationController.updateView(player, map.createProxy());
        }
    }

    @Override
    public void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower, MatchStory matchStory) {
        if (usePower) {
            player.turnSequence().chosenBox().removeBlock();
            player.turnSequence().chosenBox().buildDome();
        }
    }
}
