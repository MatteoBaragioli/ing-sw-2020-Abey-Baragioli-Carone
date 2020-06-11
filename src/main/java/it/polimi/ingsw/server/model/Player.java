package it.polimi.ingsw.server.model;

import it.polimi.ingsw.network.objects.GodCardProxy;
import it.polimi.ingsw.network.objects.PlayerProxy;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private Colour colour;
    private List<Worker> workers = new ArrayList<>();
    private GodCard card;
    private TurnSequence turnSequence = new TurnSequence();
    private boolean inGame=true;


    public Player(String name, Colour colour, GodCard card) {
        this.name = name;
        this.colour = colour;
        this.card = card;
    }

    public Player(User user){
        this.name =user.name();
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Colour colour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    private List<Worker> getWorkers() {
        return workers;
    }

    public GodCard godCard() {
        return card;
    }

    private void setCard(GodCard card) {
        this.card = card;
    }

    public TurnSequence turnSequence() { return turnSequence; }

    public void assignCard(GodCard card) {
        setCard(card);
    }

    public List<Worker> workers() {  //method that returns the player's workers for usage outside the 'player' class
        return getWorkers();
    }

    public boolean isInGame(){
        return this.inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public void playerIsOver(){
        setInGame(false);
    }

    /**
     * This method creates player proxy for view
     * @return Player proxy
     */
    public PlayerProxy createProxy() {
        GodCardProxy godCardProxy = null;
        if (godCard() != null)
            godCardProxy = godCard().createProxy();
        return new PlayerProxy(name(), colour(), godCardProxy);
    }

    /**
     * This method adds a worker to the player's list if he isn't owning it
     * @param worker assigned worker
     */
    public void assignWorker(Worker worker){
        if(worker!=null && workers().size()<2 && !workers().contains(worker))
            workers.add(worker);
    }
}
