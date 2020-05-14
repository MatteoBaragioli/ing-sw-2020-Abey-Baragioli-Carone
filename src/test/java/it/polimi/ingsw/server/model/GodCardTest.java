package it.polimi.ingsw.server.model;

import org.junit.Test;
import static org.junit.Assert.*;

import it.polimi.ingsw.server.model.godPowers.fx.DoNothing;
import it.polimi.ingsw.server.model.godPowers.setUpConditions.NoSetUpCondition;
import it.polimi.ingsw.server.model.godPowers.fx.SwapPower;
import java.util.ArrayList;
import java.util.List;

public class GodCardTest {

    @Test
    public void name() {
        //This test tries to get the name of the God Card

        List<TurnSequenceModifier> actions = new ArrayList<>();
        for(int i=0; i<4; i++){
            if(i==1)
                actions.add(i, new SwapPower());
            else
                actions.add(i, new DoNothing());
        }

        WinCondition winCondition = new StandardWin();

        SetUpCondition setUpCondition = new NoSetUpCondition();

        List<TurnSequenceModifier> effectOnOpponent = new ArrayList<>();

        for(int i=0; i<4; i++)
            effectOnOpponent.add(i,new DoNothing());

        GodCard godCard = new GodCard("Apollo", 1, actions, winCondition, setUpCondition, effectOnOpponent);

        String returnValue = godCard.name();

        assertEquals(returnValue, "Apollo");
    }

    @Test
    public void id() {
        //This test tries to get the id of the God Card

        List<TurnSequenceModifier> actions = new ArrayList<>();
        for(int i=0; i<4; i++){
            if(i==1)
                actions.add(i, new SwapPower());
            else
                actions.add(i, new DoNothing());
        }

        WinCondition winCondition = new StandardWin();

        SetUpCondition setUpCondition = new NoSetUpCondition();

        List<TurnSequenceModifier> effectOnOpponent = new ArrayList<>();

        for(int i=0; i<4; i++)
            effectOnOpponent.add(i,new DoNothing());

        GodCard godCard = new GodCard("Apollo", 1, actions, winCondition, setUpCondition, effectOnOpponent);

        int returnValue = godCard.id();

        assertEquals(returnValue, 1);
    }

    @Test
    public void actions() {
        //This test tries to get the list of actions of the God Card

        List<TurnSequenceModifier> actions = new ArrayList<>();
        for(int i=0; i<4; i++){
            if(i==1)
                actions.add(i, new SwapPower());
            else
                actions.add(i, new DoNothing());
        }

        WinCondition winCondition = new StandardWin();

        SetUpCondition setUpCondition = new NoSetUpCondition();

        List<TurnSequenceModifier> effectOnOpponent = new ArrayList<>();

        for(int i=0; i<4; i++)
            effectOnOpponent.add(i,new DoNothing());

        GodCard godCard = new GodCard("Apollo", 1, actions, winCondition, setUpCondition, effectOnOpponent);

        assertEquals(godCard.actions(), actions);
    }

    @Test
    public void winCondition() {
        //This test tries to get the win condition of the God Card

        List<TurnSequenceModifier> actions = new ArrayList<>();
        for(int i=0; i<4; i++){
            if(i==1)
                actions.add(i, new SwapPower());
            else
                actions.add(i, new DoNothing());
        }

        WinCondition winCondition = new StandardWin();

        SetUpCondition setUpCondition = new NoSetUpCondition();

        List<TurnSequenceModifier> effectOnOpponent = new ArrayList<>();

        for(int i=0; i<4; i++)
            effectOnOpponent.add(i,new DoNothing());

        GodCard godCard = new GodCard("Apollo", 1, actions, winCondition, setUpCondition, effectOnOpponent);

        assertEquals(godCard.winCondition(), winCondition);
    }

    @Test
    public void setUpCondition() {
        //This test tries to get the setup condition of the God Card

        List<TurnSequenceModifier> actions = new ArrayList<>();
        for(int i=0; i<4; i++){
            if(i==1)
                actions.add(i, new SwapPower());
            else
                actions.add(i, new DoNothing());
        }

        WinCondition winCondition = new StandardWin();

        SetUpCondition setUpCondition = new NoSetUpCondition();

        List<TurnSequenceModifier> effectOnOpponent = new ArrayList<>();

        for(int i=0; i<4; i++)
            effectOnOpponent.add(i,new DoNothing());

        GodCard godCard = new GodCard("Apollo", 1, actions, winCondition, setUpCondition, effectOnOpponent);

        assertEquals(godCard.setUpCondition(), setUpCondition);
    }

    @Test
    public void effectOnOpponent() {
        //This test tries to get the list of effects on opponents of the God Card

        List<TurnSequenceModifier> actions = new ArrayList<>();
        for(int i=0; i<4; i++){
            if(i==1)
                actions.add(i, new SwapPower());
            else
                actions.add(i, new DoNothing());
        }

        WinCondition winCondition = new StandardWin();

        SetUpCondition setUpCondition = new NoSetUpCondition();

        List<TurnSequenceModifier> effectOnOpponent = new ArrayList<>();

        for(int i=0; i<4; i++)
            effectOnOpponent.add(i,new DoNothing());

        GodCard godCard = new GodCard("Apollo", 1, actions, winCondition, setUpCondition, effectOnOpponent);

        assertEquals(godCard.effectOnOpponent(), effectOnOpponent);
    }
}