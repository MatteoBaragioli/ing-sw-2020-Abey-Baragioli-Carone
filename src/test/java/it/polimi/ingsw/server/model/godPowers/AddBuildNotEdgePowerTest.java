package it.polimi.ingsw.server.model.godPowers;

import org.junit.Test;
import static org.junit.Assert.*;

import it.polimi.ingsw.server.model.*;
import java.util.ArrayList;
import java.util.List;


public class AddBuildNotEdgePowerTest {

    @Test
    public void executeAction() {
        //-------------------------- Test 1 ---------
        //chosenWorker in (2,3) builds on (1,3)
        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        Worker chosenWorker = new Worker(false, map.position(2,3));
        Worker worker = new Worker(true, map.position(0,0));
        Player player = new Player("player1", Colour.BLUE,new GodCard("Hestia", 21, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.assignWorker(worker);
        player.turnSequence().setChosenWorker(chosenWorker);
        player.turnSequence().setChosenBox(map.position(1,3));

        TurnSequenceModifier hestiaPower = new AddBuildNotEdgePower();

        hestiaPower.executeAction(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>());

        List<Box> expectedPossibleBuilds = new ArrayList<>();
        expectedPossibleBuilds.add(map.position(1, 2));
        expectedPossibleBuilds.add(map.position(1, 3));
        expectedPossibleBuilds.add(map.position(2, 2));
        expectedPossibleBuilds.add(map.position(3, 2));
        expectedPossibleBuilds.add(map.position(3, 3));


        assertEquals(expectedPossibleBuilds , player.turnSequence().possibleBuilds());
        assertEquals(1, player.turnSequence().chosenBox().level());
    }
}