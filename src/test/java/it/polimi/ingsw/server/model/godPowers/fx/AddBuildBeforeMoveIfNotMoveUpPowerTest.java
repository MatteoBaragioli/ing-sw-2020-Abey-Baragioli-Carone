package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.network.exceptions.*;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.godPowers.setUpConditions.NoSetUpCondition;
import org.junit.Test;
import static org.junit.Assert.*;

import it.polimi.ingsw.server.model.*;

import java.util.ArrayList;


public class AddBuildBeforeMoveIfNotMoveUpPowerTest {

    @Test
    public void usePower() throws ChannelClosedException, TimeOutException {
        //-------------------------- Test 1 -------------------------------------------

        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        Worker chosenWorker = new Worker(map.position(2,2), Colour.BLUE);
        Worker worker = new Worker(map.position(3,3), Colour.BLUE);
        Player player = new Player("player1", Colour.BLUE, new GodCard("Prometheus", 10, new ArrayList<>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.assignWorker(worker);
        player.turnSequence().setChosenWorker(chosenWorker);

        TurnSequenceModifier prometheusPower = new AddBuildBeforeMoveIfNotMoveUpPower();

        assertTrue(player.turnSequence().possibleDestinations().isEmpty());
        assertTrue(player.turnSequence().newPositions().isEmpty());
        assertTrue(player.turnSequence().possibleBuilds().isEmpty());
        assertTrue(player.turnSequence().builtOnBoxes().isEmpty());
        assertEquals(1, player.turnSequence().allowedLevelDifference());

        //initialise execute action
        actionController.initialisePossibleBuilds(player.turnSequence(), map);
        actionController.applyOpponentsCondition(player, new ArrayList<>(), 2, map);

        //User doesn't use the power
        prometheusPower.usePower(player, communicationController, actionController, map, new ArrayList<>(), new ArrayList<>(), false, new MatchStory(player));
        assertEquals(1, player.turnSequence().allowedLevelDifference());
        assertTrue(player.turnSequence().builtOnBoxes().isEmpty());
        //User uses the power
        prometheusPower.usePower(player, communicationController, actionController, map, new ArrayList<>(), new ArrayList<>(), true, new MatchStory(player));
        assertFalse(player.turnSequence().builtOnBoxes().isEmpty());
        assertTrue(player.turnSequence().builtOnBoxes().contains(player.turnSequence().chosenBox()));
        assertTrue(player.turnSequence().possibleDestinations().isEmpty());
        assertTrue(player.turnSequence().newPositions().isEmpty());
        assertEquals(1, player.turnSequence().builtOnBoxes().size());
        assertEquals(1, player.turnSequence().builtOnBoxes().get(0).level());
        assertEquals(0, player.turnSequence().allowedLevelDifference());
    }

    @Test
    public void executePower() {
        //-------------------------- Test 1 ---------

        Map map = new  Map();
        ActionController actionController = new ActionController();
        Worker chosenWorker = new Worker(false, map.position(2,2));
        Worker worker = new Worker(true, map.position(3,3));
        Player player = new Player("player1", Colour.BLUE, new GodCard("Prometheus", 10, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.assignWorker(worker);
        player.turnSequence().setChosenWorker(chosenWorker);

        TurnSequenceModifier prometheusPower = new AddBuildBeforeMoveIfNotMoveUpPower();

        assertTrue(player.turnSequence().possibleDestinations().isEmpty());
        assertTrue(player.turnSequence().newPositions().isEmpty());
        assertTrue(player.turnSequence().possibleBuilds().isEmpty());
        assertTrue(player.turnSequence().builtOnBoxes().isEmpty());
        assertEquals(1, player.turnSequence().allowedLevelDifference());

        actionController.initialisePossibleBuilds(player.turnSequence(), map);


        //for each Box in possibleBuilds
        for(Box chosenBox : player.turnSequence().possibleBuilds()) {
            prometheusPower.executePower(player, actionController,  chosenBox, new MatchStory(player));
            assertFalse(player.turnSequence().possibleBuilds().isEmpty());
            assertFalse(player.turnSequence().builtOnBoxes().isEmpty());
            assertTrue(player.turnSequence().builtOnBoxes().contains(chosenBox));
            assertTrue(player.turnSequence().possibleDestinations().isEmpty());
            assertTrue(player.turnSequence().newPositions().isEmpty());
            assertEquals(1, player.turnSequence().builtOnBoxes().size());
            assertEquals(1, player.turnSequence().builtOnBoxes().get(0).level());
            assertEquals(0, player.turnSequence().allowedLevelDifference());

            player.turnSequence().clearBuiltOnBoxes();
            player.turnSequence().resetAllowedLevelDifference();
        }
    }
}