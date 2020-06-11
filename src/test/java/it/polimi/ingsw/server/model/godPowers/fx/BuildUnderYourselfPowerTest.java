package it.polimi.ingsw.server.model.godPowers.fx;


import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.godPowers.setUpConditions.NoSetUpCondition;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BuildUnderYourselfPowerTest {

    @Test
    public void changePossibleOptions() {
        //---------------Test 1-------------
        Map map = new  Map();
        ActionController actionController = new ActionController();
        Worker chosenWorker = new Worker(false, map.position(3,3));
        Worker worker = new Worker(true, map.position(2,2));
        Player player = new Player("player1", Colour.BLUE, new GodCard("Zeus", 30, new ArrayList<>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<>()));
        player.assignWorker(chosenWorker);
        player.assignWorker(worker);
        player.turnSequence().setChosenWorker(chosenWorker);

        TurnSequenceModifier zeusOptions = new BuildUnderYourselfPower();
        zeusOptions.changePossibleOptions(player, actionController, map);

        assertTrue(player.turnSequence().possibleBuilds().contains(chosenWorker.position()));
        assertEquals(1, player.turnSequence().possibleBuilds().size());

        player.turnSequence().clearPossibleBuilds();
        actionController.initialisePossibleBuilds(player.turnSequence(), map);
        zeusOptions.changePossibleOptions(player, actionController, map);

        List<Box> testList = new ArrayList<>();
        testList.add(map.position(2, 3));
        testList.add(map.position(2,4));
        testList.add(map.position(3,2));
        testList.add(map.position(3,4));
        testList.add(map.position(4,2));
        testList.add(map.position(4, 3));
        testList.add(map.position(4,4));
        testList.add(map.position(3,3));
        assertEquals(player.turnSequence().possibleBuilds(), testList);
    }
}