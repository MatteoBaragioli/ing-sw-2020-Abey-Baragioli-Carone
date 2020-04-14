package it.polimi.ingsw.server.model.phases;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.godPowers.DoNothing;
import it.polimi.ingsw.server.model.godPowers.NoSetUpCondition;
import it.polimi.ingsw.server.model.godPowers.SwapPower;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MoveTest {

    @Test
    public void executePhase() {

        //-------------------------- Test 1 --------- CommunicationController --> chooseBox returns element 4
        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        //player's workers
        Worker worker1 = new Worker(false, map.position(3,3));
        Worker worker2 = new Worker(true, map.position(1,2));
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
        GodCard apollo = new GodCard("Apollo", 1, actions, new StandardWin(), new NoSetUpCondition(), playerEffectsOnOpponents);
        //player
        Player currentPlayer = new Player("santoreene97", Colour.BLUE,apollo);
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



        assertTrue(currentPlayer.turnSequence().newPositions().isEmpty());
        //normal move
        Move move = new Move();
        assertNotNull(currentPlayer.turnSequence().chosenWorker());
        assertNotNull(currentPlayer.turnSequence().movedWorkers());
        move.executePhase(currentPlayer, communicationController, actionController, map, opponents, new ArrayList<WinCondition>());
        assertFalse(currentPlayer.turnSequence().newPositions().isEmpty());
        assertEquals(map.position(3,4), currentPlayer.turnSequence().newPositions().get(currentPlayer.turnSequence().chosenWorker()));
        //end normal move


/*
        //-------------------------- Test 2 move with swapPower -- CommunicationController --> chooseBox returns element 7
        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        //player's workers
        Worker worker1 = new Worker(false, map.position(3,3));
        Worker worker2 = new Worker(true, map.position(1,2));
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
        GodCard apollo = new GodCard("Apollo", 1, actions, new StandardWin(), new NoSetUpCondition(), playerEffectsOnOpponents);
        //player
        Player currentPlayer = new Player("santoreene97", Colour.BLUE, apollo);
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

        Move move = new Move();

        assertTrue(currentPlayer.turnSequence().newPositions().isEmpty());
        Worker targetWorker = new Worker(true, map.position(3, 4));
        opponents.get(0).workers().add(targetWorker);

        move.executePhase(currentPlayer, communicationController, actionController, map, opponents, new ArrayList<WinCondition>());

        assertFalse(currentPlayer.turnSequence().newPositions().isEmpty());
        assertEquals(currentPlayer.turnSequence().newPositions().get(worker1), map.position(3,4));
        assertEquals(currentPlayer.turnSequence().newPositions().get(targetWorker), map.position(3,3));
        assertEquals(2, currentPlayer.turnSequence().newPositions().size());
        //end move with swapPower
*/
    }
}