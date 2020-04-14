package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AddBuildNotSameBoxPowerTest {

    @Test
    public void executeAction() {
        //-------------------------- Test 1 --------- CommunicationController --> chooseBox returns element 0
        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        Worker chosenWorker = new Worker(false, map.position(2,3));
        Worker worker = new Worker(true, map.position(0,0));
        Player player = new Player("player1", Colour.BLUE, new GodCard("Demeter", 5, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.assignWorker(worker);
        player.turnSequence().setChosenWorker(chosenWorker);
        player.turnSequence().setChosenBox(map.position(1,3));

        TurnSequenceModifier demeterPower = new AddBuildNotSameBoxPower();

        actionController.initialisePossibleBuilds(player.turnSequence(), map);
        actionController.updateBuiltOnBox(player.turnSequence());
        demeterPower.executeAction(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>());

        List<Box> expectedPossibleBuilds = new ArrayList<>();
        expectedPossibleBuilds.add(map.position(1, 2));
        expectedPossibleBuilds.add(map.position(1, 4));
        expectedPossibleBuilds.add(map.position(2, 2));
        expectedPossibleBuilds.add(map.position(2, 4));
        expectedPossibleBuilds.add(map.position(3, 2));
        expectedPossibleBuilds.add(map.position(3, 3));
        expectedPossibleBuilds.add(map.position(3, 4));

        assertEquals(1, map.position(1,3).level());
        assertEquals(1, map.position(1,2).level());
        assertEquals(expectedPossibleBuilds , player.turnSequence().possibleBuilds());
    }
}