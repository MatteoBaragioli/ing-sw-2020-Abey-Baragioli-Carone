package it.polimi.ingsw.server.model.godPowers;

import org.junit.Test;
import static org.junit.Assert.*;

import it.polimi.ingsw.server.model.*;
import java.util.ArrayList;
import java.util.List;


public class AddMoveNotStartingBoxPowerTest {

    @Test
    public void usePower() {
        //-------------------------- Test 1 ---------

        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        Worker chosenWorker = new Worker(map.position(0,0), Colour.BLUE);
        Player player = new Player("player1", Colour.BLUE, new GodCard("Artemis", 2, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.turnSequence().setChosenWorker(chosenWorker);

        TurnSequenceModifier artemisPower = new AddMoveNotStartingBoxPower();

        //User doesn't use the power
        artemisPower.usePower(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>(), false);
        assertTrue(player.turnSequence().possibleDestinations().isEmpty());
        assertTrue(player.turnSequence().newPositions().isEmpty());
        assertEquals(1, player.turnSequence().allowedLevelDifference());

        //User uses the power
        //for the test the worker is in "position" (for each position in the map) and has a previous box that shouldn't be in possibleDestinations
        for(Box position : map.groundToList()){
            player.turnSequence().recordNewPosition(player.workers().get(0), position);
            player.turnSequence().confirmTurnSequence();
            player.turnSequence().setPreviousBox(map.position(position.positionX()-1,position.positionY()));
            if(player.turnSequence().previousBox()==null)
                player.turnSequence().setPreviousBox(map.position(position.positionX()+1,position.positionY()));
            player.turnSequence().setChosenWorker(player.workers().get(0));

            player.turnSequence().setChosenBox(position); //I put a casual chosenBox only to make method executePower work
            artemisPower.usePower(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>(), true);
            assertFalse(player.turnSequence().possibleDestinations().contains(player.turnSequence().previousBox()));
        }
    }

    @Test
    public void executePower() {
        //-------------------------- Test 1 ---------

        Map map = new  Map();
        ActionController actionController = new ActionController();
        Worker chosenWorker = new Worker(false, map.position(2,2));
        Worker worker = new Worker(true, map.position(0,0));
        Player player = new Player("player1", Colour.BLUE, new GodCard("Artemis", 2, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.assignWorker(worker);
        player.turnSequence().setChosenWorker(chosenWorker);
        player.turnSequence().addMovableWorker(chosenWorker);

        TurnSequenceModifier artemisPower = new AddMoveNotStartingBoxPower();

        assertTrue(player.turnSequence().possibleDestinations().isEmpty());
        assertTrue(player.turnSequence().newPositions().isEmpty());
        assertTrue(player.turnSequence().possibleBuilds().isEmpty());
        assertTrue(player.turnSequence().builtOnBoxes().isEmpty());
        assertEquals(1, player.turnSequence().allowedLevelDifference());

        actionController.initialisePossibleDestinations(player.turnSequence(), map);

        //for each Box in possibleDestinations
        for(Box chosenBox : player.turnSequence().possibleDestinations()) {
            artemisPower.executePower(player, actionController,  chosenBox);
            assertFalse(player.turnSequence().possibleDestinations().isEmpty());
            assertFalse(player.turnSequence().newPositions().isEmpty());
            assertEquals(player.turnSequence().newPositions().get(player.turnSequence().chosenWorker()), chosenBox);
            assertEquals(1, player.turnSequence().newPositions().size());
            player.turnSequence().clearNewPositions();
            player.turnSequence().resetPreviousBox();
            player.turnSequence().clearMovedWorkers();
        }
    }
}