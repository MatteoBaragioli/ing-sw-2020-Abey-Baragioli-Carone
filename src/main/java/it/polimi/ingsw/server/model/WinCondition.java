package it.polimi.ingsw.server.model;

public abstract class WinCondition {
    private Phase phase;
    private Target target;

    public void establishWinCondition(){
    };

    public Phase phase(){
        return this.phase;
    }

    public Target target(){
        return this.target;
    }
}
