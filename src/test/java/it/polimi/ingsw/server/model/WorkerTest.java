package it.polimi.ingsw.server.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class WorkerTest {

    @Test
    public void move() {
        Box box=new Box(1,2);// box used as reference
        Box secondBox=new Box(0, false,1, 1);
        Worker worker=new Worker(box);
        worker.move(secondBox);
        assertTrue(worker.position().equals(secondBox));
        //------------------------------------ new test ----------------------------------------
        worker=new Worker(box);
        secondBox=new Box(0, true,1, 1);
        worker.move(secondBox);
        assertTrue(worker.position().equals(box));
        //------------------------------------ new test ----------------------------------------
        worker=new Worker(box);
        secondBox=new Box(0, false,1, 1);
        worker.move(secondBox);
        assertFalse(worker.position().equals(box));
        //------------------------------------ new test ----------------------------------------
        worker=new Worker(box);
        secondBox=new Box(3, false,1, 1);
        worker.move(secondBox);
        assertTrue(worker.position().equals(secondBox));
        //------------------------------------ new test ----------------------------------------
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
        worker=new Worker(box);
        targetBox=new Box(1, true,1,3);
        worker.levelledMove(1, targetBox);
        assertFalse (worker.position().equals(targetBox));
        //------------------------------------ new test ----------------------------------------
        //this test tries to do a levelled move of a worker to a box within the allowed level difference permitted
        worker=new Worker(box);
        targetBox=new Box(1, false,1,3);
        worker.levelledMove(1, targetBox);
        assertTrue(worker.position().equals(targetBox));
        //------------------------------------ new test ----------------------------------------
        //this test tries to do a levelled move of a worker to a box on the same level having allowed level difference 1
        worker=new Worker(box);
        targetBox=new Box(0, false,1,3);
        worker.levelledMove(1, targetBox);
        assertTrue(worker.position().equals(targetBox));
        //------------------------------------ new test ----------------------------------------
        //this test tries to do a levelled move of a worker to a box on the same level having allowed level difference 0
        worker=new Worker(box);
        targetBox=new Box(0, false,1,3);
        worker.levelledMove(0, targetBox);
        assertTrue(worker.position().equals(targetBox));
        //------------------------------------ new test ----------------------------------------
        //this test tries to do a levelled move of a worker to a box two levels down having allowed level difference 0
        targetBox=new Box(2, false,1,3);
        worker=new Worker(targetBox);
        worker.levelledMove(0, targetBox);
        assertTrue(worker.position().equals(targetBox));
        //------------------------------------ new test ----------------------------------------
        //this test tries to do a levelled move of a worker to a box that is two levels down having allowed level difference 1
        targetBox=new Box(2, false,1,3);
        worker=new Worker(targetBox);
        worker.levelledMove(1, box);
        assertTrue(worker.position().equals(box));


    }
}