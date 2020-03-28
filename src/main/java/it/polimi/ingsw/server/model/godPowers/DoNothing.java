package it.polimi.ingsw.server.model.godPowers;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.TurnSequenceModifier;
import it.polimi.ingsw.server.model.Worker;

public class DoNothing implements TurnSequenceModifier {

    @Override
    public void executeAction(Worker worker, Player player){
    }
}
