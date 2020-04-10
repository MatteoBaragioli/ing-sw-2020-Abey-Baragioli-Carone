package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.*;

import java.util.List;

public class BuildUnderYourselfPower implements BuildModifier {
    @Override
    public void changePossibleOptions(Player player, ActionController actionController, Map map) {
        //buildPower - Zeus
        if(player.turnSequence().chosenWorker().position().level()<3){
            player.turnSequence().possibleBuilds().add(player.turnSequence().chosenWorker().position());
        }
    }

    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions) {
    }
}
