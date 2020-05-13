package it.polimi.ingsw.server.model.godPowers;

import org.junit.Test;
import static org.junit.Assert.*;

import it.polimi.ingsw.server.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AddThreeBuildsToUnmovedWorkerIfOnGroundPowerTest {

    @Test
    public void executePower() {

        //unmovedWorker in (0,0) builds three times on (0,1)
        Map map = new Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        Worker chosenWorker = new Worker(false, map.position(2, 3));
        Worker worker = new Worker(true, map.position(0, 0));
        Player player = new Player("player1", Colour.BLUE, new GodCard("Poseidon", 27, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.assignWorker(worker);
        player.turnSequence().setChosenWorker(chosenWorker);
        Box chosenBox = (map.position(0, 1));

        AddThreeBuildsToUnmovedWorkerIfOnGroundPower poseidonPower = new AddThreeBuildsToUnmovedWorkerIfOnGroundPower(); //possibile errore se metto
        //turnSequenceModifier poseidonPower = new AddThreeBuildsToUnmovedWorkerIfOnGroundPower(); //mettere in turn sequence modifier metodo use power e execute power vuoti;

        actionController.initialisePossibleDestinations(player.turnSequence(), map);
        actionController.updateNewPositions(player.turnSequence());
        player.turnSequence().setChosenBox(map.position(1, 0));
        for (int i = 0; i < 3; i++)
            poseidonPower.executePower(player, actionController,chosenBox);
        assertEquals(3, map.position(0, 1).level());
        assertEquals(chosenBox, player.turnSequence().chosenBox());


    }

    @Test
    public void usePower() throws IOException {
        //this test tries to add a build to the chosenWorker
        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        Worker chosenWorker = new Worker(map.position(2,2), Colour.BLUE);
        Worker worker = new Worker(map.position(3,3), Colour.BLUE);
        Player player = new Player("player1", Colour.BLUE, new GodCard("Prometheus", 10, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.assignWorker(worker);
        player.turnSequence().setChosenWorker(chosenWorker);

        TurnSequenceModifier poseidonPower = new AddThreeBuildsToUnmovedWorkerIfOnGroundPower();

        assertTrue(player.turnSequence().possibleDestinations().isEmpty());
        assertTrue(player.turnSequence().newPositions().isEmpty());
        assertTrue(player.turnSequence().possibleBuilds().isEmpty());
        assertTrue(player.turnSequence().builtOnBoxes().isEmpty());
        actionController.initialisePossibleBuilds(player.turnSequence(), map);
        assertEquals(1, player.turnSequence().allowedLevelDifference());
        //User doesn't use the power
        poseidonPower.usePower(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>(), false);
        assertTrue(player.turnSequence().possibleDestinations().isEmpty());
        assertTrue(player.turnSequence().newPositions().isEmpty());
        assertTrue(player.turnSequence().builtOnBoxes().isEmpty());
        assertEquals(1, player.turnSequence().allowedLevelDifference());
        //User uses the power
        player.turnSequence().setChosenBox(map.position(2,1));
        poseidonPower.usePower(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>(), true);
        assertFalse(player.turnSequence().possibleBuilds().isEmpty());
        assertFalse(player.turnSequence().builtOnBoxes().isEmpty());
        assertTrue(player.turnSequence().builtOnBoxes().contains(player.turnSequence().chosenBox()));
        assertTrue(player.turnSequence().possibleDestinations().isEmpty());
        assertTrue(player.turnSequence().newPositions().isEmpty());
        assertEquals(1, player.turnSequence().builtOnBoxes().size());
        assertTrue(player.turnSequence().builtOnBoxes().get(0).level()>0);
    }

}
