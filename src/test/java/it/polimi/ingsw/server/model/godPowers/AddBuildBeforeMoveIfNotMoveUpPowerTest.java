package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.godPowers.fx.AddBuildBeforeMoveIfNotMoveUpPower;
import it.polimi.ingsw.server.model.godPowers.setUpConditions.NoSetUpCondition;
import org.junit.Test;
import static org.junit.Assert.*;

import it.polimi.ingsw.server.model.*;

import java.io.IOException;
import java.util.ArrayList;


public class AddBuildBeforeMoveIfNotMoveUpPowerTest {

    @Test
    public void usePower() throws IOException {
        //-------------------------- Test 1 -------------------------------------------

        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        Worker chosenWorker = new Worker(map.position(2,2), Colour.BLUE);
        Worker worker = new Worker(map.position(3,3), Colour.BLUE);
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
        //User doesn't use the power
        prometheusPower.usePower(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>(), false);
        assertTrue(player.turnSequence().possibleDestinations().isEmpty());
        assertTrue(player.turnSequence().newPositions().isEmpty());
        assertTrue(player.turnSequence().possibleBuilds().isEmpty());
        assertTrue(player.turnSequence().builtOnBoxes().isEmpty());
        assertEquals(1, player.turnSequence().allowedLevelDifference());
        //User uses the power
        player.turnSequence().setChosenBox(map.position(2,1));
        prometheusPower.usePower(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>(), true);
        assertFalse(player.turnSequence().possibleBuilds().isEmpty());
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
            prometheusPower.executePower(player, actionController,  chosenBox);
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