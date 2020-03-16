package it.polimi.ingsw.Server;

import java.util.ArrayList;

public class Box {
    private int level=0;
    private boolean dome=false;
    private int[] position = new int[2];
    private ArrayList<Box> adjacentBoxes;

    public Box(int[] position) {
        this.position = position;
    }
    public Box(int level, boolean dome, int[] position) {
        this.level = level;
        this.dome = dome;
        this.position = position;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isDome() {
        return dome;
    }

    public void setDome(boolean dome) {
        this.dome = dome;
    }

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    public ArrayList<Box> getAdjacentBoxes() {
        return adjacentBoxes;
    }

    public void setAdjacentBoxes(ArrayList<Box> adjacentBoxes) {
        this.adjacentBoxes = adjacentBoxes;
    }
}