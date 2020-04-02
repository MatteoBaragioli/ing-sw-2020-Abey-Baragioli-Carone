package it.polimi.ingsw.server.model;

public interface TurnPhase {
public void executePhase(Player currentPlayer, CommunicationController communicationController, ActionController actionController, Map map);
}
