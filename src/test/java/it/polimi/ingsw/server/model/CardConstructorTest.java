package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.godPowers.*;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CardConstructorTest {

    @Test
    public void loadProtoCardsFromFile() {
        List<ProtoCard> protoCards = new CardConstructor().loadProtoCardsFromFile();
        assertTrue(protoCards.size() > 0);

        //Checking if APOLLO is generated correctly
        ProtoCard protoCard = protoCards.get(0);

        assertEquals("APOLLO", protoCard.name());
        assertEquals(1, protoCard.id());

        for (int i = 0; i<protoCard.actions().length; i++)
            if (i==1)
                assertEquals("Swap", protoCard.actions()[i]);
            else
                assertEquals("DoNothing", protoCard.actions()[i]);

        assertEquals("Standard", protoCard.winCondition());
        assertEquals("NoSetUp", protoCard.setUpCondition());

        for (String opponentFX: protoCard.fxOnOpponent())
            assertEquals("DoNothing", opponentFX);

        //Checking if ARTEMIS is generated correctly
        protoCard = protoCards.get(1);

        assertEquals("ARTEMIS", protoCard.name());
        assertEquals(2, protoCard.id());

        for (int i = 0; i<protoCard.actions().length; i++)
            if (i==1)
                assertEquals("AddMoveNotStartingBox", protoCard.actions()[i]);
            else
                assertEquals("DoNothing", protoCard.actions()[i]);

        assertEquals("Standard", protoCard.winCondition());
        assertEquals("NoSetUp", protoCard.setUpCondition());

        for (String opponentFX: protoCard.fxOnOpponent())
            assertEquals("DoNothing", opponentFX);

        //Checking if ATHENA is generated correctly
        protoCard = protoCards.get(2);

        assertEquals("ATHENA", protoCard.name());
        assertEquals(3, protoCard.id());

        for (int i = 0; i<protoCard.actions().length; i++)
            if (i==3)
                assertEquals("OpponentsCantMoveUpIfPlayerMovesUp", protoCard.actions()[i]);
            else
                assertEquals("DoNothing", protoCard.actions()[i]);

        assertEquals("Standard", protoCard.winCondition());
        assertEquals("NoSetUp", protoCard.setUpCondition());

        for (String opponentFX: protoCard.fxOnOpponent())
            assertEquals("DoNothing", opponentFX);

        //Checking if ATLAS is generated correctly
        protoCard = protoCards.get(3);

        assertEquals("ATLAS", protoCard.name());
        assertEquals(4, protoCard.id());

        for (int i = 0; i<protoCard.actions().length; i++)
            if (i==2)
                assertEquals("BuildDomeEverywhere", protoCard.actions()[i]);
            else
                assertEquals("DoNothing", protoCard.actions()[i]);

        assertEquals("Standard", protoCard.winCondition());
        assertEquals("NoSetUp", protoCard.setUpCondition());

        for (String opponentFX: protoCard.fxOnOpponent())
            assertEquals("DoNothing", opponentFX);

        //Checking if DEMETER is generated correctly
        protoCard = protoCards.get(4);

        assertEquals("DEMETER", protoCard.name());
        assertEquals(5, protoCard.id());

        for (int i = 0; i<protoCard.actions().length; i++)
            if (i==2)
                assertEquals("AddBuildNotSameBox", protoCard.actions()[i]);
            else
                assertEquals("DoNothing", protoCard.actions()[i]);

        assertEquals("Standard", protoCard.winCondition());
        assertEquals("NoSetUp", protoCard.setUpCondition());

        for (String opponentFX: protoCard.fxOnOpponent())
            assertEquals("DoNothing", opponentFX);

        //Checking if HEPHAESTUS is generated correctly
        protoCard = protoCards.get(5);

        assertEquals("HEPHAESTUS", protoCard.name());
        assertEquals(6, protoCard.id());

        for (int i = 0; i<protoCard.actions().length; i++)
            if (i==2)
                assertEquals("AddBuildOnSameBox", protoCard.actions()[i]);
            else
                assertEquals("DoNothing", protoCard.actions()[i]);

        assertEquals("Standard", protoCard.winCondition());
        assertEquals("NoSetUp", protoCard.setUpCondition());

        for (String opponentFX: protoCard.fxOnOpponent())
            assertEquals("DoNothing", opponentFX);

        //Checking if MINOTAUR is generated correctly
        protoCard = protoCards.get(6);

        assertEquals("MINOTAUR", protoCard.name());
        assertEquals(8, protoCard.id());

        for (int i = 0; i<protoCard.actions().length; i++)
            if (i==1)
                assertEquals("PushAdjacentOpponent", protoCard.actions()[i]);
            else
                assertEquals("DoNothing", protoCard.actions()[i]);

        assertEquals("Standard", protoCard.winCondition());
        assertEquals("NoSetUp", protoCard.setUpCondition());

        for (String opponentFX: protoCard.fxOnOpponent())
            assertEquals("DoNothing", opponentFX);

        //Checking if PAN is generated correctly
        protoCard = protoCards.get(7);

        assertEquals("PAN", protoCard.name());
        assertEquals(9, protoCard.id());

        for (String action: protoCard.actions())
            assertEquals("DoNothing", action);

        assertEquals("MoveTwoLevelsDown", protoCard.winCondition());
        assertEquals("NoSetUp", protoCard.setUpCondition());

        for (String opponentFX: protoCard.fxOnOpponent())
            assertEquals("DoNothing", opponentFX);

        //Checking if PROMETHEUS is generated correctly
        protoCard = protoCards.get(8);

        assertEquals("PROMETHEUS", protoCard.name());
        assertEquals(10, protoCard.id());

        for (int i = 0; i<protoCard.actions().length; i++)
            if (i == 0)
                assertEquals("AddBuildBeforeIfNotMoveUp", protoCard.actions()[i]);
            else
                assertEquals("DoNothing", protoCard.actions()[i]);

        assertEquals("Standard", protoCard.winCondition());
        assertEquals("NoSetUp", protoCard.setUpCondition());

        for (String opponentFX: protoCard.fxOnOpponent())
            assertEquals("DoNothing", opponentFX);

        //Checking if ARES is generated correctly
        protoCard = protoCards.get(9);

        assertEquals("ARES", protoCard.name());
        assertEquals(12, protoCard.id());

        for (int i = 0; i<protoCard.actions().length; i++)
            if (i==2)
                assertEquals("RemoveAdjacentBlock", protoCard.actions()[i]);
            else
                assertEquals("DoNothing", protoCard.actions()[i]);

        assertEquals("Standard", protoCard.winCondition());
        assertEquals("NoSetUp", protoCard.setUpCondition());

        for (String opponentFX: protoCard.fxOnOpponent())
            assertEquals("DoNothing", opponentFX);

        //Checking if CHRONUS is generated correctly
        protoCard = protoCards.get(10);

        assertEquals("CHRONUS", protoCard.name());
        assertEquals(16, protoCard.id());

        for (String action: protoCard.actions())
                assertEquals("DoNothing", action);

        assertEquals("TowerCountWin", protoCard.winCondition());
        assertEquals(5, protoCard.winParameter());
        assertEquals("NoSetUp", protoCard.setUpCondition());

        for (String opponentFX: protoCard.fxOnOpponent())
            assertEquals("DoNothing", opponentFX);

        //Checking if HESTIA is generated correctly
        protoCard = protoCards.get(11);

        assertEquals("HESTIA", protoCard.name());
        assertEquals(21, protoCard.id());

        for (int i = 0; i<protoCard.actions().length; i++)
            if (i==2)
                assertEquals("AddBuildNotEdge", protoCard.actions()[i]);
            else
                assertEquals("DoNothing", protoCard.actions()[i]);

        assertEquals("Standard", protoCard.winCondition());
        assertEquals("NoSetUp", protoCard.setUpCondition());

        for (String opponentFX: protoCard.fxOnOpponent())
            assertEquals("DoNothing", opponentFX);

        //Checking if POSEIDON is generated correctly
        protoCard = protoCards.get(12);

        assertEquals("POSEIDON", protoCard.name());
        assertEquals(27, protoCard.id());

        for (int i = 0; i<protoCard.actions().length; i++)
            if (i==2)
                assertEquals("AddThreeBuildsToUnmovedWorker", protoCard.actions()[i]);
            else
                assertEquals("DoNothing", protoCard.actions()[i]);

        assertEquals("Standard", protoCard.winCondition());
        assertEquals("NoSetUp", protoCard.setUpCondition());

        for (String opponentFX: protoCard.fxOnOpponent())
            assertEquals("DoNothing", opponentFX);

        //Checking if ZEUS is generated correctly
        protoCard = protoCards.get(13);

        assertEquals("ZEUS", protoCard.name());
        assertEquals(30, protoCard.id());

        for (int i = 0; i<protoCard.actions().length; i++)
            if (i==2)
                assertEquals("BuildUnderYourself", protoCard.actions()[i]);
            else
                assertEquals("DoNothing", protoCard.actions()[i]);

        assertEquals("Standard", protoCard.winCondition());
        assertEquals("NoSetUp", protoCard.setUpCondition());

        for (String opponentFX: protoCard.fxOnOpponent())
            assertEquals("DoNothing", opponentFX);
    }
    //Done

    @Test
    public void loadFX() {
        CardConstructor cardConstructor = new CardConstructor();
        Map<String, TurnSequenceModifier> fx = cardConstructor.loadFX();

        assertTrue(fx.containsKey("DoNothing"));
        assertTrue(fx.get("DoNothing").getClass().isInstance(new DoNothing()));

        assertTrue(fx.containsKey("Swap"));
        assertTrue(fx.get("Swap").getClass().isInstance(new SwapPower()));

        assertTrue(fx.containsKey("AddMoveNotStartingBox"));
        assertTrue(fx.get("AddMoveNotStartingBox").getClass().isInstance(new AddMoveNotStartingBoxPower()));

        assertTrue(fx.containsKey("OpponentsCantMoveUpIfPlayerMovesUp"));
        assertTrue(fx.get("OpponentsCantMoveUpIfPlayerMovesUp").getClass().isInstance(new OpponenentsCantMoveUpIfPlayerMovesUpPower()));

        assertTrue(fx.containsKey("BuildDomeEverywhere"));
        assertTrue(fx.get("BuildDomeEverywhere").getClass().isInstance(new BuildDomeEverywherePower()));

        assertTrue(fx.containsKey("AddBuildNotSameBox"));
        assertTrue(fx.get("AddBuildNotSameBox").getClass().isInstance(new AddBuildNotSameBoxPower()));

        assertTrue(fx.containsKey("AddBuildOnSameBox"));
        assertTrue(fx.get("AddBuildOnSameBox").getClass().isInstance(new AddBuildOnSameBoxPower()));

        assertTrue(fx.containsKey("PushAdjacentOpponent"));
        assertTrue(fx.get("PushAdjacentOpponent").getClass().isInstance(new PushAdjacentOpponentPower()));

        assertTrue(fx.containsKey("AddBuildBeforeIfNotMoveUp"));
        assertTrue(fx.get("AddBuildBeforeIfNotMoveUp").getClass().isInstance(new AddBuildBeforeMoveIfNotMoveUpPower()));

        assertTrue(fx.containsKey("RemoveAdjacentBlock"));
        assertTrue(fx.get("RemoveAdjacentBlock").getClass().isInstance(new RemoveAdjacentBlockPower()));

        assertTrue(fx.containsKey("AddBuildNotEdge"));
        assertTrue(fx.get("AddBuildNotEdge").getClass().isInstance(new AddBuildNotEdgePower()));

        assertTrue(fx.containsKey("AddThreeBuildsToUnmovedWorker"));
        assertTrue(fx.get("AddThreeBuildsToUnmovedWorker").getClass().isInstance(new AddThreeBuildsToUnmovedWorkerIfOnGroundPower()));

        assertTrue(fx.containsKey("BuildUnderYourself"));
        assertTrue(fx.get("BuildUnderYourself").getClass().isInstance(new BuildUnderYourselfPower()));
    }
    //Done

    @Test
    public void loadActions() {
        CardConstructor cardConstructor = new CardConstructor();
        assertTrue(cardConstructor.cards().size() > 0);

        //Checking if APOLLO's actions are generated correctly
        List<TurnSequenceModifier> actions = cardConstructor.cards().get(0).actions();

        for (int i = 0; i<actions.size(); i++)
            if (i==1)
                assertTrue(actions.get(i).getClass().isInstance(new SwapPower()));
            else
                assertTrue(actions.get(i).getClass().isInstance(new DoNothing()));

        //Checking if ARTEMIS' actions are generated correctly
        actions = cardConstructor.cards().get(1).actions();

        for (int i = 0; i<actions.size(); i++)
            if (i==1)
                assertTrue(actions.get(i).getClass().isInstance(new AddMoveNotStartingBoxPower()));
            else
                assertTrue(actions.get(i).getClass().isInstance(new DoNothing()));

        //Checking if ATHENA's actions are generated correctly
        actions = cardConstructor.cards().get(2).actions();

        for (int i = 0; i<actions.size(); i++)
            if (i==3)
                assertTrue(actions.get(i).getClass().isInstance(new OpponenentsCantMoveUpIfPlayerMovesUpPower()));
            else
                assertTrue(actions.get(i).getClass().isInstance(new DoNothing()));

        //Checking if ATLAS' actions are generated correctly
        actions = cardConstructor.cards().get(3).actions();

        for (int i = 0; i<actions.size(); i++)
            if (i==2)
                assertTrue(actions.get(i).getClass().isInstance(new BuildDomeEverywherePower()));
            else
                assertTrue(actions.get(i).getClass().isInstance(new DoNothing()));

        //Checking if DEMETER's actions are generated correctly
        actions = cardConstructor.cards().get(4).actions();

        for (int i = 0; i<actions.size(); i++)
            if (i==2)
                assertTrue(actions.get(i).getClass().isInstance(new AddBuildNotSameBoxPower()));
            else
                assertTrue(actions.get(i).getClass().isInstance(new DoNothing()));

        //Checking if HEPHAESTUS' actions are generated correctly
        actions = cardConstructor.cards().get(5).actions();

        for (int i = 0; i<actions.size(); i++)
            if (i==2)
                assertTrue(actions.get(i).getClass().isInstance(new AddBuildOnSameBoxPower()));
            else
                assertTrue(actions.get(i).getClass().isInstance(new DoNothing()));

        //Checking if MINOTAUR's actions are generated correctly
        actions = cardConstructor.cards().get(6).actions();

        for (int i = 0; i<actions.size(); i++)
            if (i==1)
                assertTrue(actions.get(i).getClass().isInstance(new PushAdjacentOpponentPower()));
            else
                assertTrue(actions.get(i).getClass().isInstance(new DoNothing()));

        //Checking if PAN's actions are generated correctly
        actions = cardConstructor.cards().get(7).actions();

        for (TurnSequenceModifier action: actions)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if PROMETHEUS' actions are generated correctly
        actions = cardConstructor.cards().get(8).actions();

        for (int i = 0; i<actions.size(); i++)
            if (i == 0)
                assertTrue(actions.get(i).getClass().isInstance(new AddBuildBeforeMoveIfNotMoveUpPower()));
            else
                assertTrue(actions.get(i).getClass().isInstance(new DoNothing()));

        //Checking if ARES' actions are generated correctly
        actions = cardConstructor.cards().get(9).actions();

        for (int i = 0; i<actions.size(); i++)
            if (i==2)
                assertTrue(actions.get(i).getClass().isInstance(new RemoveAdjacentBlockPower()));
            else
                assertTrue(actions.get(i).getClass().isInstance(new DoNothing()));

        //Checking if CHRONUS' actions are generated correctly
        actions = cardConstructor.cards().get(10).actions();

        for (TurnSequenceModifier action: actions)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if HESTIA's actions are generated correctly
        actions = cardConstructor.cards().get(11).actions();

        for (int i = 0; i<actions.size(); i++)
            if (i==2)
                assertTrue(actions.get(i).getClass().isInstance(new AddBuildNotEdgePower()));
            else
                assertTrue(actions.get(i).getClass().isInstance(new DoNothing()));

        //Checking if POSEIDON's actions are generated correctly
        actions = cardConstructor.cards().get(12).actions();

        for (int i = 0; i<actions.size(); i++)
            if (i==2)
                assertTrue(actions.get(i).getClass().isInstance(new AddThreeBuildsToUnmovedWorkerIfOnGroundPower()));
            else
                assertTrue(actions.get(i).getClass().isInstance(new DoNothing()));

        //Checking if ZEUS' actions are generated correctly
        actions = cardConstructor.cards().get(13).actions();

        for (int i = 0; i<actions.size(); i++)
            if (i==2)
                assertTrue(actions.get(i).getClass().isInstance(new BuildUnderYourselfPower()));
            else
                assertTrue(actions.get(i).getClass().isInstance(new DoNothing()));
    }
    //Done

    @Test
    public void loadWinCondition() {
        CardConstructor cardConstructor = new CardConstructor();

        //Checking APOLLO's win condition
        assertNull(cardConstructor.cards().get(0).winCondition());

        //Checking ARTEMIS' win condition
        assertNull(cardConstructor.cards().get(1).winCondition());

        //Checking ATHENA's win condition
        assertNull(cardConstructor.cards().get(2).winCondition());

        //Checking ATLAS' win condition
        assertNull(cardConstructor.cards().get(3).winCondition());

        //Checking DEMETER's win condition
        assertNull(cardConstructor.cards().get(4).winCondition());

        //Checking HEPHAESTUS' win condition
        assertNull(cardConstructor.cards().get(5).winCondition());

        //Checking MINOTAUR'S win condition
        assertNull(cardConstructor.cards().get(6).winCondition());

        //Checking PAN' win condition
        assertTrue(cardConstructor.cards().get(7).winCondition().getClass().isInstance(new MoveTwoLevelsDownWin()));

        //Checking PROMETHEUS' win condition
        assertNull(cardConstructor.cards().get(8).winCondition());

        //Checking ARES' win condition
        assertNull(cardConstructor.cards().get(9).winCondition());

        //Checking CHRONUS' win condition
        assertTrue(cardConstructor.cards().get(10).winCondition().getClass().isInstance(new TowerCountWin(5)));

        //Checking HESTIA's win condition
        assertNull(cardConstructor.cards().get(11).winCondition());

        //Checking POSEIDON's win condition
        assertNull(cardConstructor.cards().get(12).winCondition());

        //Checking ZEUS' win condition
        assertNull(cardConstructor.cards().get(13).winCondition());
    }
    //Done

    @Test
    public void loadSetUpCondition() {
        CardConstructor cardConstructor = new CardConstructor();

        //Checking APOLLO's setup condition
        assertTrue(cardConstructor.cards().get(0).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking ARTEMIS' setup condition
        assertTrue(cardConstructor.cards().get(1).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking ATHENA's setup condition
        assertTrue(cardConstructor.cards().get(2).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking ATLAS' setup condition
        assertTrue(cardConstructor.cards().get(3).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking DEMETER's setup condition
        assertTrue(cardConstructor.cards().get(4).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking HEPHAESTUS' setup condition
        assertTrue(cardConstructor.cards().get(5).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking MINOTAUR'S setup condition
        assertTrue(cardConstructor.cards().get(6).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking PAN' setup condition
        assertTrue(cardConstructor.cards().get(7).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking PROMETHEUS' setup condition
        assertTrue(cardConstructor.cards().get(8).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking ARES' setup condition
        assertTrue(cardConstructor.cards().get(9).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking CHRONUS' setup condition
        assertTrue(cardConstructor.cards().get(10).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking HESTIA's setup condition
        assertTrue(cardConstructor.cards().get(11).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking POSEIDON's setup condition
        assertTrue(cardConstructor.cards().get(12).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking ZEUS' setup condition
        assertTrue(cardConstructor.cards().get(13).setUpCondition().getClass().isInstance(new NoSetUpCondition()));
    }
    //Done

    @Test
    public void loadFXOnOpponents() {
        CardConstructor cardConstructor = new CardConstructor();

        //Checking if APOLLO's fx are generated correctly
        List<TurnSequenceModifier> fx = cardConstructor.cards().get(0).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if ARTEMIS' fx are generated correctly
        fx = cardConstructor.cards().get(1).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if ATHENA's fx are generated correctly
        fx = cardConstructor.cards().get(2).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if ATLAS' fx are generated correctly
        fx = cardConstructor.cards().get(3).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if DEMETER's fx are generated correctly
        fx = cardConstructor.cards().get(4).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if HEPHAESTUS' fx are generated correctly
        fx = cardConstructor.cards().get(5).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if MINOTAUR's fx are generated correctly
        fx = cardConstructor.cards().get(6).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if PAN's fx are generated correctly
        fx = cardConstructor.cards().get(7).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if PROMETHEUS' fx are generated correctly
        fx = cardConstructor.cards().get(8).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if ARES' fx are generated correctly
        fx = cardConstructor.cards().get(9).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if CHRONUS' fx are generated correctly
        fx = cardConstructor.cards().get(10).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if HESTIA's fx are generated correctly
        fx = cardConstructor.cards().get(11).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if POSEIDON's fx are generated correctly
        fx = cardConstructor.cards().get(12).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if ZEUS' fx are generated correctly
        fx = cardConstructor.cards().get(13).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));
    }
    //Done

    @Test
    public void createCard() {
        CardConstructor cardConstructor = new CardConstructor();

        //Generating APOLLO
        GodCard godCard = cardConstructor.cards().get(0);

        assertEquals("APOLLO", godCard.name());
        assertEquals(1, godCard.id());

        for (int i = 0; i<godCard.actions().size(); i++)
            if (i==1)
                assertTrue(godCard.actions().get(i).getClass().isInstance(new SwapPower()));
            else
                assertTrue(godCard.actions().get(i).getClass().isInstance(new DoNothing()));

        assertNull(godCard.winCondition());
        assertTrue(godCard.setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        for (TurnSequenceModifier effect: godCard.effectOnOpponent())
            assertTrue(effect.getClass().isInstance(new DoNothing()));

        //Generating ARTEMIS
        godCard = cardConstructor.cards().get(1);

        assertEquals("ARTEMIS", godCard.name());
        assertEquals(2, godCard.id());

        for (int i = 0; i<godCard.actions().size(); i++)
            if (i==1)
                assertTrue(godCard.actions().get(i).getClass().isInstance(new AddMoveNotStartingBoxPower()));
            else
                assertTrue(godCard.actions().get(i).getClass().isInstance(new DoNothing()));

        assertNull(godCard.winCondition());
        assertTrue(godCard.setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        for (TurnSequenceModifier effect: godCard.effectOnOpponent())
            assertTrue(effect.getClass().isInstance(new DoNothing()));

        //Generating ATHENA
        godCard = cardConstructor.cards().get(2);

        assertEquals("ATHENA", godCard.name());
        assertEquals(3, godCard.id());

        for (int i = 0; i<godCard.actions().size(); i++)
            if (i==3)
                assertTrue(godCard.actions().get(i).getClass().isInstance(new OpponenentsCantMoveUpIfPlayerMovesUpPower()));
            else
                assertTrue(godCard.actions().get(i).getClass().isInstance(new DoNothing()));

        assertNull(godCard.winCondition());
        assertTrue(godCard.setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        for (TurnSequenceModifier effect: godCard.effectOnOpponent())
            assertTrue(effect.getClass().isInstance(new DoNothing()));

        //Generating ATLAS
        godCard = cardConstructor.cards().get(3);

        assertEquals("ATLAS", godCard.name());
        assertEquals(4, godCard.id());

        for (int i = 0; i<godCard.actions().size(); i++)
            if (i==2)
                assertTrue(godCard.actions().get(i).getClass().isInstance(new BuildDomeEverywherePower()));
            else
                assertTrue(godCard.actions().get(i).getClass().isInstance(new DoNothing()));

        assertNull(godCard.winCondition());
        assertTrue(godCard.setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        for (TurnSequenceModifier effect: godCard.effectOnOpponent())
            assertTrue(effect.getClass().isInstance(new DoNothing()));

        //Generating DEMETER
        godCard = cardConstructor.cards().get(4);

        assertEquals("DEMETER", godCard.name());
        assertEquals(5, godCard.id());

        for (int i = 0; i<godCard.actions().size(); i++)
            if (i==2)
                assertTrue(godCard.actions().get(i).getClass().isInstance(new AddBuildNotSameBoxPower()));
            else
                assertTrue(godCard.actions().get(i).getClass().isInstance(new DoNothing()));

        assertNull(godCard.winCondition());
        assertTrue(godCard.setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        for (TurnSequenceModifier effect: godCard.effectOnOpponent())
            assertTrue(effect.getClass().isInstance(new DoNothing()));

        //Generating HEPHAESTUS
        godCard = cardConstructor.cards().get(5);

        assertEquals("HEPHAESTUS", godCard.name());
        assertEquals(6, godCard.id());

        for (int i = 0; i<godCard.actions().size(); i++)
            if (i==2)
                assertTrue(godCard.actions().get(i).getClass().isInstance(new AddBuildOnSameBoxPower()));
            else
                assertTrue(godCard.actions().get(i).getClass().isInstance(new DoNothing()));

        assertNull(godCard.winCondition());
        assertTrue(godCard.setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        for (TurnSequenceModifier effect: godCard.effectOnOpponent())
            assertTrue(effect.getClass().isInstance(new DoNothing()));

        //Generating MINOTAUR
        godCard = cardConstructor.cards().get(6);

        assertEquals("MINOTAUR", godCard.name());
        assertEquals(8, godCard.id());

        for (int i = 0; i<godCard.actions().size(); i++)
            if (i==1)
                assertTrue(godCard.actions().get(i).getClass().isInstance(new PushAdjacentOpponentPower()));
            else
                assertTrue(godCard.actions().get(i).getClass().isInstance(new DoNothing()));

        assertNull(godCard.winCondition());
        assertTrue(godCard.setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        for (TurnSequenceModifier effect: godCard.effectOnOpponent())
            assertTrue(effect.getClass().isInstance(new DoNothing()));

        //Generating PAN
        godCard = cardConstructor.cards().get(7);

        assertEquals("PAN", godCard.name());
        assertEquals(9, godCard.id());

        for (TurnSequenceModifier action: godCard.actions())
            assertTrue(action.getClass().isInstance(new DoNothing()));

        assertTrue(godCard.winCondition().getClass().isInstance(new MoveTwoLevelsDownWin()));
        assertTrue(godCard.setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        for (TurnSequenceModifier effect: godCard.effectOnOpponent())
            assertTrue(effect.getClass().isInstance(new DoNothing()));

        //Generating PROMETHEUS
        godCard = cardConstructor.cards().get(8);

        assertEquals("PROMETHEUS", godCard.name());
        assertEquals(10, godCard.id());

        for (int i = 0; i<godCard.actions().size(); i++)
            if (i==0)
                assertTrue(godCard.actions().get(i).getClass().isInstance(new AddBuildBeforeMoveIfNotMoveUpPower()));
            else
                assertTrue(godCard.actions().get(i).getClass().isInstance(new DoNothing()));

        assertNull(godCard.winCondition());
        assertTrue(godCard.setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        for (TurnSequenceModifier effect: godCard.effectOnOpponent())
            assertTrue(effect.getClass().isInstance(new DoNothing()));

        //Generating ARES
        godCard = cardConstructor.cards().get(9);

        assertEquals("ARES", godCard.name());
        assertEquals(12, godCard.id());

        for (int i = 0; i<godCard.actions().size(); i++)
            if (i==2)
                assertTrue(godCard.actions().get(i).getClass().isInstance(new RemoveAdjacentBlockPower()));
            else
                assertTrue(godCard.actions().get(i).getClass().isInstance(new DoNothing()));

        assertNull(godCard.winCondition());
        assertTrue(godCard.setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        for (TurnSequenceModifier effect: godCard.effectOnOpponent())
            assertTrue(effect.getClass().isInstance(new DoNothing()));

        //Generating CHRONUS
        godCard = cardConstructor.cards().get(10);

        assertEquals("CHRONUS", godCard.name());
        assertEquals(16, godCard.id());

        for (TurnSequenceModifier action: godCard.actions())
            assertTrue(action.getClass().isInstance(new DoNothing()));

        assertTrue(godCard.winCondition().getClass().isInstance(new TowerCountWin(5)));
        assertTrue(godCard.setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        for (TurnSequenceModifier effect: godCard.effectOnOpponent())
            assertTrue(effect.getClass().isInstance(new DoNothing()));

        //Generating HESTIA
        godCard = cardConstructor.cards().get(11);

        assertEquals("HESTIA", godCard.name());
        assertEquals(21, godCard.id());

        for (int i = 0; i<godCard.actions().size(); i++)
            if (i==2)
                assertTrue(godCard.actions().get(i).getClass().isInstance(new AddBuildNotEdgePower()));
            else
                assertTrue(godCard.actions().get(i).getClass().isInstance(new DoNothing()));

        assertNull(godCard.winCondition());
        assertTrue(godCard.setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        for (TurnSequenceModifier effect: godCard.effectOnOpponent())
            assertTrue(effect.getClass().isInstance(new DoNothing()));

        //Generating POSEIDON
        godCard = cardConstructor.cards().get(12);

        assertEquals("POSEIDON", godCard.name());
        assertEquals(27, godCard.id());

        for (int i = 0; i<godCard.actions().size(); i++)
            if (i==2)
                assertTrue(godCard.actions().get(i).getClass().isInstance(new AddThreeBuildsToUnmovedWorkerIfOnGroundPower()));
            else
                assertTrue(godCard.actions().get(i).getClass().isInstance(new DoNothing()));

        assertNull(godCard.winCondition());
        assertTrue(godCard.setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        for (TurnSequenceModifier effect: godCard.effectOnOpponent())
            assertTrue(effect.getClass().isInstance(new DoNothing()));

        //Generating ZEUS
        godCard = cardConstructor.cards().get(13);

        assertEquals("ZEUS", godCard.name());
        assertEquals(30, godCard.id());

        for (int i = 0; i<godCard.actions().size(); i++)
            if (i==2)
                assertTrue(godCard.actions().get(i).getClass().isInstance(new BuildUnderYourselfPower()));
            else
                assertTrue(godCard.actions().get(i).getClass().isInstance(new DoNothing()));

        assertNull(godCard.winCondition());
        assertTrue(godCard.setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        for (TurnSequenceModifier effect: godCard.effectOnOpponent())
            assertTrue(effect.getClass().isInstance(new DoNothing()));
    }
    //Done
}