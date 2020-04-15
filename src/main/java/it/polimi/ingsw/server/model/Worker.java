package it.polimi.ingsw.server.model;

public class Worker {
    private Box position;
    private boolean gender;
    private Colour colour;

    public Worker(Box position, Colour colour) {
        this.position = position;
        this.position.occupy(this);
    }
    public Worker(boolean gender, Box position){
        this.gender=gender;
        this.position=position;
        this.position.occupy(this);
    }

    public Colour colour(){return this.colour;}

    private void SetColour(Colour colour){this.colour=colour;}

    private Box getPosition() {
        return position;
    }

    private void setPosition(Box position) {
        this.position = position;
    }

    public Box position(){
        return getPosition();
    }

    /**
     * This method changes the position of a worker and sets it as the occupier of the destination
     * @param destination The new value of the worker's position
     */

    public void move(Box destination) {
        if (!destination.hasDome()  && destination.level() <= 3 && this.equals(destination.occupier()))
            setPosition(destination);
    }
}


