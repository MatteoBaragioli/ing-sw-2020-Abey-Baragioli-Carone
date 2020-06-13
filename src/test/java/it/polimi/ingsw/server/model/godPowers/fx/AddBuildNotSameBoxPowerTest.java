package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.network.exceptions.*;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.godPowers.setUpConditions.NoSetUpCondition;
import org.junit.Test;
import static org.junit.Assert.*;

import it.polimi.ingsw.server.model.*;

import java.util.ArrayList;
import java.util.List;


public class AddBuildNotSameBoxPowerTest {

    @Test
    public void usePower() throws ChannelClosedException, TimeOutException {
        //-------------------------- Test 1 ---------

        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        Worker chosenWorker = new Worker(map.position(0,0), Colour.BLUE);
        Player player = new Player("player1", Colour.BLUE, new GodCard("Demeter", 5, new ArrayList<>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.turnSequence().setChosenWorker(chosenWorker);

        TurnSequenceModifier demeterPower = new AddBuildNotSameBoxPower();

        actionController.initialisePossibleBuilds(player.turnSequence(), map);
        actionController.applyOpponentsCondition(player, new ArrayList<>(), 2, map);
        List<Box> previousBuilds = new ArrayList<>();
        for(Box box : player.turnSequence().possibleBuilds()){
            if(player.turnSequence().builtOnBoxes().contains(box))
                previousBuilds.add(box);
        }
        player.turnSequence().possibleBuilds().removeAll(previousBuilds);

        //User doesn't use the power
        demeterPower.usePower(player, communicationController, actionController, map, new ArrayList<>(), new ArrayList<>(), false, new MatchStory(player));
        assertTrue(player.turnSequence().builtOnBoxes().isEmpty());
        assertEquals(1, player.turnSequence().allowedLevelDifference());

        //User uses the power
        for(Box position : map.groundToList()){
            player.turnSequence().recordNewPosition(player.workers().get(0), position);
            player.turnSequence().confirmTurnSequence();
            player.turnSequence().setChosenBox(map.position(position.positionX()+1,position.positionY()));
            if(player.turnSequence().chosenBox()==null)
                player.turnSequence().setChosenBox(map.position(position.positionX()-1,position.positionY()));
            player.turnSequence().setChosenWorker(player.workers().get(0));
            Box firstBuild = player.turnSequence().chosenBox();
            int firstLevel = player.turnSequence().chosenBox().level();
            actionController.updateBuiltOnBox(player.turnSequence());
            assertEquals(1, player.turnSequence().builtOnBoxes().size());
            actionController.initialisePossibleBuilds(player.turnSequence(), map);
            actionController.applyOpponentsCondition(player, new ArrayList<>(), 2, map);
            previousBuilds = new ArrayList<>();
            for(Box box : player.turnSequence().possibleBuilds()){
                if(player.turnSequence().builtOnBoxes().contains(box))
                    previousBuilds.add(box);
            }
            player.turnSequence().possibleBuilds().removeAll(previousBuilds);
            demeterPower.usePower(player, communicationController, actionController, map, new ArrayList<>(), new ArrayList<>(), true, new MatchStory(player));
            assertFalse(player.turnSequence().possibleBuilds().contains(firstBuild));
            assertTrue(player.turnSequence().builtOnBoxes().contains(player.turnSequence().chosenBox()));
            assertTrue(player.turnSequence().builtOnBoxes().contains(firstBuild));
            assertEquals(2, player.turnSequence().builtOnBoxes().size());
            assertEquals(firstLevel+1, firstBuild.level());
            player.turnSequence().clearBuiltOnBoxes();
            firstBuild.removeBlock();
            position.removeBlock();
        }
    }




    @Test
    public void executePower() {
        //-------------------------- Test 1 ---------

        Map map = new  Map();
        ActionController actionController = new ActionController();
        Worker chosenWorker = new Worker(false, map.position(2,2));
        Worker worker = new Worker(true, map.position(3,3));
        Player player = new Player("player1", Colour.BLUE, new GodCard("Demeter", 5, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.assignWorker(worker);
        player.turnSequence().setChosenWorker(chosenWorker);

        TurnSequenceModifier demeterPower = new AddBuildNotSameBoxPower();

        assertTrue(player.turnSequence().possibleDestinations().isEmpty());
        assertTrue(player.turnSequence().newPositions().isEmpty());
        assertTrue(player.turnSequence().possibleBuilds().isEmpty());
        assertTrue(player.turnSequence().builtOnBoxes().isEmpty());
        assertEquals(1, player.turnSequence().allowedLevelDifference());

        actionController.initialisePossibleBuilds(player.turnSequence(), map);


        //for each Box in possibleBuilds
        for(Box chosenBox : player.turnSequence().possibleBuilds()) {
            demeterPower.executePower(player, actionController,  chosenBox, new MatchStory(player));
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