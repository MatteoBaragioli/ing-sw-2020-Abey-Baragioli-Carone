package it.polimi.ingsw.server.model;

import java.util.List;

public interface TurnPhase {
public void executePhase(Player currentPlayer, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions);
}
