package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.godPowers.NoSetUpCondition;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MatchTest {


    @Test
    public void getOpponents() {
        CommunicationController communicationController = new CommunicationController();

        Player player1 = new Player("Matteo", Colour.BLUE, new GodCard("Apollo", 1, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        Player player2 = new Player("Francesca", Colour.GREY, new GodCard("Artemis", 2, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));
        Player player3 = new Player("Jitendra", Colour.WHITE, new GodCard("Athena", 3, new ArrayList<TurnSequenceModifier>(), new StandardWin(), new NoSetUpCondition(), new ArrayList<TurnSequenceModifier>()));

        List<Player> gamePlayers = new ArrayList<>();
        gamePlayers.add(player1);
        gamePlayers.add(player2);
        gamePlayers.add(player3);

        Match match = new Match(gamePlayers, communicationController);
        for(Player player : match.getGamePlayers()){
            List<Player> opponents = match.getOpponents(player);
            List<Player> expectedOpponents = new ArrayList<>(gamePlayers);
            expectedOpponents.remove(player);
            assertEquals(gamePlayers.size()-1, opponents.size());
            assertEquals(expectedOpponents, opponents);
            assertFalse(opponents.contains(player));
        }
    }

    @Test
    public void assignColour() {
        Player player1 = new Player("player1", null, null);
        Player player2 = new Player("player2", null, null);
        Player player3 = new Player("player3", null, null);
        List<Player> players= new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        Match match = new Match(players, new CommunicationController());
        match.assignColour();
        assertNotNull(player1.getColour());
        assertNotNull(player2.getColour());
        assertNotNull(player3.getColour());
        assertNotEquals(player1.getColour(), player2.getColour());
        assertNotEquals(player1.getColour(), player3.getColour());
        assertNotEquals(player2.getColour(), player3.getColour());
    }

    @Test
    public void chooseCards() {
        Player player1 = new Player("player1", null, null);
        Player player2 = new Player("player2", null, null);
        Player player3 = new Player("player3", null, null);
        List<Player> players= new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        Match match = new Match(players, new CommunicationController());
        match.assignColour();
        match.chooseCards();

        for(GodCard godCard : match.getCards())
            System.out.println(godCard.name());

        assertEquals(match.getGamePlayers().size(), match.getCards().size());


    }

    @Test
    public void assignCard() {
        Player player1 = new Player("Matteo", null, null);
        Player player2 = new Player("Francesca", null, null);
        Player player3 = new Player("Jitendra", null, null);
        List<Player> players= new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        Match match = new Match(players, new CommunicationController());
        match.assignColour();
        match.chooseCards();
        match.assignCards();

        for(Player player : match.getGamePlayers()) {
            assertNotNull(player.godCard());
            System.out.println(player.getNickname());
            System.out.println(player.getColour());
            System.out.println(player.godCard().id());
            System.out.println(player.godCard().name());
        }
        assertNotEquals(player1.godCard(), player2.godCard());
        assertNotEquals(player1.godCard(), player3.godCard());
        assertNotEquals(player2.godCard(), player3.godCard());
    }

    @Test
    public void setUpWorkers() {
        Player player1 = new Player("Matteo", null, null);
        Player player2 = new Player("Francesca", null, null);
        Player player3 = new Player("Jitendra", null, null);
        List<Player> players= new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        Match match = new Match(players, new CommunicationController());
        match.assignColour();
        match.chooseCards();
        match.assignCards();
        match.setUpWorkers();

        for(Player player : match.getGamePlayers()){
            assertNotNull(player.workers());
            assertEquals(2, player.workers().size());
            assertNotEquals(player.workers().get(0), player.workers().get(1));
            assertEquals(player.getColour(), player.workers().get(0).colour());
            assertEquals(player.getColour(), player.workers().get(1).colour());
        }

        assertNotEquals(player1.workers().get(0), player2.workers().get(0));
        assertNotEquals(player1.workers().get(0), player2.workers().get(1));
        assertNotEquals(player1.workers().get(1), player2.workers().get(0));
        assertNotEquals(player1.workers().get(1), player2.workers().get(1));

        assertNotEquals(player1.workers().get(0), player3.workers().get(0));
        assertNotEquals(player1.workers().get(0), player3.workers().get(1));
        assertNotEquals(player1.workers().get(1), player3.workers().get(0));
        assertNotEquals(player1.workers().get(1), player3.workers().get(1));

        assertNotEquals(player2.workers().get(0), player3.workers().get(0));
        assertNotEquals(player2.workers().get(0), player3.workers().get(1));
        assertNotEquals(player2.workers().get(1), player3.workers().get(0));
        assertNotEquals(player2.workers().get(1), player3.workers().get(1));
    }

    @Test
    public void match() {
        Player player1 = new Player("Matteo", null, null);
        Player player2 = new Player("Francesca", null, null);
        Player player3 = new Player("Jitendra", null, null);
        List<Player> players= new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        Match match = new Match(players, new CommunicationController());
        match.match();
    }
}