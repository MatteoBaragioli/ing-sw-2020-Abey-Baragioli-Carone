package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.*;

import java.util.List;

public class  BuildDomeEverywherePower extends BuildModifier {


    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) {
        //buildPower - Atlas
        if(!player.turnSequence().chosenBox().hasDome()) {
            boolean usePower = communicationController.chooseToUsePower(player);
            usePower(player, communicationController, actionController, map, opponents, winConditions, usePower);
        }
    }

    @Override
    public void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower) {
        if (usePower) {
            player.turnSequence().chosenBox().removeBlock();
            player.turnSequence().chosenBox().buildDome();
        }
    }

}
