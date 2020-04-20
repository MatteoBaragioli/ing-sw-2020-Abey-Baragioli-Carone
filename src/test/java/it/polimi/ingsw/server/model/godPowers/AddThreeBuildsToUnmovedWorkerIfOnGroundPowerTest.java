package it.polimi.ingsw.server.model.godPowers;

import org.junit.Test;
import static org.junit.Assert.*;

import it.polimi.ingsw.server.model.*;
import java.util.ArrayList;
import java.util.List;


public class AddThreeBuildsToUnmovedWorkerIfOnGroundPowerTest {

    @Test
    public void executePower() {

        //-------------------------- Test 1 ---------
        //unmovedWorker in (0,0) builds three times on (1,0)
        Map map = new Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        Worker chosenWorker = new Worker(false, map.position(2, 3));
        Worker worker = new Worker(true, map.position(0, 0));
        Player player = new Player("player1", Colour.BLUE, new GodCard("Poseidon", 27, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.assignWorker(worker);
        player.turnSequence().setChosenWorker(chosenWorker);
        Box chosenBox = (map.position(1, 3));

        AddThreeBuildsToUnmovedWorkerIfOnGroundPower poseidonPower = new AddThreeBuildsToUnmovedWorkerIfOnGroundPower(); //possibile errore se metto
        //turnSequenceModifier poseidonPower = new AddThreeBuildsToUnmovedWorkerIfOnGroundPower(); //mettere in turn sequence modifier metodo use power e execute power vuoti;

        actionController.initialisePossibleDestinations(player.turnSequence(), map);
        actionController.updateNewPositions(player.turnSequence());
        player.turnSequence().setChosenBox(map.position(1, 0));
        for (int i = 0; i < 3; i++)
            poseidonPower.executePower(player, actionController, new ArrayList<WinCondition>(), chosenBox, map, new ArrayList<Player>());
        assertEquals(3, map.position(1, 3).level());
        assertEquals(chosenBox, player.turnSequence().chosenBox());


    }

    @Test
    public void usePower() {
        Map map = new Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        Worker chosenWorker = new Worker(false, map.position(2, 3));
        Worker worker = new Worker(true, map.position(0, 0));
        Player player = new Player("player1", Colour.BLUE, new GodCard("Poseidon", 27, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.assignWorker(worker);
        player.turnSequence().setChosenWorker(chosenWorker);
        Box chosenBox = (map.position(1, 3));

        AddThreeBuildsToUnmovedWorkerIfOnGroundPower poseidonPower = new AddThreeBuildsToUnmovedWorkerIfOnGroundPower(); //possibile errore se metto
        //turnSequenceModifier poseidonPower = new AddThreeBuildsToUnmovedWorkerIfOnGroundPower(); //mettere in turn sequence modifier metodo use power e execute power vuoti;

        actionController.initialisePossibleDestinations(player.turnSequence(), map);
        actionController.updateNewPositions(player.turnSequence());
        player.turnSequence().setChosenBox(map.position(1, 0));
        poseidonPower.usePower(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>(),true );
        //todo finire test
        //todo test potere di zeus build under yourself power
    }
}