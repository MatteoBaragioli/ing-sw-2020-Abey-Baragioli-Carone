package it.polimi.ingsw.Server;

public class Worker {
    private Box position;

    public Worker(Box position) {
        this.position = position;
    }

    public Box getPosition() {
        return position;
    }

    private void setPosition(Box position) {
        this.position = position;
    }

    /**
     * This method changes the position of a worker, if the
     * @param destination The new value of the worker's position
     */
    public void move(Box destination)
    {
        if (!destination.hasDome() && destination.level() <= 3)
            setPosition(destination);
    }

    /**
     * This method moves the worker to a new position that respects the level difference
     * @param destination new position
     */
    public void leveledMove(Box destination, int levelDifference)
    {
        if (destination.levelDifference(getPosition()) <= levelDifference)
            move(destination);
    }
}
