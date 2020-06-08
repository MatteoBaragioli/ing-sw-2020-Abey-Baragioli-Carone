package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.*;

import java.util.List;
import java.util.concurrent.TimeoutException;

public class BuildUnderYourselfPower extends BuildModifier{
    @Override
    public void changePossibleOptions(Player player, ActionController actionController, Map map) {
        //buildPower - Zeus
        if(player.turnSequence().chosenWorker().position().level()<3){
            player.turnSequence().possibleBuilds().add(player.turnSequence().workersCurrentPosition(player.turnSequence().chosenWorker()));
        }

    }

    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, MatchStory matchStory) throws TimeoutException, ChannelClosedException {

    }
}
