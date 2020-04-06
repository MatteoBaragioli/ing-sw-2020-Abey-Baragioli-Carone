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
        assertTrue(box.level()==1 && box.hasDome()==false);
        box.build();
        assertTrue(box.level()==2 && box.hasDome()==false);
        box.build();
        assertTrue(box.level()==3 && box.hasDome()==false);
        box.build();
        assertTrue(box.level()==3 && box.hasDome()==true);
        //------------------------------------ new test ----------------------------------------
        // this test tries to do a build on a box that has a dome
        box=new Box(0,true, 1,3);
        box.build();
        assertTrue(box.level()==0);
        //------------------------------------ new test ----------------------------------------
        // this test tries to do a build on a box that has an occupier
        box=new Box(0,false, 1,3);
        Worker worker=new Worker(box);
        box.build();
        assertTrue(box.level()==0);
        assertTrue(box.hasDome()==false);
        assertTrue( box.occupier().equals(worker));
    }

    @Test
    public void buildBlock(){
        //------------------------------------ new test ----------------------------------------
        // this test tries to build 4 consecutive blocks on the same box
        Box box=new Box(0,false,1,3);
        box.buildBlock();
        assertTrue(box.level()==1 && box.hasDome()==false);
        box.buildBlock();
        assertTrue(box.level()==2 && box.hasDome()==false);
        box.buildBlock();
        assertTrue(box.level()==3 && box.hasDome()==false);
        box.buildBlock();
        assertTrue(box.level()==3 && box.hasDome()==false);
    }

    @Test
    public void buildDome(){
        //------------------------------------ new test ----------------------------------------
        // this test tries to build  a dome on a block
        Box box=new Box(0,false,1,3);
        box.buildDome();
        assertTrue(box.level()==0 && box.hasDome()==true);
        //------------------------------------ new test ----------------------------------------
        // this test tries to build a block and a dome consecutively
        box=new Box(0,false,1,3);
        box.buildBlock();
        box.buildDome();
        assertTrue(box.level()==1 && box.hasDome()==true);
        //------------------------------------ new test ----------------------------------------
        // this test tries to build  a dome on a block that already has a dome
        box=new Box(0,true,1,3);
        box.buildDome();
        assertTrue(box.hasDome()==true);
    }

    @Test
    public void removeBlock() {
        //------------------------------------ new test ----------------------------------------
        // this test tries to remove a block on a box that has none
        Box box=new Box(0,false,1,3);
        Box paragonBox=box;
        box.removeBlock();
        assertTrue(box.equals(paragonBox));
        //------------------------------------ new test ----------------------------------------
        // this test tries to remove a block on a box that has a dome
        box=new Box(0,true,1,3);
        paragonBox=box;
        box.removeBlock();
        assertTrue(box.equals(paragonBox));
        //------------------------------------ new test ----------------------------------------
        // this test tries to remove a block on a complete tower
        box=new Box(3,true,1,3);
        paragonBox=box;
        box.removeBlock();
        assertTrue(box.equals(paragonBox));
        //------------------------------------ new test ----------------------------------------
        // this test tries to remove a block on a tower that has two blocks and a dome
        box=new Box(2,true,1,3);
        paragonBox=box;
        box.removeBlock();
        assertTrue(box.equals(paragonBox));
        //------------------------------------ new test ----------------------------------------
        // this test tries to remove a block 3 times consecutively on a box that has 3 blocks
        box=new Box(3,false,1,3);
        box.removeBlock();
        assertTrue(box.level()==2);
        box.removeBlock();
        assertTrue(box.level()==1);
        box.removeBlock();
        assertTrue(box.level()==0);
    }


    @Test
    public void removeDome() {
        //------------------------------------ new test ----------------------------------------
        // this test tries to remove a dome on a box that does not have one
        Box box=new Box(0,false,1,3);
        Box paragonBox=box;
        box.removeDome();
        assertTrue(box.equals(paragonBox));
        //------------------------------------ new test ----------------------------------------
        // this test tries to remove a dome on a box that has zero level and a dome
        box=new Box(0,true,1,3);
        box.removeDome();
        assertTrue(box.hasDome()==false);
        //------------------------------------ new test ----------------------------------------
        // this test tries to remove a dome on a box that has two levels and a dome
        box=new Box(3,true,1,3);
        box.removeDome();
        assertTrue(box.hasDome()==false);
    }

    @Test
    public void isOnEdge() {
        //------------------------------------ new test ----------------------------------------
        //given a box that is not on the edge of the board this test verifies the correct result of the method
        Map map=new Map();
        assertTrue(map.position(1,1).isOnEdge()==false);
        assertTrue(map.position(3,3).isOnEdge()==false);
        //------------------------------------ new test ----------------------------------------
        //given a box that is on the edge of the board this test verifies the correct result of the method
        assertTrue(map.position(0,0).isOnEdge()==true);
        assertTrue(map.position(4,4).isOnEdge()==true);
        assertTrue(map.position(4,3).isOnEdge()==true);
    }

    @Test
    public void isFree() {
        //------------------------------------ new test ----------------------------------------
        //given a box that is occupied by a worker the test asserts the method returns the right value
        Box box = new Box(0, false, 1, 3);
        Worker worker=new Worker(box);
        assertTrue(box.isFree()==false);
        box.removeOccupier(); //then we remove the occupier
        assertTrue(box.isFree()==true);
        box.buildDome(); //then we build a dome on the box
        assertTrue(box.isFree()==false);
    }
    @Test
    public void isOccupiedByWorkers() {
        //------------------------------------ new test ----------------------------------------
        //given a box that is occupied by a worker the test asserts the method returns the right value
        Box box = new Box(0, false, 1, 3);
        Worker worker=new Worker(box);
        assertTrue(box.isOccupiedByWorkers()==true);
        box.removeOccupier(); //then we remove the occupier
        assertTrue(box.isOccupiedByWorkers()==false);
        box.buildDome(); //then we build a dome on the box
        assertTrue(box.isOccupiedByWorkers()==false);
    }
}