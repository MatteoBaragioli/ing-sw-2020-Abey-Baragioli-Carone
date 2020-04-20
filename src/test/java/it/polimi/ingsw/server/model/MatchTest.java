package it.polimi.ingsw.server.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MatchTest {

    @Test
    public void assignColour() {
        Player player1 = new Player("player1", null, null);
        Player player2 = new Player("player2", null, null);
        List<Player> players= new ArrayList<>();
        players.add(player1);
        players.add(player2);
        Match match = new Match(players, new CommunicationController());
        match.assignColour();
    }
}