package it.polimi.ingsw.server.model;

public interface BuildModifier extends TurnSequenceModifier {
    @Override
    void executeAction(Worker worker, Player player);
}
