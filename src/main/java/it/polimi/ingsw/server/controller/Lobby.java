package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.Match;
import it.polimi.ingsw.server.model.User;

import java.util.ArrayList;
import java.util.List;

public class Lobby extends Thread{
    final private int nPlayers;
    private List<User> users = new ArrayList<>();
    boolean readyToGo=false;

    public int nPlayers() {
        return nPlayers;
    }

    public List<User> users() {
        return users;
    }

    public Lobby(int nPlayers, User firstPlayer){
        this.nPlayers = nPlayers;
        this.users.add(firstPlayer);
    }

    public void run(){
        while(!readyToGo){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Match Match=new Match(users);
    }

}
