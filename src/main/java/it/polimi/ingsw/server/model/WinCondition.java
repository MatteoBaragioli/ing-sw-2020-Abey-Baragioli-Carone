package it.polimi.ingsw.server.model;

public abstract class WinCondition {
    private Phase phase;
    private Target target;

    public boolean establishWinCondition(Player currentPlayer, Map map){
        return true;
    };
    public WinCondition(Phase phase, Target target){ }

    public Phase phase(){
        return this.phase;
    }

    public Target target(){
        return this.target;
    }
}
