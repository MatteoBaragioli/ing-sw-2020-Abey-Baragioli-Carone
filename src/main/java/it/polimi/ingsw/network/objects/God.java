package it.polimi.ingsw.network.objects;

import it.polimi.ingsw.server.model.GodCard;

public class God {
    final private String name;
    final private int id;
    final private String description;
    final private String winDescription;
    final private String setUpDescription;
    final private String opponentsFxDescription;

    public God(GodCard godCard) {
        name = godCard.name();
        id = godCard.id();
        description = godCard.description();
        winDescription = godCard.winDescription();
        setUpDescription = godCard.setUpDescription();
        opponentsFxDescription = godCard.opponentsFxDescription();
    }

    public String name() {
        return name;
    }

    public int id() {
        return id;
    }

    public String description() {
        return description;
    }

    public String winDescription() {
        return winDescription;
    }

    public String setUpDescription() {
        return setUpDescription;
    }

    public String opponentsFxDescription() {
        return opponentsFxDescription;
    }
}
