package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.server.model.*;

import java.util.List;

public class BuildUnderYourselfPower extends BuildModifier{
    @Override
    public void changePossibleOptions(Player player, ActionController actionController, Map map) {
        //buildPower - Zeus
        if(player.turnSequence().chosenWorker().position().level()<3){
            player.turnSequence().possibleBuilds().add(player.turnSequence().chosenWorker().position());
        }
    }
}
