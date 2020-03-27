package it.polimi.ingsw.server.model;

public enum Phase {
    START('S'), MOVE('M'), BUILD('B'), UNDO('U'), END('E');

    private char phase;

    Phase(char phase) {
        this.phase = phase;
    }
}
