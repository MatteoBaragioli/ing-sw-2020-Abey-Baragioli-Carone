package it.polimi.ingsw.server.model;

public interface MoveModifier extends TurnSequenceModifier {

    @Override
    void changePossibleOptions(Player player, ActionController actionController, Map map);

    @Override
    void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map);
}
