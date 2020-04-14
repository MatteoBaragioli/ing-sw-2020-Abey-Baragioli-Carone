package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PushAdjacentOpponentPowerTest {

    @Test
    public void changePossibleOptions() {
        /*
        //-------------------------- Test 1 ----------- edgeBox
        Map map = new  Map();
        ActionController actionController = new ActionController();
        Worker chosenWorker = new Worker(false, map.position(3,3));
        Worker worker = new Worker(true, map.position(2,2));
        Worker targetWorker = new Worker(true, map.position(3,4));
        List<Worker> workers = new ArrayList<>();
        workers.add(chosenWorker);
        workers.add(worker);
        Player player = new Player("player1", Colour.BLUE, workers, new GodCard("Minotaur", 8, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()), new TurnSequence());
        player.turnSequence().setChosenWorker(chosenWorker);
        player.turnSequence().addMovableWorker(chosenWorker);
        actionController.initialisePossibleDestinations(player.turnSequence(), map);
        TurnSequenceModifier minotaurOptions = new PushAdjacentOpponentPower();
        minotaurOptions.changePossibleOptions(player, actionController, map);

        assertFalse(player.turnSequence().possibleDestinations().contains(targetWorker.position()));
        List<Box> testList = new ArrayList<>();
        testList.add(map.position(2, 3));
        testList.add(map.position(2,4));
        testList.add(map.position(3,2));
        testList.add(map.position(4,2));
        testList.add(map.position(4, 3));
        testList.add(map.position(4,4));
        assertEquals(player.turnSequence().possibleDestinations(), testList);
*/

        //-------------------------- Test 2 ---------------
        Map map = new  Map();
        ActionController actionController = new ActionController();
        Worker chosenWorker = new Worker(false, map.position(2,2));
        Worker worker = new Worker(true, map.position(0,0));
        Worker targetWorker = new Worker(true, map.position(2,3));
        Player player = new Player("player1", Colour.BLUE, new GodCard("Minotaur", 8, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.assignWorker(worker);
        player.turnSequence().setChosenWorker(chosenWorker);
        player.turnSequence().addMovableWorker(chosenWorker);
        actionController.initialisePossibleDestinations(player.turnSequence(), map);
        TurnSequenceModifier minotaurOptions = new PushAdjacentOpponentPower();
        minotaurOptions.changePossibleOptions(player, actionController, map);

        assertTrue(player.turnSequence().possibleDestinations().contains(targetWorker.position()));
        List<Box> testList = new ArrayList<>();
        testList.add(map.position(1,1));
        testList.add(map.position(1, 2));
        testList.add(map.position(1,3));
        testList.add(map.position(2, 1));
        testList.add(map.position(3,1));
        testList.add(map.position(3,2));
        testList.add(map.position(3, 3));
        testList.add(map.position(2,3));
        assertEquals(player.turnSequence().possibleDestinations(), testList);
    }

    @Test
    public void executeAction() {
        //-------------------------- Test 1 --------- CommunicationController --> chooseBox returns element 7
        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        Worker chosenWorker = new Worker(false, map.position(2,2));
        Worker worker = new Worker(true, map.position(0,0));
        Worker targetWorker = new Worker(true, map.position(2,3));
        Player player = new Player("player1", Colour.BLUE,new GodCard("Minotaur", 8, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.assignWorker(worker);
        player.turnSequence().setChosenWorker(chosenWorker);
        player.turnSequence().addMovableWorker(chosenWorker);
        actionController.initialisePossibleDestinations(player.turnSequence(), map);
        TurnSequenceModifier minotaurPower = new PushAdjacentOpponentPower();
        minotaurPower.changePossibleOptions(player, actionController, map);
        Box chosenBox = communicationController.chooseBox(player.turnSequence().possibleDestinations());
        player.turnSequence().setChosenBox(chosenBox);
        actionController.updateNewPositions(player.turnSequence());
        minotaurPower.executeAction(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>());

        assertTrue(player.turnSequence().newPositions().containsKey(chosenWorker));
        assertTrue(player.turnSequence().newPositions().containsKey(targetWorker));
        assertEquals(map.position(2, 4), player.turnSequence().newPositions().get(targetWorker));
        assertEquals(map.position(2,3), player.turnSequence().newPositions().get(chosenWorker));
    }
}