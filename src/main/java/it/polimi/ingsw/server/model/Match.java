package it.polimi.ingsw.server.model;

import java.util.*;

public class Match {
    private int turn;
    private List<Player> gamePlayers;
    private Map gameMap = new Map();
    private Deque<GodCard> cards;
    private List<WinCondition> winConditions = new ArrayList<>();
    private ActionController actionController;
    private Player winner=null;

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

    public void setGameMap(Map gameMap) { this.gameMap = gameMap; }

    public List<Player> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(List<Player> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Deque<GodCard> getCards() { return cards; }

    public void setCards(Deque<GodCard> cards) {
        this.cards = cards;
    }

    public List<WinCondition> winConditions(){ return this.winConditions;}

    public void setWinConditions(List<WinCondition> winConditions){ this.winConditions=winConditions; }

    public void setActionController(ActionController actionController){
        this.actionController=actionController;
    }

    public Player winner(){return winner;}

    private void setWinner(Player winner){this.winner=winner;}


    /**
     * this method returns the opponents of the player given as a parameter
     * @param player
     * @return list of players
     */
    public List<Player> getOpponents(Player player){
        Iterator<Player> iterator= gamePlayers.iterator();
        List<Player> opponents=new ArrayList<>();
        Player target;
        while(iterator.hasNext()){
            target=iterator.next();
            if (!target.equals(player))
                opponents.add(target);
        }
        return opponents;
    }


    public void startMatch(CommunicationController communicationController){
        /*
        while(winner == null){
            boolean confirm = true;
            iterator = turnPhases.iterator();
            while(iterator.hasNext() && confirm){
                confirm = iterator.executePhase(tante belle cose);
            }
            if(!confirm)
                currentPlayer.turnSequence.undo();
            else{
                currentPlayer.turnSequence.confirm();
                currentPlayer = nextPlayer();
            }
        }
         */
    }

}