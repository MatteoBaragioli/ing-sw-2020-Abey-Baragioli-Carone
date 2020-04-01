package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TurnSequence {
    private HashMap<Worker,Box> newPositions = new HashMap<>();
    private List<Box> builtOnBoxes = new ArrayList<>();
    private List<Box> removedBlocks = new ArrayList<>();
    private int allowedLevelDifference;
    private List<Box> possibleDestinations = new ArrayList<>();
    private List<Box> possibleBuilds = new ArrayList<>();
    private List<Worker> movableWorkers = new ArrayList<>();
    private List<Worker> movedWorkers = new ArrayList<>();
    private Worker chosenWorker = null;


    public HashMap<Worker,Box> newPosition(){
        return newPositions;
    }

    public List<Box> builtOnBoxes(){
        return builtOnBoxes;
    }

    public List<Box> removedBlocks() {
        return removedBlocks;
    }

    public int allowedLevelDifference(){
        return allowedLevelDifference;
    }

    public void setAllowedLevelDifference(int allowedLevelDifference) {
        this.allowedLevelDifference = allowedLevelDifference;
    }

    public List<Box> possibleDestinations(){
        return possibleDestinations;
    }

    public List<Box> possibleBuilds(){
        return  possibleBuilds;
    }

    public List<Worker> movableWorkers(){
        return movableWorkers;
    }

    public List<Worker> movedWorkers(){
        return movedWorkers;
    }

    public Worker chosenWorker()
    {
        return chosenWorker;
    }

    public void setChosenWorker(Worker worker)
    {
        chosenWorker = worker;
    }

    /**
     * This method records the new position of the worker
     * @param worker Chosen worker
     * @param box Target box
     */
    public void recordNewPosition(Worker worker, Box box){
        newPositions.put(worker, box);
        recordMovedWorkers(worker);
    }

    /**
     * Reset new positions' list
     */
    public void clearNewPositions()
    {
        newPositions.clear();
    }

    /**
     * This method records one new build
     * @param target Target box
     */
    public void recordBuiltOnBox(Box target){
        builtOnBoxes.add(target);
    }

    /**
     * Reset new builds' list
     */
    public void clearBuiltOnBoxes()
    {
        builtOnBoxes.clear();
    }

    /**
     * This method records one block removal
     * @param box
     */
    public void recordRemovedBlock(Box box)
    {
        removedBlocks.add(box);
    }

    /**
     * Reset removed blocks' list
     */
    public void clearRemovedBlocks()
    {
        removedBlocks.clear();
    }

    /**
     * This method makes a box available for moving
     * @param destination box
     */
    public void addPossibleDestination(Box destination)
    {
        if (!possibleDestinations.contains(destination))
            possibleDestinations.add(destination);
    }

    /**
     * Reset possible destinations' list
     */
    public void clearPossibleDestinations()
    {
        possibleDestinations.clear();
    }

    /**
     * This method makes a box available for building
     * @param box
     */
    public void addPossibleBuild(Box box)
    {
        if (!possibleBuilds.contains(box))
            possibleBuilds.add(box);

    }

    /**
     * Reset possible builds' list
     */
    public void clearPossibleBuilds()
    {
        possibleBuilds.clear();
    }

    /**
     * This method makes a worker available for moving
     * @param worker
     */
    public void addMovableWorker(Worker worker)
    {
        if (!movableWorkers.contains(worker))
            movableWorkers.add(worker);
    }

    /**
     * Reset movable workers' list
     */
    public void clearMovableWorkers()
    {
        movableWorkers.clear();
    }

    /**
     * This method records a worker that has been moved
     * @param worker
     */
    public void recordMovedWorkers(Worker worker){
        if (movedWorkers.contains(worker))
            movedWorkers.add(worker);
    }

    /**
     * Reset moved workers' list
     */
    public void clearMovedWorkers()
    {
        movedWorkers.clear();
    }

    /**
     * This method restarts the parameters
     */
    public void reset()
    {
        if (!newPositions.isEmpty())
            clearNewPositions();
        if (!builtOnBoxes.isEmpty())
            clearBuiltOnBoxes();
        if (!removedBlocks.isEmpty())
            clearRemovedBlocks();
        if (!possibleDestinations.isEmpty())
            clearPossibleDestinations();
        if (!possibleBuilds.isEmpty())
            clearPossibleBuilds();
        if (!movableWorkers.isEmpty())
            clearMovableWorkers();
        if (!movedWorkers.isEmpty())
            clearMovedWorkers();
    }
    public void clearTurnSequence(){
        //apply turn

        reset();
        //reset builtOnBoxes, phase
    }
}
