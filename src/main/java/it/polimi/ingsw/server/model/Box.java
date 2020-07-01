package it.polimi.ingsw.server.model;

import it.polimi.ingsw.network.objects.BoxProxy;
import it.polimi.ingsw.network.objects.WorkerProxy;

public class Box {
    private int level=0;
    private boolean dome=false;
    private int[] position = new int[2];
    private Worker occupier=null;

    public Box(int positionX, int positionY) {
        this.position[0] = positionX;
        this.position[1] = positionY;
    }

    public Box(int level, boolean dome, int positionX, int positionY) {
        this.level = level;
        this.dome = dome;
        this.position[0] = positionX;
        this.position[1] = positionY;
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

    public int positionX() {
        return position[0];
    }

    public int positionY() {
        return position[1];
    }

    public Worker occupier(){ return occupier;}

    public void occupy(Worker occupier){this.occupier=occupier;}

    public BoxProxy crateProxy() {
        WorkerProxy workerProxy = null;
        if (isOccupiedByWorkers())
            workerProxy = occupier().createProxy();
        return new BoxProxy(level(), hasDome(), position(), workerProxy);
    }

    /**
     * This method removes the occupier
     */
    public void removeOccupier() {occupy(null);}

    /**
     * This method tells if a box is occupied by any of the workers of the players
     * @return Boolean (true if occupied)
     */
    public boolean isOccupiedByWorkers() {
        return occupier != null;
    }

    /**
     * This method tells if the Box is free (not occupied by neither workers nor domes).
     * @return Boolean (true if free)
     */
    public boolean isFree(){
        return !isOccupiedByWorkers() && !hasDome();
    }

    /**
     * This method tells if a box is occupied by any worker in the game or a dome
     * @return Boolean (true if occupied, false otherwise)
     */
    public boolean isOccupied(){
        return !isFree();
    }

    /**
     * This method increases the level of the box or sets the dome attribute of box to true
     */
    public void build() {
        if (!hasDome())
            if (level() < 3 && level()>=0)
                buildBlock();
            else if(level()>=3 && occupier==null)
                buildDome();
    }

    /**
     * This method increases the level by one
     */
    public void buildBlock() {
        if(level() < 3)
            setLevel(level() + 1);
    }

    /**
     * This method sets the dome attribute of the box to true
     */
    public void buildDome() {
        setDome(true);
    }

    /**
     * This method sets the dome attribute of the box to false
     */
    public void removeDome() {setDome(false); }

    /**
     * This method decreases the level attribute by 1
     */
    public void removeBlock() {
        if (level()>0)
            setLevel(level() - 1);
    }


    /**
     * This method tells if the box is on the edge of the board
     * @return Boolean (true if on edge)
     */
    public boolean isOnEdge(){
        return positionX() == 0 || positionY() == 0 || positionX() == 4 || positionY() == 4;
    }

    /**
     * This method tells if the box has a complete tower
     * @return Boolean that is true if the box has a complete tower
     */
    public boolean isCompleteTower() {
        return hasDome() && level==3;
    }
}
