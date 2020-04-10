package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private Box[][] ground = new Box[5][5];
    private int completeTowers = 0;

    public Map() {
        for (int i = 0; i<ground.length; i++)
            for (int j=0; j<ground[i].length; j++)
                this.ground[i][j] = new Box(i, j);
    }

    public Box[][] ground() {
        return ground;
    }

    public int completeTowers() {
        return completeTowers;
    }

    public void setCompleteTowers(int completeTowers) {
        this.completeTowers = completeTowers;
    }

    /**
     * This method returns a box depending on the given coordinates
     * @param x longitude
     * @param y latitude
     * @return box
     */
    public Box position(int x, int y) {
        if (x>=0 && x<=4 && y>=0 && y<=4)
            return ground[x][y];
        return null;
    }

    /**
     * This method converts the whole map ground into a box list
     * @return A List of boxes
     */
    public List<Box> groundToList() {
        List<Box> out = new ArrayList<>();

        for (int x = 0; x<ground.length; x++)
            for (int y = 0; y<ground[x].length; y++)
                out.add(position(x,y));

        return out;
    }

    /**
     * This method returns a list of Boxes that are adjacent to a Box
     * @param box Examined Box
     * @return List of Boxes
     */
    public List<Box> adjacent(Box box){
        List<Box> adjacentList = new ArrayList<>();
        int x = box.position()[0];
        int y = box.position()[1];
        for(int i=x-1; i<=x+1; i++){
            if(i >= 0 && i <= 4){
                for(int j=y-1; j<=y+1; j++) {
                    if (j >= 0 && j <= 4 && !(i == x && j == y)) {
                        adjacentList.add(ground[i][j]);
                    }
                }
            }
        }
        return adjacentList;
    }

    /**
     * This method returns a list of Boxes, that are in the same direction of two given Boxes
     * @param start Starting Box
     * @param direction Second Box that gives the direction
     * @return List of Boxes
     */
    public List<Box> boxesSameDirection(Box start, Box direction){
        int x_difference = direction.position()[0] - start.position()[0];
        int y_difference = direction.position()[1] - start.position()[1];
        int i, j;
        int pos=0;
        List<Box> directionBoxes = new ArrayList<>();
        for(i = start.position()[0], j = start.position()[1]; i >= 0 && i <= 4 && j >= 0 && j <= 4; i += x_difference, j += y_difference, pos++){
            directionBoxes.add(pos,ground[i][j]);
        }
        return directionBoxes;
    }

    /**
     * This method return the level difference between two Boxes
     * @param startBox First Box
     * @param targetBox Chosen Box
     * @return An int that is the level difference between the two Boxes
     */
    public int levelDifference(Box startBox, Box targetBox){
        return targetBox.level() - startBox.level();
    }

}
