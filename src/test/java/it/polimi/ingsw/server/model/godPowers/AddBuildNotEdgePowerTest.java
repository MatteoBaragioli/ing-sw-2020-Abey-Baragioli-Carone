package it.polimi.ingsw.server.model.godPowers;

import org.junit.Test;
import static org.junit.Assert.*;

import it.polimi.ingsw.server.model.*;
import java.util.ArrayList;
import java.util.List;


public class AddBuildNotEdgePowerTest {


    @Test
    public void usePower() {
        //-------------------------- Test 1 ---------

        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        Worker chosenWorker = new Worker(map.position(0,0), Colour.BLUE);
        Player player = new Player("player1", Colour.BLUE, new GodCard("Prometheus", 10, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.turnSequence().setChosenWorker(chosenWorker);

        AddBuildNotEdgePower hestiaPower = new AddBuildNotEdgePower();

        //User doesn't use the power
        hestiaPower.usePower(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>(), false);
        assertTrue(player.turnSequence().possibleBuilds().isEmpty());
        assertTrue(player.turnSequence().builtOnBoxes().isEmpty());
        assertEquals(1, player.turnSequence().allowedLevelDifference());


        //User uses the power
        for(Box position : map.groundToList()){
            player.turnSequence().recordNewPosition(player.workers().get(0), position);
            player.turnSequence().confirmTurnSequence();
            player.turnSequence().setChosenBox(map.position(0,0));
            player.turnSequence().setChosenWorker(player.workers().get(0));
            hestiaPower.usePower(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>(), true);
            for(Box possibleBuild : player.turnSequence().possibleBuilds())
                assertFalse(possibleBuild.isOnEdge());
        }
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

        AddBuildNotEdgePower hestiaPower = new AddBuildNotEdgePower();

        assertTrue(player.turnSequence().possibleDestinations().isEmpty());
        assertTrue(player.turnSequence().newPositions().isEmpty());
        assertTrue(player.turnSequence().possibleBuilds().isEmpty());
        assertTrue(player.turnSequence().builtOnBoxes().isEmpty());
        assertEquals(1, player.turnSequence().allowedLevelDifference());

        actionController.initialisePossibleBuilds(player.turnSequence(), map);


        //for each Box in possibleBuilds
        for(Box chosenBox : player.turnSequence().possibleBuilds()) {
            hestiaPower.executePower(player, actionController,  chosenBox);
            assertFalse(player.turnSequence().possibleBuilds().isEmpty());
            assertFalse(player.turnSequence().builtOnBoxes().isEmpty());
            assertTrue(player.turnSequence().builtOnBoxes().contains(chosenBox));
            assertEquals(1, player.turnSequence().builtOnBoxes().size());
            assertEquals(1, player.turnSequence().builtOnBoxes().get(0).level());
            assertEquals(1, player.turnSequence().allowedLevelDifference());
            player.turnSequence().clearBuiltOnBoxes();
        }
    }
}