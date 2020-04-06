package it.polimi.ingsw.server.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class WorkerTest {

    @Test
    public void move() {
        Box box = new Box(1, 2);// box used as reference
        //this test tries to do a move of a worker to a common box
        Box secondBox = new Box(0, false, 1, 1);
        Worker worker = new Worker(box);
        worker.move(secondBox);
        assertTrue(worker.position().equals(secondBox));
        //------------------------------------ new test ----------------------------------------
        //this test tries to do a move of a worker to a box that has a dome
        box = new Box(1, 2);// box used as reference
        worker = new Worker(box);
        secondBox = new Box(0, true, 1, 1);
        worker.move(secondBox);
        assertTrue(worker.position().equals(box));
        //------------------------------------ new test ----------------------------------------
        //this test tries to do a move of a worker to a box occupied by another worker
        box = new Box(1, 2);// box used as reference
        secondBox = new Box(3, false, 1, 1);
        Worker opponentsWorker = new Worker(secondBox);
        worker = new Worker(box);
        worker.move(secondBox);
        assertFalse(worker.position().equals(secondBox));
    }
}