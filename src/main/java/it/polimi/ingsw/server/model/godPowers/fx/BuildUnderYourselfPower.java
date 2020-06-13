package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.network.exceptions.*;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.*;

import java.util.List;

public class BuildUnderYourselfPower extends BuildModifier{
    @Override
    public void changePossibleOptions(Player player, ActionController actionController, Map map) {
        //buildPower - Zeus
        Box currentBox=player.turnSequence().workersCurrentPosition(player.turnSequence().chosenWorker());
        if(currentBox.level()<3){
            player.turnSequence().possibleBuilds().add(currentBox);
        }

    }

    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, MatchStory matchStory) throws TimeOutException, ChannelClosedException {

    }
}
