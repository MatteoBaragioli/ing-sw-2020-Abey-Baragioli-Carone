package it.polimi.ingsw.server.model;

public enum Target {

    SELF('S'), OPPONENT('O'), ALL('A');

    private char abbreviation;

    Target(char abbreviation) {
        this.abbreviation = abbreviation;
    }
}
