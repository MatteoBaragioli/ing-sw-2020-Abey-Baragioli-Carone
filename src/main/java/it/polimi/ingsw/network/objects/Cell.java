package it.polimi.ingsw.network.objects;

import it.polimi.ingsw.server.model.Box;

public class Cell {
    final private int level;
    final private boolean dome;
    final private int[] position;
    final private Pawn occupier;

    public Cell(Box box) {
        level = box.level();
        dome = box.hasDome();
        position = box.position();
        if (box.isOccupiedByWorkers())
            occupier = new Pawn(box.occupier());
        else
            occupier = null;
    }

    public int level() {
        return level;
    }

    public boolean hasDome() {
        return dome;
    }

    public int[] position() {
        return position;
    }

    public Pawn occupier(){ return occupier;}
}
