package it.polimi.ingsw.Server;

public enum TurnPhase {

    START('s'), MOVE ('m'), BUILD('b'), END ('e');

    private char abbreviation;
    TurnPhase(char abbreviation){
        this.abbreviation = abbreviation;
    }
}
