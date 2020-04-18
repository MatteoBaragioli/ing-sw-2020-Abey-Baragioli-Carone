package it.polimi.ingsw.server.model;

import java.util.*;

public class Match {
    private int turn;
    private List<Player> gamePlayers;
    private Map gameMap = new Map();
    private Deque<GodCard> cards;
    private List<WinCondition> winConditions = new ArrayList<>();
    private ActionController actionController = new ActionController();
    private Player winner=null;
    private CommunicationController communicationController;

    public Match(List<Player> gamePlayers, CommunicationController communicationController) {
        this.gamePlayers = gamePlayers;
        this.communicationController = communicationController;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public Map getGameMap() {
        return gameMap;
    }

    public void setGameMap(Map gameMap) { this.gameMap = gameMap; }

    public List<Player> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(List<Player> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Deque<GodCard> getCards() { return cards; }

    public void setCards(Deque<GodCard> cards) {
        this.cards = cards;
    }

    public List<WinCondition> winConditions(){ return this.winConditions;}

    public void setWinConditions(List<WinCondition> winConditions){ this.winConditions=winConditions; }

    public void setActionController(ActionController actionController){
        this.actionController=actionController;
    }

    public Player winner(){return winner;}

    private void setWinner(Player winner){this.winner=winner;}


    /**
     * this method returns the opponents of the player given as a parameter
     * @param player
     * @return list of players
     */
    public List<Player> getOpponents(Player player){
        Iterator<Player> iterator= gamePlayers.iterator();
        List<Player> opponents=new ArrayList<>();
        Player target;
        while(iterator.hasNext()){
            target=iterator.next();
            if (!target.equals(player))
                opponents.add(target);
        }
        return opponents;
    }


    public void match(){
        //START MATCH
        assignColour();
        Collections.shuffle(gamePlayers);
        chooseCards();
            //assegnazione carte
            assignCards();
        setUpWorkers();


        gameTurns();

        endMatch();
    }

    private void gameTurns(){
        //ciclo dei giocatori
        //ciclo delle fasi
        //finchè non vince qualcuno
    }

    private void endMatch(){
        //dico vincitore
        //dico gg a tutti
    }

    protected void assignColour(){
        List<Colour> colours = new ArrayList<>();
        colours.add(Colour.BLUE);
        colours.add(Colour.WHITE);
        colours.add(Colour.GREY);

        Random rand = new Random();
        for (Player gamePlayer : gamePlayers) {
            int randomIndex = rand.nextInt(colours.size());
            Colour randomElement = colours.get(randomIndex);
            colours.remove(randomIndex);
            gamePlayer.setColour(randomElement);
        }
    }

    protected void chooseCards(){
        CardConstructor cardConstructor = new CardConstructor();
        List<ProtoCard> matchProtoCards = new ArrayList<>();
        List<ProtoCard> deckProtocards = cardConstructor.protoCards();
        Player challenger = gamePlayers.get(0);
        for(int i =0; i < gamePlayers.size(); i++) {
            ProtoCard chosenCard = communicationController.chooseCard(challenger, deckProtocards);
            matchProtoCards.add(chosenCard);
            deckProtocards.remove(chosenCard);
        }
        for(ProtoCard cardToCreate : matchProtoCards){
            GodCard newCard = cardConstructor.createCard(cardToCreate);
            cards.add(newCard);
        }
    }

    protected void assignCards(){

    }

    protected void setUpWorkers(){
        List<Box> freeMap = new ArrayList<>(gameMap.groundToList());
        List<Box> possibleSetUpPosition;
        Box position;
        List<Player> setUpOrder = new ArrayList<>(gamePlayers);
        for(Player player : gamePlayers){
            player.godCard().setUpCondition().modifySetUpOrder(player, setUpOrder);
        }
        for(Player player : setUpOrder){
            possibleSetUpPosition = player.godCard().setUpCondition().applySetUpCondition(player, freeMap);
            position = communicationController.chooseBox(player, possibleSetUpPosition);
            Worker worker1 = new Worker(position, player.getColour());
            freeMap.remove(position);
            player.assignWorker(worker1);

            possibleSetUpPosition = player.godCard().setUpCondition().applySetUpCondition(player, freeMap);
            position = communicationController.chooseBox(player, possibleSetUpPosition);
            Worker worker2 = new Worker(position, player.getColour());
            freeMap.remove(position);
            player.assignWorker(worker2);
        }
    }

}