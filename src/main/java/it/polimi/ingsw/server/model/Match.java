package it.polimi.ingsw.server.model;

import it.polimi.ingsw.network.exceptions.*;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.server.model.phases.*;

import java.util.*;

public class Match extends Thread{
    private int currentPlayerIndex;
    private List<User> users;
    private HashMap<User, Player> userToPlayer = new HashMap<>();
    private List<Player> players = new ArrayList<>();
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
        communicationController = new CommunicationController(users, players);
        this.users = users;
    }

    /**
     * This method is used in the Match constructor and adds one user to the match
     * @param user new player
     */
    private void addNewPlayer(User user) {
        Player player = new Player(user);
        userToPlayer.put(user, player);
        players.add(player);
        gamePlayers.add(player);
    }

    public Match(List<Player> gamePlayers, CommunicationController communicationController) {
        this.gamePlayers = gamePlayers;
        this.players.addAll(gamePlayers);
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
        for(currentPlayerIndex = firstPlayerIndex; gamePlayers.size()>1 && currentPlayerIndex<gamePlayers.size() && winner==null;){
            Player currentPlayer = gamePlayers.get(currentPlayerIndex);
            announceCurrentPlayer(currentPlayer);
            try {
                takeTurn(currentPlayer);
            }
            catch (TimeOutException e) {
                e.printStackTrace();
                currentPlayer.turnSequence().undo();
                removePlayer(currentPlayer);
                announceRemovedPlayer(currentPlayer);
            }
            catch (ChannelClosedException e) {
                e.printStackTrace();
                currentPlayer.turnSequence().undo();
                removeUser(findUser(e.name()));
            }

            try {
                communicationController.updateView(players, gameMap.createProxy());
            } catch (ChannelClosedException e) {
                removeUser(findUser(e.name()));
            }

            setWinner(currentPlayer.turnSequence().possibleWinner());
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
        if (gamePlayers.size()==1)
            winner = gamePlayers.get(0);
        if(winner!=null)
            announceWinner();

        //END MATCH
        //todo endGame()
    }

    private void takeTurn(Player player) throws ChannelClosedException, TimeOutException {
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
        List<Player> receivers = new ArrayList<>();
        for (int i = 0; i<players.size() && gamePlayers.size()>1; i++) {
            Player player = players.get(i);
            if (!receivers.contains(player))
                try {
                    communicationController.tellMatchStory(player, matchStory);
                    receivers.add(player);
                } catch (ChannelClosedException e) {
                    e.printStackTrace();
                    removeUser(findUser(e.name()));
                    i = -1;
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
        List<Player> receivers = new ArrayList<>();
        for (int i = 0; i<players.size() && gamePlayers.size()>1; i++) {
            Player player = players.get(i);
            if (!receivers.contains(player))
                try {
                    communicationController.announceParticipants(player, gamePlayers);
                    receivers.add(player);
                } catch (ChannelClosedException e) {
                    removeUser(findUser(e.name()));
                    i = -1;
                }
        }
    }

    private void announceCurrentPlayer(Player currentPlayer) {
        List<Player> receivers = new ArrayList<>();
        for (int i = 0; i<players.size() && gamePlayers.size()>1; i++) {
            Player player = players.get(i);
            if (!receivers.contains(player))
                try {
                    communicationController.announceCurrentPlayer(player, currentPlayer);
                    receivers.add(player);
                } catch (ChannelClosedException e) {
                    removeUser(findUser(e.name()));
                    i = -1;
                }
        }
    }

    private void announceWinner() {
        List<Player> receivers = new ArrayList<>();
        for (int i = 0; i<players.size() && !players.isEmpty(); i++) {
            Player player = players.get(i);
            if(!receivers.contains(player))
                try {
                    communicationController.announceWinner(player, winner);
                    receivers.add(player);
                } catch (ChannelClosedException e) {
                    removeUser(findUser(e.name()));
                }
        }
    }

    private void announceLoser(Player loser) {
        List<Player> receivers = new ArrayList<>();
        for (int i = 0; i<players.size() && !players.isEmpty(); i++) {
            Player player = players.get(i);
            if (!receivers.contains(player))
                try {
                    communicationController.announceLoser(player, loser);
                    receivers.add(player);
                } catch (ChannelClosedException e) {
                    e.printStackTrace();
                    removeUser(findUser(e.name()));
                }
        }
    }

    public void announceRemovedPlayer(Player player) {
        if (!player.equals(winner))
            announceLoser(player);
        try {
            communicationController.updateView(players, gameMap.createProxy());
        } catch (ChannelClosedException e) {
            e.printStackTrace();
            removeUser(findUser(e.name()));
        }
    }

    private void endGame() {
        //todo
    }

    /**
     * This method assigns a colour to every player
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
        boolean valid = false;
        while (!valid && gamePlayers.size()>1) {
            Player challenger = gamePlayers.get(0);
            if (communicationController.playerIsUser(challenger)) {
                try {
                    cards = communicationController.chooseDeck(challenger, deck);
                    valid = true;
                } catch (TimeOutException e) {
                    e.printStackTrace();
                    removePlayer(challenger);
                    announceRemovedPlayer(challenger);
                    valid = false;
                } catch (ChannelClosedException e) {
                    e.printStackTrace();
                    removeUser(findUser(e.name()));
                    valid = false;
                }
            } else {
                for (int i = 0; i < gamePlayers.size(); i++) {
                    int index = new Random().nextInt(deck.size());
                    cards.add(deck.get(index));
                    deck.remove(index);
                }
                valid = true;
            }
        }
    }

    /**
     * This method makes every player choose their own GodCard
     */
    protected void assignCards() {
        List<GodCard> availableCards = new ArrayList<>(cards);
        if (gamePlayers.size()>1)
            for(int i = gamePlayers.size()-1; i>=0; i--) {
                Player player = gamePlayers.get(i);

                if (player.godCard()==null) {
                    announceCurrentPlayer(player);
                    try {
                        GodCard chosenCard = communicationController.chooseCard(player, availableCards);
                        player.assignCard(chosenCard);
                        availableCards.remove(chosenCard);
                        while (i>gamePlayers.size())
                            i--;
                    } catch (TimeOutException e) {
                        e.printStackTrace();
                        removePlayer(player);
                        announceRemovedPlayer(player);
                    } catch (ChannelClosedException e) {
                        e.printStackTrace();
                        removeUser(findUser(e.name()));
                    }
                }
            }
    }


    /**
     * This method assigns two workers to every player. Every player chooses the starting position of their workers
     */
    protected void setUpWorkers(){
        if(gamePlayers.size()>1) {
            List<Box> possibleSetUpPosition;
            Box position;

            List<Player> setUpOrder = new ArrayList<>(gamePlayers);
            for (Player player : gamePlayers) {
                player.godCard().setUpCondition().modifySetUpOrder(player, setUpOrder);
            }
            List<Player> readyPlayers = new ArrayList<>();
            for (int j = 0; j < gamePlayers.size() && gamePlayers.size() > 1; j++) {
                Player player = gamePlayers.get(j);
                if (!readyPlayers.contains(player)) {
                    List<Box> freeMap = new ArrayList<>(gameMap.freePositions());
                    for (int i = 0; i < 2 && player.isInGame() && gamePlayers.size() > 1; i++) {
                        possibleSetUpPosition = player.godCard().setUpCondition().applySetUpCondition(player, freeMap);
                        announceCurrentPlayer(player);
                        try {
                            Worker worker;
                            position = communicationController.chooseStartPosition(player, possibleSetUpPosition);
                            if (i == 0)
                                worker = new Worker(position, player.colour(), true);
                            else
                                worker = new Worker(position, player.colour(), false);
                            freeMap.remove(position);
                            player.assignWorker(worker);
                            readyPlayers.add(player);
                        } catch (TimeOutException e) {
                            e.printStackTrace();
                            removePlayer(player);
                            announceRemovedPlayer(player);
                            j = -1;
                        } catch (ChannelClosedException e) {
                            e.printStackTrace();
                            player.setInGame(false);
                            removeUser(findUser(e.name()));
                            j = -1;
                        }

                        try {
                            communicationController.updateView(players, gameMap.createProxy());
                        } catch (ChannelClosedException e) {
                            e.printStackTrace();
                            removeUser(findUser(e.name()));
                        }
                    }
                }
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
        gamePlayers.remove(loser);
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

            if (player.godCard()!=null) //if the player is lost before choosing a card
                cards.add(player.godCard());

            if (player.isInGame()) { //if the user was in game and is quitting
                removePlayer(player);
                players.remove(player);
            }

            communicationController.removeUser(player);
            userToPlayer.remove(user);
            users().remove(user);

            announceRemovedPlayer(player);
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
