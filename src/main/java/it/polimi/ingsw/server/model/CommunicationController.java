package it.polimi.ingsw.server.model;

import java.util.List;

public class CommunicationController {

    public Worker chooseWorker(Map map){
        return new Worker(map.position(3,3));
    }

    public Box chooseBox(List<Box> chosableBoxes){
        if(chosableBoxes != null){
            return chosableBoxes.get(1);
        }
        else
            return null;
    }

    public void youLost(){
        System.out.println("Hai perso");
    }

    public boolean chooseToUsePower(){
        return true;
    }
}
