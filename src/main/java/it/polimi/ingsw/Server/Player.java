package it.polimi.ingsw.Server;

import java.util.ArrayList;

public class Player {
    private String nickname;
    private Colour colour;
    private ArrayList<Worker> workers;
    private ArrayList<Player> opponents;
    private GodCard card;
    private int allowedLevelDifference = 1;

    public Player(String nickname, Colour colour, ArrayList<Worker> workers, GodCard card) {
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

    public ArrayList<Player> getOpponents() {
        return opponents;
    }

    public void setOpponents(ArrayList<Player> opponents) {
        this.opponents = opponents;
    }

    public ArrayList<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(ArrayList<Worker> workers) {
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
}