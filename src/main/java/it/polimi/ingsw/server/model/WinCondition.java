package it.polimi.ingsw.server.model;

public abstract class WinCondition {
    private Phase phase;
    private Target target;

    public boolean establishWinCondition(Player currentPlayer, Map map){
        return true;
    };
    public WinCondition(Phase phase, Target target){
        this.phase=phase;
        this.target=target;
    }

    public Phase phase(){
        return phase;
    }

    public Target target(){
        return target;
    }
}
