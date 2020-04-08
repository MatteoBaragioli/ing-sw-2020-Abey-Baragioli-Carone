package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Player {
    private String nickname;
    private Colour colour;
    private List<Worker> workers;
    private GodCard card;
    private TurnSequence turnSequence;
    private boolean inGame=true;


    public Player(String nickname, Colour colour, List<Worker> workers, GodCard card, TurnSequence turnSequence) {
        this.nickname = nickname;
        this.colour = colour;
        this.workers = workers;
        this.card = card;
        this.turnSequence = turnSequence;
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

    private void setTurnSequence (TurnSequence turnSequence){this.turnSequence= turnSequence; }

    public TurnSequence turnSequence() { return turnSequence; }

    public void giveCard(GodCard card) {
        setCard(card);
    }

    public List<Worker> workers() {  //method that returns the player's workers for usage outside the 'player' class
        return getWorkers();
    }

    public GodCard godCard() {
        return card;
    }

    public boolean isInGame(){
        return this.inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public void playerIsOver(){
        setInGame(false);
    }

}