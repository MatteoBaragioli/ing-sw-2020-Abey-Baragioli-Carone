package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.server.model.*;

import java.util.List;
import java.util.concurrent.TimeoutException;

public class BuildDomeEverywherePower extends BuildModifier{
    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) throws TimeoutException, ChannelClosedException {
        //buildPower - Atlas
        if(!player.turnSequence().chosenBox().hasDome()) {
            boolean usePower = communicationController.chooseToUsePower(player);
            usePower(player, communicationController, actionController, map, opponents, winConditions, usePower);
        }
    }

    @Override
    public void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower) {
        if (usePower) {
            player.turnSequence().chosenBox().removeBlock();
            player.turnSequence().chosenBox().buildDome();
        }
    }
}
