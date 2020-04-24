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
        int x = box.positionX();
        int y = box.positionY();
        for(int i=x-1; i<=x+1; i++){
            if(i >= 0 && i <= 4){
                for(int j=y-1; j<=y+1; j++) {
                    if (j >= 0 && j <= 4 && !(i == x && j == y)) {
                        adjacentList.add(position(i,j));
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
        int x_difference = direction.positionX() - start.positionX();
        int y_difference = direction.positionY() - start.positionY();
        int i, j;
        List<Box> directionBoxes = new ArrayList<>();

        if (x_difference != 0 || y_difference != 0)
            for(i = direction.positionX() + x_difference, j = direction.positionY() + y_difference; i >= 0 && i <= 4 && j >= 0 && j <= 4; i += x_difference, j += y_difference)
                directionBoxes.add(position(i,j));
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

    public void updateCompleteTowers(TurnSequence turnSequence){
        for(Box builtOnBox: turnSequence.builtOnBoxes()){
            if(builtOnBox.isCompleteTower())
                setCompleteTowers(completeTowers + 1);
        }
    }

}
