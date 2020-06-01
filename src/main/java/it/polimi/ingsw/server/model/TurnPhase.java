package it.polimi.ingsw.server.model;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.network.objects.MatchStory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public interface TurnPhase {
    public void executePhase(Player currentPlayer, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, MatchStory matchStory) throws IOException, TimeoutException, ChannelClosedException;
}
