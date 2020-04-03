package it.polimi.ingsw.server.model;

public interface TurnSequenceModifier {

    public void changePossibleOptions(Player player, ActionController actionController, Map map);

    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map);
}
