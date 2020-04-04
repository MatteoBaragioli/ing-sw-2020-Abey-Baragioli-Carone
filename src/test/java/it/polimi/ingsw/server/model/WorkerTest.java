package it.polimi.ingsw.server.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class WorkerTest {

    @Test
    public void move() {
        Box box=new Box(1,2);// box used as reference
        //this test tries to do a move of a worker to a common box
        Box secondBox=new Box(0, false,1, 1);
        Worker worker=new Worker(box);
        worker.move(secondBox);
        assertTrue(worker.position().equals(secondBox));
        //------------------------------------ new test ----------------------------------------
        //this test tries to do a move of a worker to a box that has a dome
        box=new Box(1,2);// box used as reference
        worker=new Worker(box);
        secondBox=new Box(0, true,1, 1);
        worker.move(secondBox);
        assertTrue(worker.position().equals(box));
        //------------------------------------ new test ----------------------------------------
        //this test tries to do a move of a worker to a box occupied by another worker
        box=new Box(1,2);// box used as reference
        secondBox=new Box(3, false,1, 1);
        Worker opponentsWorker=new Worker(secondBox);
        worker=new Worker(box);
        worker.move(secondBox);
        assertFalse(worker.position().equals(secondBox));
        //------------------------------------ new test ----------------------------------------
        //this test tries to do a move of a worker to a box previously occupied by another worker but that now is free
        box=new Box(1,2);// box used as reference
        secondBox=new Box(3, false,1, 1);
        Box thirdBox=new Box(1,3);
        opponentsWorker=new Worker(secondBox);
        opponentsWorker.move(thirdBox);
        worker=new Worker(box);
        worker.move(secondBox);
        assertTrue(worker.position().equals(secondBox));


    }

    @Test
    public void levelledMove() {
        Box box=new Box(0, false,1, 1);// box used as reference
        //this test tries to do a levelled move of a worker to a box that is to high
        Box targetBox=new Box(2, false, 1,2);
        Worker worker=new Worker(box);
        worker.levelledMove(1, targetBox);
        assertFalse(worker.position().equals(targetBox));
        //------------------------------------ new test ----------------------------------------
        //this test tries to do a levelled move of a worker to a box within the allowed level difference permitted but has a dome
        box=new Box(0, false,1, 1);// box used as reference
        worker=new Worker(box);
        targetBox=new Box(1, true,1,3);
        worker.levelledMove(1, targetBox);
        assertFalse (worker.position().equals(targetBox));
        //------------------------------------ new test ----------------------------------------
        //this test tries to do a levelled move of a worker to a box within the allowed level difference permitted
        box=new Box(0, false,1, 1);// box used as reference
        worker=new Worker(box);
        targetBox=new Box(1, false,1,3);
        worker.levelledMove(1, targetBox);
        assertTrue(worker.position().equals(targetBox));
        //------------------------------------ new test ----------------------------------------
        //this test tries to do a levelled move of a worker to a box on the same level having allowed level difference 1
        box=new Box(0, false,1, 1);// box used as reference
        worker=new Worker(box);
        targetBox=new Box(0, false,1,3);
        worker.levelledMove(1, targetBox);
        assertTrue(worker.position().equals(targetBox));
        //------------------------------------ new test ----------------------------------------
        //this test tries to do a levelled move of a worker to a box on the same level having allowed level difference 0
        box=new Box(0, false,1, 1);// box used as reference
        worker=new Worker(box);
        targetBox=new Box(0, false,1,3);
        worker.levelledMove(0, targetBox);
        assertTrue(worker.position().equals(targetBox));
        //------------------------------------ new test ----------------------------------------
        //this test tries to do a levelled move of a worker to a box two levels down having allowed level difference 0
        box=new Box(0, false,1, 1);// box used as reference
        targetBox=new Box(2, false,1,3);
        worker=new Worker(targetBox);
        worker.levelledMove(0, targetBox);
        assertTrue(worker.position().equals(targetBox));
        //------------------------------------ new test ----------------------------------------
        //this test tries to do a levelled move of a worker to a box that is two levels down having allowed level difference 1
        box=new Box(0, false,1, 1);// box used as reference
        targetBox=new Box(2, false,1,3);
        worker=new Worker(targetBox);
        worker.levelledMove(1, box);
        assertTrue(worker.position().equals(box));
        //------------------------------------ new test ----------------------------------------
        //this test tries to do a move of a worker to a box occupied by another worker
        box=new Box(1,2);// box used as reference
        targetBox=new Box(3, false,1, 1);
        Worker opponentsWorker=new Worker(targetBox);
        worker=new Worker(box);
        worker.move(targetBox);
        assertFalse(worker.position().equals(targetBox));



    }
}