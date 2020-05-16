package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TurnSequence {
    private Box chosenBox = null;
    private Box previousBox = null;
    private HashMap<Worker,Box> newPositions = new HashMap<>();
    private List<Box> builtOnBoxes = new ArrayList<>();
    private List<Box> removedBlocks = new ArrayList<>();
    private int allowedLevelDifference = 1;
    private List<Box> possibleDestinations = new ArrayList<>();
    private List<Box> possibleBuilds = new ArrayList<>();
    private List<Worker> movableWorkers = new ArrayList<>();
    private List<Worker> movedWorkers = new ArrayList<>();
    private Worker chosenWorker = null;
    private Player possibleWinner = null;

    public Box chosenBox() {
        return chosenBox;
    }

    public void setChosenBox(Box box) {
        chosenBox = box;
    }

    public Box previousBox() {
        return previousBox;
    }

    public void setPreviousBox(Box box) {
        previousBox = box;
    }

    public HashMap<Worker,Box> newPositions(){
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

    public Player possibleWinner() {
        return possibleWinner;
    }

    public void setPossibleWinner(Player player) {
        possibleWinner = player;
    }

    public void resetChosenBox() {
        setChosenBox(null);
    }

    public void resetPreviousBox() {
        setPreviousBox(null);
    }

    public void resetChosenWorker() {
        setChosenWorker(null);
    }

    public void resetPossibleWinner() {
        setPossibleWinner(null);
    }

    /**
     * This method tells the location of a worker during the turn sequence
     * @param worker requested worker
     * @return Box
     */
    public Box workersCurrentPosition(Worker worker) {
        if (movedWorkers.contains(worker))
            return newPositions.get(worker);
        return worker.position();
    }

    /**
     * This method records the new position of the worker and updates the involved boxes' occupiers
     * @param worker Chosen worker
     * @param box Target box
     */
    public void recordNewPosition(Worker worker, Box box) {
        Box preBox = workersCurrentPosition(worker);
        //registering the previous position if the player owns the moved worker
        if (movableWorkers.contains(worker))
            setPreviousBox(preBox);

        //checking if the box will be free after the worker is moved
        if (worker.equals(preBox.occupier()))
            preBox.removeOccupier();

        if(box.occupier()!=null){
            newPositions.put(box.occupier(), box);
            recordMovedWorker(box.occupier());
        }

        box.occupy(worker);
        newPositions.put(worker, box);
        recordMovedWorker(worker);
    }

    /**
     * Reset new positions' list
     */
    public void clearNewPositions() {
        if (!newPositions().isEmpty())
            newPositions.clear();
    }

    /**
     * This method undoes the movements and the occupations taken in the turn sequence
     */
    public void undoNewPositions() {
        for (Worker worker: movedWorkers)
            newPositions.get(worker).removeOccupier();
        for (Worker worker: movedWorkers)
            worker.position().occupy(worker);
    }

    /**
     * This method executes the workers' moves
     */
    public void getTheMovesDone() {
        for (Worker worker: movedWorkers)
            worker.move(newPositions.get(worker));
    }

    /**
     * This method records one new build (the box must have one contraction)
     * @param target Target box
     */
    public void recordBuiltOnBox(Box target) {
        if (target.hasDome() || target.level() > 0)
            if (removedBlocks().contains(target) && !target.hasDome())
                removedBlocks.remove(target);
            else
                builtOnBoxes.add(target);
    }

    /**
     * Reset new builds' list
     */
    public void clearBuiltOnBoxes() {
        if (!builtOnBoxes().isEmpty())
            builtOnBoxes.clear();
    }

    /**
     * This method undoes the builds that took place in this turn
     */
    public void undoBuilds() {
        for (Box box : builtOnBoxes) {
            if (box.hasDome())
                box.removeDome();
            else
                box.removeBlock();
        }
    }

    /**
     * This method records one block removal
     * @param box
     */
    public void recordRemovedBlock(Box box) {
        if (box.level() < 3 && !box.hasDome()) {
            if (builtOnBoxes.contains(box))
                builtOnBoxes.remove(box);
            else
                removedBlocks.add(box);
        }
    }

    /**
     * Reset removed blocks' list
     */
    public void clearRemovedBlocks() {
        if (!removedBlocks().isEmpty())
            removedBlocks.clear();
    }

    /**
     * This method rebuilds the removed blocks
     */
    public void undoRemovals() {
        for (Box box : removedBlocks)
            box.buildBlock();
    }

    /**
     * This method resets the allowed level difference
     */
    public void resetAllowedLevelDifference() {
        setAllowedLevelDifference(1);
    }

    /**
     * This method makes a box available for moving
     * @param destination box
     */
    public void addPossibleDestination(Box destination) {
        if (!possibleDestinations.contains(destination))
            possibleDestinations.add(destination);
    }

    /**
     * This method makes a box unavailable for moving
     * @param destination box
     */
    public void removePossibleDestination(Box destination) {
        possibleDestinations.remove(destination);
    }

    /**
     * Reset possible destinations' list
     */
    public void clearPossibleDestinations() {
        if (!possibleDestinations().isEmpty())
            possibleDestinations.clear();
    }

    /**
     * This method makes a box available for building
     * @param box new building location
     */
    public void addPossibleBuild(Box box) {
        if (!possibleBuilds.contains(box) && !box.isCompleteTower())
            possibleBuilds.add(box);
    }

    /**
     * This method makes a box unavailable for building
     * @param box unavailable building location
     */
    public void removePossibleBuild(Box box) {
        possibleBuilds.remove(box);
    }

    /**
     * Reset possible builds' list
     */
    public void clearPossibleBuilds() {
        if (!possibleBuilds().isEmpty())
        possibleBuilds.clear();
    }

    /**
     * This method makes a worker available for moving
     * @param worker movable worker
     */
    public void addMovableWorker(Worker worker) {
        if (!movableWorkers.contains(worker))
            movableWorkers.add(worker);
    }

    /**
     * This method makes a worker unavailable for moving
     * @param worker unmovable worker
     */
    public void removeMovableWorker(Worker worker) {
        movableWorkers.remove(worker);
    }

    /**
     * Reset movable workers' list
     */
    public void clearMovableWorkers() {
        if (!movableWorkers().isEmpty())
            movableWorkers.clear();
    }

    /**
     * This method records a worker that has been moved
     * @param worker moved worker
     */
    public void recordMovedWorker(Worker worker) {
        if (!movedWorkers.contains(worker))
            movedWorkers.add(worker);
    }

    /**
     * Reset moved workers' list
     */
    public void clearMovedWorkers() {
        if (!movedWorkers().isEmpty()) {
            movedWorkers.clear();
        }
    }

    /**
     * This method records a possible possibleWinner if nobody was already winning
     * @param player possible winner
     */
    public void registerPossibleWinner(Player player) {
        if (possibleWinner() == null)
            setPossibleWinner(player);
    }

    /**
     * This method restarts the parameters
     */
    public void reset() {
        resetChosenBox();
        resetPreviousBox();
        resetChosenWorker();
        clearNewPositions();
        clearBuiltOnBoxes();
        clearRemovedBlocks();
        clearPossibleDestinations();
        clearPossibleBuilds();
        clearMovableWorkers();
        clearMovedWorkers();
    }

    /**
     * This method undoes the turn sequence
     */
    public void undo() {
        undoNewPositions();
        undoBuilds();
        undoRemovals();
        resetPossibleWinner();
        reset();
    }

    /**
     * This method resets the variables when a player has finished its turn
     */
    public void clearTurnSequence() {
        resetAllowedLevelDifference();
        reset();
    }

    /**
     * This method moves the workers to their new location
     */
    public void confirmTurnSequence() {
        getTheMovesDone();
        clearTurnSequence();
    }
}