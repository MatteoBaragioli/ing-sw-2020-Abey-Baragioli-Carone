package it.polimi.ingsw.server.model;

public class ProtoCard {
    private final String name;
    private final int id;
    private final String[] actions;
    private final String winCondition;
    private final int winParameter;
    private final String setUpCondition;
    private final String[] fxOnOpponent;

    public ProtoCard(String name, int id, String[] actions, String winCondition, int winParameter, String setUpCondition, String[] fxOnOpponent) {
        this.name = name;
        this.id = id;
        this.actions = actions;
        this.winCondition = winCondition;
        this.winParameter = winParameter;
        this.setUpCondition = setUpCondition;
        this.fxOnOpponent = fxOnOpponent;
    }

    public String name() {
        return name;
    }

    public int id() {
        return id;
    }

    public String[] actions() {
        return actions;
    }

    public String winCondition() {
        return winCondition;
    }

    public int winParameter() {
        return winParameter;
    }

    public String setUpCondition() {
        return setUpCondition;
    }

    public String[] fxOnOpponent() {
        return fxOnOpponent;
    }
}