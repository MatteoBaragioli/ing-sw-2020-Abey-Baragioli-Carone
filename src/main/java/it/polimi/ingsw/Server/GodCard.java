package it.polimi.ingsw.Server;

public class GodCard {
    private String name;
    private int id;

    public GodCard(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
