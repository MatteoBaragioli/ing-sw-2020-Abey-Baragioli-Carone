package it.polimi.ingsw.Server;

import java.util.ArrayList;

public class Player {
    private String nickname;
    private Colour colour;
    private ArrayList<Worker> workers;
    private GodCard card;

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

    public ArrayList<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(ArrayList<Worker> workers) {
        this.workers = workers;
    }

    public GodCard getCard() {
        return card;
    }

    public void setCard(GodCard card) {
        this.card = card;
    }
}