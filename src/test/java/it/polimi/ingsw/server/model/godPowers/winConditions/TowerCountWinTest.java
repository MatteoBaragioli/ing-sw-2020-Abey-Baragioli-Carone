package it.polimi.ingsw.server.model.godPowers.winConditions;

import it.polimi.ingsw.server.model.godPowers.winConditions.TowerCountWin;
import org.junit.Test;
import static org.junit.Assert.*;

import it.polimi.ingsw.server.model.*;
import static it.polimi.ingsw.server.model.Colour.*;

public class TowerCountWinTest {

    @Test
    public void establishWinCondition() {
        //this test tries to establish the count of towers Win of a player that has to have 4 towers built on the map
        Map map=new Map();
        map.setCompleteTowers(2);
        WinCondition countTowersWin=new TowerCountWin(4);
        Player player= new Player("blue", GREY, null);
        Box box1=map.position(0,0);
        Box box2=map.position(0,1);
        box1.build();
        box1.build();
        box1.build();
        box1.build();
        box2.build();
        box2.build();
        box2.build();
        box2.build();
        player.turnSequence().recordBuiltOnBox(box1);
        assertFalse(countTowersWin.establishWinCondition(player,map));
        player.turnSequence().recordBuiltOnBox(box2);
        assertTrue(countTowersWin.establishWinCondition(player,map));
        ////this test tries to establish the count of towers Win of a player that has to have 3 towers built on the map
        map=new Map();
        map.setCompleteTowers(0);
        countTowersWin=new TowerCountWin(3);
        player= new Player("blue", GREY, null);
        box1=map.position(0,0);
        box2=map.position(0,1);
        Box box3=map.position(0,2);
        box1.build();
        box1.build();
        box1.build();
        box1.build();
        box2.build();
        box2.build();
        box2.build();
        box2.build();
        box3.build();
        box3.build();
        box3.build();
        box3.build();
        player.turnSequence().recordBuiltOnBox(box1);
        player.turnSequence().recordBuiltOnBox(box2);
        assertFalse(countTowersWin.establishWinCondition(player,map));
        player.turnSequence().recordBuiltOnBox(box3);
        assertTrue(countTowersWin.establishWinCondition(player,map));
    }
}