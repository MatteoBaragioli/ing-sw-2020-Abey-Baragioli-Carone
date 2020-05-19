package it.polimi.ingsw.network.objects;

import it.polimi.ingsw.server.model.Colour;

public class PlayerProxy {
    final public String name;
    final public Colour colour;
    final public GodCardProxy godCardProxy;

    public PlayerProxy(String name, Colour colour, GodCardProxy godCardProxy) {
        this.name = name;
        this.colour = colour;
        this.godCardProxy = godCardProxy;
    }
}
