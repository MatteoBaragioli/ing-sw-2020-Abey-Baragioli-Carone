package it.polimi.ingsw.server.model.godPowers;

import org.junit.Test;
import static org.junit.Assert.*;

import it.polimi.ingsw.server.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class BuildDomeEverywherePowerTest {

    @Test
    public void executeAction() throws IOException {
        //chosenWorker in (2,4) builds a dome on (1,3)
        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        Worker chosenWorker = new Worker(false, map.position(2,4));
        Player player = new Player("player1", Colour.BLUE,new GodCard("Atlas", 4, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.turnSequence().setChosenWorker(chosenWorker);
        player.turnSequence().setChosenBox(map.position(1,4));
        map.position(1, 4).build();
        map.position(1, 4).build();
        map.position(1, 4).build();
        map.position(1, 3).build();
        map.position(1, 3).build();//level 2

        TurnSequenceModifier atlasPower = new BuildDomeEverywherePower();

        //first box (1,4)
        actionController.updateBuiltOnBox(player.turnSequence());
        atlasPower.executeAction(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>());

        assertTrue(player.turnSequence().builtOnBoxes().contains(player.turnSequence().chosenBox()));
        assertEquals(3, player.turnSequence().chosenBox().level());
        assertTrue(player.turnSequence().chosenBox().hasDome());

        //second box (1,3)
        player.turnSequence().clearBuiltOnBoxes();
        player.turnSequence().setChosenBox(map.position(1,3));

        actionController.updateBuiltOnBox(player.turnSequence());
        atlasPower.executeAction(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>());

        assertTrue(player.turnSequence().builtOnBoxes().contains(player.turnSequence().chosenBox()));
        assertEquals(2, player.turnSequence().chosenBox().level());
        assertTrue(player.turnSequence().chosenBox().hasDome());
    }

    @Test
    public void usePower() throws IOException {
        Map map = new  Map();
        ActionController actionController = new ActionController();
        CommunicationController communicationController = new CommunicationController();
        Worker chosenWorker = new Worker(false, map.position(2,4));
        Player player = new Player("player1", Colour.BLUE,new GodCard("Atlas", 4, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        player.assignWorker(chosenWorker);
        player.turnSequence().setChosenWorker(chosenWorker);
        player.turnSequence().setChosenBox(map.position(1,4));
        map.position(1, 4).build();

        TurnSequenceModifier atlasPower = new BuildDomeEverywherePower();

        //first build no power
        actionController.updateBuiltOnBox(player.turnSequence());
        atlasPower.usePower(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>(), false);

        assertTrue(player.turnSequence().builtOnBoxes().contains(player.turnSequence().chosenBox()));
        assertEquals(2, player.turnSequence().chosenBox().level());
        assertFalse(player.turnSequence().chosenBox().hasDome());

        //second build with power
        actionController.updateBuiltOnBox(player.turnSequence());
        atlasPower.usePower(player, communicationController, actionController, map, new ArrayList<Player>(), new ArrayList<WinCondition>(), true);

        assertTrue(player.turnSequence().builtOnBoxes().contains(player.turnSequence().chosenBox()));
        assertEquals(2, player.turnSequence().chosenBox().level());
        assertTrue(player.turnSequence().chosenBox().hasDome());
    }
}