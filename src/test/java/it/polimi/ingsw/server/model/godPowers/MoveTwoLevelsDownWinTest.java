package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.*;
import org.junit.Test;

import static it.polimi.ingsw.server.model.Colour.*;
import static org.junit.Assert.*;

public class MoveTwoLevelsDownWinTest {

    @Test
    public void establishWinCondition() {
        //this test tries to establish the win of a player that moves from a second level box to a ground level box
        Map map = new Map();
        WinCondition win = new MoveTwoLevelsDownWin();
        Player player = new Player("blue", GREY, null);
        Box box1 = map.position(0, 0);
        Box box2 = map.position(0, 1);
        box1.build();
        box1.build();
        Worker worker = new Worker(box1,Colour.GREY );
        player.workers().add(worker);
        player.turnSequence().movableWorkers().add(worker);
        player.turnSequence().setChosenWorker(worker);
        assertEquals(map.levelDifference(box1, box2), -2);
        player.turnSequence().recordNewPosition(worker, box2);
        assertTrue(win.establishWinCondition(player, map));
        //this test tries to establish the win of a player that moves from a third level box to a ground level box
        map = new Map();
        player = new Player("blue", GREY, null);
        box1 = map.position(0, 0);
        box2 = map.position(0, 1);
        box1.build();
        box1.build();
        box1.build();
        worker = new Worker(box1,Colour.GREY );
        player.workers().add(worker);
        player.turnSequence().movableWorkers().add(worker);
        player.turnSequence().setChosenWorker(worker);
        assertEquals(map.levelDifference(box1, box2), -3);
        player.turnSequence().recordNewPosition(worker, box2);
        assertTrue(win.establishWinCondition(player, map));
        //this test tries to establish the win of a player that moves from a first level box to a ground level box
        map = new Map();
        player = new Player("blue", GREY, null);
        box1 = map.position(0, 0);
        box2 = map.position(0, 1);
        box1.build();
        worker = new Worker(box1,Colour.GREY );
        player.workers().add(worker);
        player.turnSequence().movableWorkers().add(worker);
        player.turnSequence().setChosenWorker(worker);
        assertEquals(map.levelDifference(box1, box2), -1);
        player.turnSequence().recordNewPosition(worker, box2);
        assertFalse(win.establishWinCondition(player, map));
    }
}