package it.polimi.ingsw.server.model;

public class Worker {
    private Box position;
    private boolean gender;

    public Worker(Box position) {
        this.position = position;
        this.position.occupy(this);
    }
    public Worker(boolean gender, Box position){
        this.gender=gender;
        this.position=position;
        this.position.occupy(this);
    }

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
        if (destination.isFree() && destination.level() <= 3) {
            position.removeOccupier();
            setPosition(destination);
            destination.occupy(this);
        }
    }

    /**
     * this method changes the position of a worker according to the allowedLevelDifference value
     * @param allowedLevelDifference
     * @param destination
     */
    public void levelledMove(int allowedLevelDifference, Box destination){
        if (destination.level()- position().level()<=allowedLevelDifference)
            move(destination);
    }

}


