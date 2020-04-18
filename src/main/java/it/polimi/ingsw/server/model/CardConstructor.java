package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.server.model.godPowers.*;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardConstructor {
    private List<ProtoCard> protoCards = loadCardsFromFile();


    public List<ProtoCard> protoCards() {
        return protoCards;
    }

    /**
     * This method loads a Protocard List from a file
     * @return List of Protocards
     */
    public List<ProtoCard> loadCardsFromFile(){
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
    public Map<String, TurnSequenceModifier> loadFX() {
        Map<String, TurnSequenceModifier> fx = new HashMap<>();
        fx.put("DoNothing", new DoNothing());
        fx.put("Swap", new SwapPower());
        fx.put("AddMoveNotStartingBox", new AddMoveNotStartingBoxPower());
        fx.put("OpponentsCantMoveUpIfPlayerMovesUp", new OpponenentsCantMoveUpIfPlayerMovesUpPower());
        fx.put("BuildDomeEverywhere", new BuildDomeEverywherePower());
        fx.put("AddBuildNotSameBox", new AddBuildNotSameBoxPower());
        fx.put("AddBuildOnSameBox", new AddBuildOnSameBoxPower());
        fx.put("PushAdjacentOpponent", new PushAdjacentOpponentPower());
        fx.put("AddBuildBeforeIfNotMoveUp", new AddBuildBeforeMoveIfNotMoveUpPower());
        fx.put("RemoveAdjacentBlock", new RemoveAdjacentBlockPower());
        fx.put("AddBuildNotEdge", new AddBuildNotEdgePower());
        fx.put("AddThreeBuildsToUnmovedWorker", new AddThreeBuildsToUnmovedWorkerIfOnGroundPower());
        fx.put("BuildUnderYourself", new BuildUnderYourselfPower());

        return fx;
    }

    /**
     * This method returns the actions needed to create the card
     * @param protoCard the card I need to create
     * @return List of TurnSequenceModifiers
     */
    public List<TurnSequenceModifier> loadActions(ProtoCard protoCard) {
        List<TurnSequenceModifier> actions = new ArrayList<>();
        Map<String, TurnSequenceModifier> powers = loadFX();

        for (String power: protoCard.actions())
            actions.add(powers.get(power));

        return actions;
    }

    /**
     * This method returns the WinCondition needed to create the card
     * @param protoCard the card I need to create
     * @return WinCondition
     */
    public WinCondition loadWinCondition(ProtoCard protoCard) {
        Map<String, WinCondition> winConditions = new HashMap<>();
        winConditions.put("Standard", null);
        winConditions.put("MoveTwoLevelsDown", new MoveTwoLevelsDownWin());
        winConditions.put("TowerCountWin", new TowerCountWin(protoCard.winParameter()));
        return winConditions.get(protoCard.winCondition());
    }

    /**
     * This method returns the SetUpCondition needed to create the card
     * @param protoCard the card I need to create
     * @return SetUpCondition
     */
    public SetUpCondition loadSetUpCondition(ProtoCard protoCard) {
        Map<String, SetUpCondition> setUpConditions = new HashMap<>();
        setUpConditions.put("NoSetUp", new NoSetUpCondition());
        return setUpConditions.get(protoCard.setUpCondition());
    }

    /**
     * This method returns the opponent actions needed to create the card
     * @param protoCard the card I need to create
     * @return List of TurnSequenceModifiers
     */
    public List<TurnSequenceModifier> loadFXOnOpponents(ProtoCard protoCard) {
        List<TurnSequenceModifier> fx = new ArrayList<>();
        Map<String, TurnSequenceModifier> effects = loadFX();

        for (String power: protoCard.fxOnOpponent())
            fx.add(effects.get(power));

        return fx;
    }

    /**
     * This method generates a GodCard from a Protocard
     * @param protoCard the Protocard I want to create
     * @return GodCard
     */
    public GodCard createCard(ProtoCard protoCard) {
        return new GodCard(protoCard.name(), protoCard.id(), loadActions(protoCard), loadWinCondition(protoCard), loadSetUpCondition(protoCard), loadFXOnOpponents(protoCard));
    }
}
