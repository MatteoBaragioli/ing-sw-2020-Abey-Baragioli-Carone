package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Player {
    private String nickname;
    private Colour colour;
    private List<Worker> workers;
    private GodCard card;

    public Player(String nickname, Colour colour, List<Worker> workers, GodCard card) {
        this.nickname = nickname;
        this.colour = colour;
        this.workers = workers;
        this.card = card;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    private List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }

    private GodCard getCard() {
        return card;
    }

    private void setCard(GodCard card) {
        this.card = card;
    }


    public void giveCard(GodCard card) {
        setCard(card);
    }

    public List<Worker> Workers() {  //method that returns the player's workers for usage outside the 'player' class
        List<Worker> workers = getWorkers();
        return workers;
    }

    public GodCard godCard() {
        return card;
    }

    public List<Player> getOpponents(List<Player> gamePlayers) {
        List<Player> opponents = new ArrayList<>();
        Player target;
        ListIterator<Player> iterator = gamePlayers.listIterator();
        while (iterator.hasNext()) {
            target=iterator.next();
            if(!target.equals(this)){
                opponents.add(target);
            }
        }
        return opponents;
    }
}