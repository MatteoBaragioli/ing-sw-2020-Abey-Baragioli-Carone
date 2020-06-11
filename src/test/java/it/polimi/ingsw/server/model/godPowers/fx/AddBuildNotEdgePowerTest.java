package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.godPowers.fx.AddBuildNotEdgePower;
import it.polimi.ingsw.server.model.godPowers.setUpConditions.NoSetUpCondition;
import org.junit.Test;
import static org.junit.Assert.*;

import it.polimi.ingsw.server.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;


public class AddBuildNotEdgePowerTest {


    @Test
    public void usePower() throws IOException, TimeoutException, ChannelClosedException {
        //-------------------------- Test 1 ---------

        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        Worker chosenWorker = new Worker(map.position(0,0), Colour.BLUE);
        Player player = new Player("player1", Colour.BLUE, new GodCard("Hestia", 21, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.turnSequence().setChosenWorker(chosenWorker);

        TurnSequenceModifier hestiaPower = new AddBuildNotEdgePower();

        actionController.initialisePossibleBuilds(player.turnSequence(), map);
        actionController.applyOpponentsCondition(player, new ArrayList<>(), 2, map);
        List<Box> edgeBoxes = new ArrayList<>();
        for(Box box : player.turnSequence().possibleBuilds()){
            if(box.isOnEdge())
                edgeBoxes.add(box);
        }
        player.turnSequence().possibleBuilds().removeAll(edgeBoxes);

        //User doesn't use the power
        hestiaPower.usePower(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>(), false, new MatchStory(player));
        assertTrue(player.turnSequence().builtOnBoxes().isEmpty());
        assertEquals(1, player.turnSequence().allowedLevelDifference());


        //User uses the power
        for(Box position : map.groundToList()){
            player.turnSequence().recordNewPosition(player.workers().get(0), position);
            player.turnSequence().confirmTurnSequence();
            player.turnSequence().setChosenBox(map.position(0,0));
            player.turnSequence().setChosenWorker(player.workers().get(0));
            actionController.initialisePossibleBuilds(player.turnSequence(), map);
            actionController.applyOpponentsCondition(player, new ArrayList<>(), 2, map);
            edgeBoxes = new ArrayList<>();
            for(Box box : player.turnSequence().possibleBuilds()){
                if(box.isOnEdge())
                    edgeBoxes.add(box);
            }
            player.turnSequence().possibleBuilds().removeAll(edgeBoxes);
            hestiaPower.usePower(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>(), true, new MatchStory(player));
            for(Box possibleBuild : player.turnSequence().possibleBuilds())
                assertFalse(possibleBuild.isOnEdge());
        }
    }

    @Test
    public void executePower() {
        //-------------------------- Test 1 ---------

        Map map = new  Map();
        ActionController actionController = new ActionController();
        Worker chosenWorker = new Worker(false, map.position(2,2));
        Worker worker = new Worker(true, map.position(3,3));
        Player player = new Player("player1", Colour.BLUE, new GodCard("Hestia", 21, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.assignWorker(worker);
        player.turnSequence().setChosenWorker(chosenWorker);

        TurnSequenceModifier hestiaPower = new AddBuildNotEdgePower();

        assertTrue(player.turnSequence().possibleDestinations().isEmpty());
        assertTrue(player.turnSequence().newPositions().isEmpty());
        assertTrue(player.turnSequence().possibleBuilds().isEmpty());
        assertTrue(player.turnSequence().builtOnBoxes().isEmpty());
        assertEquals(1, player.turnSequence().allowedLevelDifference());

        actionController.initialisePossibleBuilds(player.turnSequence(), map);


        //for each Box in possibleBuilds
        for(Box chosenBox : player.turnSequence().possibleBuilds()) {
            hestiaPower.executePower(player, actionController,  chosenBox, new MatchStory(player));
            assertFalse(player.turnSequence().possibleBuilds().isEmpty());
            assertFalse(player.turnSequence().builtOnBoxes().isEmpty());
            assertTrue(player.turnSequence().builtOnBoxes().contains(chosenBox));
            assertEquals(1, player.turnSequence().builtOnBoxes().size());
            assertEquals(1, player.turnSequence().builtOnBoxes().get(0).level());
            assertEquals(1, player.turnSequence().allowedLevelDifference());
            player.turnSequence().clearBuiltOnBoxes();
        }
    }
}