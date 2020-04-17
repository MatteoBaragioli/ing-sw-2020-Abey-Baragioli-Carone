package it.polimi.ingsw.server.model;

import java.util.List;

public class CommunicationController {

    public Worker chooseWorker(List<Worker> movableWorkers){
        return null;
    }

    public Box chooseBox(Player currentPlayer, List<Box> chosableBoxes){
        //todo if chosableBox.isEmpty();
        return null;
    }

    public void youLost(){
        System.out.println("Hai perso");
    }

    public boolean chooseToUsePower(){
        return true;
    }
}
