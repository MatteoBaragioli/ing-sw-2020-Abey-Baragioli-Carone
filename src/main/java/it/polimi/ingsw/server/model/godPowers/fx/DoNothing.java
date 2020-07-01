package it.polimi.ingsw.server.model.godPowers.fx;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.*;

import java.util.List;

public class DoNothing implements TurnSequenceModifier{

    /**
     * This method does anything (for powers that haven't a power in a specific phase of the turn)
     * @param player Player that has the card
     * @param actionController Action controller
     * @param map Map of the match
     */
    @Override
    public void changePossibleOptions(Player player, ActionController actionController, Map map) {

    }

    /**
     * This method does anything (for powers that haven't a power in a specific phase of the turn)
     * @param player Player that has the card
     * @param communicationController Communication controller
     * @param actionController Action controller
     * @param map Map of the match
     * @param opponents Player's opponents
     * @param winConditions List of win conditions
     * @param matchStory Last turn story
     */
    @Override
    public void executeAction(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, MatchStory matchStory) {

    }

    /**
     * This method does anything (for powers that haven't a power in a specific phase of the turn)
     * @param player Player that has the card
     * @param communicationController Communication controller
     * @param actionController Action controller
     * @param map Map of the match
     * @param opponents Player's opponents
     * @param winConditions List of win conditions
     * @param usePower Boolean that is true if player chose to use the power
     * @param matchStory Last turn story
     */
    @Override
    public void usePower(Player player, CommunicationController communicationController, ActionController actionController, Map map, List<Player> opponents, List<WinCondition> winConditions, boolean usePower, MatchStory matchStory) {

    }

    /**
     * This method does anything (for powers that haven't a power in a specific phase of the turn)
     * @param player Player that has the card
     * @param actionController Action controller
     * @param chosenBox Chosen box
     * @param matchStory Last turn story
     */
    @Override
    public void executePower(Player player, ActionController actionController, Box chosenBox, MatchStory matchStory) {

    }
}
