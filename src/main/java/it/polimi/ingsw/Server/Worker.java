package it.polimi.ingsw.Server;

public class Worker {
    private Box position;

    public Worker(Box position) {
        this.position = position;
    }

    public Box getPosition() {
        return position;
    }

    public void setPosition(Box position) {
        this.position = position;
    }
}