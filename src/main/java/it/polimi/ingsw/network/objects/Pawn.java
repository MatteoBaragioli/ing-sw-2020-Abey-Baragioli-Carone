package it.polimi.ingsw.network.objects;

import it.polimi.ingsw.server.model.Colour;
import it.polimi.ingsw.server.model.Worker;

public class Pawn {
    final private Colour colour;
    final private boolean gender;

    public Pawn(Worker worker) {
        colour = worker.colour();
        gender = worker.gender();
    }

    public Colour colour() {
        return colour;
    }

    public boolean gender() {
        return gender;
    }
}
