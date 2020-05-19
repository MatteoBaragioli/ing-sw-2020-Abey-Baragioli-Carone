package it.polimi.ingsw.network.objects;

public class BoxProxy {
    final public int level;
    final public boolean dome;
    final public int[] position;
    final public WorkerProxy occupier;

    public BoxProxy(int level, boolean dome, int[] position, WorkerProxy occupier) {
        this.level = level;
        this.dome = dome;
        this.position = position;
        this.occupier = occupier;
    }
}
