package it.polimi.ingsw.network.objects;

public class GodCardProxy {
    final public String name;
    final public int id;
    final public String description;
    final public String winDescription;
    final public String setUpDescription;
    final public String opponentsFxDescription;

    public GodCardProxy(String name, int id, String description, String winDescription, String setUpDescription, String opponentsFxDescription) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.winDescription = winDescription;
        this.setUpDescription = setUpDescription;
        this.opponentsFxDescription = opponentsFxDescription;
    }
}
