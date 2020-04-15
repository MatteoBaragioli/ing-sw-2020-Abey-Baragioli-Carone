package it.polimi.ingsw.server.model.phases;

import org.junit.Test;
import static org.junit.Assert.*;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.godPowers.*;
import java.util.ArrayList;
import java.util.List;


public class BuildTest {

    @Test
    public void executePhase() {
        //-------------------------- Test 1 --------- CommunicationController --> usePower returns false, chooseBox returns element 4
        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        //player's workers
        Worker worker1 = new Worker(false, map.position(3,3));
        Worker worker2 = new Worker(true, map.position(1,2));
        //player card
        List<TurnSequenceModifier> actions = new ArrayList<>();
        actions.add(0, new DoNothing());
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
        Player currentPlayer = new Player("santoreene97", Colour.BLUE,none);
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
        currentPlayer.turnSequence().setChosenBox(map.position(3,4));


        assertTrue(currentPlayer.turnSequence().builtOnBoxes().isEmpty());
        //normal build
        Build build = new Build();
        assertNotNull(currentPlayer.turnSequence().chosenWorker());
        build.executePhase(currentPlayer, communicationController, actionController, map, opponents, new ArrayList<WinCondition>());
        assertFalse(currentPlayer.turnSequence().builtOnBoxes().isEmpty());
        assertTrue(currentPlayer.turnSequence().builtOnBoxes().contains(map.position(3,4)));
        assertEquals(1, map.position(3,4).level());
        //end normal build


        //build with hephaestus (box(3,4) now is on level 1 --> it should be on level 3 at the end of this build
        actions = new ArrayList<>();
        actions.add(0, new DoNothing());
        actions.add(1, new DoNothing());
        actions.add(2, new AddBuildOnSameBoxPower());
        actions.add(3, new DoNothing());
        GodCard hephaestus = new GodCard("Hephaestus", 6, actions, new StandardWin(), new NoSetUpCondition(), playerEffectsOnOpponents);
        //player
        currentPlayer = new Player("santoreene97", Colour.BLUE,hephaestus);
        currentPlayer.assignWorker(worker1);
        currentPlayer.assignWorker(worker2);
        currentPlayer.turnSequence().setChosenWorker(worker1);
        currentPlayer.turnSequence().setChosenBox(map.position(3,4));

        assertTrue(currentPlayer.turnSequence().builtOnBoxes().isEmpty());
        build.executePhase(currentPlayer, communicationController, actionController, map, opponents, new ArrayList<WinCondition>());
        assertFalse(currentPlayer.turnSequence().builtOnBoxes().isEmpty());
        assertTrue(currentPlayer.turnSequence().builtOnBoxes().contains(map.position(3,4)));
        assertEquals(3, map.position(3,4).level());
    }
}