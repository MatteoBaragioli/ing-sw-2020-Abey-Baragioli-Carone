package it.polimi.ingsw.Server;

import java.util.ArrayList;

public class Match {
    private int turn;
    private ArrayList<Player> GamePlayers;
    private Map GameMap;
    /** TODO
     add deck attribute, deck getter?
     **/

    public Match(ArrayList<Player> gamePlayers) {
        GamePlayers = gamePlayers;
    }

    public Match(int turn, ArrayList<Player> gamePlayers) {
        this.turn = turn;
        GamePlayers = gamePlayers;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public Map getGameMap() {
        return GameMap;
    }

    public ArrayList<Player> getGamePlayers() {
        return GamePlayers;
    }

    public void setGamePlayers(ArrayList<Player> gamePlayers) {
        GamePlayers = gamePlayers;
    }
}

