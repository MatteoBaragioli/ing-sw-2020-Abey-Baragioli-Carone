package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AddMoveNotStartingBoxPowerTest {

    @Test
    public void executeAction() {

        //-------------------------- Test 1 --------- CommunicationController --> chooseBox returns element 0
        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        Worker chosenWorker = new Worker(false, map.position(4,3));
        Worker worker2 = new Worker(false, map.position(0,0));
        Player player = new Player("player1", Colour.BLUE, new GodCard("Artemis", 2, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.assignWorker(worker2);
        player.turnSequence().setChosenWorker(chosenWorker);
        player.turnSequence().setChosenBox(map.position(3,3));
        player.turnSequence().addMovableWorker(chosenWorker);
        actionController.updateNewPositions(player.turnSequence());

        assertTrue(player.turnSequence().movedWorkers().contains(chosenWorker));
        assertTrue(player.turnSequence().newPositions().containsKey(chosenWorker));
        assertEquals(map.position(3,3), player.turnSequence().newPositions().get(chosenWorker));
        assertEquals(map.position(4,3), player.turnSequence().previousBox());

        TurnSequenceModifier artemisPower = new AddMoveNotStartingBoxPower();
        //check first move and update possible destinations

        artemisPower.executeAction(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>());

        assertFalse(player.turnSequence().newPositions().isEmpty());
        assertFalse(player.turnSequence().possibleDestinations().isEmpty());

        assertEquals(player.turnSequence().newPositions().get(player.turnSequence().chosenWorker()), map.position(2,2));
        assertEquals(1, player.turnSequence().newPositions().size());
        List<Box> expectedList = new ArrayList<>();
        expectedList.add(map.position(2,2));
        expectedList.add(map.position(2,3));
        expectedList.add(map.position(2,4));
        expectedList.add(map.position(3,2));
        expectedList.add(map.position(3,4));
        expectedList.add(map.position(4,2));
        expectedList.add(map.position(4,4));
        assertEquals(expectedList, player.turnSequence().possibleDestinations());


        //-------------------------- Test 2 --------- CommunicationController --> chooseBox returns element 0
/*
        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        Worker chosenWorker = new Worker(false, map.position(3,3));
        Worker worker2 = new Worker(false, map.position(0,0));
        Worker opponentWorker = new Worker(false, map.position(4, 3));
        Player player = new Player("player1", Colour.BLUE, new GodCard("Artemis", 2, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.assignWorker(worker2);
        player.turnSequence().setChosenWorker(chosenWorker);
        player.turnSequence().setChosenBox(map.position(4,4));
        player.turnSequence().addMovableWorker(chosenWorker);
        actionController.updateNewPositions(player.turnSequence());

        assertTrue(player.turnSequence().movedWorkers().contains(chosenWorker));
        assertTrue(player.turnSequence().newPositions().containsKey(chosenWorker));
        assertEquals(map.position(4,4), player.turnSequence().newPositions().get(chosenWorker));
        assertEquals(map.position(3,3), player.turnSequence().previousBox());

        TurnSequenceModifier artemisPower = new AddMoveNotStartingBoxPower();
        //check first move and update possible destinations

        artemisPower.executeAction(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>());

        assertFalse(player.turnSequence().newPositions().isEmpty());
        assertFalse(player.turnSequence().possibleDestinations().isEmpty());

        assertEquals(player.turnSequence().newPositions().get(player.turnSequence().chosenWorker()), map.position(3,4));
        assertEquals(1, player.turnSequence().newPositions().size());
        List<Box> expectedList = new ArrayList<>();
        expectedList.add(map.position(3,4));
        assertEquals(expectedList, player.turnSequence().possibleDestinations());
*/
    }
}