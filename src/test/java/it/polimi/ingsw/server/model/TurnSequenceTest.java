package it.polimi.ingsw.server.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class TurnSequenceTest {

    @Test
    public void workersCurrentPosition() {
        Map map = new Map();
        TurnSequence turnSequence = new TurnSequence();
        Box box1 = map.position(1,1);
        Box box2 = map.position(2,2);
        Worker worker = new Worker(box1,Colour.GREY );

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
        Worker worker1 = new Worker(spawn1,Colour.GREY );

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
        Worker worker2 = new Worker(spawn2,Colour.GREY );
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

        //the one who let another worker in his box should be without location
        assertEquals(turnSequence.workersCurrentPosition(worker1), turnSequence.workersCurrentPosition(worker2));
        assertEquals(turnSequence.newPositions().get(worker1), box2);
        assertEquals(worker1, box2.occupier());
        assertEquals(spawn1, turnSequence.previousBox());

        turnSequence.recordNewPosition(worker2, spawn1);
        assertEquals(turnSequence.newPositions().get(worker2), spawn1);
        assertEquals(turnSequence.workersCurrentPosition(worker2), spawn1);

        //box occupiers should be updated
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
        Worker worker1 = new Worker(box00,Colour.GREY );
        Worker worker2 = new Worker(box04,Colour.GREY );

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
        Worker worker1 = new Worker(box00,Colour.GREY );
        Worker worker2 = new Worker(box04, Colour.GREY);

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
    //Done

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
        assertEquals(turnSequence.builtOnBoxes().get(0),turnSequence.builtOnBoxes().get(1));

        //checking that if a removed block is build again, it is removed just once from removedBlock
        turnSequence.recordRemovedBlock(box2);
        turnSequence.recordRemovedBlock(box2);
        assertTrue(turnSequence.removedBlocks().contains(box2));
        assertEquals(2, turnSequence.removedBlocks().size());
        box2.build();
        turnSequence.recordBuiltOnBox(box2);
        assertFalse(turnSequence.builtOnBoxes().contains(box2));
        assertTrue(turnSequence.removedBlocks().contains(box2));
        assertEquals(1, turnSequence.removedBlocks().size());
    }
    //Done

    @Test
    public void clearBuiltOnBoxes() {
    }

    @Test
    public void undoBuilds() {
        Map map = new Map();
        TurnSequence turnSequence = new TurnSequence();
        Box box1 = map.position(1,1);
        Box box2 = map.position(2,2);

        box1.build();
        box1.build();
        box1.buildDome();
        box2.build();
        box2.build();
        box2.build();
        turnSequence.recordBuiltOnBox(box1);
        turnSequence.recordBuiltOnBox(box1);
        turnSequence.recordBuiltOnBox(box2);


        turnSequence.undoBuilds();
        assertFalse(box1.hasDome());
        assertEquals(1, box1.level());
        assertEquals(2, box2.level());
    }
    //Done

    @Test
    public void recordRemovedBlock() {
        Map map = new Map();
        TurnSequence turnSequence = new TurnSequence();
        Box box1 = map.position(1,1);
        Box box2 = map.position(2,2);
        box1.build();
        turnSequence.recordBuiltOnBox(box1);
        turnSequence.recordBuiltOnBox(box1);
        //checking that if a box that was built on two times and that has its two blocks removed has an empty builtOnBox and an empty removedBlocks
        turnSequence.recordRemovedBlock(box1);
        assertTrue(turnSequence.builtOnBoxes().size()==1);
        turnSequence.recordRemovedBlock(box1);
        assertTrue(turnSequence.builtOnBoxes().isEmpty());
        //checking that it is not removing block on boxes that have a dome
        box1.buildDome();
        turnSequence.recordRemovedBlock(box1);
        assertTrue(turnSequence.builtOnBoxes().isEmpty());
        //checking that a box is added correctly
        turnSequence.recordRemovedBlock(box2);
        assertTrue(turnSequence.removedBlocks().contains(box2));
        assertTrue(turnSequence.removedBlocks().size()==1);
    }
    //Done

    @Test
    public void clearRemovedBlocks() {
    }

    @Test
    public void undoRemovals() {
        Map map = new Map();
        TurnSequence turnSequence = new TurnSequence();
        Box box1 = map.position(1,1);
        Box box2 = map.position(2,2);

        turnSequence.recordRemovedBlock(box1);
        turnSequence.recordRemovedBlock(box1);
        turnSequence.recordRemovedBlock(box2);

        turnSequence.undoRemovals();
        assertEquals(2, box1.level());
        assertEquals(1, box2.level());
        assertFalse(box1.hasDome());
        assertFalse(box2.hasDome());
    }
    //Done

    @Test
    public void resetAllowedLevelDifference() {
        TurnSequence turnSequence = new TurnSequence();
        turnSequence.setAllowedLevelDifference(1234);
        turnSequence.resetAllowedLevelDifference();
        assertEquals(1, turnSequence.allowedLevelDifference());
    }
    //Done

    @Test
    public void addPossibleDestination() {
        TurnSequence turnSequence = new TurnSequence();
        Box box1 = new Box(1,1);

        turnSequence.addPossibleDestination(box1);
        assertTrue(turnSequence.possibleDestinations().contains(box1));
        turnSequence.addPossibleDestination(box1);
        assertEquals(1,turnSequence.possibleDestinations().size());
    }
    //Done

    @Test
    public void removePossibleDestination() {
        TurnSequence turnSequence = new TurnSequence();
        Box box1 = new Box(1,1);
        Box box2 = new Box(2,2);

        turnSequence.addPossibleDestination(box1);
        assertTrue(turnSequence.possibleDestinations().contains(box1));
        turnSequence.removePossibleDestination(box2);
        assertEquals(1,turnSequence.possibleDestinations().size());
        turnSequence.removePossibleDestination(box1);
        assertTrue(turnSequence.possibleDestinations().isEmpty());
    }
    //Done

    @Test
    public void clearPossibleDestinations() {
    }

    @Test
    public void addPossibleBuild() {
        TurnSequence turnSequence = new TurnSequence();
        Box box1 = new Box(1,1);

        turnSequence.addPossibleBuild(box1);
        assertTrue(turnSequence.possibleBuilds().contains(box1));
        turnSequence.addPossibleBuild(box1);
        assertEquals(1,turnSequence.possibleBuilds().size());
    }
    //Done

    @Test
    public void removePossibleBuild() {
        TurnSequence turnSequence = new TurnSequence();
        Box box1 = new Box(1,1);
        Box box2 = new Box(2,2);

        turnSequence.addPossibleBuild(box1);
        assertTrue(turnSequence.possibleBuilds().contains(box1));
        turnSequence.removePossibleBuild(box2);
        assertEquals(1,turnSequence.possibleBuilds().size());
        turnSequence.removePossibleBuild(box1);
        assertTrue(turnSequence.possibleBuilds().isEmpty());
    }
    //Done

    @Test
    public void clearPossibleBuilds() {
    }

    @Test
    public void addMovableWorker() {
        TurnSequence turnSequence = new TurnSequence();
        Box box1 = new Box(1,1);
        Worker worker = new Worker(box1,Colour.GREY );

        turnSequence.addMovableWorker(worker);
        assertTrue(turnSequence.movableWorkers().contains(worker));
        turnSequence.addMovableWorker(worker);
        assertEquals(1,turnSequence.movableWorkers().size());
    }
    //Done

    @Test
    public void removeMovableWorker() {
        TurnSequence turnSequence = new TurnSequence();
        Box box1 = new Box(1,1);
        Box box2 = new Box(2,2);
        Worker worker1 = new Worker(box1,Colour.GREY );
        Worker worker2 = new Worker(box2,Colour.GREY );

        turnSequence.addMovableWorker(worker1);
        assertTrue(turnSequence.movableWorkers().contains(worker1));
        turnSequence.removeMovableWorker(worker2);
        assertEquals(1,turnSequence.movableWorkers().size());
        turnSequence.removeMovableWorker(worker1);
        assertTrue(turnSequence.movableWorkers().isEmpty());
    }
    //Done

    @Test
    public void clearMovableWorkers() {
    }

    @Test
    public void recordMovedWorkers() {
        TurnSequence turnSequence = new TurnSequence();
        Box box = new Box(1,1);
        Worker worker = new Worker(box,Colour.GREY );

        turnSequence.recordMovedWorker(worker);
        assertTrue(turnSequence.movedWorkers().contains(worker));
        turnSequence.recordMovedWorker(worker);
        assertEquals(1,turnSequence.movedWorkers().size());
    }
    //Done

    @Test
    public void clearMovedWorkers() {
    }

    @Test
    public void reset() {
        Map map = new Map();
        TurnSequence turnSequence = new TurnSequence();
        Box box1 = map.position(1,1);
        Box box2 = map.position(2,2);
        Box box3= map.position(3,3);
        Box box4= map.position(4,4);
        Worker worker1 = new Worker(box1,Colour.GREY );

        turnSequence.setChosenWorker(worker1);
        turnSequence.setChosenBox(box1);
        turnSequence.addMovableWorker(worker1);
        //this test resets the values of some attributes that are NOT null/empty
        turnSequence.recordRemovedBlock(box1);
        assertTrue(turnSequence.removedBlocks().contains(box1));

        turnSequence.addPossibleBuild(box2);
        assertTrue(turnSequence.possibleBuilds().contains(box2));

        box3.build();
        turnSequence.recordBuiltOnBox(box3);
        assertTrue(turnSequence.builtOnBoxes().contains(box3));

        turnSequence.addPossibleDestination(box4);
        assertTrue(turnSequence.possibleDestinations().contains(box4));

        turnSequence.recordNewPosition(worker1,box4);
        assertEquals(box4, turnSequence.newPositions().get(worker1));
        assertEquals(box1, turnSequence.previousBox());

        turnSequence.reset();
        assertNull(turnSequence.chosenWorker());
        assertNull(turnSequence.chosenBox());
        assertNull(turnSequence.previousBox());
        assertTrue(turnSequence.newPositions().isEmpty());
        assertTrue(turnSequence.builtOnBoxes().isEmpty());
        assertTrue(turnSequence.removedBlocks().isEmpty());
        assertTrue(turnSequence.possibleDestinations().isEmpty());
        assertTrue(turnSequence.possibleBuilds().isEmpty());
        assertTrue(turnSequence.movableWorkers().isEmpty());
        assertTrue(turnSequence.movedWorkers().isEmpty());
    }
    //Done

    @Test
    public void undo() {
        Map map = new Map();
        TurnSequence turnSequence = new TurnSequence();
        Player player = new Player("SAS", Colour.GREY, null);

        Box builtBox1 = map.position(0,1);
        Box builtBox2 = map.position(0,2);

        Box removedBox1 = map.position(1,0);
        Box removedBox2 = map.position(2,0);

        Box box00 = map.position(0,0);
        Box box11 = map.position(1,1);
        Box box22 = map.position(2, 2);
        Box box33 = map.position(3,3);
        Box box44 = map.position(4,4);

        Box box04 = map.position(0,4);
        Box box13 = map.position(1,3);
        Box box31 = map.position(3,1);
        Box box40 = map.position(4,0);

        turnSequence.setPossibleWinner(player);
        //Building
        builtBox1.build();
        builtBox1.build();
        builtBox1.buildDome();
        builtBox2.build();
        builtBox2.build();
        builtBox2.build();
        turnSequence.recordBuiltOnBox(builtBox1);
        turnSequence.recordBuiltOnBox(builtBox1);
        turnSequence.recordBuiltOnBox(builtBox2);

        //Removing
        turnSequence.recordRemovedBlock(removedBox1);
        turnSequence.recordRemovedBlock(removedBox1);
        turnSequence.recordRemovedBlock(removedBox2);

        //making two workers move (crossing paths)
        Worker worker1 = new Worker(box00,Colour.GREY );
        Worker worker2 = new Worker(box04,Colour.GREY );

        turnSequence.recordNewPosition(worker1, box11);
        turnSequence.recordNewPosition(worker2, box13);
        turnSequence.recordNewPosition(worker1, box22);
        turnSequence.recordNewPosition(worker2, box22);
        turnSequence.recordNewPosition(worker1, box33);
        turnSequence.recordNewPosition(worker2, box31);
        turnSequence.recordNewPosition(worker1, box44);
        turnSequence.recordNewPosition(worker2, box40);

        ////////////////////////////////////

        turnSequence.undo();

        //No winner
        assertNull(turnSequence.possibleWinner());

        //built boxes should have their original level
        assertFalse(builtBox1.hasDome());
        assertEquals(1, builtBox1.level());
        assertEquals(2, builtBox2.level());

        //built boxes should have their original level
        assertEquals(2, removedBox1.level());
        assertEquals(1, removedBox2.level());
        assertFalse(removedBox1.hasDome());
        assertFalse(removedBox2.hasDome());

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
    public void clearTurnSequence() {
        Map map = new Map();
        TurnSequence turnSequence = new TurnSequence();
        Box box1 = map.position(1,1);
        Box box2 = map.position(2,2);
        Box box3= map.position(3,3);
        Box box4= map.position(4,4);
        Worker worker = new Worker(box1,Colour.GREY );

        turnSequence.setAllowedLevelDifference(0);
        turnSequence.setChosenWorker(worker);
        turnSequence.setChosenBox(box1);
        turnSequence.addMovableWorker(worker);
        //this test resets the values of some attributes that are NOT null/empty
        turnSequence.recordRemovedBlock(box1);
        assertTrue(turnSequence.removedBlocks().contains(box1));

        turnSequence.addPossibleBuild(box2);
        assertTrue(turnSequence.possibleBuilds().contains(box2));

        box3.build();
        turnSequence.recordBuiltOnBox(box3);
        assertTrue(turnSequence.builtOnBoxes().contains(box3));

        turnSequence.addPossibleDestination(box4);
        assertTrue(turnSequence.possibleDestinations().contains(box4));

        turnSequence.recordNewPosition(worker,box4);
        assertEquals(box4, turnSequence.newPositions().get(worker));
        assertEquals(box1, turnSequence.previousBox());

        turnSequence.clearTurnSequence();
        assertEquals(1, turnSequence.allowedLevelDifference());
        assertNull(turnSequence.chosenWorker());
        assertNull(turnSequence.chosenBox());
        assertNull(turnSequence.previousBox());
        assertTrue(turnSequence.newPositions().isEmpty());
        assertTrue(turnSequence.builtOnBoxes().isEmpty());
        assertTrue(turnSequence.removedBlocks().isEmpty());
        assertTrue(turnSequence.possibleDestinations().isEmpty());
        assertTrue(turnSequence.possibleBuilds().isEmpty());
        assertTrue(turnSequence.movableWorkers().isEmpty());
        assertTrue(turnSequence.movedWorkers().isEmpty());
    }
    //Done

    @Test
    public void confirmTurnSequence() {
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

        Worker worker1 = new Worker(box00,Colour.GREY );
        Worker worker2 = new Worker(box04,Colour.GREY );

        turnSequence.setAllowedLevelDifference(0);
        turnSequence.setChosenWorker(worker1);
        turnSequence.setChosenBox(box11);
        turnSequence.addMovableWorker(worker1);
        //this test resets the values of some attributes that are NOT null/empty
        turnSequence.recordRemovedBlock(box11);
        assertTrue(turnSequence.removedBlocks().contains(box11));

        turnSequence.addPossibleBuild(box22);
        assertTrue(turnSequence.possibleBuilds().contains(box22));

        box33.build();
        turnSequence.recordBuiltOnBox(box33);
        assertTrue(turnSequence.builtOnBoxes().contains(box33));

        turnSequence.addPossibleDestination(box44);
        assertTrue(turnSequence.possibleDestinations().contains(box44));

        //making two workers move (crossing paths)

        turnSequence.recordNewPosition(worker1, box11);
        turnSequence.recordNewPosition(worker2, box13);
        turnSequence.recordNewPosition(worker1, box22);
        turnSequence.recordNewPosition(worker2, box22);
        turnSequence.recordNewPosition(worker1, box33);
        turnSequence.recordNewPosition(worker2, box31);
        turnSequence.recordNewPosition(worker1, box44);
        assertEquals(box33, turnSequence.previousBox());
        turnSequence.recordNewPosition(worker2, box40);

        turnSequence.confirmTurnSequence();

        assertEquals(1, turnSequence.allowedLevelDifference());
        assertNull(turnSequence.chosenWorker());
        assertNull(turnSequence.chosenBox());
        assertNull(turnSequence.previousBox());
        assertTrue(turnSequence.newPositions().isEmpty());
        assertTrue(turnSequence.builtOnBoxes().isEmpty());
        assertTrue(turnSequence.removedBlocks().isEmpty());
        assertTrue(turnSequence.possibleDestinations().isEmpty());
        assertTrue(turnSequence.possibleBuilds().isEmpty());
        assertTrue(turnSequence.movableWorkers().isEmpty());
        assertTrue(turnSequence.movedWorkers().isEmpty());

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
    //Done
}