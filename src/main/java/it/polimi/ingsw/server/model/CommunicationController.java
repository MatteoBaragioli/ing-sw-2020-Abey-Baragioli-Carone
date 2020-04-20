package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CommunicationController {

    public Worker chooseWorker(List<Worker> movableWorkers){
        return null;
    }

    public Box chooseBox(Player currentPlayer, List<Box> chosableBoxes){
        //todo if chosableBox.isEmpty();
        return chosableBoxes.get(new Random().nextInt(chosableBoxes.size()));
    }

    public void youLost(){
        System.out.println("Hai perso");
    }

    public boolean chooseToUsePower(){
        return true;
    }

    public ProtoCard chooseProtoCard(Player challenger, List<ProtoCard> deckProtocards){
        //todo chiedo a utente una protocarta
        int i = new Random().nextInt(deckProtocards.size());
        return deckProtocards.get(i);
    }

    public void updateView(List<Player> players, Map map) {
        //todo view communication to each player
    }

    public GodCard chooseCard(Player player, List<GodCard> cards){
        //todo chiedo a utente una carta
        int i = new Random().nextInt(cards.size());
        return cards.get(i);
    }

    public int chooseFirstPlayer(List<Player> players){
        //todo chiedere a players.get(0) chi inizia
        int index = 1; //todo index = what the Challenger chooses
        return index;
    }

    public boolean confirmPhase(){
        int i = new Random().nextInt(1);
        if(i==0)
            return false;
        return true;
    }
}
