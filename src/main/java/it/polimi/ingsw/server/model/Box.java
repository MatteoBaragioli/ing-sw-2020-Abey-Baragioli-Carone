package it.polimi.ingsw.server.model;

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

    public void occupy(Worker occupier){this.occupier=occupier;}

    /**
     * This method removes the occupier
     */
    public void removeOccupier() {occupy(null);}



    public Worker occupier(){ return occupier;}

    /**
     * this method tells if a box is occupied by any of the workers of the players
     * @return boolean (true if occupied)
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
     * this method tells if a box is occupied by any worker in the game or a dome
     * @return boolean (true if occupied, false otherwise)
     */
    public boolean isOccupied(){
        return !isFree();
    }

    /**
     * this method increases the level of the box or sets the dome attribute of box to true
     */
    public void build() {
        if (!hasDome())
            if (level() < 3 && level()>=0)
                buildBlock();
            else if(level()>=3 && occupier==null)
                buildDome();
    }

    /**
     * this method increases the level by one
     */
    public void buildBlock() {
        if(level() < 3)
            setLevel(level() + 1);
    }

    /**
     * this method sets the dome attribute of the box to true
     */
    public void buildDome() {
        setDome(true);
    }

    /**
     * this method sets the dome attribute of the box to false
     */
    public void removeDome() {setDome(false); }

    /**
     * this method decreases the level attribute by 1
     */
    public void removeBlock() {
        if (level()>0)
            setLevel(level() - 1);
    }


    /**
     * this method tells if the box is on the edge of the board
     * @return boolean (true if on edge)
     */
    public boolean isOnEdge(){
        return positionX() == 0 || positionY() == 0 || positionX() == 4 || positionY() == 4;
    }

    /**
     * This method tells if the box has a complete tower
     * @return boolean
     */
    public boolean isCompleteTower() {
        return hasDome() && level==3;
    }
}
