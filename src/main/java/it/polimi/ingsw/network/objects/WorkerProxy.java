package it.polimi.ingsw.network.objects;

import it.polimi.ingsw.server.model.Colour;

public class WorkerProxy {
    final public Colour colour;
    final public boolean gender;

    public WorkerProxy(Colour colour, boolean gender) {
        this.colour = colour;
        this.gender = gender;
    }
}
