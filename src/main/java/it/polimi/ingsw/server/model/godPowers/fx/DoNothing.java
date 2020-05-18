package it.polimi.ingsw.server.model.godPowers.fx;
import it.polimi.ingsw.server.model.*;

import java.util.List;

public class DoNothing implements TurnSequenceModifier{
    @Override
    public void changePossibleOptions(Player player, ActionController actionController, Map map) {

    }

    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) {

    }

    @Override
    public void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower) {

    }

    @Override
    public void executePower(Player player, ActionController actionController, Box chosenBox) {

    }
}
