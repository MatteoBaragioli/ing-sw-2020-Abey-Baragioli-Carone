package it.polimi.ingsw.server.model.godPowers.fx;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.godPowers.fx.AddBuildOnSameBoxPower;
import it.polimi.ingsw.server.model.godPowers.setUpConditions.NoSetUpCondition;
import org.junit.Test;
import static org.junit.Assert.*;

import it.polimi.ingsw.server.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class AddBuildOnSameBoxPowerTest {

    @Test
    public void usePower() throws IOException, TimeoutException, ChannelClosedException {
        //-------------------------- Test 1 ---------

        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        Worker chosenWorker = new Worker(map.position(0,0), Colour.BLUE);
        Player player = new Player("player1", Colour.BLUE, new GodCard("Hephaestus", 6, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.turnSequence().setChosenWorker(chosenWorker);

        TurnSequenceModifier hephaestusPower = new AddBuildOnSameBoxPower();

        //User doesn't use the power
        hephaestusPower.usePower(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>() , false, new MatchStory(player));
        assertTrue(player.turnSequence().possibleBuilds().isEmpty());
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
            actionController.updateBuiltOnBox(player.turnSequence());
            assertEquals(1, player.turnSequence().builtOnBoxes().size());
            assertNotNull(player.turnSequence().chosenBox());
            hephaestusPower.usePower(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>(), true, new MatchStory(player));
            assertTrue(player.turnSequence().builtOnBoxes().contains(player.turnSequence().chosenBox()));
            assertEquals(2, player.turnSequence().builtOnBoxes().size());
            assertTrue(player.turnSequence().builtOnBoxes().contains(player.turnSequence().chosenBox()));
            assertEquals(player.turnSequence().builtOnBoxes().get(0), player.turnSequence().builtOnBoxes().get(1));
            assertEquals(2, player.turnSequence().chosenBox().level());
            player.turnSequence().clearBuiltOnBoxes();
            player.turnSequence().chosenBox().removeBlock();
            player.turnSequence().chosenBox().removeBlock();
        }
    }
}