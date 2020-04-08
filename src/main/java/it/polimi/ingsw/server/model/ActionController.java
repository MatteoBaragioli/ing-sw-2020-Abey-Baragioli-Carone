package it.polimi.ingsw.server.model;

import java.util.List;

public class ActionController {

    public ActionController() {
    }

    /**
     * This method updates the new worker's position in currentTurnSequence
     * @param currentTurnSequence Current player's TurnSequence
     *
     */
    public void updateNewPositions(TurnSequence currentTurnSequence){
        currentTurnSequence.recordNewPosition(currentTurnSequence.chosenWorker(), currentTurnSequence.chosenBox());
    }

    /**
     * This method updates the building position in currentTurnSequence
     * @param currentTurnSequence Current player's TurnSequence
     *
     */
    public void updateBuiltOnBox(TurnSequence currentTurnSequence){
        currentTurnSequence.chosenBox().build();
        currentTurnSequence.recordBuiltOnBox(currentTurnSequence.chosenBox());
    }

    /**
     * This method adds a box in PossibleDestinations list if it's possible for the chosen worker to move on it
     * @param currentTurnSequence Current player's TurnSequence
     * @param map Map of the match
     */

    public void initialisePossibleDestinations(TurnSequence currentTurnSequence, Map map){
        currentTurnSequence.clearPossibleDestinations();
        Box workerBox = currentTurnSequence.workersCurrentPosition(currentTurnSequence.chosenWorker());
        for(Box box : map.adjacent(workerBox)){
            if(box.isFree() && map.levelDifference(workerBox, box)<=currentTurnSequence.allowedLevelDifference())
                currentTurnSequence.addPossibleDestination(box);
        }
    }

    /**
     * This method adds a box in PossibleBuilds list if it's possible for the chosen worker to build on it
     * @param currentTurnSequence Current player's TurnSequence
     * @param map Map of the match
     */

    public void initialisePossibleBuilds(TurnSequence currentTurnSequence, Map map){
        currentTurnSequence.clearPossibleBuilds();
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
     * @param map
     */
//todo implementare il metodo in modo che usando establish winCondition mi restituisca un giocatore, che sarà il giocatore vincente;
    public void verifyWinCondition(Phase phase, List<WinCondition> winConditions, Player currentPlayer, Map map){
        for(WinCondition currentWinCondition : winConditions){
            if(phase.equals(currentWinCondition.phase())) {
                switch (currentWinCondition.target()){
                    case ALL:
                        currentWinCondition.establishWinCondition(currentPlayer, map);
                        break;
                    case SELF:
                        if (currentPlayer.godCard().winCondition().equals(currentWinCondition)) {
                            currentWinCondition.establishWinCondition(currentPlayer, map);
                        }
                        break;
                    case OPPONENT:
                        if (!currentPlayer.godCard().winCondition().equals(currentWinCondition)) {
                            currentWinCondition.establishWinCondition(currentPlayer, map);
                        }
                }
            }
        }
    }


}
