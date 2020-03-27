package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TurnSequence {
    private HashMap<Worker,Box> newPositions = new HashMap<>();
    private List<Box> buildOnBox = new ArrayList<>();
    private List<Worker> movedWorkers;
    private int allowedLevelDifference;
    private Phase phase;
    private List<Box> possibleDestinations = new ArrayList<>();
    private List<Box> possibleBuilds = new ArrayList<>();
    private List<Worker> movableWorkers = new ArrayList<>();
    private List<Box> removedBlocks;

    public HashMap<Worker,Box> newPosition(){
        return newPositions;
    }

    /**
     * This method records the new position of the worker
     * @param worker Chosen worker
     * @param box Target box
     */
    public void recordNewPosition(Worker worker, Box box){
        newPositions.put(worker, box);
        recordMovedWorker(worker);
    }

    public List<Box> builtOnBox(){
        return buildOnBox;
    }

    /**
     * This method records chosen boxes for building
     * @param target Target box
     */
    public void recordBuiltOnBox(Box target){
        buildOnBox.add(target);
    }

    public List<Worker> movedWorkers(){
        return movedWorkers;
    }

    /**
     * This method records moved worker
     * @param worker Chosen worker
     */
    public void recordMovedWorker(Worker worker){
        movedWorkers.add(worker);
    }

    public int allowedLevelDifference(){
        return allowedLevelDifference;
    }

    public void setAllowedLevelDifference(int allowedLevelDifference) {
        this.allowedLevelDifference = allowedLevelDifference;
    }

    public Phase phase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public List<Box> possibleDestinations(){
        return possibleDestinations;
    }

    public void setPossibleDestinations(List<Box> possibleDestinations) {
        this.possibleDestinations = possibleDestinations;
    }

    public List<Box> possibleBuilds(){
        return  possibleBuilds;
    }

    public void setPossibleBuilds(List<Box> possibleBuilds) {
        this.possibleBuilds = possibleBuilds;
    }

    public List<Worker> movableWorkers(){
        return movableWorkers;
    }

    public void setMovableWorkers(List<Worker> movableWorkers) {
        this.movableWorkers = movableWorkers;
    }

    public void clearTurnSequence(){
        //confirm actions (if not undo)
        //reset builtOnBox, phase
    }
}
