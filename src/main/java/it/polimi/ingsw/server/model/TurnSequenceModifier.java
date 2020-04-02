package it.polimi.ingsw.server.model;

public interface TurnSequenceModifier {

    public void changePossibleOptions(Worker worker, Player player, ActionController actionController, Map map);

    public void executeAction(Worker worker, Player player, CommunicationController communicationController, ActionController actionController);
}
