package it.polimi.ingsw.Server;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Match {
    private int turn;
    private List<Player> gamePlayers;
    private Map gameMap;
    private Deque<GodCard> cards;

    public Match(List<Player> gamePlayers, Deque<GodCard> cards) {
        this.gamePlayers = gamePlayers;
        this.cards = cards;
    }

    public Match(int turn, List<Player> gamePlayers, Deque<GodCard> cards) {
        this.turn = turn;
        this.gamePlayers = gamePlayers;
        this.cards = cards;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public Map getGameMap() {
        return gameMap;
    }

    public List<Player> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(List<Player> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Deque<GodCard> getCards() {
        return cards;
    }

    public void setCards(Deque<GodCard> cards) {
        this.cards = cards;
    }
}