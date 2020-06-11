package it.polimi.ingsw.server.model;

import org.junit.Test;
import static org.junit.Assert.*;

import static it.polimi.ingsw.server.model.Colour.*;
public class BoxTest {

    @Test
    public void isOccupiedByWorkers() {
        //------------------------------------ new test ----------------------------------------
        //given a box that is occupied by a worker the test asserts the method returns the right value
        Box box = new Box(0, false, 1, 3);
        Worker worker=new Worker(box, GREY);
        assertTrue(box.isOccupiedByWorkers());
        box.removeOccupier(); //then we remove the occupier
        assertFalse(box.isOccupiedByWorkers());
    }
    //Done

    @Test
    public void isFree() {
        //------------------------------------ new test ----------------------------------------
        //given a box that is occupied by a worker the test asserts the method returns the right value
        Box box = new Box(0, false, 1, 3);
        Worker worker=new Worker(box, GREY);
        assertFalse(box.isFree());
        box.removeOccupier(); //then we remove the occupier
        assertTrue(box.isFree());
        box.buildDome(); //then we build a dome on the box
        assertFalse(box.isFree());
    }
    //Done

    @Test
    public void isOccupied() {
        Box box = new Box(0, false, 1, 3);
        Worker worker=new Worker(box, GREY);
        assertTrue(box.isOccupied());
        box.removeOccupier(); //then we remove the occupier
        assertFalse(box.isOccupied());
        box.buildDome(); //then we build a dome on the box
        assertTrue(box.isOccupied());
    }
    //Done

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
        Worker worker=new Worker(box, GREY);
        box.build();
        assertEquals(1, box.level());
        assertFalse(box.hasDome());
        box.build();
        box.build();
        assertEquals(3, box.level());
        assertFalse(box.hasDome());
        box.build();
        assertFalse(box.hasDome());
        assertEquals(worker, box.occupier());
    }
    //Done

    @Test
    public void buildBlock(){
        //------------------------------------ new test ----------------------------------------
        // this test tries to build 4 consecutive blocks on the same box
        Box box=new Box(0,false,1,3);
        box.buildBlock();
        assertEquals(1, box.level());
        box.buildBlock();
        assertEquals(2, box.level());
        box.buildBlock();
        assertEquals(3, box.level());
        box.buildBlock();
        assertEquals(3, box.level());
    }
    //Done

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
    //Done

    @Test
    public void removeBlock() {
        Box box = new Box(3,false,1,3);

        box.removeBlock();
        assertEquals(2, box.level());
        box.removeBlock();
        assertEquals(1, box.level());
        box.removeBlock();
        assertEquals(0, box.level());
        box.removeBlock();
        assertEquals(0, box.level());
    }
    //Done

    @Test
    public void removeDome() {
        //------------------------------------ new test ----------------------------------------
        // this test tries to remove a dome on a box that does not have one
        Box box=new Box(0,false,1,3);
        box.removeDome();
        assertFalse(box.hasDome());
        //------------------------------------ new test ----------------------------------------
        // this test tries to remove a dome on a box that has zero level and a dome
        box=new Box(2,true,1,3);
        box.removeDome();
        assertFalse(box.hasDome());
    }
    //Done

    @Test
    public void isOnEdge() {
        //------------------------------------ new test ----------------------------------------
        //given a box that is not on the edge of the board this test verifies the correct result of the method
        Map map=new Map();

        for (int i = 0; i<map.ground().length; i++)
            for (int j = 0; j<map.ground()[i].length; j++)
                if (i==0 || i==4 || j==0 || j==4)
                    assertTrue(map.position(i,j).isOnEdge());
                else
                    assertFalse(map.position(i,j).isOnEdge());
    }
    //Done

    @Test
    public void isCompleteTower() {
        Box box = new Box(2,false,2,2);

        assertFalse(box.isCompleteTower());

        box.buildDome();
        assertFalse(box.isCompleteTower());

        box.removeDome();
        box.build();
        assertFalse(box.isCompleteTower());

        box.build();
        assertTrue(box.isCompleteTower());
    }
    //Done
}