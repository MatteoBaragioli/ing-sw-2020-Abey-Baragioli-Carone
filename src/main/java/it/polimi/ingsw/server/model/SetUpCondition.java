package it.polimi.ingsw.server.model;

import java.util.List;

public interface SetUpCondition {

    /**
     * This method changes the players order for set up based on the power
     * @param currentPlayer Current player
     * @param players All players of the match
     */
    void modifySetUpOrder(Player currentPlayer, List<Player> players);

    /**
     * This method adds or removes possible destination for the stating box of the workers based on the power
     * @param currentPlayer Current player
     * @param possibleBoxes Possible destinations
     * @return List of possible destination based on the power
     */
    List<Box> applySetUpCondition(Player currentPlayer, List<Box> possibleBoxes);
}
