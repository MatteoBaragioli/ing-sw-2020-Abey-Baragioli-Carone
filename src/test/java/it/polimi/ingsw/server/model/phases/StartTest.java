package it.polimi.ingsw.server.model.phases;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.godPowers.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class StartTest {
    @Test
    public void executePhase() {
        //-------------------------- Test 1 -----------------------------------------------------
        //this test tries a start for add build if not move up power
        Map map = new Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        //player's workers
        Worker worker1 = new Worker(false, map.position(3, 3));
        Worker worker2 = new Worker(true, map.position(1, 2));
        //player card
        List<TurnSequenceModifier> actions = new ArrayList<>();
        actions.add(0, new AddBuildBeforeMoveIfNotMoveUpPower());
        actions.add(1, new DoNothing());
        actions.add(2, new DoNothing());
        actions.add(3, new DoNothing());
        List<TurnSequenceModifier> playerEffectsOnOpponents = new ArrayList<>();
        playerEffectsOnOpponents.add(0, new DoNothing());
        playerEffectsOnOpponents.add(1, new DoNothing());
        playerEffectsOnOpponents.add(2, new DoNothing());
        playerEffectsOnOpponents.add(3, new DoNothing());
        GodCard none = new GodCard("none", 0, actions, new StandardWin(), new NoSetUpCondition(), playerEffectsOnOpponents);
        //player
        Player currentPlayer = new Player("santoreene97", Colour.BLUE, none);
        currentPlayer.assignWorker(worker1);
        currentPlayer.assignWorker(worker2);
        //opponents
        List<TurnSequenceModifier> effectsOnOpponents = new ArrayList<>();
        effectsOnOpponents.add(0, new DoNothing());
        effectsOnOpponents.add(1, new DoNothing());
        effectsOnOpponents.add(2, new DoNothing());
        effectsOnOpponents.add(3, new DoNothing());
        Player opponent = new Player("opponent1", Colour.GREY, new GodCard("Artemis", 2, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), effectsOnOpponents));
        List<Player> opponents = new ArrayList<>();
        opponents.add(opponent);
        currentPlayer.turnSequence().setChosenWorker(worker1);
        currentPlayer.turnSequence().addMovableWorker(worker1);
        currentPlayer.turnSequence().addMovableWorker(worker2);
        currentPlayer.turnSequence().setChosenBox(map.position(3, 4));

        assertTrue(currentPlayer.turnSequence().builtOnBoxes().isEmpty());
        assertTrue(currentPlayer.turnSequence().newPositions().isEmpty());
        Start start=new Start();
        start.executePhase(currentPlayer, communicationController, actionController, map, opponents, new ArrayList<WinCondition>());
        assertTrue(currentPlayer.turnSequence().builtOnBoxes().size()<=1);
        assertTrue(currentPlayer.turnSequence().newPositions().isEmpty());
        if(currentPlayer.turnSequence().builtOnBoxes().contains(map.position(3, 4))) {
            assertEquals(0, currentPlayer.turnSequence().allowedLevelDifference());
            assertEquals(1, map.position(3, 4).level());
            assertEquals(currentPlayer.turnSequence().possibleBuilds(),map.adjacent(map.position(3,3)));
        }
        currentPlayer.turnSequence().undo();
        //-------------------------- Test 1 ------------------------------------------------------------------------
        //this test tries a start for a doNothing
        actions.set(0,new DoNothing());
        assertTrue(currentPlayer.turnSequence().builtOnBoxes().isEmpty());
        assertTrue(currentPlayer.turnSequence().newPositions().isEmpty());
        start=new Start();
        start.executePhase(currentPlayer, communicationController, actionController, map, opponents, new ArrayList<WinCondition>());
        assertTrue(currentPlayer.turnSequence().builtOnBoxes().isEmpty());
        assertTrue(currentPlayer.turnSequence().newPositions().isEmpty());

    }
}