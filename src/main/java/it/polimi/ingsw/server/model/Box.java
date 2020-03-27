package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Worker;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Box {
    private int level=0;
    private boolean dome=false;
    private int[] position = new int[2];

    public Box(int[] position) {
        this.position = position;
    }
    public Box(int level, boolean dome, int[] position) {
        this.level = level;
        this.dome = dome;
        this.position = position;
    }

    public int Level() {
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

    public int[] Position() {
        return position;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    public int level() {
        return this.level();
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

    /**
     * this method tells if a box is occupied by any worker in the game or a dome
     * @param players list of all players in the game
     * @return boolean (true if occupied, false otherwise)
     */
    boolean isOccupied(List<Player> players){
        return !this.isFree(players);
    }

    /**
     * this method tells if a box is occupied by any of the workers of the players passed as parameter
     * @param players list of players
     * @return boolean (true if occupied)
     */
    boolean isOccupiedByWorkers(List<Player> players) {
        ListIterator<Player> iterator = players.listIterator();
        Iterator<Worker> workersIterator;
        while (iterator.hasNext()) {
            workersIterator = iterator.next().getWorkers().iterator();
            //if the player has workers in game, look at their position
            while (workersIterator.hasNext())
                if (workersIterator.next().getPosition().equals(this))
                    return true;

        }
        return false;
    }

    /**
     * this method increases the level of the box or sets the dome attribute of box to true
     */
    public void build()
    {
        if (!hasDome())
            if (level() < 3 && level()>0)
                buildBlock();
            else
                buildDome();
    }

    /**
     * this method increases the level attribute of one
     */
    private void buildBlock()
    {
        setLevel(level() + 1);
    }

    /**
     * this method sets the dome attribute of the box to true
     */
    private void buildDome()
    {
        setDome(true);
    }

    /**
     * this method decreases the level attribute of 1
     */
    public void removeBlock() {
        if (level()>0 && !hasDome())
            setLevel(level() - 1);
    }

    /**
     * this method tells if the box is on the edge of the board
     * @return boolean (true if on edge)
     */
    public boolean isOnEdge(){
        if (position[0]==0 || position[1]==0 || position[0]==4 ||position[1]==4)
            return true;
        return false;
    }
}
