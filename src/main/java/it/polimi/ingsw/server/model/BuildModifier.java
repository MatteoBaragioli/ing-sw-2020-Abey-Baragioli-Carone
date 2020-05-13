package it.polimi.ingsw.server.model;

import java.io.IOException;
import java.util.List;

public abstract class BuildModifier implements TurnSequenceModifier {

    public void changePossibleOptions(Player player, ActionController actionController, Map map) {

    }

    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) throws IOException {
        boolean usePower = communicationController.chooseToUsePower(player);
        usePower(player, communicationController, actionController, map, opponents, winConditions, usePower);

    }

    public void executePower(Player player, ActionController actionController, Box chosenBox) {}

    public void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower) throws IOException {}
}
