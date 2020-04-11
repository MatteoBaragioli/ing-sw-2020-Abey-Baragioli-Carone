package it.polimi.ingsw.server.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class TurnSequenceTest {

    //Done
    @Test
    public void workersCurrentPosition() {
        Map map = new Map();
        TurnSequence turnSequence = new TurnSequence();
        Box box1 = map.position(1,1);
        Box box2 = map.position(2,2);
        Worker worker = new Worker(box1);

        //checking that with an unmoved worker the return has the same position
        assertEquals(worker.position(), turnSequence.workersCurrentPosition(worker));

        //checking that with a moved worker the return value is different
        turnSequence.recordNewPosition(worker, box2);
        assertEquals(turnSequence.newPositions().get(worker), turnSequence.workersCurrentPosition(worker));

        //checking that the method prioritizes the movedWorkers list even if newPositions contains the key
        turnSequence.clearMovedWorkers();
        assertEquals(worker.position(), turnSequence.workersCurrentPosition(worker));

        turnSequence.recordNewPosition(worker, box1);
        assertEquals(turnSequence.newPositions().get(worker), turnSequence.workersCurrentPosition(worker));
    }

    //Done
    @Test
    public void recordNewPosition() {
        Map map = new Map();
        TurnSequence turnSequence = new TurnSequence();
        Box spawn1 = map.position(0,0);
        Box box1 = map.position(1,1);
        Worker worker1 = new Worker(spawn1);

        //the record must be empty at the start
        assertTrue(turnSequence.newPositions().isEmpty());
        assertTrue(turnSequence.movedWorkers().isEmpty());
        assertFalse(turnSequence.newPositions().containsKey(worker1));
        assertNull(turnSequence.previousBox());

        //making the player's worker move
        turnSequence.addMovableWorker(worker1);
        turnSequence.recordNewPosition(worker1,box1);

        //checking if the record updates correctly (did it add an element?)
        assertFalse(turnSequence.newPositions().isEmpty());
        assertTrue(turnSequence.newPositions().containsKey(worker1));
        assertEquals(turnSequence.newPositions().get(worker1), box1);
        //the new box must be occupied by the worker
        assertEquals(turnSequence.newPositions().get(worker1).occupier(), worker1);
        //worker.position() should still the spawn box
        assertEquals(worker1.position(), spawn1);

        //the previous box must be updated (if it's a player's worker)
        assertNotNull(turnSequence.previousBox());
        assertEquals(turnSequence.previousBox(), spawn1);
        //the previous box mustn't be occupied by the worker
        assertNotEquals(worker1, turnSequence.previousBox().occupier());

        //the moved workers' list must be updated as well
        assertFalse(turnSequence.movedWorkers().isEmpty());

        //checking if the latest position overwrites the older position
        turnSequence.recordNewPosition(worker1, spawn1);
        assertEquals(turnSequence.newPositions().get(worker1), spawn1);
        assertEquals(turnSequence.previousBox(), box1);

        //making an opponent worker move
        Box spawn2 = map.position(0, 1);
        Worker worker2 = new Worker(spawn2);
        Box box2 = map.position(1,0);

        turnSequence.recordNewPosition(worker2, box2);

        //checking if the record updates correctly (did it add an element?)
        assertTrue(turnSequence.movedWorkers().contains(worker2));
        assertTrue(turnSequence.newPositions().containsKey(worker2));
        assertEquals(turnSequence.newPositions().get(worker2), box2);

        //previousBox shouldn't update
        assertEquals(turnSequence.previousBox(), box1);

        //making two workers swap positions
        turnSequence.recordNewPosition(worker1,box2);
        turnSequence.recordNewPosition(worker2, spawn1);

        assertEquals(turnSequence.newPositions().get(worker1), box2);
        assertEquals(turnSequence.newPositions().get(worker2), spawn1);

        //box occupiers should be updated
        assertEquals(worker1, box2.occupier());
        assertEquals(worker2, spawn1.occupier());

        //the previous box should follow the worker1
        assertEquals(spawn1, turnSequence.previousBox());
    }

    //Done
    @Test
    public void clearNewPositions() {}

    //Done
    @Test
    public void undoNewPositions() {
        Map map = new Map();
        TurnSequence turnSequence = new TurnSequence();
        Box box00 = map.position(0,0);
        Box box11 = map.position(1,1);
        Box box22 = map.position(2, 2);
        Box box33 = map.position(3,3);
        Box box44 = map.position(4,4);

        Box box04 = map.position(0,4);
        Box box13 = map.position(1,3);
        Box box31 = map.position(3,1);
        Box box40 = map.position(4,0);

        //making two workers move (crossing paths)
        Worker worker1 = new Worker(box00);
        Worker worker2 = new Worker(box04);

        turnSequence.recordNewPosition(worker1, box11);
        turnSequence.recordNewPosition(worker2, box13);
        turnSequence.recordNewPosition(worker1, box22);
        turnSequence.recordNewPosition(worker2, box22);
        turnSequence.recordNewPosition(worker1, box33);
        turnSequence.recordNewPosition(worker2, box31);
        turnSequence.recordNewPosition(worker1, box44);
        turnSequence.recordNewPosition(worker2, box40);

        turnSequence.undoNewPositions();

        //the stepping boxes should be free of occupiers
        assertTrue(box11.isFree());
        assertTrue(box13.isFree());
        assertTrue(box22.isFree());
        assertTrue(box33.isFree());
        assertTrue(box31.isFree());
        assertTrue(box44.isFree());
        assertTrue(box40.isFree());

        //the workers original positions should be occupied
        assertEquals(worker1, worker1.position().occupier());
        assertEquals(worker1, box00.occupier());
        assertEquals(worker2, worker2.position().occupier());
        assertEquals(worker2, box04.occupier());
    }

    //Done
    @Test
    public void getTheMovesDone() {
        Map map = new Map();
        TurnSequence turnSequence = new TurnSequence();
        Box box00 = map.position(0,0);
        Box box11 = map.position(1,1);
        Box box22 = map.position(2, 2);
        Box box33 = map.position(3,3);
        Box box44 = map.position(4,4);

        Box box04 = map.position(0,4);
        Box box13 = map.position(1,3);
        Box box31 = map.position(3,1);
        Box box40 = map.position(4,0);

        //making two workers move (crossing paths)
        Worker worker1 = new Worker(box00);
        Worker worker2 = new Worker(box04);

        turnSequence.recordNewPosition(worker1, box11);
        turnSequence.recordNewPosition(worker2, box13);
        turnSequence.recordNewPosition(worker1, box22);
        turnSequence.recordNewPosition(worker2, box22);
        turnSequence.recordNewPosition(worker1, box33);
        turnSequence.recordNewPosition(worker2, box31);
        turnSequence.recordNewPosition(worker1, box44);
        turnSequence.recordNewPosition(worker2, box40);

        turnSequence.getTheMovesDone();

        //the workers' final positions should be updated and occupied
        assertEquals(worker1, box44.occupier());
        assertEquals(worker2, box40.occupier());
        assertEquals(box44, worker1.position());
        assertEquals(box40, worker2.position());

        //the other positions should be free
        assertTrue(box00.isFree());
        assertTrue(box04.isFree());
        assertTrue(box11.isFree());
        assertTrue(box13.isFree());
        assertTrue(box22.isFree());
        assertTrue(box31.isFree());
        assertTrue(box33.isFree());
    }

    @Test
    public void recordBuiltOnBox() {
        Map map = new Map();
        TurnSequence turnSequence = new TurnSequence();
        Box box1 = map.position(1,1);
        Box box2 = map.position(2,2);

        //checking that it isn't adding unbuilt boxes
        turnSequence.recordBuiltOnBox(box1);
        assertTrue(turnSequence.builtOnBoxes().isEmpty());

        //checking that a box is added correctly
        box1.build();
        turnSequence.recordBuiltOnBox(box1);
        assertFalse(turnSequence.builtOnBoxes().isEmpty());
        assertTrue(turnSequence.builtOnBoxes().contains(box1));

        //checking that the same box can be added twice
        turnSequence.recordBuiltOnBox(box1);
        assertTrue(turnSequence.builtOnBoxes().size()>1);
    }

    @Test
    public void clearBuiltOnBoxes() {
    }

    @Test
    public void recordRemovedBlock() {
    }

    @Test
    public void clearRemovedBlocks() {
    }

    @Test
    public void undoRemovals() {
    }

    @Test
    public void resetAllowedLevelDifference() {
    }

    @Test
    public void addPossibleDestination() {
    }

    @Test
    public void clearPossibleDestinations() {
    }

    @Test
    public void addPossibleBuild() {
    }

    @Test
    public void clearPossibleBuilds() {
    }

    @Test
    public void addMovableWorker() {
    }

    @Test
    public void clearMovableWorkers() {
    }

    @Test
    public void recordMovedWorkers() {
    }

    @Test
    public void clearMovedWorkers() {
    }

    @Test
    public void reset() {
    }

    @Test
    public void confirmTurnSequence() {
    }

    @Test
    public void undo() {
    }

    @Test
    public void clearTurnSequence() {
    }
}