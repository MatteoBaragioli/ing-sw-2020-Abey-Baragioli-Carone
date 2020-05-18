package it.polimi.ingsw.network.objects;

import it.polimi.ingsw.server.model.Colour;
import it.polimi.ingsw.server.model.Player;

public class PlayerCard {
    final private String name;
    final private Colour colour;
    final private God god;

    public PlayerCard (Player player) {
        name = player.name();
        colour = player.colour();
        if (player.godCard()!=null)
            god = new God(player.godCard());
        else
            god = null;
    }

    public String name() {
        return name;
    }

    public Colour colour() {
        return colour;
    }

    public God god() {
        return god;
    }
}
