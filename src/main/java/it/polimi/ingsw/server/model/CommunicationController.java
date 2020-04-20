package it.polimi.ingsw.server.model;

import java.util.List;
import java.util.Random;

public class CommunicationController {

    public Worker chooseWorker(List<Worker> movableWorkers){
        return null;
    }

    public Box chooseBox(Player currentPlayer, List<Box> chosableBoxes){
        //todo if chosableBox.isEmpty();
        if (new Random().nextBoolean())
            return chosableBoxes.get(new Random().nextInt(chosableBoxes.size()));
        else
            return null;
    }

    public void youLost(){
        System.out.println("Hai perso");
    }

    public boolean chooseToUsePower(){
        return true;
    }

    public ProtoCard chooseCard(Player challenger, List<ProtoCard> deckProtocards){
        //todo chiedo a utente una carta
        int i = new Random().nextInt(deckProtocards.size());
        return deckProtocards.get(i);
    }

    public void updateView(List<Player> players, Map map) {
        //todo view communication to each player
    }
}
