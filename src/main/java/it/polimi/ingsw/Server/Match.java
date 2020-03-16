package it.polimi.ingsw.Server;

import java.util.ArrayList;

public class Match {
    private int turn;
    private ArrayList<Player> gamePlayers;
    private Map gameMap;
    /** TODO
     add deck attribute, deck getter?
     **/

    public Match(ArrayList<Player> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Match(int turn, ArrayList<Player> gamePlayers) {
        this.turn = turn;
        this.gamePlayers = gamePlayers;
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
}

