package it.polimi.ingsw.server.model;

public interface MoveModifier extends TurnSequenceModifier {

    @Override
    void changePossibleOptions(Worker worker, Player player, ActionController actionController, Map map);

    @Override
    void executeAction(Worker worker, Player player, CommunicationController communicationController, ActionController actionController);
}
