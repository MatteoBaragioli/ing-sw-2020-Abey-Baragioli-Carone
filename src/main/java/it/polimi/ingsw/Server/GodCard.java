package it.polimi.ingsw.Server;

public class GodCard {
    private final int id;
    private final String name;
    private GodPower godPower;
    private final TurnPhase turnPhase;

    public GodCard(int id, String name, GodPower godPower, TurnPhase turnPhase) {
        this.id = id;
        this.name = name;
        this.turnPhase = turnPhase;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public GodPower getGodPower() {
        return godPower;
    }

    public void setGodPower(GodPower godPower){
        this.godPower = godPower;
    }

    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

}