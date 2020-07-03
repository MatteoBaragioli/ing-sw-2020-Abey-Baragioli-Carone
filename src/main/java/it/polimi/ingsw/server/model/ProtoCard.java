package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.godPowers.fx.GodFX;
import it.polimi.ingsw.server.model.godPowers.setUpConditions.GodSetup;
import it.polimi.ingsw.server.model.godPowers.winConditions.GodWin;

public class ProtoCard {
    final private String name;
    final private int id;
    final private GodFX[] actions;
    final private GodWin winCondition;
    final private int winParameter;
    final private GodSetup setUpCondition;
    final private GodFX[] fxOnOpponent;
    final private String description;
    final private String winDescription;
    final private String setUpDescription;
    final private String opponentsFxDescription;

    public ProtoCard(String name, int id, GodFX[] actions, GodWin winCondition, int winParameter, GodSetup setUpCondition, GodFX[] fxOnOpponent, String description, String winDescription, String setUpDescription, String opponentsFxDescription) {
        this.name = name;
        this.id = id;
        this.actions = actions;
        this.winCondition = winCondition;
        this.winParameter = winParameter;
        this.setUpCondition = setUpCondition;
        this.fxOnOpponent = fxOnOpponent;
        this.description = description;
        this.winDescription = winDescription;
        this.setUpDescription = setUpDescription;
        this.opponentsFxDescription = opponentsFxDescription;
    }

    public String name() {
        return name;
    }

    public int id() {
        return id;
    }

    public GodFX[] actions() {
        return actions;
    }

    GodWin winCondition() {
        return winCondition;
    }

    int winParameter() {
        return winParameter;
    }

    GodSetup setUpCondition() {
        return setUpCondition;
    }

    GodFX[] fxOnOpponent() {
        return fxOnOpponent;
    }

    String description() {
        return description;
    }

    String winDescription() {
        return winDescription;
    }

    String setUpDescription() {
        return setUpDescription;
    }

    String opponentsFxDescription() {
        return opponentsFxDescription;
    }
}