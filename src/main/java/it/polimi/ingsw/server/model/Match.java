package it.polimi.ingsw.server.model;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.phases.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class Match extends Thread{
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
    private List<TurnPhase> phasesSequence = createPhaseSequence();

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
        announceParticipants();
        chooseCards();
        assignCards();
        announceParticipants();
        setUpWorkers();
        setUpWinConditions();
        int firstPlayerIndex = communicationController.chooseFirstPlayer(winner, gamePlayers);

        //MATCH
        for(currentPlayerIndex = firstPlayerIndex; currentPlayerIndex<gamePlayers.size()&&winner==null;){
            Player currentPlayer = gamePlayers.get(currentPlayerIndex);
            announceCurrentPlayer(currentPlayer);
            try {
                takeTurn(currentPlayer);
            } catch (IOException | ChannelClosedException | TimeoutException e) {
                e.printStackTrace();
                currentPlayer.setInGame(false);
                currentPlayer.turnSequence().undo();
            }

            try {
                communicationController.updateView(gamePlayers, gameMap.createProxy());
            } catch (ChannelClosedException e) {
                removeUser(findUser(e.name()));
            }

            winner = currentPlayer.turnSequence().possibleWinner();
            if(!currentPlayer.isInGame()){
                removePlayer(currentPlayer);
                currentPlayerIndex--;
                if(gamePlayers.size()==1)
                    winner=gamePlayers.get(0);
                else
                    //todo vediamo se va
                    announceLoser(currentPlayer);
            }
            if(currentPlayerIndex >= gamePlayers.size()-1){
                currentPlayerIndex=0;
            } else {
                currentPlayerIndex++;
            }
        }
        if(winner!=null)
            announceWinner();

        //END MATCH
        //todo endGame()
    }

    private void takeTurn(Player player) throws ChannelClosedException, TimeoutException, IOException {
        int undoCounter = 0;
        MatchStory matchStory = new MatchStory(player);
        for(int phaseIndex = 0; phaseIndex < phasesSequence.size() && player.isInGame();){
            boolean confirm = true;
            phasesSequence.get(phaseIndex).executePhase(player, communicationController, actionController, gameMap, getOpponents(player), winConditions, matchStory);
            if(undoCounter<3 && phaseIndex==2)
                confirm = communicationController.confirmPhase(player);
            if(!confirm){
                player.turnSequence().undo();
                matchStory.clear();
                communicationController.updateView(player, gameMap.createProxy());
                undoCounter++;
                phaseIndex = 0;
            }
            else
                if (player.equals(player.turnSequence().possibleWinner()))
                    phaseIndex = phasesSequence.size();
                else
                    phaseIndex++;
        }
        tellMatchStory(matchStory);
    }

    private void tellMatchStory(MatchStory matchStory) {
        for (Player player: gamePlayers) {
            try {
                communicationController.tellMatchStory(player, matchStory);
            } catch (ChannelClosedException e) {
                e.printStackTrace();
                removeUser(findUser(e.name()));
            }
        }
    }

    private List<TurnPhase> createPhaseSequence() {
        List<TurnPhase> turnPhases = new ArrayList<>();
        turnPhases.add(new Start());
        turnPhases.add(new Move());
        turnPhases.add(new Build());
        turnPhases.add(new End());
        return turnPhases;
    }

    public void announceParticipants() {
        for (Player player: gamePlayers) {
            try {
                communicationController.announceParticipants(player, gamePlayers);
            } catch (ChannelClosedException e) {
                removeUser(findUser(e.name()));
                if (gamePlayers.size() <= 1)
                    endGame();
            }
        }
    }

    private void announceCurrentPlayer(Player currentPlayer) {
        for (Player player: gamePlayers) {
            try {
                communicationController.announceCurrentPlayer(player, currentPlayer);
            } catch (ChannelClosedException e) {
                removeUser(findUser(e.name()));
                if (gamePlayers.size() <= 1)
                    endGame();
            }
        }
    }

    private void announceWinner() {
        for (Player player: gamePlayers) {
            try {
                communicationController.announceWinner(player, winner);
            } catch (ChannelClosedException e) {
                removeUser(findUser(e.name()));
                endGame();
            }
        }
    }

    private void announceLoser(Player loser) {
        for (Player player: gamePlayers) {
            try {
                communicationController.announceLoser(player, loser);
            } catch (ChannelClosedException e) {
                removeUser(findUser(e.name()));
            }
        }
    }

    private void endGame() {
        //todo
    }

    /**
     * This method assignes a colour to every player
     */
    public void assignColour(){
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
    protected void chooseCards()  {
        CardConstructor cardConstructor = new CardConstructor();
        List<GodCard> deck = cardConstructor.cards();
        Player challenger = gamePlayers.get(0);
        try {
            cards = communicationController.chooseDeck(challenger, deck);
        } catch (TimeoutException e) {
            e.printStackTrace();
            //todo
        } catch (ChannelClosedException e) {
            e.printStackTrace();
            //todo
        }
    }

    /**
     * This method makes every player choose their own GodCard
     */
    protected void assignCards() {
        List<GodCard> availableCards = new ArrayList<>(cards);
        for(int i = gamePlayers.size()-1; i>=0; i--){
            GodCard chosenCard = null;
            announceCurrentPlayer(gamePlayers.get(i));
            try {
                chosenCard = communicationController.chooseCard(gamePlayers.get(i), availableCards);
            } catch (TimeoutException e) {
                e.printStackTrace();
                //todo
            } catch (ChannelClosedException e) {
                e.printStackTrace();
                removeUser(findUser(e.name()));
            }
            gamePlayers.get(i).assignCard(chosenCard);
            availableCards.remove(chosenCard);
        }
    }


    /**
     * This method assignes two workers to every player. Every player chooses the starting position of their workers
     */
    protected void setUpWorkers(){

        List<Box> possibleSetUpPosition;
        Box position = null;
        List<Player> setUpOrder = new ArrayList<>(gamePlayers);
        for(Player player : gamePlayers){
            player.godCard().setUpCondition().modifySetUpOrder(player, setUpOrder);
        }
        for(Player player : setUpOrder){
            List<Box> freeMap = new ArrayList<>(gameMap.freePositions());
            for (int i = 0; i<2 && player.isInGame(); i++) {
                possibleSetUpPosition = player.godCard().setUpCondition().applySetUpCondition(player, freeMap);
                announceCurrentPlayer(player);
                try {
                    Worker worker;
                    position = communicationController.chooseStartPosition(player, possibleSetUpPosition);
                    if(i==0)
                        worker = new Worker(position, player.colour(), true);
                    else
                        worker = new Worker(position, player.colour(), false);
                    freeMap.remove(position);
                    player.assignWorker(worker);
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    player.setInGame(false);
                } catch (ChannelClosedException e) {
                    e.printStackTrace();
                    player.setInGame(false);
                }

                try {
                    communicationController.updateView(gamePlayers, gameMap.createProxy());
                } catch (ChannelClosedException e) {
                    removeUser(findUser(e.name()));
                }
            }
            if (!player.isInGame()) {
                removeUser(findUser(player.name()));
                //todo update Map
                announceParticipants();
            }
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
        loser.setInGame(false);
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

    private User findUser(String name) {
        for (User user: users())
            if (user.name().equals(name))
                return user;
        return null;
    }

    /**
     * This method removes a user from the match
     * @param user quitting user
     */
    public synchronized void removeUser(User user) {
        if (userIsPlayer(user)) {
            Player player = findPlayer(user);
            if (player.isInGame()) {
                removePlayer(player);
                gamePlayers.remove(player);
            }
            communicationController.removeUser(player);
            userToPlayer.remove(user);
            users().remove(user);
            if (!findPlayer(user).equals(winner))
                announceLoser(player);
            try {
                communicationController.updateView(gamePlayers, gameMap.createProxy());
            } catch (ChannelClosedException e) {
                e.printStackTrace();
                removeUser(findUser(e.name()));
            }
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
