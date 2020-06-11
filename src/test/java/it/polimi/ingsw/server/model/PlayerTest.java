package it.polimi.ingsw.server.model;

import it.polimi.ingsw.network.objects.PlayerProxy;
import it.polimi.ingsw.server.model.godPowers.setUpConditions.NoSetUpCondition;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void createProxy() {
        GodCard card1 = new GodCard("Apollo", 1, new ArrayList<TurnSequenceModifier>(), null, new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>());
        Player player1 = new Player("Player1", Colour.BLUE, card1);

        PlayerProxy playerProxy = player1.createProxy();

        assertEquals(player1.name(), playerProxy.name);
        assertEquals(player1.colour(), playerProxy.colour);
        assertEquals(player1.godCard().name(), playerProxy.godCardProxy.name);
        assertEquals(player1.godCard().id(), playerProxy.godCardProxy.id);
    }

    @Test
    public void assignWorker() {
        Worker worker1 = new Worker(new Box(0, 0), Colour.BLUE, true);
        Player player1 = new Player("Player1", Colour.BLUE, null);
        player1.assignWorker(worker1);

        assertEquals(player1.workers().get(0), worker1);
    }
}