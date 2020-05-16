package it.polimi.ingsw.server.model;

import java.util.List;

public interface SetUpCondition {

    public void modifySetUpOrder(Player currentPlayer, List<Player> players);

    public List<Box> applySetUpCondition(Player currentPlayer, List<Box> possibleBoxes);
}
