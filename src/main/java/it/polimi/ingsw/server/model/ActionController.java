package it.polimi.ingsw.server.model;

import java.util.List;

public class ActionController {

    public ActionController() {
    }

    /**
     * This method updates the new worker's position in currentTurnSequence
     * @param currentTurnSequence Current player's TurnSequence
     * @param chosenBox Chosen worker's new position
     */
    public void updateNewPositions(TurnSequence currentTurnSequence, Box chosenBox){
        currentTurnSequence.recordNewPosition(currentTurnSequence.chosenWorker(), chosenBox);
    }

    /**
     * This method updates the building position in currentTurnSequence
     * @param currentTurnSequence Current player's TurnSequence
     * @param targetBox Chosen box for building
     */
    public void updateBuiltOnBox(TurnSequence currentTurnSequence, Box targetBox){
        currentTurnSequence.recordBuiltOnBox(targetBox);
    }

    /**
     * This method adds a box in PossibleDestinations list if it's possible for the chosen worker to move on it
     * @param currentTurnSequence Current player's TurnSequence
     * @param map Map of the match
     */

    public void initialisePossibleDestinations(TurnSequence currentTurnSequence, Map map){
        Box workerBox = currentTurnSequence.chosenWorker().position();
        for(Box box : map.adjacent(workerBox)){
            if(box.isFree()&&map.levelDifference(workerBox, box)<currentTurnSequence.allowedLevelDifference())
                currentTurnSequence.addPossibleDestination(box);
        }
    }


    public void initialisePossibleBuilds(TurnSequence currentTurnSequence, Map map){
        Box workerBox = currentTurnSequence.chosenWorker().position();
        for(Box box : map.adjacent(workerBox)){
            if(box.isFree())
                currentTurnSequence.addPossibleBuild(box);
        }
    }

    /**
     * This method calls changePossibleOptions method, based on current phase, for each opponents
     * @param currentPlayer Current player
     * @param opponents List of opponents
     * @param phaseIndex Index of the current phase
     * @param map Map of the match
     */

    public void applyOpponentsCondition(Player currentPlayer, List<Player> opponents, int phaseIndex, Map map){
        for(Player currentOpponent : opponents){
            currentOpponent.godCard().effectOnOpponent().get(phaseIndex).changePossibleOptions(currentPlayer, this, map);
        }
    }

    /**
     * This method checks if there are applicable win conditions and calls the win condition method
     * @param phase Current phase
     * @param winConditions List of all active win conditions
     * @param currentPlayer Current player
     */

    public void verifyWinCondition(Phase phase, List<WinCondition> winConditions, Player currentPlayer){
        for(WinCondition currentWinCondition : winConditions){
            if(phase.equals(currentWinCondition.phase())) {
                switch (currentWinCondition.target()){
                    case ALL:
                        currentWinCondition.establishWinCondition();
                        break;
                    case SELF:
                        if (currentPlayer.godCard().winCondition().equals(currentWinCondition)) {
                            currentWinCondition.establishWinCondition();
                        }
                        break;
                    case OPPONENT:
                        if (!currentPlayer.godCard().winCondition().equals(currentWinCondition)) {
                            currentWinCondition.establishWinCondition();
                        }
                }
            }
        }
    }


}
