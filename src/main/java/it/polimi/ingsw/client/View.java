package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.*;


import java.util.List;

public interface View {
    public int askBox(List<Box> boxes);
    public int askWorker(List<Worker> workers);
    public int askCards(List<GodCard> cards);
}
