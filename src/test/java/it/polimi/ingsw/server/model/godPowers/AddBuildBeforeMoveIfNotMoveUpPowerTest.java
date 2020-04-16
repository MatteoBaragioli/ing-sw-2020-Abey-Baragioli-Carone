package it.polimi.ingsw.server.model.godPowers;

import org.junit.Test;
import static org.junit.Assert.*;

import it.polimi.ingsw.server.model.*;
import java.util.ArrayList;
import java.util.List;


public class AddBuildBeforeMoveIfNotMoveUpPowerTest {

    @Test
    public void executeAction() {
        //-------------------------- Test 1 ---------
        //chosenWorker in (2,2) builds on (1,3)
        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        Worker chosenWorker = new Worker(false, map.position(2,2));
        Worker worker = new Worker(true, map.position(3,3));
        Player player = new Player("player1", Colour.BLUE, new GodCard("Prometheus", 10, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.assignWorker(worker);
        player.turnSequence().setChosenWorker(chosenWorker);
        player.turnSequence().setChosenBox(map.position(1,3));
        TurnSequenceModifier prometheusPower = new AddBuildBeforeMoveIfNotMoveUpPower();

        assertTrue(player.turnSequence().possibleDestinations().isEmpty());
        assertTrue(player.turnSequence().newPositions().isEmpty());
        assertTrue(player.turnSequence().possibleBuilds().isEmpty());
        assertTrue(player.turnSequence().builtOnBoxes().isEmpty());
        assertEquals(1, player.turnSequence().allowedLevelDifference());


        prometheusPower.executeAction(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>());

        assertTrue(player.turnSequence().possibleDestinations().isEmpty());
        assertTrue(player.turnSequence().newPositions().isEmpty());
        assertFalse(player.turnSequence().possibleBuilds().isEmpty());
        assertFalse(player.turnSequence().builtOnBoxes().isEmpty());

        List<Box> expectedPossibleBuilds = new ArrayList<>();
        expectedPossibleBuilds.add(map.position(1,1));
        expectedPossibleBuilds.add(map.position(1,2));
        expectedPossibleBuilds.add(map.position(1,3));
        expectedPossibleBuilds.add(map.position(2,1));
        expectedPossibleBuilds.add(map.position(2,3));
        expectedPossibleBuilds.add(map.position(3,1));
        expectedPossibleBuilds.add(map.position(3,2));

        assertEquals(expectedPossibleBuilds, player.turnSequence().possibleBuilds());
        assertTrue(player.turnSequence().builtOnBoxes().contains(map.position(1,3)));
        assertEquals(1, player.turnSequence().builtOnBoxes().size());
        assertEquals(1, player.turnSequence().builtOnBoxes().get(0).level());
        assertEquals(0, player.turnSequence().allowedLevelDifference());
    }
}