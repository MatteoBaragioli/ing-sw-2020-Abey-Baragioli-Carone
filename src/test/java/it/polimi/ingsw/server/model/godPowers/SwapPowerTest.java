package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SwapPowerTest {

    @Test
    public void changePossibleOptions() {

        Map map = new  Map();
        ActionController actionController = new ActionController();
        Worker chosenWorker = new Worker(false, map.position(3,3));
        Worker worker = new Worker(true, map.position(2,2));
        Worker targetWorker = new Worker(true, map.position(3,4));
        Player player = new Player("player1", Colour.BLUE, new GodCard("Apollo", 1, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.assignWorker(worker);
        player.turnSequence().setChosenWorker(chosenWorker);

        TurnSequenceModifier apolloOptions = new SwapPower();
        apolloOptions.changePossibleOptions(player, actionController, map);

        assertTrue(player.turnSequence().possibleDestinations().contains(targetWorker.position()));
        assertEquals(1, player.turnSequence().possibleDestinations().size());

        player.turnSequence().clearPossibleDestinations();
        actionController.initialisePossibleDestinations(player.turnSequence(), map);
        apolloOptions.changePossibleOptions(player, actionController, map);

        List<Box> testList = new ArrayList<>();
        testList.add(map.position(2, 3));
        testList.add(map.position(2,4));
        testList.add(map.position(3,2));
        testList.add(map.position(4,2));
        testList.add(map.position(4, 3));
        testList.add(map.position(4,4));
        testList.add(map.position(3,4));
        assertEquals(player.turnSequence().possibleDestinations(), testList);


    }

    @Test
    public void executeAction() {
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