package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.network.exceptions.TimeOutException;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.godPowers.setUpConditions.NoSetUpCondition;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OpponentsCantMoveUpIfPlayerMovesUpPowerTest {

    @Test
    public void executeAction() throws ChannelClosedException, TimeOutException {
        //-------------------------- Test 1 ---------
        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        Worker chosenWorker = new Worker(false, map.position(3,3));
        Worker worker = new Worker(true, map.position(2,2));
        Worker opponentWorker = new Worker(true, map.position(0,0));
        Player player = new Player("player1", Colour.BLUE, new GodCard("Athena", 3, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.assignWorker(worker);
        player.turnSequence().setChosenWorker(chosenWorker);
        player.turnSequence().setChosenBox(map.position(3,4));
        Player opponent = new Player("opponent1", Colour.GREY, new GodCard("Artemis", 2, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        opponent.assignWorker(opponentWorker);
        List<Player> opponents = new ArrayList<>();
        opponents.add(opponent);
        player.turnSequence().addMovableWorker(chosenWorker);
        TurnSequenceModifier athenaPower = new OpponentsCantMoveUpIfPlayerMovesUpPower();

        //no move up
        actionController.updateNewPositions(player.turnSequence());
        athenaPower.executeAction(player, communicationController, actionController, map, opponents, new ArrayList<WinCondition>(), new MatchStory(player));

        assertEquals(1, opponent.turnSequence().allowedLevelDifference());

        //move up
        player.turnSequence().confirmTurnSequence();
        player.turnSequence().setChosenWorker(chosenWorker);
        player.turnSequence().addMovableWorker(chosenWorker);
        map.position(4,4).build();
        player.turnSequence().setChosenBox(map.position(4,4));
        actionController.updateNewPositions(player.turnSequence());
        athenaPower.executeAction(player, communicationController, actionController, map, opponents, new ArrayList<WinCondition>(), new MatchStory(player));

        assertEquals(0, opponent.turnSequence().allowedLevelDifference());
    }
}