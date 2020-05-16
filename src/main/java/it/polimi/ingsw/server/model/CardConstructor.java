package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.server.model.godPowers.fx.*;
import it.polimi.ingsw.server.model.godPowers.setUpConditions.GodSetup;
import it.polimi.ingsw.server.model.godPowers.setUpConditions.NoSetUpCondition;
import it.polimi.ingsw.server.model.godPowers.winConditions.GodWin;
import it.polimi.ingsw.server.model.godPowers.winConditions.MoveTwoLevelsDownWin;
import it.polimi.ingsw.server.model.godPowers.winConditions.TowerCountWin;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.server.model.godPowers.fx.GodFX.*;
import static it.polimi.ingsw.server.model.godPowers.setUpConditions.GodSetup.*;
import static it.polimi.ingsw.server.model.godPowers.winConditions.GodWin.*;

public class CardConstructor {
    private List<GodCard> cards = loadCards();

    public List<GodCard> cards() {
        return cards;
    }

    /**
     * This method loads the ProtoCard List from a file
     * @return List of ProtoCards
     */
    public List<ProtoCard> loadProtoCardsFromFile(){
        String filename = "src" + File.separator + "main" + File.separator + "resources"+ File.separator + "GodCards.json";
        List<ProtoCard> protoCards = null;
        try {
            Type listType = new TypeToken<List<ProtoCard>>() {}.getType();
            protoCards = new Gson().fromJson(new FileReader(filename), listType);
            String jsonObject = new Gson().toJson(protoCards);
            protoCards = new Gson().fromJson(jsonObject, listType);
        } catch (Exception FileNotFoundException) {
            //
        }
        return protoCards;
    }

    /**
     * This method loads the fx used by the protocards identifying them by their name
     * @return HashMap
     */
    public Map<GodFX, TurnSequenceModifier> loadFX() {
        Map<GodFX, TurnSequenceModifier> fx = new HashMap<>();
        fx.put(DO_NOTHING, new DoNothing());
        fx.put(SWAP, new SwapPower());
        fx.put(ADD_MOVE_NOT_STARTING_BOX, new AddMoveNotStartingBoxPower());
        fx.put(OPPONENTS_CANT_MOVE_UP_IF_PLAYER_MOVES_UP, new OpponenentsCantMoveUpIfPlayerMovesUpPower());
        fx.put(BUILD_DOME_EVERYWHERE, new BuildDomeEverywherePower());
        fx.put(ADD_BUILD_NOT_SAME_BOX, new AddBuildNotSameBoxPower());
        fx.put(ADD_BUILD_ON_SAME_BOX, new AddBuildOnSameBoxPower());
        fx.put(PUSH_ADJACENT_OPPONENT, new PushAdjacentOpponentPower());
        fx.put(ADD_BUILD_BEFORE_IF_NOT_MOVE_UP, new AddBuildBeforeMoveIfNotMoveUpPower());
        fx.put(REMOVE_ADJACENT_BLOCK, new RemoveAdjacentBlockPower());
        fx.put(ADD_BUILD_NOT_EDGE, new AddBuildNotEdgePower());
        fx.put(ADD_THREE_BUILDS_TO_UNMOVED_WORKER, new AddThreeBuildsToUnmovedWorkerIfOnGroundPower());
        fx.put(BUILD_UNDER_YOURSELF, new BuildUnderYourselfPower());

        return fx;
    }

    /**
     * This method returns the actions needed to create the card
     * @param protoCard the card I need to create
     * @return List of TurnSequenceModifiers
     */
    public List<TurnSequenceModifier> loadActions(ProtoCard protoCard) {
        List<TurnSequenceModifier> actions = new ArrayList<>();
        Map<GodFX, TurnSequenceModifier> powers = loadFX();

        for (GodFX power: protoCard.actions())
            actions.add(powers.get(power));

        return actions;
    }

    /**
     * This method returns the WinCondition needed to create the card
     * @param protoCard the card I need to create
     * @return WinCondition
     */
    public WinCondition loadWinCondition(ProtoCard protoCard) {
        Map<GodWin, WinCondition> winConditions = new HashMap<>();
        winConditions.put(STANDARD, null);
        winConditions.put(MOVE_TWO_LEVELS_DOWN, new MoveTwoLevelsDownWin());
        winConditions.put(TOWER_COUNT, new TowerCountWin(protoCard.winParameter()));
        return winConditions.get(protoCard.winCondition());
    }

    /**
     * This method returns the SetUpCondition needed to create the card
     * @param protoCard the card I need to create
     * @return SetUpCondition
     */
    public SetUpCondition loadSetUpCondition(ProtoCard protoCard) {
        Map<GodSetup, SetUpCondition> setUpConditions = new HashMap<>();
        setUpConditions.put(NO_SETUP, new NoSetUpCondition());
        return setUpConditions.get(protoCard.setUpCondition());
    }

    /**
     * This method returns the opponent actions needed to create the card
     * @param protoCard the card I need to create
     * @return List of TurnSequenceModifiers
     */
    public List<TurnSequenceModifier> loadFXOnOpponents(ProtoCard protoCard) {
        List<TurnSequenceModifier> fx = new ArrayList<>();
        Map<GodFX, TurnSequenceModifier> effects = loadFX();

        for (GodFX power: protoCard.fxOnOpponent())
            fx.add(effects.get(power));

        return fx;
    }

    /**
     * This method generates a GodCard from a Protocard
     * @param protoCard the Protocard I want to create
     * @return GodCard
     */
    public GodCard createCard(ProtoCard protoCard) {
        return new GodCard(protoCard.name(), protoCard.id(), loadActions(protoCard), loadWinCondition(protoCard), loadSetUpCondition(protoCard), loadFXOnOpponents(protoCard), protoCard.description(), protoCard.winDescription(), protoCard.setUpDescription(), protoCard.opponentsFxDescription());
    }

    /**
     * This method loads the GodCard deck from a file
     * @return List of GodCards
     */
    public List<GodCard> loadCards() {
        List<GodCard> cards = new ArrayList<>();
        List<ProtoCard> protoCards = loadProtoCardsFromFile();
        for (ProtoCard protoCard: protoCards)
            cards.add(createCard(protoCard));
        return cards;
    }
}
