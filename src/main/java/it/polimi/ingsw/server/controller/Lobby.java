package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.Match;
import it.polimi.ingsw.server.model.User;

import java.util.ArrayList;
import java.util.List;

public class Lobby extends Thread{
    final private int nPlayers;
    private List<User> users = new ArrayList<>();
    boolean readyToGo=false;

    public Lobby(User firstPlayer) {
        this.users.add(firstPlayer);
        nPlayers = firstPlayer.askTwoOrThreePlayerMatch();
    }

    public int nPlayers() {
        return nPlayers;
    }

    public List<User> users() {
        return users;
    }

    public boolean isFree() {
        return !readyToGo;
    }

    public void setReadyToGo(boolean readyToGo) {
        this.readyToGo = readyToGo;
    }

    public boolean isReady() {
        return users().size() == nPlayers();
    }

    public void close() {
        setReadyToGo(true);
    }

    public synchronized void addUser(User user) {
        if (!users().contains(user) && !isReady())
            users().add(user);
        notifyAll();
    }

    public void run(){
        while(!readyToGo){
            try {
                wait();
                if (isReady())
                    close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Match Match=new Match(users);
    }
}
