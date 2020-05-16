package it.polimi.ingsw.server.model;

public enum Colour {
    WHITE('W'), GREY('G'), BLUE('B');

    private char abbreviation;

    Colour(char abbreviation) {
        this.abbreviation = abbreviation;
    }
}
