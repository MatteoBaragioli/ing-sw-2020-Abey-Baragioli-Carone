package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.godPowers.fx.*;
import it.polimi.ingsw.server.model.godPowers.setUpConditions.NoSetUpCondition;
import it.polimi.ingsw.server.model.godPowers.winConditions.MoveTwoLevelsDownWin;
import it.polimi.ingsw.server.model.godPowers.winConditions.TowerCountWin;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.server.model.godPowers.fx.GodFX.*;
import static it.polimi.ingsw.server.model.godPowers.setUpConditions.GodSetup.*;
import static it.polimi.ingsw.server.model.godPowers.winConditions.GodWin.*;
import static org.junit.Assert.*;

public class CardConstructorTest {

    @Test
    public void loadProtoCardsFromFile() {
        List<ProtoCard> protoCards = new CardConstructor().loadProtoCardsFromFile();
        assertTrue(protoCards.size() > 0);

        //Checking if Apollo is generated correctly
        ProtoCard protoCard = protoCards.get(0);

        assertEquals("Apollo", protoCard.name());
        assertEquals(1, protoCard.id());

        for (int i = 0; i<protoCard.actions().length; i++)
            if (i==1)
                assertEquals(SWAP, protoCard.actions()[i]);
            else
                assertEquals(DO_NOTHING, protoCard.actions()[i]);

        assertEquals(STANDARD, protoCard.winCondition());
        assertEquals(NO_SETUP, protoCard.setUpCondition());

        for (GodFX opponentFX: protoCard.fxOnOpponent())
            assertEquals(DO_NOTHING, opponentFX);

        //Checking if Artemis is generated correctly
        protoCard = protoCards.get(1);

        assertEquals("Artemis", protoCard.name());
        assertEquals(2, protoCard.id());

        for (int i = 0; i<protoCard.actions().length; i++)
            if (i==1)
                assertEquals(ADD_MOVE_NOT_STARTING_BOX, protoCard.actions()[i]);
            else
                assertEquals(DO_NOTHING, protoCard.actions()[i]);

        assertEquals(STANDARD, protoCard.winCondition());
        assertEquals(NO_SETUP, protoCard.setUpCondition());

        for (GodFX opponentFX: protoCard.fxOnOpponent())
            assertEquals(DO_NOTHING, opponentFX);

        //Checking if Athena is generated correctly
        protoCard = protoCards.get(2);

        assertEquals("Athena", protoCard.name());
        assertEquals(3, protoCard.id());

        for (int i = 0; i<protoCard.actions().length; i++)
            if (i==3)
                assertEquals(OPPONENTS_CANT_MOVE_UP_IF_PLAYER_MOVES_UP, protoCard.actions()[i]);
            else
                assertEquals(DO_NOTHING, protoCard.actions()[i]);

        assertEquals(STANDARD, protoCard.winCondition());
        assertEquals(NO_SETUP, protoCard.setUpCondition());

        for (GodFX opponentFX: protoCard.fxOnOpponent())
            assertEquals(DO_NOTHING, opponentFX);

        //Checking if Atlas is generated correctly
        protoCard = protoCards.get(3);

        assertEquals("Atlas", protoCard.name());
        assertEquals(4, protoCard.id());

        for (int i = 0; i<protoCard.actions().length; i++)
            if (i==2)
                assertEquals(BUILD_DOME_EVERYWHERE, protoCard.actions()[i]);
            else
                assertEquals(DO_NOTHING, protoCard.actions()[i]);

        assertEquals(STANDARD, protoCard.winCondition());
        assertEquals(NO_SETUP, protoCard.setUpCondition());

        for (GodFX opponentFX: protoCard.fxOnOpponent())
            assertEquals(DO_NOTHING, opponentFX);

        //Checking if Demeter is generated correctly
        protoCard = protoCards.get(4);

        assertEquals("Demeter", protoCard.name());
        assertEquals(5, protoCard.id());

        for (int i = 0; i<protoCard.actions().length; i++)
            if (i==2)
                assertEquals(ADD_BUILD_NOT_SAME_BOX, protoCard.actions()[i]);
            else
                assertEquals(DO_NOTHING, protoCard.actions()[i]);

        assertEquals(STANDARD, protoCard.winCondition());
        assertEquals(NO_SETUP, protoCard.setUpCondition());

        for (GodFX opponentFX: protoCard.fxOnOpponent())
            assertEquals(DO_NOTHING, opponentFX);

        //Checking if Hephaestus is generated correctly
        protoCard = protoCards.get(5);

        assertEquals("Hephaestus", protoCard.name());
        assertEquals(6, protoCard.id());

        for (int i = 0; i<protoCard.actions().length; i++)
            if (i==2)
                assertEquals(ADD_BUILD_ON_SAME_BOX, protoCard.actions()[i]);
            else
                assertEquals(DO_NOTHING, protoCard.actions()[i]);

        assertEquals(STANDARD, protoCard.winCondition());
        assertEquals(NO_SETUP, protoCard.setUpCondition());

        for (GodFX opponentFX: protoCard.fxOnOpponent())
            assertEquals(DO_NOTHING, opponentFX);

        //Checking if Minotaur is generated correctly
        protoCard = protoCards.get(6);

        assertEquals("Minotaur", protoCard.name());
        assertEquals(8, protoCard.id());

        for (int i = 0; i<protoCard.actions().length; i++)
            if (i==1)
                assertEquals(PUSH_ADJACENT_OPPONENT, protoCard.actions()[i]);
            else
                assertEquals(DO_NOTHING, protoCard.actions()[i]);

        assertEquals(STANDARD, protoCard.winCondition());
        assertEquals(NO_SETUP, protoCard.setUpCondition());

        for (GodFX opponentFX: protoCard.fxOnOpponent())
            assertEquals(DO_NOTHING, opponentFX);

        //Checking if Pan is generated correctly
        protoCard = protoCards.get(7);

        assertEquals("Pan", protoCard.name());
        assertEquals(9, protoCard.id());

        for (GodFX action: protoCard.actions())
            assertEquals(DO_NOTHING, action);

        assertEquals(MOVE_TWO_LEVELS_DOWN, protoCard.winCondition());
        assertEquals(NO_SETUP, protoCard.setUpCondition());

        for (GodFX opponentFX: protoCard.fxOnOpponent())
            assertEquals(DO_NOTHING, opponentFX);

        //Checking if Prometheus is generated correctly
        protoCard = protoCards.get(8);

        assertEquals("Prometheus", protoCard.name());
        assertEquals(10, protoCard.id());

        for (int i = 0; i<protoCard.actions().length; i++)
            if (i == 0)
                assertEquals(ADD_BUILD_BEFORE_IF_NOT_MOVE_UP, protoCard.actions()[i]);
            else
                assertEquals(DO_NOTHING, protoCard.actions()[i]);

        assertEquals(STANDARD, protoCard.winCondition());
        assertEquals(NO_SETUP, protoCard.setUpCondition());

        for (GodFX opponentFX: protoCard.fxOnOpponent())
            assertEquals(DO_NOTHING, opponentFX);

        //Checking if Ares is generated correctly
        protoCard = protoCards.get(9);

        assertEquals("Ares", protoCard.name());
        assertEquals(12, protoCard.id());

        for (int i = 0; i<protoCard.actions().length; i++)
            if (i==2)
                assertEquals(REMOVE_ADJACENT_BLOCK, protoCard.actions()[i]);
            else
                assertEquals(DO_NOTHING, protoCard.actions()[i]);

        assertEquals(STANDARD, protoCard.winCondition());
        assertEquals(NO_SETUP, protoCard.setUpCondition());

        for (GodFX opponentFX: protoCard.fxOnOpponent())
            assertEquals(DO_NOTHING, opponentFX);

        //Checking if Chronus is generated correctly
        protoCard = protoCards.get(10);

        assertEquals("Chronus", protoCard.name());
        assertEquals(16, protoCard.id());

        for (GodFX action: protoCard.actions())
                assertEquals(DO_NOTHING, action);

        assertEquals(TOWER_COUNT, protoCard.winCondition());
        assertEquals(5, protoCard.winParameter());
        assertEquals(NO_SETUP, protoCard.setUpCondition());

        for (GodFX opponentFX: protoCard.fxOnOpponent())
            assertEquals(DO_NOTHING, opponentFX);

        //Checking if Hestia is generated correctly
        protoCard = protoCards.get(11);

        assertEquals("Hestia", protoCard.name());
        assertEquals(21, protoCard.id());

        for (int i = 0; i<protoCard.actions().length; i++)
            if (i==2)
                assertEquals(ADD_BUILD_NOT_EDGE, protoCard.actions()[i]);
            else
                assertEquals(DO_NOTHING, protoCard.actions()[i]);

        assertEquals(STANDARD, protoCard.winCondition());
        assertEquals(NO_SETUP, protoCard.setUpCondition());

        for (GodFX opponentFX: protoCard.fxOnOpponent())
            assertEquals(DO_NOTHING, opponentFX);

        //Checking if Poseidon is generated correctly
        protoCard = protoCards.get(12);

        assertEquals("Poseidon", protoCard.name());
        assertEquals(27, protoCard.id());

        for (int i = 0; i<protoCard.actions().length; i++)
            if (i==2)
                assertEquals(ADD_THREE_BUILDS_TO_UNMOVED_WORKER, protoCard.actions()[i]);
            else
                assertEquals(DO_NOTHING, protoCard.actions()[i]);

        assertEquals(STANDARD, protoCard.winCondition());
        assertEquals(NO_SETUP, protoCard.setUpCondition());

        for (GodFX opponentFX: protoCard.fxOnOpponent())
            assertEquals(DO_NOTHING, opponentFX);

        //Checking if Zeus is generated correctly
        protoCard = protoCards.get(13);

        assertEquals("Zeus", protoCard.name());
        assertEquals(30, protoCard.id());

        for (int i = 0; i<protoCard.actions().length; i++)
            if (i==2)
                assertEquals(BUILD_UNDER_YOURSELF, protoCard.actions()[i]);
            else
                assertEquals(DO_NOTHING, protoCard.actions()[i]);

        assertEquals(STANDARD, protoCard.winCondition());
        assertEquals(NO_SETUP, protoCard.setUpCondition());

        for (GodFX opponentFX: protoCard.fxOnOpponent())
            assertEquals(DO_NOTHING, opponentFX);
    }
    //Done

    @Test
    public void loadFX() {
        CardConstructor cardConstructor = new CardConstructor();
        Map<GodFX, TurnSequenceModifier> fx = cardConstructor.loadFX();

        assertTrue(fx.containsKey(DO_NOTHING));
        assertTrue(fx.get(DO_NOTHING).getClass().isInstance(new DoNothing()));

        assertTrue(fx.containsKey(SWAP));
        assertTrue(fx.get(SWAP).getClass().isInstance(new SwapPower()));

        assertTrue(fx.containsKey(ADD_MOVE_NOT_STARTING_BOX));
        assertTrue(fx.get(ADD_MOVE_NOT_STARTING_BOX).getClass().isInstance(new AddMoveNotStartingBoxPower()));

        assertTrue(fx.containsKey(OPPONENTS_CANT_MOVE_UP_IF_PLAYER_MOVES_UP));
        assertTrue(fx.get(OPPONENTS_CANT_MOVE_UP_IF_PLAYER_MOVES_UP).getClass().isInstance(new OpponenentsCantMoveUpIfPlayerMovesUpPower()));

        assertTrue(fx.containsKey(BUILD_DOME_EVERYWHERE));
        assertTrue(fx.get(BUILD_DOME_EVERYWHERE).getClass().isInstance(new BuildDomeEverywherePower()));

        assertTrue(fx.containsKey(ADD_BUILD_NOT_SAME_BOX));
        assertTrue(fx.get(ADD_BUILD_NOT_SAME_BOX).getClass().isInstance(new AddBuildNotSameBoxPower()));

        assertTrue(fx.containsKey(ADD_BUILD_ON_SAME_BOX));
        assertTrue(fx.get(ADD_BUILD_ON_SAME_BOX).getClass().isInstance(new AddBuildOnSameBoxPower()));

        assertTrue(fx.containsKey(PUSH_ADJACENT_OPPONENT));
        assertTrue(fx.get(PUSH_ADJACENT_OPPONENT).getClass().isInstance(new PushAdjacentOpponentPower()));

        assertTrue(fx.containsKey(ADD_BUILD_BEFORE_IF_NOT_MOVE_UP));
        assertTrue(fx.get(ADD_BUILD_BEFORE_IF_NOT_MOVE_UP).getClass().isInstance(new AddBuildBeforeMoveIfNotMoveUpPower()));

        assertTrue(fx.containsKey(REMOVE_ADJACENT_BLOCK));
        assertTrue(fx.get(REMOVE_ADJACENT_BLOCK).getClass().isInstance(new RemoveAdjacentBlockPower()));

        assertTrue(fx.containsKey(ADD_BUILD_NOT_EDGE));
        assertTrue(fx.get(ADD_BUILD_NOT_EDGE).getClass().isInstance(new AddBuildNotEdgePower()));

        assertTrue(fx.containsKey(ADD_THREE_BUILDS_TO_UNMOVED_WORKER));
        assertTrue(fx.get(ADD_THREE_BUILDS_TO_UNMOVED_WORKER).getClass().isInstance(new AddThreeBuildsToUnmovedWorkerIfOnGroundPower()));

        assertTrue(fx.containsKey(BUILD_UNDER_YOURSELF));
        assertTrue(fx.get(BUILD_UNDER_YOURSELF).getClass().isInstance(new BuildUnderYourselfPower()));
    }
    //Done

    @Test
    public void loadActions() {
        CardConstructor cardConstructor = new CardConstructor();
        assertTrue(cardConstructor.cards().size() > 0);

        //Checking if Apollo's actions are generated correctly
        List<TurnSequenceModifier> actions = cardConstructor.cards().get(0).actions();

        for (int i = 0; i<actions.size(); i++)
            if (i==1)
                assertTrue(actions.get(i).getClass().isInstance(new SwapPower()));
            else
                assertTrue(actions.get(i).getClass().isInstance(new DoNothing()));

        //Checking if Artemis' actions are generated correctly
        actions = cardConstructor.cards().get(1).actions();

        for (int i = 0; i<actions.size(); i++)
            if (i==1)
                assertTrue(actions.get(i).getClass().isInstance(new AddMoveNotStartingBoxPower()));
            else
                assertTrue(actions.get(i).getClass().isInstance(new DoNothing()));

        //Checking if Athena's actions are generated correctly
        actions = cardConstructor.cards().get(2).actions();

        for (int i = 0; i<actions.size(); i++)
            if (i==3)
                assertTrue(actions.get(i).getClass().isInstance(new OpponenentsCantMoveUpIfPlayerMovesUpPower()));
            else
                assertTrue(actions.get(i).getClass().isInstance(new DoNothing()));

        //Checking if Atlas' actions are generated correctly
        actions = cardConstructor.cards().get(3).actions();

        for (int i = 0; i<actions.size(); i++)
            if (i==2)
                assertTrue(actions.get(i).getClass().isInstance(new BuildDomeEverywherePower()));
            else
                assertTrue(actions.get(i).getClass().isInstance(new DoNothing()));

        //Checking if Demeter's actions are generated correctly
        actions = cardConstructor.cards().get(4).actions();

        for (int i = 0; i<actions.size(); i++)
            if (i==2)
                assertTrue(actions.get(i).getClass().isInstance(new AddBuildNotSameBoxPower()));
            else
                assertTrue(actions.get(i).getClass().isInstance(new DoNothing()));

        //Checking if Hephaestus' actions are generated correctly
        actions = cardConstructor.cards().get(5).actions();

        for (int i = 0; i<actions.size(); i++)
            if (i==2)
                assertTrue(actions.get(i).getClass().isInstance(new AddBuildOnSameBoxPower()));
            else
                assertTrue(actions.get(i).getClass().isInstance(new DoNothing()));

        //Checking if Minotaur's actions are generated correctly
        actions = cardConstructor.cards().get(6).actions();

        for (int i = 0; i<actions.size(); i++)
            if (i==1)
                assertTrue(actions.get(i).getClass().isInstance(new PushAdjacentOpponentPower()));
            else
                assertTrue(actions.get(i).getClass().isInstance(new DoNothing()));

        //Checking if Pan's actions are generated correctly
        actions = cardConstructor.cards().get(7).actions();

        for (TurnSequenceModifier action: actions)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if Prometheus' actions are generated correctly
        actions = cardConstructor.cards().get(8).actions();

        for (int i = 0; i<actions.size(); i++)
            if (i == 0)
                assertTrue(actions.get(i).getClass().isInstance(new AddBuildBeforeMoveIfNotMoveUpPower()));
            else
                assertTrue(actions.get(i).getClass().isInstance(new DoNothing()));

        //Checking if Ares' actions are generated correctly
        actions = cardConstructor.cards().get(9).actions();

        for (int i = 0; i<actions.size(); i++)
            if (i==2)
                assertTrue(actions.get(i).getClass().isInstance(new RemoveAdjacentBlockPower()));
            else
                assertTrue(actions.get(i).getClass().isInstance(new DoNothing()));

        //Checking if Chronus' actions are generated correctly
        actions = cardConstructor.cards().get(10).actions();

        for (TurnSequenceModifier action: actions)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if Hestia's actions are generated correctly
        actions = cardConstructor.cards().get(11).actions();

        for (int i = 0; i<actions.size(); i++)
            if (i==2)
                assertTrue(actions.get(i).getClass().isInstance(new AddBuildNotEdgePower()));
            else
                assertTrue(actions.get(i).getClass().isInstance(new DoNothing()));

        //Checking if Poseidon's actions are generated correctly
        actions = cardConstructor.cards().get(12).actions();

        for (int i = 0; i<actions.size(); i++)
            if (i==2)
                assertTrue(actions.get(i).getClass().isInstance(new AddThreeBuildsToUnmovedWorkerIfOnGroundPower()));
            else
                assertTrue(actions.get(i).getClass().isInstance(new DoNothing()));

        //Checking if Zeus' actions are generated correctly
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

        //Checking Apollo's win condition
        assertNull(cardConstructor.cards().get(0).winCondition());

        //Checking Artemis' win condition
        assertNull(cardConstructor.cards().get(1).winCondition());

        //Checking Athena's win condition
        assertNull(cardConstructor.cards().get(2).winCondition());

        //Checking Atlas' win condition
        assertNull(cardConstructor.cards().get(3).winCondition());

        //Checking Demeter's win condition
        assertNull(cardConstructor.cards().get(4).winCondition());

        //Checking Hephaestus' win condition
        assertNull(cardConstructor.cards().get(5).winCondition());

        //Checking Minotaur'S win condition
        assertNull(cardConstructor.cards().get(6).winCondition());

        //Checking Pan' win condition
        assertTrue(cardConstructor.cards().get(7).winCondition().getClass().isInstance(new MoveTwoLevelsDownWin()));

        //Checking Prometheus' win condition
        assertNull(cardConstructor.cards().get(8).winCondition());

        //Checking Ares' win condition
        assertNull(cardConstructor.cards().get(9).winCondition());

        //Checking Chronus' win condition
        assertTrue(cardConstructor.cards().get(10).winCondition().getClass().isInstance(new TowerCountWin(5)));

        //Checking Hestia's win condition
        assertNull(cardConstructor.cards().get(11).winCondition());

        //Checking Poseidon's win condition
        assertNull(cardConstructor.cards().get(12).winCondition());

        //Checking Zeus' win condition
        assertNull(cardConstructor.cards().get(13).winCondition());
    }
    //Done

    @Test
    public void loadSetUpCondition() {
        CardConstructor cardConstructor = new CardConstructor();

        //Checking Apollo's setup condition
        assertTrue(cardConstructor.cards().get(0).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking Artemis' setup condition
        assertTrue(cardConstructor.cards().get(1).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking Athena's setup condition
        assertTrue(cardConstructor.cards().get(2).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking Atlas' setup condition
        assertTrue(cardConstructor.cards().get(3).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking Demeter's setup condition
        assertTrue(cardConstructor.cards().get(4).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking Hephaestus' setup condition
        assertTrue(cardConstructor.cards().get(5).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking Minotaur'S setup condition
        assertTrue(cardConstructor.cards().get(6).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking Pan' setup condition
        assertTrue(cardConstructor.cards().get(7).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking Prometheus' setup condition
        assertTrue(cardConstructor.cards().get(8).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking Ares' setup condition
        assertTrue(cardConstructor.cards().get(9).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking Chronus' setup condition
        assertTrue(cardConstructor.cards().get(10).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking Hestia's setup condition
        assertTrue(cardConstructor.cards().get(11).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking Poseidon's setup condition
        assertTrue(cardConstructor.cards().get(12).setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        //Checking Zeus' setup condition
        assertTrue(cardConstructor.cards().get(13).setUpCondition().getClass().isInstance(new NoSetUpCondition()));
    }
    //Done

    @Test
    public void loadFXOnOpponents() {
        CardConstructor cardConstructor = new CardConstructor();

        //Checking if Apollo's fx are generated correctly
        List<TurnSequenceModifier> fx = cardConstructor.cards().get(0).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if Artemis' fx are generated correctly
        fx = cardConstructor.cards().get(1).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if Athena's fx are generated correctly
        fx = cardConstructor.cards().get(2).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if Atlas' fx are generated correctly
        fx = cardConstructor.cards().get(3).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if Demeter's fx are generated correctly
        fx = cardConstructor.cards().get(4).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if Hephaestus' fx are generated correctly
        fx = cardConstructor.cards().get(5).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if Minotaur's fx are generated correctly
        fx = cardConstructor.cards().get(6).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if Pan's fx are generated correctly
        fx = cardConstructor.cards().get(7).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if Prometheus' fx are generated correctly
        fx = cardConstructor.cards().get(8).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if Ares' fx are generated correctly
        fx = cardConstructor.cards().get(9).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if Chronus' fx are generated correctly
        fx = cardConstructor.cards().get(10).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if Hestia's fx are generated correctly
        fx = cardConstructor.cards().get(11).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if Poseidon's fx are generated correctly
        fx = cardConstructor.cards().get(12).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));

        //Checking if Zeus' fx are generated correctly
        fx = cardConstructor.cards().get(13).effectOnOpponent();

        for (TurnSequenceModifier action: fx)
            assertTrue(action.getClass().isInstance(new DoNothing()));
    }
    //Done

    @Test
    public void createCard() {
        CardConstructor cardConstructor = new CardConstructor();

        //Generating Apollo
        GodCard godCard = cardConstructor.cards().get(0);

        assertEquals("Apollo", godCard.name());
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

        //Generating Artemis
        godCard = cardConstructor.cards().get(1);

        assertEquals("Artemis", godCard.name());
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

        //Generating Athena
        godCard = cardConstructor.cards().get(2);

        assertEquals("Athena", godCard.name());
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

        //Generating Atlas
        godCard = cardConstructor.cards().get(3);

        assertEquals("Atlas", godCard.name());
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

        //Generating Demeter
        godCard = cardConstructor.cards().get(4);

        assertEquals("Demeter", godCard.name());
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

        //Generating Hephaestus
        godCard = cardConstructor.cards().get(5);

        assertEquals("Hephaestus", godCard.name());
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

        //Generating Minotaur
        godCard = cardConstructor.cards().get(6);

        assertEquals("Minotaur", godCard.name());
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

        //Generating Pan
        godCard = cardConstructor.cards().get(7);

        assertEquals("Pan", godCard.name());
        assertEquals(9, godCard.id());

        for (TurnSequenceModifier action: godCard.actions())
            assertTrue(action.getClass().isInstance(new DoNothing()));

        assertTrue(godCard.winCondition().getClass().isInstance(new MoveTwoLevelsDownWin()));
        assertTrue(godCard.setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        for (TurnSequenceModifier effect: godCard.effectOnOpponent())
            assertTrue(effect.getClass().isInstance(new DoNothing()));

        //Generating Prometheus
        godCard = cardConstructor.cards().get(8);

        assertEquals("Prometheus", godCard.name());
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

        //Generating Ares
        godCard = cardConstructor.cards().get(9);

        assertEquals("Ares", godCard.name());
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

        //Generating Chronus
        godCard = cardConstructor.cards().get(10);

        assertEquals("Chronus", godCard.name());
        assertEquals(16, godCard.id());

        for (TurnSequenceModifier action: godCard.actions())
            assertTrue(action.getClass().isInstance(new DoNothing()));

        assertTrue(godCard.winCondition().getClass().isInstance(new TowerCountWin(5)));
        assertTrue(godCard.setUpCondition().getClass().isInstance(new NoSetUpCondition()));

        for (TurnSequenceModifier effect: godCard.effectOnOpponent())
            assertTrue(effect.getClass().isInstance(new DoNothing()));

        //Generating Hestia
        godCard = cardConstructor.cards().get(11);

        assertEquals("Hestia", godCard.name());
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

        //Generating Poseidon
        godCard = cardConstructor.cards().get(12);

        assertEquals("Poseidon", godCard.name());
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

        //Generating Zeus
        godCard = cardConstructor.cards().get(13);

        assertEquals("Zeus", godCard.name());
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