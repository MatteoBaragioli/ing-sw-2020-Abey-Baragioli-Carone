package it.polimi.ingsw.server.model;

import static it.polimi.ingsw.server.model.Phase.*;

import it.polimi.ingsw.server.model.godPowers.fx.DoNothing;
import it.polimi.ingsw.server.model.godPowers.fx.SwapPower;
import it.polimi.ingsw.server.model.godPowers.setUpConditions.NoSetUpCondition;
import it.polimi.ingsw.server.model.godPowers.winConditions.TowerCountWin;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.server.model.Colour.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

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
        //the cards we implemented do not have effects that happen during other players' turn, although there are such cards in the game
    }

    @Test
    public void verifyWinCondition() {
        //this test verifies the win of a player that moves from a second level box to a third level box;
        Map map=new Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        WinCondition standardWin=new StandardWin();
        //player card
        List<TurnSequenceModifier> actions = new ArrayList<>();
        actions.add(0, new DoNothing());
        actions.add(1, new SwapPower());
        actions.add(2, new DoNothing());
        actions.add(3, new DoNothing());
        List<TurnSequenceModifier> playerEffectsOnOpponents = new ArrayList<>();
        playerEffectsOnOpponents.add(0, new DoNothing());
        playerEffectsOnOpponents.add(1, new DoNothing());
        playerEffectsOnOpponents.add(2, new DoNothing());
        playerEffectsOnOpponents.add(3, new DoNothing());
        GodCard generic = new GodCard("generic", 1, actions, new StandardWin(), new NoSetUpCondition(), playerEffectsOnOpponents);
        Player player= new Player("blue", GREY, generic);
        //opponent
        List<TurnSequenceModifier> effectsOnOpponents = new ArrayList<>();
        effectsOnOpponents.add(0, new DoNothing());
        effectsOnOpponents.add(1, new DoNothing());
        effectsOnOpponents.add(2, new DoNothing());
        effectsOnOpponents.add(3, new DoNothing());
        Player opponent = new Player("opponent1", Colour.GREY, new GodCard("Artemis", 2, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), effectsOnOpponents));
        List<Player> opponents = new ArrayList<>();
        opponents.add(opponent);

        Box box1=map.position(0,0);
        Box box2=map.position(0,1);
        box1.build();
        box1.build();
        box2.build();
        box2.build();
        box2.build();
        Worker worker=new Worker(box1,Colour.GREY );
        player.workers().add(worker);
        player.turnSequence().addMovableWorker(worker);
        player.turnSequence().setChosenWorker(worker);
        player.turnSequence().recordNewPosition(worker, box2);

        actionController.verifyWinCondition(MOVE, new ArrayList<WinCondition>(),player, map, opponents);
        assertEquals(player.turnSequence().possibleWinner(), player);
        //-------------------------------------new test----------------------------------
        //this test verifies the win of a opponent player whoose win condition consists in having three complete towers on the board
        map=new Map();
        map.setCompleteTowers(2);
        actionController = new ActionController();
        communicationController = new CommunicationController();
        standardWin=new StandardWin();
        ArrayList<WinCondition> winConditions= new ArrayList<WinCondition>();
        WinCondition opponentWin=new TowerCountWin(3);
        winConditions.add(opponentWin);

        //player card
        actions = new ArrayList<>();
        actions.add(0, new DoNothing());
        actions.add(1, new SwapPower());
        actions.add(2, new DoNothing());
        actions.add(3, new DoNothing());
        playerEffectsOnOpponents = new ArrayList<>();
        playerEffectsOnOpponents.add(0, new DoNothing());
        playerEffectsOnOpponents.add(1, new DoNothing());
        playerEffectsOnOpponents.add(2, new DoNothing());
        playerEffectsOnOpponents.add(3, new DoNothing());
        generic = new GodCard("generic", 1, actions, new StandardWin(), new NoSetUpCondition(), playerEffectsOnOpponents);
        player= new Player("blue", GREY, generic);
        //opponent
        effectsOnOpponents = new ArrayList<>();
        effectsOnOpponents.add(0, new DoNothing());
        effectsOnOpponents.add(1, new DoNothing());
        effectsOnOpponents.add(2, new DoNothing());
        effectsOnOpponents.add(3, new DoNothing());
        opponent = new Player("opponent1", Colour.GREY, new GodCard("Artemis", 2, new ArrayList<TurnSequenceModifier>(), opponentWin, new NoSetUpCondition(), effectsOnOpponents));
        opponents = new ArrayList<>();
        opponents.add(opponent);

        box1=map.position(0,0);
        box2=map.position(0,1);
        Box box3=map.position(0,0);
        box1.build();
        box1.build();
        box1.build();
        box1.build(); //complete tower

        box2.build();
        box2.build();
        box2.build();
        box2.build();//complete tower

        box3.build();
        box3.build();
        box3.build();
        box3.build();//complete tower
        player.turnSequence().recordBuiltOnBox(box3);
        assertTrue(opponentWin.establishWinCondition(player,map));
        assertNull(player.turnSequence().possibleWinner());

        actionController.verifyWinCondition(BUILD, winConditions,player, map, opponents);
        assertEquals(player.turnSequence().possibleWinner(), opponent);
    }

    @Test
    public void canPlayerPlay() {
        //---test1: a worker has no adjacent free to boxes to move to. Method must return false value-----------
        boolean result;
        ActionController actionController=new ActionController();
        CardConstructor cardConstructor=new CardConstructor();
        GodCard card1=cardConstructor.cards().get(0); //apollo
        GodCard card2=cardConstructor.cards().get(cardConstructor.cards().size()-1); //zeus
        Player player1 = new Player("Matteo", null, card1);
        Player player2 = new Player("Francesca", null, card2);
        List<Player> opponents=new ArrayList<>();
        opponents.add(player2);
        Map map=new Map();
        Worker worker=new Worker(map.position(1,1), BLUE);
        player1.assignWorker(worker);
        for(Box adjacent: map.adjacent(map.position(1,1))){
            adjacent.build();
            adjacent.build();
        }
        result=actionController.canPlayerPlay(player1, opponents, map);
        assertEquals(result, false);
        // ---test2: a worker with apollo power can move to another worker's box but then is surrounded with domes. Method must return false value
        map=new Map();
        player1 = new Player("Matteo", BLUE, card1);
        player2 = new Player("Francesca", WHITE, card2);
        worker=new Worker(map.position(1,1), BLUE);
        Worker worker2=new Worker(map.position(0,1), WHITE);
        player1.assignWorker(worker);
        player2.assignWorker(worker2);
        for(Box adjacent: map.adjacent(map.position(1,1))){
            if(!(adjacent.positionX()==0 && adjacent.positionY()==1))
                adjacent.buildDome();
        }
        result=actionController.canPlayerPlay(player1,opponents, map);
        assertEquals(result, false);
    }
}