package it.polimi.ingsw.Server;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String nickname;
    private Colour colour;
    private List<Worker> workers;
    private List<Player> opponents;
    private GodCard card;
    private int allowedLevelDifference = 1;

    public Player(String nickname, Colour colour,List<Worker> workers, GodCard card) {
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

    public List<Player> getOpponents() {
        return opponents;
    }

    public void setOpponents(List<Player> opponents) {
        this.opponents = opponents;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }

    public GodCard getCard() {
        return card;
    }

    private void setCard(GodCard card) {
        this.card = card;
    }

    public int getAllowedLevelDifference() {
        return allowedLevelDifference;
    }

    public void setAllowedLevelDifference(int allowedLevelDifference) {
        this.allowedLevelDifference = allowedLevelDifference;
    }

    public void giveCard(GodCard card)
    {
        setCard(card);
    }

    public List<Worker> Workers(){ //method that returns the player's workers for usage outside the 'player' class
        List<Worker> workers = getWorkers();
        return workers;
    }

    public List<Player> opponents() {
        return getOpponents();
    }

}