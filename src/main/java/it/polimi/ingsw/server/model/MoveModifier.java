package it.polimi.ingsw.server.model;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public abstract class MoveModifier implements TurnSequenceModifier {
    public void changePossibleOptions(Player player, ActionController actionController, Map map) {

    }

    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) throws TimeoutException, ChannelClosedException {
        //startPower-Prometheus
        boolean usePower = communicationController.chooseToUsePower(player);
        usePower(player, communicationController, actionController, map, opponents, winConditions, usePower);

    }

    public void executePower(Player player, ActionController actionController, Box chosenBox) {}

    public void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower) throws TimeoutException, ChannelClosedException {}

}
