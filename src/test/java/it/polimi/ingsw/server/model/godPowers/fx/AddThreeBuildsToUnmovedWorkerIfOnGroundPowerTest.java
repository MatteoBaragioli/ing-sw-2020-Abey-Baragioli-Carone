package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.network.exceptions.*;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.godPowers.setUpConditions.NoSetUpCondition;
import org.junit.Test;
import static org.junit.Assert.*;

import it.polimi.ingsw.server.model.*;

import java.util.ArrayList;


public class AddThreeBuildsToUnmovedWorkerIfOnGroundPowerTest {

    @Test
    public void executePower() {

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

        AddThreeBuildsToUnmovedWorkerIfOnGroundPower poseidonPower = new AddThreeBuildsToUnmovedWorkerIfOnGroundPower();
        actionController.initialisePossibleDestinations(player.turnSequence(), map);
        actionController.updateNewPositions(player.turnSequence());
        player.turnSequence().setChosenBox(map.position(1, 0));
        for (int i = 0; i < 3; i++)
            poseidonPower.executePower(player, actionController,chosenBox, new MatchStory(player));
        assertEquals(3, map.position(0, 1).level());
        assertEquals(chosenBox, player.turnSequence().chosenBox());


    }

    @Test
    public void usePower() throws ChannelClosedException, TimeOutException {
        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        Worker chosenWorker = new Worker(map.position(2,2), Colour.BLUE);
        Worker worker = new Worker(map.position(3,3), Colour.BLUE);
        Player player = new Player("player1", Colour.BLUE, new GodCard("Prometheus", 10, new ArrayList<>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
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
        for (int i = 0; i < 3 && !player.turnSequence().possibleBuilds().isEmpty(); i++) {
            actionController.initialisePossibleBuilds(player.turnSequence(), map);
            actionController.applyOpponentsCondition(player, new ArrayList<>(), 2, map);
            poseidonPower.usePower(player, communicationController, actionController, map, new ArrayList<>(), new ArrayList<>(), false, new MatchStory(player));
        }
        assertTrue(player.turnSequence().newPositions().isEmpty());
        assertTrue(player.turnSequence().builtOnBoxes().isEmpty());
        assertEquals(1, player.turnSequence().allowedLevelDifference());
        //User uses the power
        for (int i = 0; i < 3 && !player.turnSequence().possibleBuilds().isEmpty(); i++) {
            actionController.initialisePossibleBuilds(player.turnSequence(), map);
            actionController.applyOpponentsCondition(player, new ArrayList<>(), 2, map);
            poseidonPower.usePower(player, communicationController, actionController, map, new ArrayList<>(), new ArrayList<>(), true, new MatchStory(player));
        }
        assertFalse(player.turnSequence().possibleBuilds().isEmpty());
        assertFalse(player.turnSequence().builtOnBoxes().isEmpty());
        assertTrue(player.turnSequence().builtOnBoxes().contains(player.turnSequence().chosenBox()));
        assertTrue(player.turnSequence().possibleDestinations().isEmpty());
        assertTrue(player.turnSequence().newPositions().isEmpty());
        assertEquals(3, player.turnSequence().builtOnBoxes().size());
        assertTrue(player.turnSequence().builtOnBoxes().get(0).level()>0);
        assertTrue(player.turnSequence().builtOnBoxes().get(1).level()>0);
        assertTrue(player.turnSequence().builtOnBoxes().get(2).level()>0);
    }
}