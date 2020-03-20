package it.polimi.ingsw.Server;

import java.util.ArrayList;
import java.util.Deque;

public class Match {
    private int turn;
    private ArrayList<Player> gamePlayers;
    private Map gameMap;
    private Deque<GodCard> cards;

    public Match(ArrayList<Player> gamePlayers, Deque<GodCard> cards) {
        this.gamePlayers = gamePlayers;
        this.cards = cards;
    }

    public Match(int turn, ArrayList<Player> gamePlayers, Deque<GodCard> cards) {
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

    public ArrayList<Player> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(ArrayList<Player> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Deque<GodCard> getCards() {
        return cards;
    }

    public void setCards(Deque<GodCard> cards) {
        this.cards = cards;
    }
}