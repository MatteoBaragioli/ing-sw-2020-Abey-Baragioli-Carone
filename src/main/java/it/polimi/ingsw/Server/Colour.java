package it.polimi.ingsw.Server;

public enum Colour {
    WHITE('W'), GREY('G'), BLUE('B');

    private char abbreviation;

    Colour(char abbreviation) {
        this.abbreviation = abbreviation;
    }
}
