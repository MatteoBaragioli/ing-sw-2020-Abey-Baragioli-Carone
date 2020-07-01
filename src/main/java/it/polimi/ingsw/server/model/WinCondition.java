package it.polimi.ingsw.server.model;

public abstract class WinCondition {
    private Phase phase;
    private Target target;

    public WinCondition(Phase phase, Target target){
        this.phase=phase;
        this.target=target;
    }

    /**
     * This method verifies if a win condition is satisfied for the current player
     * @param currentPlayer Current player
     * @param map Map of the match
     * @return Boolean that is true if the win condition is satisfied
     */
    public boolean establishWinCondition(Player currentPlayer, Map map){
        return true;
    }

    public Phase phase(){
        return phase;
    }

    public Target target(){
        return target;
    }
}
