package it.polimi.ingsw.Server;

import java.util.ArrayList;

public class Player {
    private String nickname;
    private Colour colour;
    private ArrayList<Worker> workers;

    public Player(String nickname) {
        this.nickname = nickname;
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

}