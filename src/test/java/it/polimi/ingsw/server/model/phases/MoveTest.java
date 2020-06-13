package it.polimi.ingsw.server.model.phases;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.network.exceptions.TimeOutException;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.godPowers.fx.DoNothing;
import it.polimi.ingsw.server.model.godPowers.fx.SwapPower;
import it.polimi.ingsw.server.model.godPowers.setUpConditions.NoSetUpCondition;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class MoveTest {

    @Test
    public void executePhase() throws ChannelClosedException, TimeOutException {

        //-------------------------- Test 1 ---------
        Map map = new Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        //player's workers
        Worker worker1 = new Worker(false, map.position(3, 3));
        Worker worker2 = new Worker(true, map.position(1, 2));
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
        Worker opponentWorker = new Worker(false, map.position(3,4));
        opponent.assignWorker(opponentWorker);
        currentPlayer.turnSequence().setChosenWorker(worker1);
        currentPlayer.turnSequence().addMovableWorker(worker1);
        currentPlayer.turnSequence().addMovableWorker(worker2);

        assertTrue(currentPlayer.turnSequence().newPositions().isEmpty());
        //normal move
        Move move = new Move();
        assertNotNull(currentPlayer.turnSequence().chosenWorker());
        move.executePhase(currentPlayer, communicationController, actionController, map, opponents, new ArrayList<WinCondition>(), new MatchStory(currentPlayer));
        assertFalse(currentPlayer.turnSequence().newPositions().isEmpty());
        assertTrue(currentPlayer.turnSequence().movedWorkers().size()>0);
        assertEquals(currentPlayer.turnSequence().chosenBox(), currentPlayer.turnSequence().newPositions().get(currentPlayer.turnSequence().chosenWorker()));


        //move with swapPower
        if(currentPlayer.turnSequence().movedWorkers().size()>1) {
            assertTrue(currentPlayer.turnSequence().movedWorkers().contains(opponentWorker));
            assertTrue(currentPlayer.turnSequence().newPositions().containsKey(opponentWorker));
            assertEquals(map.position(3, 3), currentPlayer.turnSequence().newPositions().get(opponentWorker));
        }
        //end swapPower

    }
}