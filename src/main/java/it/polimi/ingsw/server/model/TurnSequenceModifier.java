package it.polimi.ingsw.server.model;

import java.util.List;

public interface TurnSequenceModifier {

    void changePossibleOptions(Player player, ActionController actionController, Map map);

    void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions);

    void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower);

    void executePower(Player player, ActionController actionController, Box chosenBox);
    }
