package it.polimi.ingsw.server.model;

import it.polimi.ingsw.network.objects.BoxProxy;

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

    Box[][] ground() {
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
     * @param x Longitude
     * @param y Latitude
     * @return Box
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
     * This method return all free boxes of the map
     * @return List of free boxes
     */
    List<Box> freePositions() {
        List<Box> freeBoxes = new ArrayList<>();
        for (Box box: groundToList())
            if (box.isFree())
                freeBoxes.add(box);
        return freeBoxes;
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
        int xDifference = direction.positionX() - start.positionX();
        int yDifference = direction.positionY() - start.positionY();
        int i;
        int j;
        List<Box> directionBoxes = new ArrayList<>();

        if (xDifference != 0 || yDifference != 0)
            for(i = direction.positionX() + xDifference, j = direction.positionY() + yDifference; i >= 0 && i <= 4 && j >= 0 && j <= 4; i += xDifference, j += yDifference)
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

    /**
     * This method updates the tower counter by using the turnsequence
     * @param turnSequence Last turn record
     */
    public void updateCompleteTowers(TurnSequence turnSequence){
        List<Box> countedTowers = new ArrayList<>();

        for (Box builtOnBox:turnSequence.builtOnBoxes()) {
            if (!countedTowers.contains(builtOnBox) && builtOnBox.isCompleteTower())
                countedTowers.add(builtOnBox); //number of towers completed in turn ++
        }
        setCompleteTowers(completeTowers + countedTowers.size());

    }

    /**
     * This method creates map proxy
     * @return List of all boxes proxies
     */
    public List<BoxProxy> createProxy() {
        List<BoxProxy> map = new ArrayList<>();
        for (Box box: groundToList())
            map.add(box.crateProxy());
        return map;
    }
}
