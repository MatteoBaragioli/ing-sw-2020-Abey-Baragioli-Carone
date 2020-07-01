package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.network.exceptions.*;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.*;

import java.util.List;

public class BuildUnderYourselfPower extends BuildModifier{

    /**
     * This method adds worker position in possible destinations list
     * @param player Player that has the card
     * @param actionController Action controller
     * @param map Map of the match
     */
    @Override
    public void changePossibleOptions(Player player, ActionController actionController, Map map) {
        //buildPower - Zeus
        Box currentBox=player.turnSequence().workersCurrentPosition(player.turnSequence().chosenWorker());
        if(currentBox.level()<3){
            player.turnSequence().possibleBuilds().add(currentBox);
        }

    }

    /**
     * This method does anything (for this power)
     * @param player Player that has the card
     * @param communicationController Communication controller
     * @param actionController Action controller
     * @param map Map of the match
     * @param opponents Player's opponents
     * @param winConditions List of win conditions
     * @param matchStory Last turn story
     * @throws TimeOutException Exception thrown when the time to do an action runs out
     * @throws ChannelClosedException Exception thrown when communication channel is closed
     */
    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, MatchStory matchStory) throws TimeOutException, ChannelClosedException {
    }
}
