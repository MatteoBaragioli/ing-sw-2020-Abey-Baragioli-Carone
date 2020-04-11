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


    @Test
    public void undoNewPositions() {
        Map map = new Map();
        TurnSequence turnSequence = new TurnSequence();
        Box spawn1 = map.position(0,0);
        Box box11 = map.position(1,1);
        Box spawn2 = map.position(4,4);
        Box box21 = map.position(3,3);

        Worker worker1 = new Worker(spawn1);
        turnSequence.addMovableWorker(worker1);

        turnSequence.recordNewPosition(worker1, box11);

        Worker worker2 = new Worker(spawn2);

        turnSequence.recordNewPosition(worker2, box21);

        
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