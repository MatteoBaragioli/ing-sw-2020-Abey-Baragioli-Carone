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
    private Worker occupier=null;

    public Box(int[] position) {
        this.position = position;
    }
    public Box(int level, boolean dome, int[] position) {
        this.level = level;
        this.dome = dome;
        this.position = position;
    }

    public int level() {
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

    public int[] position() {
        return position;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    public void occupy(Worker occupier){this.occupier=occupier;}

    public Worker Occupier(){ return occupier;}


    /**
     * This method tells if the Box is free (not occupied by neither workers nor domes).
     * @return Boolean (true if free)
     */
     public boolean isFree(){
        if (occupier==null || !hasDome())
            return true;
    return false;
    }

    /**
     * this method tells if a box is occupied by any worker in the game or a dome
     * @return boolean (true if occupied, false otherwise)
     */
    public boolean isOccupied(){
        return !this.isFree();
    }

    /**
     * this method tells if a box is occupied by any of the workers of the players
     * @return boolean (true if occupied)
     */
    public boolean isOccupiedByWorkers() {
        if(occupier==null)
            return false;
        return true;
    }

    /**
     * this method increases the level of the box or sets the dome attribute of box to true
     */
    public void build()
    {
        if (!hasDome())
            if (level() < 3 && level()>0)
                buildBlock();
            else if(level()>3)
                buildDome();
    }

    /**
     * this method increases the level of one
     */
    private void buildBlock()
    {
        setLevel(level() + 1);
    }

    /**
     * this method sets the dome attribute of the box to true
     */
    public void buildDome()
    {
        setDome(true);
    }

    /**
     * this method sets the dome attribute of the box to false
     */
    public void removeDome() {setDome(false); }

    /**
     * this method decreases the level attribute of 1
     */
    public void removeBlock() {
        if (level()>0 && level()<=3 && !hasDome())
            setLevel(level() - 1);
    }

    /**
     * this method tells if the box is on the edge of the board
     * @return boolean (true if on edge)
     */
    public boolean isOnEdge(){
        return position[0] == 0 || position[1] == 0 || position[0] == 4 || position[1] == 4;
    }
}
