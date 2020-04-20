package it.polimi.ingsw.server.model.godPowers;

import org.junit.Test;
import static org.junit.Assert.*;

import it.polimi.ingsw.server.model.*;
import java.util.ArrayList;
import java.util.List;


public class SwapPowerTest {

    @Test
    public void changePossibleOptions() {
        //---------------Test 1-------------worker on ground
        Map map = new  Map();
        ActionController actionController = new ActionController();
        //player's workers
        Worker chosenWorker = new Worker(map.position(3,3), Colour.GREY); // level 0
        Worker worker = new Worker(map.position(2,2), Colour.GREY);
        //opponents workers
        Worker targetWorker = new Worker(map.position(3,4), Colour.BLUE); //level 0
        map.position(3,2).build();
        Worker opponentWorker = new Worker(map.position(3,2), Colour.BLUE); //level 1
        map.position(2,3).build();
        map.position(2,3).build();
        Worker opponentWorker2 = new Worker(map.position(2,3), Colour.WHITE); //level 2
        map.position(4,3).build();
        map.position(4,3).build();
        map.position(4,3).build();
        Worker opponentWorker3 = new Worker(map.position(4,3), Colour.WHITE); // level 3
        Player player = new Player("player1", Colour.GREY, new GodCard("Apollo", 1, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.assignWorker(worker);
        player.turnSequence().setChosenWorker(chosenWorker);

        TurnSequenceModifier apolloOptions = new SwapPower();
        apolloOptions.changePossibleOptions(player, actionController, map);

        assertTrue(player.turnSequence().possibleDestinations().contains(targetWorker.position()));
        assertEquals(2, player.turnSequence().possibleDestinations().size()); //only Apollo possible destinations without standard initialise

        player.turnSequence().clearPossibleDestinations();
        actionController.initialisePossibleDestinations(player.turnSequence(), map);
        apolloOptions.changePossibleOptions(player, actionController, map);

        List<Box> testList = new ArrayList<>();
        testList.add(map.position(2,4));
        testList.add(map.position(4,2));
        testList.add(map.position(4,4));
        testList.add(map.position(3,2));
        testList.add(map.position(3,4));
        assertEquals(player.turnSequence().possibleDestinations(), testList);



        //---------------Test 2-------------worker on 3rd level
        map = new  Map();
        //player's workers
        map.position(3,3).build();
        map.position(3,3).build();
        map.position(3,3).build();
        chosenWorker = new Worker(map.position(3,3), Colour.GREY); // level 3
        worker = new Worker(map.position(2,2), Colour.GREY);
        //opponents workers
        targetWorker = new Worker(map.position(3,4), Colour.BLUE); //level 0
        map.position(3,2).build();
        opponentWorker = new Worker(map.position(3,2), Colour.BLUE); //level 1
        map.position(2,3).build();
        map.position(2,3).build();
        opponentWorker2 = new Worker(map.position(2,3), Colour.WHITE); //level 2
        map.position(4,3).build();
        map.position(4,3).build();
        map.position(4,3).build();
        opponentWorker3 = new Worker(map.position(4,3), Colour.WHITE); // level 3
        player = new Player("player1", Colour.GREY, new GodCard("Apollo", 1, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.assignWorker(worker);
        player.turnSequence().setChosenWorker(chosenWorker);

        apolloOptions = new SwapPower();
        apolloOptions.changePossibleOptions(player, actionController, map);

        assertTrue(player.turnSequence().possibleDestinations().contains(targetWorker.position()));
        assertEquals(4, player.turnSequence().possibleDestinations().size()); //only Apollo possible destinations without standard initialise

        player.turnSequence().clearPossibleDestinations();
        actionController.initialisePossibleDestinations(player.turnSequence(), map);
        apolloOptions.changePossibleOptions(player, actionController, map);

        testList = new ArrayList<>();
        testList.add(map.position(2,4));
        testList.add(map.position(4,2));
        testList.add(map.position(4,4));
        testList.add(map.position(2,3));
        testList.add(map.position(3,2));
        testList.add(map.position(3,4));
        testList.add(map.position(4,3));
        assertEquals(player.turnSequence().possibleDestinations(), testList);
    }

    @Test
    public void executeAction() {
        //-------------------------- Test 1 ---------
        //chosenWorker in (3,3) moves on (3,4) swapping the opponent's worker on (3,3)
        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        Worker chosenWorker = new Worker(false, map.position(3,3));
        Worker worker = new Worker(true, map.position(2,2));
        Worker targetWorker = new Worker(true, map.position(3,4));
        Player player = new Player("player1", Colour.BLUE, new GodCard("Apollo", 1, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.assignWorker(worker);
        player.turnSequence().setChosenWorker(chosenWorker);
        player.turnSequence().setChosenBox(map.position(3,4));
        Player opponent = new Player("opponent1", Colour.GREY, new GodCard("Artemis", 2, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        opponent.assignWorker(targetWorker);
        List<Player> opponents = new ArrayList<>();
        opponents.add(opponent);

        TurnSequenceModifier apolloPower = new SwapPower();

        //updateNewPositions
        player.turnSequence().setPreviousBox(chosenWorker.position());
        actionController.updateNewPositions(player.turnSequence());

        apolloPower.executeAction(player, communicationController, actionController, map, opponents, new ArrayList<WinCondition>());

        assertTrue(player.turnSequence().newPositions().containsKey(targetWorker));
        assertEquals(player.turnSequence().newPositions().get(targetWorker), player.turnSequence().previousBox());
    }
}