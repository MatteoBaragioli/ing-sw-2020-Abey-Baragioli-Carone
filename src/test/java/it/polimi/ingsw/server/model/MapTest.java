package it.polimi.ingsw.server.model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;


public class MapTest {

    @Test
    public void position() {
        Map map=new Map();
        //checking this method returns the right box and that it actually is contained in Map
        Box box1;
        for (int i = 0; i<map.ground().length; i++) {
            for (int j = 0; j < map.ground().length; j++) {
                box1 = map.position(i, j);
                assertEquals(box1.positionX(), i);
                assertEquals(box1.positionY(), j);
                assertTrue(map.groundToList().contains(box1));
            }
        }
        //checking this method returns null when given positions that do not exist in the map
        box1=map.position(6,6);
        assertNull(box1);
    }
    //Done

    @Test
    public void groundToList() {
        Map map=new Map();
        //checking this method returns the right box and that it actually is contained in Map
        Box box1;
        for (int i = 0; i<map.ground().length; i++) {
            for (int j = 0; j < map.ground().length; j++) {
                box1 = map.position(i, j);
                assertTrue(map.groundToList().contains(box1));
            }
        }
        assertEquals(25, map.groundToList().size());
    }
    //Done

    @Test
    public void adjacent() {
        //checking that for every box in the map the method returns the right boxes
        Map map=new Map();
        Box box1;
        List adjacent;
        for (int i = 0; i<map.ground().length; i++) {
            for (int j = 0; j < map.ground().length; j++) {
                box1 = map.position(i, j);
                adjacent=map.adjacent(box1);
                for(int k=i-1; k<=i+1; k++){
                    if(k >= 0 && k <= 4){
                        for(int h=j-1; h<=j+1; h++) {
                            if (h>= 0 && h <= 4 && !(k == i && h == j))
                                assertTrue(adjacent.contains(map.position(k,h)));
                         }
                    }
                }
            }
        }
    }
    //Done

    @Test
    public void boxesSameDirection() {
        //checking that for every box in the map and its adjacent boxes the method returns the right boxes in the correct direction
        Map map = new Map();
        Box box1;
        List sameDirection;
        int x_difference;
        int y_difference;
        int h, k;
        for (int i = 0; i < map.ground().length; i++) {
            for (int j = 0; j < map.ground().length; j++) {
                box1 = map.position(i, j);
                for (Box direction : map.adjacent(box1)) {
                    x_difference = direction.positionX() - box1.positionX();
                    y_difference = direction.positionY() - box1.positionY();
                    sameDirection = map.boxesSameDirection(box1, direction);
                    if (x_difference != 0 || y_difference != 0)
                        for (h = direction.positionX() + x_difference, k = direction.positionY() + y_difference; h >= 0 && h <= 4 && k >= 0 && k <= 4; h += x_difference, k += y_difference)
                            assertTrue(sameDirection.contains(map.position(h, k)));
                }
            }
        }
    }
    //Done

    @Test
    public void levelDifference() {
        //checking that every time (for 3 times) a block is built on a box the level difference between that box and a ground level box is correct
        Map map=new Map();
        int difference;
        Box box1=map.position(0,0);
        Box box2=map.position(1,0);
        for(int i=1;i<4; i++){
            box1.build();
            difference=map.levelDifference(box2,box1);
            assertEquals(difference,i);
        }


    }
    //Done

    @Test
    public void updateCompleteTowers(){
        Map map=new Map();
        //checking that if a complete tower is built and added in turnSequence.builtOnBoxes the method returns 1
        TurnSequence turnSequence=new TurnSequence();
        Box box1=map.position(0,0);
        Box box2=map.position(0,1);
        box2.build();//level 1 tower;
        box1.build();
        box1.build();
        box1.build();
        box1.build(); //complete tower;
        turnSequence.recordBuiltOnBox(box1);
        turnSequence.recordBuiltOnBox(box2);
        map.updateCompleteTowers(turnSequence);
        assertEquals(1, map.completeTowers());
        //checking that if 2 complete towers are built and added in turnSequence.builtOnBoxes the method returns 2
        turnSequence.reset();
        map.setCompleteTowers(0);
        box1=map.position(0,2);
        box2=map.position(0,3);
        box2.build();
        box2.build();
        box2.build();
        box2.build();//complete tower;
        box1.build();
        box1.build();
        box1.build();
        box1.build();//complete tower;
        turnSequence.recordBuiltOnBox(box1);
        turnSequence.recordBuiltOnBox(box2);
        map.updateCompleteTowers(turnSequence);
        assertEquals(2, map.completeTowers());
    }
    //Done
}