package it.polimi.ingsw.server.model;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class ActionControllerTest {

    @Test
    public void updateNewPositions() {
        ActionController actionController = new ActionController();
        Map map = new Map();
        TurnSequence turnSequence = new TurnSequence();
        Box chosenBox = map.position(3,4);
        Box workerPosition = new Box(3,3);
        Worker chosenWorker = new Worker(true, workerPosition);
        turnSequence.setChosenWorker(chosenWorker);
        turnSequence.setChosenBox(map.position(3,4));

        //checking if turnSequence attribute newPositions is empty
        assertTrue(turnSequence.newPositions().isEmpty());
        assertFalse(turnSequence.newPositions().containsKey(chosenWorker));

        //checking if this method updates correctly
        actionController.updateNewPositions(turnSequence);
        assertFalse(turnSequence.newPositions().isEmpty());
        assertTrue(turnSequence.newPositions().containsKey(chosenWorker));
        assertEquals(turnSequence.newPositions().get(chosenWorker), chosenBox);
    }

    @Test
    public void updateBuiltOnBox() {

        ActionController actionController = new ActionController();
        TurnSequence turnSequence = new TurnSequence();
        Box targetBox = new Box(0, false, 3,3); //level 0 not dome
        turnSequence.setChosenBox(targetBox);

        //checking if turnSequence attribute builtOnBoxes is empty
        assertTrue(turnSequence.builtOnBoxes().isEmpty());
        assertFalse(turnSequence.builtOnBoxes().contains(targetBox));

        //checking if this method updates correctly
        actionController.updateBuiltOnBox(turnSequence);
        assertFalse(turnSequence.builtOnBoxes().isEmpty());
        assertTrue(turnSequence.builtOnBoxes().contains(targetBox));
        assertEquals(targetBox.level(), 1);
        assertFalse(targetBox.hasDome());

        //-------------------------------New Test--------------------------------------------------

        ActionController actionController2 = new ActionController();
        TurnSequence turnSequence2 = new TurnSequence();
        Box targetBox2 = new Box(3, false, 3,3); //level 3 not dome
        turnSequence2.setChosenBox(targetBox2);
        //checking if turnSequence attribute builtOnBoxes is empty
        assertTrue(turnSequence2.builtOnBoxes().isEmpty());
        assertFalse(turnSequence2.builtOnBoxes().contains(targetBox2));

        //checking if this method updates correctly
        actionController2.updateBuiltOnBox(turnSequence2);
        assertFalse(turnSequence2.builtOnBoxes().isEmpty());
        assertTrue(turnSequence2.builtOnBoxes().contains(targetBox2));
        assertEquals(targetBox2.level(), 3);
        assertTrue(targetBox2.hasDome());

        //-------------------------------New Test--------------------------------------------------

        ActionController actionController3 = new ActionController();
        TurnSequence turnSequence3 = new TurnSequence();
        Box targetBox3 = new Box(2, true, 3,3); //level 2 dome
        turnSequence3.setChosenBox(targetBox3);
        //checking if turnSequence attribute builtOnBoxes is empty
        assertTrue(turnSequence3.builtOnBoxes().isEmpty());
        assertFalse(turnSequence3.builtOnBoxes().contains(targetBox3));

        //checking if this method updates correctly
        actionController3.updateBuiltOnBox(turnSequence3);
        assertFalse(turnSequence3.builtOnBoxes().isEmpty());
        assertTrue(turnSequence3.builtOnBoxes().contains(targetBox3));
        assertEquals(targetBox3.level(), 2);
        assertTrue(targetBox3.hasDome());
    }

    @Test
    public void initialisePossibleDestinations() {

        ActionController actionController = new ActionController();
        TurnSequence turnSequence = new TurnSequence();
        Map map = new Map();
        Box workerPosition = map.position(3, 3); //level 0 no dome
        Worker chosenWorker = new Worker(workerPosition,Colour.GREY );
        turnSequence.setChosenWorker(chosenWorker);



        List<Box> testList = new ArrayList<>();
        testList.add(map.position(2,2));
        testList.add(map.position(2, 3));
        testList.add(map.position(2,4));
        testList.add(map.position(3,2));
        testList.add(map.position(3,4));
        testList.add(map.position(4,2));
        testList.add(map.position(4, 3));
        testList.add(map.position(4,4));

        //checking if this method updates correctly
        actionController.initialisePossibleDestinations(turnSequence, map);
        assertThat(turnSequence.possibleDestinations(), is(testList));


        //-------------------------------New Test--------------------------------------------------

        map = new Map();
        workerPosition = map.position(3, 3); //level 1 no dome
        workerPosition.build();                         //level 1 no dome
        chosenWorker = new Worker(workerPosition,Colour.GREY );
        turnSequence.setChosenWorker(chosenWorker);



        testList = new ArrayList<>();
        map.position(2,2).build();
        map.position(2,2).build(); //level 2
        testList.add(map.position(2,2));

        map.position(2, 3).build();
        map.position(2, 3).build();
        map.position(2, 3).build(); //level 3

        map.position(2, 4).build();
        map.position(2, 4).build();
        map.position(2, 4).build(); //level 3

        map.position(3, 2).buildDome(); //dome

        testList.add(map.position(3,4));
        testList.add(map.position(4,2));
        testList.add(map.position(4, 3));
        testList.add(map.position(4,4));

        //checking if this method updates correctly
        actionController.initialisePossibleDestinations(turnSequence, map);
        assertThat(turnSequence.possibleDestinations(), is(testList));

        //-------------------------------New Test--------------------------------------------------
        map = new Map();
        workerPosition = map.position(3, 3);
        workerPosition.build();                         //level 3 no dome
        workerPosition.build();
        workerPosition.build();
        chosenWorker = new Worker(workerPosition,Colour.GREY );
        turnSequence.setChosenWorker(chosenWorker);



        testList = new ArrayList<>();
        map.position(2,2).build();
        map.position(2,2).build(); //level 2
        testList.add(map.position(2,2));

        map.position(2, 3).build();
        map.position(2, 3).build();
        map.position(2, 3).build(); //level 3
        testList.add(map.position(2,3));

        map.position(2, 4).build();
        map.position(2, 4).build();
        map.position(2, 4).build(); //level 3
        testList.add(map.position(2,4));

        map.position(3, 2).buildDome(); //dome

        map.position(3, 4).build(); //level 1
        testList.add(map.position(3,4));
        map.position(4,2).occupy(new Worker(map.position(4,2),Colour.GREY )); //occupier
        testList.add(map.position(4, 3));
        testList.add(map.position(4,4));

        //checking if this method updates correctly
        actionController.initialisePossibleDestinations(turnSequence, map);
        assertThat(turnSequence.possibleDestinations(), is(testList));
    }

    @Test
    public void initialisePossibleBuilds() {

        ActionController actionController = new ActionController();
        TurnSequence turnSequence = new TurnSequence();
        Map map = new Map();
        Box workerPosition = map.position(3, 3); //level 0 no dome
        Worker chosenWorker = new Worker(workerPosition,Colour.GREY );
        turnSequence.setChosenWorker(chosenWorker);



        List<Box> testList = new ArrayList<>();
        testList.add(map.position(2,2));
        testList.add(map.position(2, 3));
        testList.add(map.position(2,4));
        testList.add(map.position(3,2));
        testList.add(map.position(3,4));
        testList.add(map.position(4,2));
        testList.add(map.position(4, 3));
        testList.add(map.position(4,4));

        //checking if this method updates correctly
        actionController.initialisePossibleBuilds(turnSequence, map);
        assertThat(turnSequence.possibleBuilds(), is(testList));


        //-------------------------------New Test--------------------------------------------------
        map = new Map();
        workerPosition = map.position(3, 3);
        chosenWorker = new Worker(workerPosition,Colour.GREY );
        turnSequence.setChosenWorker(chosenWorker);


        testList = new ArrayList<>();
        map.position(2,2).build();
        map.position(2,2).build(); //level 2
        testList.add(map.position(2,2));

        map.position(2, 3).build();
        map.position(2, 3).build();
        map.position(2, 3).build(); //level 3
        testList.add(map.position(2,3));

        map.position(2, 4).build();
        map.position(2, 4).build();
        map.position(2, 4).build(); //level 3
        testList.add(map.position(2,4));

        map.position(3, 2).buildDome(); //dome

        map.position(3, 4).build(); //level 1
        testList.add(map.position(3,4));
        map.position(4,2).occupy(new Worker(map.position(4,2),Colour.GREY )); //occupier
        testList.add(map.position(4, 3));
        testList.add(map.position(4,4));

        //checking if this method updates correctly
        actionController.initialisePossibleBuilds(turnSequence, map);
        assertThat(turnSequence.possibleBuilds(), is(testList));
    }

    @Test
    public void applyOpponentsCondition() {

    }

    @Test
    public void verifyWinCondition() {
    }
}