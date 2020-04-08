package it.polimi.ingsw.server.model;

import java.util.List;

public interface BuildModifier extends TurnSequenceModifier {

    @Override
    void changePossibleOptions(Player player, ActionController actionController, Map map);

    @Override
    void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions);
}
