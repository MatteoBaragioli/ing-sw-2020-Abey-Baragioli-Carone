package it.polimi.ingsw.server.model;

import java.util.List;

import static it.polimi.ingsw.server.model.Phase.*;

public class ActionController {
    public ActionController() {
    }

    /**
     * This method updates the new worker's position in currentTurnSequence
     * @param currentTurnSequence Current player's TurnSequence
     *
     */
    public void updateNewPositions(TurnSequence currentTurnSequence){
        if(currentTurnSequence.chosenBox()!=null)
            currentTurnSequence.recordNewPosition(currentTurnSequence.chosenWorker(), currentTurnSequence.chosenBox());
    }

    /**
     * This method updates the building position in currentTurnSequence
     * @param currentTurnSequence Current player's TurnSequence
     *
     */
    public void updateBuiltOnBox(TurnSequence currentTurnSequence) {
        if (currentTurnSequence.chosenBox() != null) {
            currentTurnSequence.chosenBox().build();
            currentTurnSequence.recordBuiltOnBox(currentTurnSequence.chosenBox());
        }
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
        Box workerBox = currentTurnSequence.workersCurrentPosition(currentTurnSequence.chosenWorker());
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
     * @param map Map of the match
     * @param opponents List of opponents
     */

    public void verifyWinCondition(Phase phase, List<WinCondition> winConditions, Player currentPlayer, Map map, List<Player> opponents) {
        if (phase.equals(MOVE)) {
            currentPlayer.turnSequence().registerPossibleWinner(verifyStandardWin(currentPlayer, map));
        }
        if (currentPlayer.godCard().winCondition()!=null && currentPlayer.turnSequence().possibleWinner()== null) {
            for (WinCondition currentWinCondition : winConditions) {
                if (phase.equals(currentWinCondition.phase()) && currentPlayer.turnSequence().possibleWinner() == null) {
                    switch (currentWinCondition.target()) {
                        case ALL: //if this win condition applies to all the player's phases (such as chronus' win condition)
                            if (currentWinCondition.establishWinCondition(currentPlayer, map)) {
                                if (currentWinCondition.equals(currentPlayer.godCard().winCondition()))
                                    currentPlayer.turnSequence().registerPossibleWinner(currentPlayer);
                                else {
                                    for (Player opponent : opponents) {
                                        if (currentWinCondition.equals(opponent.godCard().winCondition()))
                                            currentPlayer.turnSequence().registerPossibleWinner(opponent);
                                    }
                                }
                            }
                            break;
                        case SELF:
                            if (currentWinCondition.equals(currentPlayer.godCard().winCondition())) {
                                if (currentWinCondition.establishWinCondition(currentPlayer, map))
                                    currentPlayer.turnSequence().registerPossibleWinner(currentPlayer);
                            }
                            break;
                        case OPPONENT:
                            if (!currentWinCondition.equals(currentPlayer.godCard().winCondition())) {
                                if (currentWinCondition.establishWinCondition(currentPlayer, map))
                                    for (Player opponent : opponents) {
                                        if (currentWinCondition.equals(opponent.godCard().winCondition()))
                                            currentPlayer.turnSequence().registerPossibleWinner(opponent);
                                    }
                            }
                    }
                }
            }
        }
    }

    /**
     * This method verifies if the standard win condition occurs given the current player and the map
     * @param currentPlayer Current player
     * @param map Map of the match
     * @return This method returns the current player if he's the winner or null if not
     */

    public Player verifyStandardWin(Player currentPlayer, Map map){
        WinCondition standardWin=new StandardWin();
        if(standardWin.establishWinCondition(currentPlayer, map))
            return currentPlayer;
        return null;
    }

    /**
     * This method tells if the current player has won
     * @param currentPlayer Current player
     * @return Boolean returns true if the current player has won
     */
    public boolean currentPlayerHasWon(Player currentPlayer){
        return currentPlayer.equals(currentPlayer.turnSequence().possibleWinner());

    }

    /**
     * Given a player this method establishes which one of their workers can be moved during their turn
     * @param currentPlayer Current player
     * @param map Map of the match
     */
    public void initialiseMovableWorker(Player currentPlayer, Map map){
        for(Worker worker : currentPlayer.workers()){
            currentPlayer.turnSequence().setChosenWorker(worker);
            initialisePossibleDestinations(currentPlayer.turnSequence(), map);
            if(!currentPlayer.turnSequence().possibleDestinations().isEmpty())
                currentPlayer.turnSequence().addMovableWorker(worker);
            currentPlayer.turnSequence().resetChosenWorker();
        }
    }

    /**
     * This method tells if the player is able play a normal turn
     * @param player Examined player
     * @param opponents Examined player's opponents
     * @param map Map of the match
     * @return Boolean that is true if player is able to play a normal turn
     */
    public boolean canPlayerPlay(Player player, List<Player> opponents, Map map) {
        initialiseMovableWorker(player, map);
        applyOpponentsCondition(player, opponents, 0,map);
        player.godCard().actions().get(0).changePossibleOptions(player, this, map);
        if (!player.turnSequence().movableWorkers().isEmpty())
            for (Worker worker: player.turnSequence().movableWorkers()) {
                player.turnSequence().setChosenWorker(worker);
                initialisePossibleDestinations(player.turnSequence(), map);
                applyOpponentsCondition(player, opponents, 1, map);
                player.godCard().actions().get(1).changePossibleOptions(player, this, map);// per poteri aumentano le possible destinations
                if (!player.turnSequence().possibleDestinations().isEmpty()) {
                    for (Box box:player.turnSequence().possibleDestinations()) {
                        player.turnSequence().setChosenBox(box);
                        initialisePossibleBuilds(player.turnSequence(), map);
                        applyOpponentsCondition(player, opponents, 2, map);
                        player.godCard().actions().get(2).changePossibleOptions(player, this, map); // per poteri che aumentano le possible buids (zeus)
                        if (!player.turnSequence().possibleBuilds().isEmpty()) {
                            player.turnSequence().resetChosenWorker();
                            player.turnSequence().possibleBuilds().clear();
                            player.turnSequence().possibleDestinations().clear();
                            return true;
                        }
                    }
                }
            }
        player.turnSequence().resetChosenWorker();
        player.turnSequence().possibleBuilds().clear();
        player.turnSequence().possibleDestinations().clear();
        return false;
    }
}
