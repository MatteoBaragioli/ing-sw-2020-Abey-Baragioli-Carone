package it.polimi.ingsw.server.model;

import it.polimi.ingsw.network.objects.GodCardProxy;

import java.util.ArrayList;
import java.util.List;

public class GodCard {
    private String name;
    public final int id;
    private List<TurnSequenceModifier> actions = new ArrayList<>();
    public final WinCondition winCondition;
    public final SetUpCondition setUpCondition;
    public final List<TurnSequenceModifier> effectsOnOpponents;
    public final String description;
    public final String winDescription;
    public final String setUpDescription;
    public final String opponentsFxDescription;

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

    public GodCardProxy createProxy() {
        return new GodCardProxy(name(), id(), description, winDescription, setUpDescription, opponentsFxDescription);
    }
}
