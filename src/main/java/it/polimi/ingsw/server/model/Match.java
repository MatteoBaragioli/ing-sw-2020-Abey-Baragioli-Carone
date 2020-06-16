package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.phases.*;

import java.io.IOException;
import java.util.*;

public class Match extends Thread {
    private int currentPlayerIndex;
    private List<User> users;
    private HashMap<User, Player> userToPlayer = new HashMap<>();
    private List<Player> gamePlayers = new ArrayList<>();
    private Map gameMap = new Map();
    private List<GodCard> cards = new ArrayList<>();
    private List<WinCondition> winConditions = new ArrayList<>();
    private ActionController actionController = new ActionController();
    private Player winner=null;
    private CommunicationController communicationController;

    public Match(List<User>  users) {
        for (User user : users)
            addNewPlayer(user);
        communicationController = new CommunicationController(users, gamePlayers);
        this.users = users;
    }

    /**
     * This method is used in the Match constructor and adds one user to the match
     * @param user new player
     */
    private void addNewPlayer(User user) {
        Player player = new Player(user);
        userToPlayer.put(user, player);
        gamePlayers.add(player);
    }

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

    public List<User> users() {
        return users;
    }

    public Map getGameMap() {
        return gameMap;
    }

    public void setGameMap(Map gameMap) { this.gameMap = gameMap; }

    public List<Player> gamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(List<Player> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public List<GodCard> getCards() { return cards; }

    public void setCards(List<GodCard> cards) {
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


    /**
     * This method executes a match
     */
    public void match(){
        //START MATCH
        assignColour();
        Collections.shuffle(gamePlayers);
        chooseCards();
        assignCards();
        setUpWorkers();
        setUpWinConditions();
        //OOOOOOOOOOOOOOOOOOOOOOOEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
        int firstPlayerIndex = communicationController.chooseFirstPlayer(winner, gamePlayers);

        //MATCH
        List<TurnPhase> phasesSequence = new ArrayList<>();
        phasesSequence.add(new Start());
        phasesSequence.add(new Move());
        phasesSequence.add(new Build());
        phasesSequence.add(new End());
        for(currentPlayerIndex = firstPlayerIndex; currentPlayerIndex<gamePlayers.size()&&winner==null;){
            Player currentPlayer = gamePlayers.get(currentPlayerIndex);
            int undoCounter = 0;
            for(int phaseIndex = 0; phaseIndex < phasesSequence.size() && currentPlayer.isInGame();){
                boolean confirm = true;
                try {
                    phasesSequence.get(phaseIndex).executePhase(currentPlayer, communicationController, actionController, gameMap, getOpponents(currentPlayer), winConditions);
                } catch (IOException e) {
                    e.printStackTrace();
                    //todo eliminare il giocatore e terminare la partita
                }
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
            if(!currentPlayer.isInGame()){
                removePlayer(currentPlayer);
                if(gamePlayers.size()==1)
                    winner=gamePlayers.get(0);
            }
            if(currentPlayerIndex>=gamePlayers.size()-1){
                currentPlayerIndex=0;
            } else {
                currentPlayerIndex++;
            }
        }
        if(winner!=null)
            System.out.println(winner.getNickname());

        //END MATCH
        //todo dire chi ha vinto
    }


    /**
     * This method assignes a colour to every player
     */
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

    /**
     * This method makes the challenger choose the match cards
     */
    protected void chooseCards(){
        CardConstructor cardConstructor = new CardConstructor();
        List<GodCard> deck = cardConstructor.cards();
        Player challenger = gamePlayers.get(0);
        for(Player player : gamePlayers) {
            GodCard chosenCard = communicationController.chooseCard(challenger, deck);
            cards.add(chosenCard);
            deck.remove(chosenCard);
        }
    }

    /**
     * This method makes every player choose their own GodCard
     */
    protected void assignCards(){
        List<GodCard> availableCards = new ArrayList<>(cards);
        for(int i = gamePlayers.size()-1; i>=0; i--){
            GodCard chosenCard = communicationController.chooseCard(gamePlayers.get(i), availableCards);
            gamePlayers.get(i).assignCard(chosenCard);
            availableCards.remove(chosenCard);
        }
    }


    /**
     * This method assignes two workers to every player. Every player chooses the starting position of their workers
     */
    protected void setUpWorkers(){
        List<Box> freeMap = new ArrayList<>(gameMap.groundToList());
        List<Box> possibleSetUpPosition;
        Box position = null;
        List<Player> setUpOrder = new ArrayList<>(gamePlayers);
        for(Player player : gamePlayers){
            player.godCard().setUpCondition().modifySetUpOrder(player, setUpOrder);
        }
        for(Player player : setUpOrder){
            possibleSetUpPosition = player.godCard().setUpCondition().applySetUpCondition(player, freeMap);
            try {
                position = communicationController.chooseStartPosition(player, possibleSetUpPosition);
            } catch (IOException e) {
                e.printStackTrace();
                //TODO
            }
            Worker worker1 = new Worker(position, player.getColour());
            freeMap.remove(position);
            player.assignWorker(worker1);

            possibleSetUpPosition = player.godCard().setUpCondition().applySetUpCondition(player, freeMap);
            try {
                position = communicationController.chooseStartPosition(player, possibleSetUpPosition);
            } catch (IOException e) {
                e.printStackTrace();
                //TODO
            }
            Worker worker2 = new Worker(position, player.getColour());
            freeMap.remove(position);
            player.assignWorker(worker2);
        }
    }

    /**
     * This method removes a player from the list of gamePlayers
     * @param loser
     */
    protected void removePlayer(Player loser){
        for(Worker worker : loser.workers()){
            worker.position().removeOccupier();
        }
        loser.workers().clear();
        gamePlayers().remove(loser);
    }

    /**
     * This method tells if a user is in this match
     * @param user user
     * @return boolean
     */
    public boolean userIsPlayer(User user) {
        return users().contains(user);
    }

    /**
     * This method finds the player related to a user (null if the user isn't part of the match)
     * @param user user
     * @return player
     */
    public Player findPlayer(User user) {
        if (userIsPlayer(user))
            return userToPlayer.get(user);
        return null;
    }

    /**
     * This method removes a user from the match
     * @param user quitting user
     */
    public synchronized void removeUser(User user) {
        if (userIsPlayer(user)) {
            Player player = findPlayer(user);
            if (player.isInGame())
                removePlayer(player);
            communicationController.removeUser(player);
            userToPlayer.remove(user);
            users().remove(user);
        }
    }

    /**
     * This method adds every Non-Standard win condition to the list of WinConditions
     */
    protected void setUpWinConditions(){
        for(GodCard godCard : cards){
            if(godCard.winCondition()!=null){
                winConditions.add(godCard.winCondition());
            }
        }
    }

    @Override
    public void run() {
        match();
    }
}