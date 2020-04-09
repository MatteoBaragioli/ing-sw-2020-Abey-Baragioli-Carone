package it.polimi.ingsw.server.model;

import java.util.List;

public class CommunicationController {

    public Worker chooseWorker(Map map){
        return new Worker(map.position(1,1));
    }

    public Box chooseBox(Map map){
        return map.position(3,4);
    }

    public void youLost(){
        System.out.println("Hai perso");
    }

    public boolean chooseToUsePower(){
        return true;
    }
}
