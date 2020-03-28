package it.polimi.ingsw.server.model;

public interface MoveModifier extends TurnSequenceModifier {
    @Override
    void executeAction(Worker worker, Player player);
}
