package it.polimi.ingsw.Server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Box {
    private int level=0;
    private boolean dome=false;
    private int[] position = new int[2];
    private Box[] adjacentBoxes = new Box[8];

    public Box(int[] position) {
        this.position = position;
    }
    public Box(int level, boolean dome, int[] position) {
        this.level = level;
        this.dome = dome;
        this.position = position;
    }

    private int getLevel() {
        return level;
    }

    private void setLevel(int level) {
        this.level = level;
    }

    public boolean hasDome() {
        return dome;
    }

    private void setDome(boolean dome) {
        this.dome = dome;
    }

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    public Box[] getAdjacentBoxes() {
        return adjacentBoxes;
    }

    public void setAdjacentBoxes(Box[] adjacentBoxes) {
        this.adjacentBoxes = adjacentBoxes;
    }

    /**
     * This method tells if the Box is free (not occupied by neither workers nor domes).
     * @param players The list of all players
     * @return Boolean (true if free)
     */
    boolean isFree(List<Player> players){
        ListIterator<Player> iterator = players.listIterator();
        Iterator<Worker> workerIterator;

        //If the box has a dome it can't be free
        if (hasDome())
            return false;

        //If there is a player, look at his/her workers
        while (iterator.hasNext())
        {
            workerIterator = iterator.next().getWorkers().iterator();
            //if the player has workers in game, look at their position
            while (workerIterator.hasNext())
                if (workerIterator.next().getPosition().equals(this))
                    return false;
        }

        return true;
    }


    public int level() {
        return getLevel();
    }

    public void build()
    {
        if (!hasDome())
            if (getLevel() < 3)
                buildBlock();
            else
                buildDome();
    }

    private void buildBlock()
    {
        setLevel(getLevel() + 1);
    }

    private void buildDome()
    {
        setDome(true);
    }

    /**
     * This method finds the difference between this box and the input box's level
     * @param position the box to compare
     * @return Int
     */
    public int levelDifference(Box position)
    {
        return getLevel() - position.getLevel();
    }

    /**
     * this method finds the free boxes that are adjacent to this
     * @param gamePlayers
     * @return list of boxes
     */
    public List<Box> adjacentFree( List<Player> gamePlayers) {
        List<Box> free= new ArrayList();
        for(int i=0; i<getAdjacentBoxes().length; i++){
            if (getAdjacentBoxes()[i].isFree(gamePlayers))
                free.add(getAdjacentBoxes()[i]);
        }
        return free;
    }

    /**
     * this method finds the adjacent boxes that are occupied by opponent workers
     * @param gamePlayers
     * @return list of boxes
     */
    public List<Box> adjacentOpponent( List<Player> gamePlayers) {
        List<Box> occupied = new ArrayList();
        for (int i = 0; i < getAdjacentBoxes().length; i++) {
            if (!getAdjacentBoxes()[i].isFree(gamePlayers) && !getAdjacentBoxes()[i].hasDome())
                occupied.add(getAdjacentBoxes()[i]);
        }
        return occupied;
    }

}
