package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.phases.*;

import java.util.*;

public class Match {
    private int currentPlayerIndex;
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

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
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
        assignCards();
        setUpWorkers();
        int firstPlayerIndex = communicationController.chooseFirstPlayer(gamePlayers);

        //MATCH
        List<TurnPhase> phasesSequence = new ArrayList<>();
        phasesSequence.add(new Start());
        phasesSequence.add(new Move());
        phasesSequence.add(new Build());
        phasesSequence.add(new End());
        for(currentPlayerIndex = firstPlayerIndex; currentPlayerIndex<gamePlayers.size()&&winner==null;){
            Player currentPlayer = gamePlayers.get(currentPlayerIndex);
            int undoCounter = 0;
            for(int phaseIndex = 0; phaseIndex < phasesSequence.size();){
                boolean confirm = true;
                phasesSequence.get(phaseIndex).executePhase(currentPlayer, communicationController, actionController, gameMap, getOpponents(currentPlayer), winConditions);
                if(undoCounter<3 && phaseIndex<3) {
                    confirm = communicationController.confirmPhase();
                }
                if(!confirm){
                    phaseIndex = 0;
                    undoCounter++;
                }
                else phaseIndex++;
            }
            winner = currentPlayer.turnSequence().possibleWinner();
            if(currentPlayerIndex==gamePlayers.size()){
                currentPlayerIndex=0;
            } else {
                currentPlayerIndex++;
            }
        }

        //END MATCH
        //todo dire chi ha vinto
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
        for(Player player : gamePlayers) {
            ProtoCard chosenCard = communicationController.chooseProtoCard(challenger, deckProtocards);
            matchProtoCards.add(chosenCard);
            deckProtocards.remove(chosenCard);
        }
        for(ProtoCard cardToCreate : matchProtoCards){
            GodCard newCard = cardConstructor.createCard(cardToCreate);
            cards.add(newCard);
        }
    }

    protected void assignCards(){
        List<GodCard> availableCards = new ArrayList<>(cards);
        for(int i = gamePlayers.size()-1; i>=0; i--){
            GodCard chosenCard = communicationController.chooseCard(gamePlayers.get(i), availableCards);
            gamePlayers.get(i).assignCard(chosenCard);
            availableCards.remove(chosenCard);
        }
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