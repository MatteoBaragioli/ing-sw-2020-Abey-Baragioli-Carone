package it.polimi.ingsw.server.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoxTest {
    @Test
    public void build(){
        //------------------------------------ new test ----------------------------------------
        // this test tries to do 4 consecutive builds on the same box
        Box box=new Box(1,3);
        box.build();
        assertTrue(box.level()==1 && !box.hasDome());
        box.build();
        assertTrue(box.level()==2 && !box.hasDome());
        box.build();
        assertTrue(box.level()==3 && !box.hasDome());
        box.build();
        assertTrue(box.level()==3 && box.hasDome());
        //------------------------------------ new test ----------------------------------------
        // this test tries to do a build on a box that has a dome
        box=new Box(0,true, 1,3);
        box.build();
        assertEquals(0, box.level());
        //------------------------------------ new test ----------------------------------------
        // this test tries to do a build on a box that has an occupier
        box=new Box(0,false, 1,3);
        Worker worker=new Worker(box,Colour.GREY );
        box.build();
        assertEquals(0, box.level());
        assertFalse(box.hasDome());
        assertEquals(box.occupier(), worker);
    }

    @Test
    public void buildBlock(){
        //------------------------------------ new test ----------------------------------------
        // this test tries to build 4 consecutive blocks on the same box
        Box box=new Box(0,false,1,3);
        box.buildBlock();
        assertTrue(box.level()==1 && !box.hasDome());
        box.buildBlock();
        assertTrue(box.level()==2 && !box.hasDome());
        box.buildBlock();
        assertTrue(box.level()==3 && !box.hasDome());
        box.buildBlock();
        assertTrue(box.level()==3 && !box.hasDome());
    }

    @Test
    public void buildDome(){
        //------------------------------------ new test ----------------------------------------
        // this test tries to build  a dome on a block
        Box box=new Box(0,false,1,3);
        box.buildDome();
        assertTrue(box.level()==0 && box.hasDome());
        //------------------------------------ new test ----------------------------------------
        // this test tries to build a block and a dome consecutively
        box=new Box(0,false,1,3);
        box.buildBlock();
        box.buildDome();
        assertTrue(box.level()==1 && box.hasDome());
        //------------------------------------ new test ----------------------------------------
        // this test tries to build  a dome on a block that already has a dome
        box=new Box(0,true,1,3);
        box.buildDome();
        assertTrue(box.hasDome());
    }

    @Test
    public void removeBlock() {
        //------------------------------------ new test ----------------------------------------
        // this test tries to remove a block on a box that has none
        Box box=new Box(0,false,1,3);
        box.removeBlock();
        assertTrue(box.level()==0 && !box.hasDome());
        //------------------------------------ new test ----------------------------------------
        // this test tries to remove a block on a box that has a dome
        box=new Box(0,true,1,3);
        box.removeBlock();
        assertTrue(box.level()==0 && box.hasDome());
        //------------------------------------ new test ----------------------------------------
        // this test tries to remove a block on a complete tower
        box=new Box(3,true,1,3);
        box.removeBlock();
        assertTrue(box.level()==3 && box.hasDome());
        //------------------------------------ new test ----------------------------------------
        // this test tries to remove a block on a tower that has two blocks and a dome
        box=new Box(2,true,1,3);
        box.removeBlock();
        assertTrue(box.level()==2 && box.hasDome());
        //------------------------------------ new test ----------------------------------------
        // this test tries to remove a block 3 times consecutively on a box that has 3 blocks
        box=new Box(3,false,1,3);
        box.removeBlock();
        assertEquals(2, box.level());
        box.removeBlock();
        assertEquals(1, box.level());
        box.removeBlock();
        assertEquals(0, box.level());
    }


    @Test
    public void removeDome() {
        //------------------------------------ new test ----------------------------------------
        // this test tries to remove a dome on a box that does not have one
        Box box=new Box(0,false,1,3);
        box.removeDome();
        assertTrue(box.level()==0 && !box.hasDome());
        //------------------------------------ new test ----------------------------------------
        // this test tries to remove a dome on a box that has zero level and a dome
        box=new Box(0,true,1,3);
        box.removeDome();
        assertFalse(box.hasDome());
        //------------------------------------ new test ----------------------------------------
        // this test tries to remove a dome on a box that has two levels and a dome
        box=new Box(3,true,1,3);
        box.removeDome();
        assertFalse(box.hasDome());
    }

    @Test
    public void isOnEdge() {
        //------------------------------------ new test ----------------------------------------
        //given a box that is not on the edge of the board this test verifies the correct result of the method
        Map map=new Map();
        assertFalse(map.position(1, 1).isOnEdge());
        assertFalse(map.position(3, 3).isOnEdge());
        //------------------------------------ new test ----------------------------------------
        //given a box that is on the edge of the board this test verifies the correct result of the method
        assertTrue(map.position(0,0).isOnEdge());
        assertTrue(map.position(4,4).isOnEdge());
        assertTrue(map.position(4,3).isOnEdge());
    }

    @Test
    public void isFree() {
        //------------------------------------ new test ----------------------------------------
        //given a box that is occupied by a worker the test asserts the method returns the right value
        Box box = new Box(0, false, 1, 3);
        Worker worker=new Worker(box,Colour.GREY );
        assertFalse(box.isFree());
        box.removeOccupier(); //then we remove the occupier
        assertTrue(box.isFree());
        box.buildDome(); //then we build a dome on the box
        assertFalse(box.isFree());
    }
    @Test
    public void isOccupiedByWorkers() {
        //------------------------------------ new test ----------------------------------------
        //given a box that is occupied by a worker the test asserts the method returns the right value
        Box box = new Box(0, false, 1, 3);
        Worker worker=new Worker(box,Colour.GREY );
        assertTrue(box.isOccupiedByWorkers());
        box.removeOccupier(); //then we remove the occupier
        assertFalse(box.isOccupiedByWorkers());
        box.buildDome(); //then we build a dome on the box
        assertFalse(box.isOccupiedByWorkers());
    }
}