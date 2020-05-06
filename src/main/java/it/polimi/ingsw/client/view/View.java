package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.*;


import java.util.List;

public interface View {
    int askBox(List<Box> boxes);
    int askWorker(List<Worker> workers);
    int askCards(List<GodCard> cards);
    boolean askConfirmation();
    void prepareAdditionalCommunication(String message);
    void updateMap(List<Box> boxes);
    String askIp();
    int askPort();
}
