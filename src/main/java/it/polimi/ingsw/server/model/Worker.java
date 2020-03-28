package it.polimi.ingsw.server.model;

public class Worker {
    private Box position;
    private boolean gender;

    public Worker(Box position) {
        this.position = position;
    }
    public Worker(boolean gender, Box position){
        this.gender=gender;
        this.position=position;
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
     * This method changes the position of a worker
     * @param destination The new value of the worker's position
     */
    public void move(Box destination)
    {
        if (!destination.hasDome() && destination.level() <= 3)
            setPosition(destination);
    }

}


