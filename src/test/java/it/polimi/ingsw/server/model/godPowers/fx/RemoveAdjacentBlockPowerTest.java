package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.godPowers.setUpConditions.NoSetUpCondition;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

public class RemoveAdjacentBlockPowerTest {

    @Test
    public void executeAction() throws IOException, TimeoutException, ChannelClosedException {
        //-------------------------- Test 1 ---------
        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        Worker worker = new Worker(false, map.position(2,3));
        Worker unmovedWorker = new Worker(true, map.position(0,0));
        Player player = new Player("player1", Colour.BLUE,new GodCard("Ares", 12, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(worker);
        player.assignWorker(unmovedWorker);
        player.turnSequence().addMovableWorker(worker);
        player.turnSequence().setChosenWorker(worker);
        player.turnSequence().setChosenBox(map.position(1,3));

        actionController.updateNewPositions(player.turnSequence());

        assertTrue(player.turnSequence().newPositions().containsKey(worker));
        assertTrue(player.turnSequence().movedWorkers().contains(worker));

        TurnSequenceModifier aresPower = new RemoveAdjacentBlockPower();

        aresPower.executeAction(player, communicationController, actionController, map, new ArrayList<>(), new ArrayList<>(), new MatchStory(player));

        assertEquals(unmovedWorker, player.turnSequence().chosenWorker());

    }


    @Test
    public void usePower() throws IOException, TimeoutException, ChannelClosedException {
        //---------------Test 1-------------worker on ground
        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        //player's workers
        Worker unmovedWorker = new Worker(map.position(3,3), Colour.GREY);
        //opponents workers
        map.position(3,2).build();
        Worker opponentWorker = new Worker(map.position(3,2), Colour.BLUE); //level 1
        map.position(2,3).build();
        map.position(2,3).build();
        map.position(3,4).build();
        map.position(3,4).build();
        map.position(4,3).build();
        map.position(4,3).build();
        map.position(4,3).build();
        map.position(4,3).build(); //dome
        Player player = new Player("player1", Colour.GREY, new GodCard("Apollo", 1, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(unmovedWorker);
        player.turnSequence().setChosenWorker(unmovedWorker);

        TurnSequenceModifier aresPower = new RemoveAdjacentBlockPower();

        aresPower.usePower(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>(), false, new MatchStory(player));
        assertTrue(player.turnSequence().removedBlocks().isEmpty());
        player.turnSequence().setChosenBox(map.position(3,4));
        player.turnSequence().setChosenWorker(unmovedWorker);
        player.turnSequence().clearPossibleBuilds();
        for(Box box : map.adjacent(player.turnSequence().chosenWorker().position())){
            if(box.level() > 0 && box.isFree()){
                player.turnSequence().addPossibleBuild(box);
            }
        }
        aresPower.usePower(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>(), true, new MatchStory(player));

        List<Box> expectedList = new ArrayList<>();
        expectedList.add(map.position(2,3));
        expectedList.add(map.position(3,4));

        assertEquals(expectedList, player.turnSequence().possibleBuilds());
        assertTrue(player.turnSequence().removedBlocks().contains(player.turnSequence().chosenBox()));
    }

    @Test
    public void executePower() {
        //-------------------------- Test 1 ---------

        Map map = new  Map();
        ActionController actionController = new ActionController();
        Worker chosenWorker = new Worker(false, map.position(2,2));
        Player player = new Player("player1", Colour.BLUE, new GodCard("Ares", 12, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.turnSequence().setChosenWorker(chosenWorker);

        TurnSequenceModifier aresPower = new RemoveAdjacentBlockPower();

        assertTrue(player.turnSequence().removedBlocks().isEmpty());

        map.position(1, 3).build();

        assertEquals(1, map.position(1,3).level());

        Box chosenBox = map.position(1, 3);

        aresPower.executePower(player, actionController, chosenBox, new MatchStory(player));

        assertEquals(0, map.position(1,3).level());
        assertTrue(player.turnSequence().removedBlocks().contains(chosenBox));
    }
}