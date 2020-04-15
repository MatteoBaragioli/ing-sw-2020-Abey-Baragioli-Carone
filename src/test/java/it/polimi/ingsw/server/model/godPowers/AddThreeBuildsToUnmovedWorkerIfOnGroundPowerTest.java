package it.polimi.ingsw.server.model.godPowers;

import org.junit.Test;
import static org.junit.Assert.*;

import it.polimi.ingsw.server.model.*;
import java.util.ArrayList;
import java.util.List;


public class AddThreeBuildsToUnmovedWorkerIfOnGroundPowerTest {

    @Test
    public void executeAction() {

        //-------------------------- Test 1 ---------
        //unmovedWorker in (0,0) builds three times on (1,0)
        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        Worker chosenWorker = new Worker(false, map.position(2,3));
        Worker worker = new Worker(true, map.position(0,0));
        Player player = new Player("player1", Colour.BLUE,new GodCard("Poseidon", 27, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.assignWorker(worker);
        player.turnSequence().setChosenWorker(chosenWorker);
        player.turnSequence().setChosenBox(map.position(1,3));

        TurnSequenceModifier poseidonPower = new AddThreeBuildsToUnmovedWorkerIfOnGroundPower();

        actionController.initialisePossibleDestinations(player.turnSequence(), map);
        actionController.updateNewPositions(player.turnSequence());
        player.turnSequence().setChosenBox(map.position(1,0));
        poseidonPower.executeAction(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>());

        List<Box> expectedPossibleBuilds = new ArrayList<>();
        expectedPossibleBuilds.add(map.position(0, 1));
        expectedPossibleBuilds.add(map.position(1, 0));
        expectedPossibleBuilds.add(map.position(1, 1));


        assertEquals(expectedPossibleBuilds , player.turnSequence().possibleBuilds());
        assertEquals(3, map.position(1,0).level());
        assertEquals(map.position(1,3), player.turnSequence().newPositions().get(chosenWorker));
    }
}