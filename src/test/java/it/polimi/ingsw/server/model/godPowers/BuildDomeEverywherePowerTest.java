package it.polimi.ingsw.server.model.godPowers;

import org.junit.Test;
import static org.junit.Assert.*;

import it.polimi.ingsw.server.model.*;
import java.util.ArrayList;
import java.util.List;


public class BuildDomeEverywherePowerTest {

    @Test
    public void executeAction() {
        //chosenWorker in (2,4) builds a dome on (1,3)
        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        Worker chosenWorker = new Worker(false, map.position(2,4));
        Worker worker = new Worker(true, map.position(0,0));
        Player player = new Player("player1", Colour.BLUE,new GodCard("Atlas", 4, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.assignWorker(worker);
        player.turnSequence().setChosenWorker(chosenWorker);
        player.turnSequence().setChosenBox(map.position(1,3));
        map.position(1, 4).build();
        map.position(1, 4).build();
        map.position(1, 4).build();
        map.position(1, 4).build(); //hasDome
        map.position(1, 3).build();
        map.position(1, 3).build();//level 2

        TurnSequenceModifier atlasPower = new BuildDomeEverywherePower();
        actionController.initialisePossibleBuilds(player.turnSequence(), map);
        actionController.updateBuiltOnBox(player.turnSequence());
        atlasPower.executeAction(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>());

        List<Box> expectedPossibleBuilds = new ArrayList<>();
        expectedPossibleBuilds.add(map.position(1, 3));
        expectedPossibleBuilds.add(map.position(2, 3));
        expectedPossibleBuilds.add(map.position(3, 3));
        expectedPossibleBuilds.add(map.position(3, 4));


        assertEquals(expectedPossibleBuilds , player.turnSequence().possibleBuilds());
        assertEquals(2, map.position(1, 3).level());
        assertTrue(map.position(1, 3).hasDome());
    }
}