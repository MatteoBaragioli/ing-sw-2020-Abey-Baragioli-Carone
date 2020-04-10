package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.*;

import java.util.List;

public class  BuildDomeEverywherePower implements BuildModifier {
    @Override
    public void changePossibleOptions(Player player, ActionController actionController, Map map) {

    }

    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) {
        //buildPower - Atlas
        if(!player.turnSequence().chosenBox().hasDome()) {
            boolean usePower = communicationController.chooseToUsePower();
            if (usePower) {
                player.turnSequence().chosenBox().removeBlock();
                player.turnSequence().chosenBox().buildDome();
            }
        }
    }
}
