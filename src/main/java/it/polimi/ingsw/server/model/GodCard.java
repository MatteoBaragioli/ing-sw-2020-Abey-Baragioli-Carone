package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.List;

public class GodCard {
    private String name;
    private int id;
    private List<TurnSequenceModifier> actions = new ArrayList<>();
    private WinCondition winCondition;
    private SetUpCondition setUpCondition;
    private List<TurnSequenceModifier> effectsOnOpponents;
    final private String description;
    final private String winDescription;
    final private String setUpDescription;
    final private String opponentsFxDescription;


    public GodCard(String name, int id, List<TurnSequenceModifier> actions, WinCondition winCondition, SetUpCondition setUpCondition, List<TurnSequenceModifier> effectsOnOpponents, String description, String winDescription, String setUpDescription, String opponentsFxDescription) {
        this.name = name;
        this.id = id;
        this.actions = actions;
        this.winCondition = winCondition;
        this.setUpCondition = setUpCondition;
        this.effectsOnOpponents = effectsOnOpponents;
        this.description = description;
        this.winDescription = winDescription;
        this.setUpDescription = setUpDescription;
        this.opponentsFxDescription = opponentsFxDescription;
    }

    public GodCard(String name, int id, List<TurnSequenceModifier> actions, WinCondition winCondition, SetUpCondition setUpCondition, List<TurnSequenceModifier> effectsOnOpponents) {
        this.name = name;
        this.id = id;
        this.actions = actions;
        this.winCondition = winCondition;
        this.setUpCondition = setUpCondition;
        this.effectsOnOpponents = effectsOnOpponents;
        description = null;
        winDescription = null;
        setUpDescription = null;
        opponentsFxDescription = null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActions(List<TurnSequenceModifier> actions) {
        this.actions = actions;
    }

    public String name() {
        return name;
    }

    public int id() {
        return id;
    }

    public List<TurnSequenceModifier> actions() {
        return actions;
    }

    public WinCondition winCondition() {
        return winCondition;
    }

    public SetUpCondition setUpCondition() {
        return setUpCondition;
    }

    public List<TurnSequenceModifier> effectOnOpponent() {
        return effectsOnOpponents;
    }

    public String description() {
        return description;
    }

    public String winDescription() {
        return winDescription;
    }

    public String setUpDescription() {
        return setUpDescription;
    }

    public String opponentsFxDescription() {
        return opponentsFxDescription;
    }
}
