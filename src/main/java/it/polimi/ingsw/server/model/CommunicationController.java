package it.polimi.ingsw.server.model;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Random;

public class CommunicationController {
    private Map <Player, User> playerToSocket = new HashMap<>();

    public CommunicationController() {
    }

    public CommunicationController(List<User>users, List<Player> gamePlayers){
        for(int i=0; i<gamePlayers.size();i++){
            playerToSocket.put(gamePlayers.get(i), users.get(i));
        }
    }

    public Worker chooseWorker(Player chooser, List<Worker> movableWorkers){
        return movableWorkers.get(new Random().nextInt(movableWorkers.size()));
    }

    public Box chooseBox(Player chooser, List<Box> chosableBoxes){
        //todo if chosableBox.isEmpty();
        return chosableBoxes.get(new Random().nextInt(chosableBoxes.size()));
    }

    public void youLost(Player loser){
        System.out.println("Hai perso");
    }

    public boolean chooseToUsePower(Player chooser){
        return true;
    }

    public void updateView(List<Player> players, Map map) {
        //todo view communication to each player
    }

    public GodCard chooseCard(Player chooser, List<GodCard> cards){
        //todo chiedo a utente una carta
        int i = new Random().nextInt(cards.size());
        return cards.get(i);
    }

    public int chooseFirstPlayer(Player chooser, List<Player> players){
        //todo chiedere a players.get(0) chi inizia
        int index = 1; //todo index = what the Challenger chooses
        return index;
    }

    public boolean confirmPhase(){
        return new Random().nextBoolean();
    }
}
