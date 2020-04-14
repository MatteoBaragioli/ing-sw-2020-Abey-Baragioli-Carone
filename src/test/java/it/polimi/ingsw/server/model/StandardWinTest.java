package it.polimi.ingsw.server.model;

import org.junit.Test;

import static it.polimi.ingsw.server.model.Colour.*;
import static org.junit.Assert.*;

public class StandardWinTest {

    @Test
    public void establishWinCondition() {
        //this test tries to establish the standard win of a player that moves from a second level to a third a third level
        Map map=new Map();
        WinCondition standardWin=new StandardWin();
        Player player= new Player("blue", GREY, null);
        Box box1=map.position(0,0);
        Box box2=map.position(0,1);
        box1.build();
        box1.build();
        box2.build();
        box2.build();
        box2.build();
        Worker worker=new Worker(box1);
        player.workers().add(worker);
        player.turnSequence().movableWorkers().add(worker);
        player.turnSequence().setChosenWorker(worker);
        player.turnSequence().recordNewPosition(worker, box2);
        assertTrue(standardWin.establishWinCondition(player,map));
        //this test tries to establish if a player that moves from a third level box to another third level box win
        map=new Map();
        box1=map.position(0,0);
        box2=map.position(0,1);
        box1.build();
        box1.build();
        box1.build();
        box2.build();
        box2.build();
        box2.build();
        worker=new Worker(box1);
        player.workers().add(worker);
        player.turnSequence().movableWorkers().add(worker);
        player.turnSequence().setChosenWorker(worker);
        player.turnSequence().recordNewPosition(worker, box2);
        assertFalse(standardWin.establishWinCondition(player,map));

    }
}