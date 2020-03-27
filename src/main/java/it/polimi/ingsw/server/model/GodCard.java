package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.List;

public class GodCard {
    private String name;
    private int id;
    private List<TurnSequenceModifier> actions = new ArrayList<>();
    private WinCondition winCondition;
    private SetUpCondition setUpCondition;
    private TurnSequenceModifier effectOnOpponent;


    public GodCard(String name, int id, List<TurnSequenceModifier> actions, WinCondition winCondition, SetUpCondition setUpCondition, TurnSequenceModifier effectOnOpponent) {
        this.name = name;
        this.id = id;
        this.actions = actions;
        this.winCondition = winCondition;
        this.setUpCondition = setUpCondition;
        this.effectOnOpponent = effectOnOpponent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setActions(List<TurnSequenceModifier> actions) {
        this.actions = actions;
    }

    public void setWinCondition(WinCondition winCondition) {
        this.winCondition = winCondition;
    }

    public void setSetUpCondition(SetUpCondition setUpCondition) {
        this.setUpCondition = setUpCondition;
    }

    public void setEffectOnOpponent(TurnSequenceModifier effectOnOpponent) {
        this.effectOnOpponent = effectOnOpponent;
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

    public TurnSequenceModifier effectOnOpponent() {
        return effectOnOpponent;
    }
}
