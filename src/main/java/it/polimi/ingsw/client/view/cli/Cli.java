package it.polimi.ingsw.client.view.cli;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.client.Client;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.network.CountDown;
import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.network.exceptions.TimeOutException;
import it.polimi.ingsw.network.objects.BoxProxy;
import it.polimi.ingsw.network.objects.GodCardProxy;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.network.objects.PlayerProxy;
import static it.polimi.ingsw.client.view.Coordinates.*;
import static it.polimi.ingsw.client.view.cli.Colors.*;
import static it.polimi.ingsw.network.CommunicationProtocol.UNIQUE_USERNAME;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cli implements View {
    private static BufferedReader commandline = new BufferedReader(new InputStreamReader(System.in));
    private PrintStream printStream = new PrintStream(System.out, true);
    private ScreenView view = new ScreenView(printStream);
    private PlayerProxy myPlayer;
    private PlayerProxy currentPlayer;
    private List<PlayerProxy> opponents=new ArrayList<>();
    private boolean ended= false;
    private boolean opponentsAnnounced=false;
    private List<String> buffer=new ArrayList<>();
    private boolean started=false;
    private boolean restart = false;
    private boolean myTurn=false;
//salvare ip e porta per nuova partita


    public ScreenView screenView() {
        return this.view;
    }

    /**
     * this method is used to check on the buffer's updates for any user's input and establishes a countDown for the user when needed
     * @param needCountDown
     * @return String, the user's answer
     * @throws TimeOutException
     */
    public synchronized String askAnswer(boolean needCountDown) throws TimeOutException{
        boolean received=false;
        String answer=null;
        CountDown countDown = new CountDown(buffer);
        if (needCountDown) {
            countDown.start();
        }

        while (!received && !ended && !restart) {
            if (!buffer.isEmpty()) {
                received = true;
                answer = buffer.get(0);
                if (needCountDown)
                    countDown.finish();
                eraseBuffer();
            } else {
                try {
                    if(needCountDown)
                        wait(countDown.getAvailableTime());
                    else
                        wait();
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
                if(needCountDown) {
                    if (countDown.isRunnedOut()) {
                        printStream.println("More than 2 minutes passed and you ran out of time! you have been disconnected from the server ");
                        throw new TimeOutException();
                    }
                }
            }
        }

        return answer;
    }

    /**
     * this method clears the buffer content
     */
    public synchronized void eraseBuffer() {
        buffer.clear();
    }


    /**
     * this method verifies if the user is quitting (by writing "quit" or "Quit" or "QUIT")  by using class regex
     * @param answer String to check
     * @return boolean , tells if the user actually wrote "quit" or "Quit" or "QUIT"
     */
    public boolean findQuitInString(String answer) {
        Pattern pattern1 = Pattern.compile("QUIT");
        Pattern pattern2 = Pattern.compile("Quit");
        Pattern pattern3 = Pattern.compile("quit");
        Matcher matcher1 = pattern1.matcher(answer);
        Matcher matcher2 = pattern2.matcher(answer);
        Matcher matcher3 = pattern3.matcher(answer);
        if (matcher1.find() || matcher2.find() || matcher3.find())
            return true;
        return false;
    }


    /**
     * this method verifies if the user wants to restart (by writing "RESTART" or "Restart" or "restart") by using class regex
     * @param answer String to check
     * @return boolean , tells if the user actually wrote "RESTART" or "Restart" or "restart"
     */
    public boolean findRestartInString(String answer) {
        Pattern pattern1 = Pattern.compile("RESTART");
        Pattern pattern2 = Pattern.compile("Restart");
        Pattern pattern3 = Pattern.compile("restart");
        Matcher matcher1 = pattern1.matcher(answer);
        Matcher matcher2 = pattern2.matcher(answer);
        Matcher matcher3 = pattern3.matcher(answer);
        if (matcher1.find() || matcher2.find() || matcher3.find())
            return true;
        return false;
    }
    /**
     * this method checks if a user is trying to quit using regex
     * @param answer String inserted by user
     * @return boolean
     */
    private boolean checkIfUserIsQuitting(String answer) {
        if (findQuitInString(answer))
            return manageQuit();
        return false;
    }

    /**
     * when the user tries to quit this method asks if the user in sure to quit
     * @return boolean the answer given by the user
     */
    public boolean manageQuit() {
        printStream.println("\nare you sure you want to quit the match?");
        printStream.println("Press");
        printStream.println("1  for Yes | 2  for No");
        boolean valid = false;
        boolean userIsQuitting = false;
        String input;
        int answer = 0;
        while (!valid) {//dopo aver letto quit nella fase iniziale del match
                try {                                                 //non ho ho caricato l'input nel buffer
                    input = read();                                   //quindi leggo la conferma del quit  tramite read
                    answer = Integer.parseInt(input);
                } catch (IOException | NumberFormatException e) {
                    answer = 0;
                }

            if (answer == -1 || answer == 1 || answer == 2) {
                valid = true;

            } else
                printStream.println("Not valid answer. Try again");
        }
        if (answer == -1 || answer == 1) {
            userIsQuitting = true;
            printStream.println("you are quitting");
            if (started)
                printStream.println("press \"enter\" key to exit or write \"restart\" to begin another match");
        }
        return userIsQuitting;
    }

    /**
     * this method handles the answer inserted by the player when expected a number
     *
     * @return int, the answer expected
     * @throws TimeOutException
     * @throws NumberFormatException
     * @param needCountDown boolean, represents the need for the ask to have a count down
     */
    public int askNumber(boolean needCountDown) throws NumberFormatException, TimeOutException {
        String answer = askAnswer(needCountDown);
        if(findQuitInString(answer))
            return -1;
        return Integer.parseInt(answer);
    }

    /**
     * this method manages with regex the coordinates inserted in cli by the player, like A5 or B2
     * @param positions list of coordinates available sent by the server
     * @return int, index of the correspondent coordinate inserted by the user
     * @throws TimeOutException
     */
    @Override
    public int askPosition(List<int[]> positions) throws TimeOutException { //nuovo ask position che legge stringa
        myTurn=true;
        String answer = null;
        boolean validAnswer;
        boolean validConfirmation;
        boolean found;
        boolean sure = false;
        int answerToInt = 0;
        Pattern pattern = Pattern.compile("[A-Ea-e].*[1-5]"); //regex for chess board coordinates input;
        view.clearScreen();
        view.turn();
        while (!sure ) {

            eraseBuffer();
            validAnswer = false;
            validConfirmation = false;
            found = false;
            while (!validAnswer) {
                printStream.println("Enter the coordinates of a box on the board to choose it");
                answer = askAnswer(true);

                Matcher matcher = pattern.matcher(answer);

                if (matcher.find()) {
                    answer = cleanPosition(answer, matcher);
                    //printStream.println(answer);
                    int[] paragon = getCartesianCoordinates(answer);
                    for (int i = 0; i < positions.size() && !found; i++) {

                        if (Arrays.equals(positions.get(i), paragon)) {
                            answerToInt = i;
                            found = true;
                        }
                    }
                    if (!found) {
                        printStream.println("the box you chose is not available according to the game rules");
                        printStream.println("You must enter a box between ");
                        for (int[] position : positions) {
                            printStream.print(getChessCoordinates(position) + "  ");
                        }
                    }
                    validAnswer = found;

                } else { //possibilità di quittare
                    if(findQuitInString(answer)) {
                        myTurn=false;
                        //ended = true;
                        return -1;
                    } else {
                        printStream.println("Not valid answer. Try again");
                        printStream.println("You must enter a letter and a number that refer to a box on the board");
                    }
                }
            }

            int confirm = 0;
            while (!validConfirmation) {
                printStream.println("you entered box " + answer + " are you sure?");
                printStream.println("Press");
                printStream.println("1  for Yes | 2  for No");
                try {
                    confirm = askNumber(true);
                } catch (NumberFormatException e) {
                    confirm = 0;
                }
                if (confirm == 1 || confirm == 2 || confirm == -1)
                    validConfirmation = true;

                else {
                    printStream.println("Not valid answer. Try again");
                    printStream.println("Press");
                    printStream.println("1  for Yes | 2  for No");
                }

                if (confirm == -1)
                    return confirm;
            }
            if (confirm == 1)
                sure = true;
        }
        List<String> turnInfo = view.getTurnMessage();
        boolean foundLine=false;
        for(int i=0; i<turnInfo.size() && !foundLine; i++ )
            if(turnInfo.get(i).contains("where do you want")) {
                foundLine = true;
                turnInfo.remove(i);
            }
        if(foundLine)
            view.setTurnMessage(turnInfo);
        view.clearScreen();
        myTurn=false;
        return answerToInt;
    }

    /**
     * this method "cleans" String answer of useless characters inserted by the player when inserting coordinates
     * @param answer  the coordinates inserted by the player that may contain wrong characters such as spaces or "ù" ecc
     * @param matcher of regex pattern found in answer
     * @return String,  the position interpreted by cli
     */

    public String cleanPosition(String answer, Matcher matcher) {
        char letter;
        switch (answer.charAt(matcher.start())) {
            case ('a'):
                letter = 'A';
                break;
            case ('b'):
                letter = 'B';
                break;
            case ('c'):
                letter = 'C';
                break;
            case ('d'):
                letter = 'D';
                break;
            case ('e'):
                letter = 'E';
                break;
            default:
                letter = answer.charAt(matcher.start());
        }
        return ("" + letter + answer.charAt(matcher.end() - 1) + "");
    }

    /**
     * this method handles the choice of the worker for the player's turn
     * @param workers is a list of coordinates that represent the workers' positions
     * @return int is the index in 'workers' list that represent the worker chosen by the player
     * @throws TimeOutException
     */
    @Override
    public int askWorker(List<int[]> workers) throws TimeOutException {
        myTurn=true;
        boolean valid = false;
        int answer = 0;
        eraseBuffer();
        view.clearScreen();
        view.turn();
        while (!valid) {
            for (int i = 0; i < workers.size(); i++) {
                printStream.print((i + 1) + "   for worker in " + getChessCoordinates(workers.get(i)) + "    ");
            }
            printStream.print("\n");
            try {
                answer = askNumber(true);
            } catch (NumberFormatException e) {
                answer = 0;
            }
            if (answer <= workers.size() && answer > 0 || answer == -1) {
                valid = true;
            } else
                System.out.println("Not valid answer. Try again");
        }
        if (answer != -1)
            answer--;
        view.clearScreen();
        //view.turn();
        List<String> turnInfo = view.getTurnMessage();
        boolean foundLine=false;
        for(int i=0; i<turnInfo.size() && !foundLine; i++ )
            if(turnInfo.get(i).contains("choose your worker")) {
                foundLine = true;
                turnInfo.remove(i);
            }
        if(foundLine)
            view.setTurnMessage(turnInfo);
        myTurn=false;
        return answer;

    }

    /**
     * this method handles the choice of the player's Godcard
     * @param cards list of cards to choose from
     * @return int is the index in 'cards' list that represent the card chosen by the player
     * @throws TimeOutException
     */
    @Override
    public int askCards(List<GodCardProxy> cards) throws TimeOutException {
        myTurn=true;
        boolean sure = false;
        boolean validCard = false;
        boolean validConfirmation = false;
        int answer = 0;
        if (cards.size() == 1)
            return 0;

        while (!sure) {
            answer = 0;
            validCard = false;
            validConfirmation = false;
            while (!validCard) {
                view.clearScreen();
                printStream.println("choose your game card!");
                for (int i = 0; i < cards.size(); i++) {
                    printStream.println((i + 1) + " " + cards.get(i).name + ":    ");
                    if (cards.get(i).setUpDescription != null)
                        printStream.println(cards.get(i).setUpDescription);
                    if (cards.get(i).description != null)
                        printStream.println(cards.get(i).description);
                    if (cards.get(i).winDescription != null)
                        printStream.println(cards.get(i).winDescription);
                }
                printStream.print("\n");
                printStream.println("press the number displayed next to the god's name to choose it");
                try {
                    answer = askNumber(true);
                } catch (NumberFormatException e) {
                    answer = 0;
                }
                if ((answer <= cards.size() && answer > 0) ) {
                    validCard = true;
                } else if(answer == -1) {
                    myTurn=false;
                    return answer;
                }else {
                    printStream.println("Not valid answer. Try again");
                    printStream.println("press the number displayed next to the god's name to choose it");
                }
            }
            answer--;

            int confirm = 0;
            while (!validConfirmation) {
                view.clearScreen();
                printStream.print("you chose:");
                printStream.print("   " + cards.get(answer).name);
                printStream.println(" ");
                printStream.println("these will be your game card, are you sure? ");
                printStream.println("Press");
                printStream.println("1  for Yes | 2  for No");
                try {
                    confirm = askNumber(true);
                } catch (NumberFormatException e) {
                    confirm = 0;
                }
                if (confirm == 1 || confirm == 2 )
                    validConfirmation = true;
                else
                    if (confirm == -1) {
                        myTurn=false;
                        return -1;
                    }
                else {
                    printStream.println("Not valid answer. Try again");
                    printStream.println("Press");
                    printStream.println("1  for Yes | 2  for No");
                }
            }
            if (confirm == 1)
                sure = true;
        }
        view.clearScreen();
        myTurn=false;
        return answer;
    }

    /**
     * this method manages the confirmation of the user when they can undo or use their GodCard power
     * @param key indicates what the user is chosing for, undo or use power
     * @return int that corresponds to the user's answer
     * @throws TimeOutException
     */
    @Override
    public int askConfirmation(CommunicationProtocol key) throws TimeOutException {
        myTurn=true;
        boolean valid = false;
        int answer = 0;
        while (!valid) {

            try {
                answer = askNumber(true);
            } catch (NumberFormatException e) {
                answer = 0;
            }
            if ( answer == 1 || answer == 2) {
                valid = true;
            } else if(answer==-1) {
                myTurn=false;
                return -1;
            }else
                printStream.println("Not valid answer. Try again");
        }

        answer--;
        view.clearScreen();
        myTurn=false;
        return answer;
    }

    /**
     * this method views additional communications depending on the key
     * @param key is a communication protocol key that indicates what the server is about to send
     */
    @Override
    public void prepareAdditionalCommunication(CommunicationProtocol key) {
        List<String> whatToDo=new ArrayList<>();
        switch (key) {
            case GOD_POWER:
                view.clearScreen();
                view.turn();
                printStream.println("\nWould you like to use your power?");
                printStream.println("Press");
                printStream.println("1  for Yes | 2  for No");
                break;
            case UNDO:
                view.clearScreen();
                view.turn();
                printStream.println("\nDo you want end your turn?");
                printStream.println("1  GO AHEAD;  2 UNDO");
                break;
            case BUILD://mettere cosa l'utente deve fare nella info box da togliere da qui in poi
                //view.clearScreen();
                //view.turn();
                //printStream.println("\nwhere do you want to build? ");
                whatToDo=view.getTurnMessage();
                whatToDo.add("where do you want to build? ");
                view.setTurnMessage(whatToDo);
                break;
            case DESTINATION: //mettere cosa l'utente deve fare nella info box
                //view.clearScreen();
               // view.turn();
               // printStream.println("\nwhere do you want to move your worker? ");
                whatToDo=view.getTurnMessage();
                whatToDo.add("where do you want to move? ");
                view.setTurnMessage(whatToDo);
                break;
            case START_POSITION:
                //view.clearScreen();
                //view.turn();
                whatToDo=view.getTurnMessage();
                whatToDo.add("where do you want to set up your workers? ");
                view.setTurnMessage(whatToDo);
                break;
            case WORKER:
                //view.clearScreen();
                //view.turn();
                whatToDo=view.getTurnMessage();
                whatToDo.add("choose your worker!");
                view.setTurnMessage(whatToDo);
                break;

        }
    }

    /**
     * this method updates the user's view, showing the changes made during the previous turn
     * @param boxes, is the game map sent by the server as a list boxes (more precisely box proxys)
     */
    @Override
    public void updateMap(List<BoxProxy> boxes) {
        for (BoxProxy boxProxy : boxes) {

            if (boxProxy.level == 0) {
                view.map().position(boxProxy.position[0], boxProxy.position[1]).groundFloorBlock();
            }

            if (boxProxy.level == 1) {
                view.map().position(boxProxy.position[0], boxProxy.position[1]).firstFloorBlock();
            }

            if (boxProxy.level == 2) {
                view.map().position(boxProxy.position[0], boxProxy.position[1]).secondFloorBlock();
            }

            if (boxProxy.level == 3) {
                view.map().position(boxProxy.position[0], boxProxy.position[1]).thirdFloorBlock();
            }

            if (boxProxy.dome) {
                view.map().position(boxProxy.position[0], boxProxy.position[1]).domeBlock();
            }

            if (boxProxy.occupier != null) {
                view.map().position(boxProxy.position[0], boxProxy.position[1]).withWorker(boxProxy.occupier.gender, boxProxy.occupier.colour);
            }

        }
    }

    /**
     * this method stores the data of the UI's player
     * @param player
     */
    @Override
    public void setMyPlayer(PlayerProxy player) {
        if(myPlayer==null) {
            started = true;
        }
        this.myPlayer = player;
    }

    /**
     * this method stores the data of the opponents (just their color, name and card)
     * @param players
     */
    @Override
    public void setOpponentsInfo(List<PlayerProxy> players) {
        if (!opponents.isEmpty())
            opponents.clear();
        else opponentsAnnounced = true;
        opponents = players;
        if (players.get(0).godCardProxy != null) {
            List<String> info = new ArrayList<>();
            info.add(getActualWrittenColor(myPlayer.colour) + myPlayer.name + RESET);
            info.add(getActualWrittenColor(myPlayer.colour) + "your card is " + myPlayer.godCardProxy.name + RESET);
            info.add("your opponents are:");
            for (PlayerProxy opponent : players) {
                info.add(getActualWrittenColor(opponent.colour) + opponent.name + ":" + RESET);
                info.add(getActualWrittenColor(opponent.colour) + opponent.name + "'s card is " + opponent.godCardProxy.name + RESET);
            }
            view.setInfoMessage(info);

        } else {
            for (PlayerProxy opponent : players) {
                if (opponentsAnnounced) {
                    printStream.println(opponent.name + " joined the game");
                }

            }
            printStream.println("the game is ready to start!");
        }
    }

    /**
     * this method communicates to the user that the connection was refused
     * @param host
     */
    @Override
    public void connectionFailed(String host) {
        System.err.println("Refused connection to" + host + "\nRetry.");
    }

    /**
     * this method stores the player that is currently playing
     * @param player current player
     */
    @Override
    public void setCurrentPlayer(PlayerProxy player) {
        this.currentPlayer = player;

        boolean foundLine=false;
        List<String> turnInfo=new ArrayList<>();
        turnInfo=view.getTurnMessage();
        for(int i=0; i<turnInfo.size() && !foundLine; i++ )
            if(turnInfo.get(i).contains("it's") && turnInfo.get(i).contains("'s turn!") || turnInfo.get(i).contains("it's your turn!")) {
                foundLine = true;
                turnInfo.remove(i);
            }
        if(player.name.equals(myPlayer.name))
            turnInfo.add("it's your turn!");
        else turnInfo.add("it's "+player.name+"'s turn!");
        view.setTurnMessage(turnInfo);
        if(myPlayer.godCardProxy!=null) {
            view.clearScreen();
            view.turn();
        }
    }

    /**
     * this method manages the string 'events' that represents the events that happened during the last turn and that is sent by the server
     * @param events is a String that contains all the details necessary to recall what happened in the last turn
     */
    @Override
    public void tellStory(List<String> events) {
        List<String> turnMessage = new ArrayList<>();
        for (String event : events) {
            String[] content = event.split(MatchStory.STORY_SEPARATOR);

            String player = content[0];

            Type chosenWorkerType = new TypeToken<int[]>() {
            }.getType();
            int[] chosenWorker = new Gson().fromJson(content[1], chosenWorkerType);

            Type actionType = new TypeToken<CommunicationProtocol>() {
            }.getType();
            CommunicationProtocol action = new Gson().fromJson(content[2], actionType);

            Type destinationType = new TypeToken<int[]>() {
            }.getType();
            int[] destination = new Gson().fromJson(content[3], destinationType);
            turnMessage.addAll(writeStory(player, chosenWorker, action, destination));
        }
        view.setTurnMessage(turnMessage);
    }

    /**
     * this method writes the events occurred in the last turn in a readable way for the user
     * @param player       name of the player that made the action
     * @param chosenWorker worker used
     * @param action       action made
     * @param destination  destination of the action
     * @return
     */
    public List<String> writeStory(String player, int[] chosenWorker, CommunicationProtocol action, int[] destination) {
        List<String> turnEvent = new ArrayList<>();

        switch (action) {
            case DESTINATION: {
                turnEvent.add(player + " moved from box " + getChessCoordinates(chosenWorker) + " to box " + getChessCoordinates(destination));
                break;
            }
            case BUILD: {
                turnEvent.add(player + " Built on box " + getChessCoordinates(destination));
                break;
            }
            case REMOVAL: {
                turnEvent.add(player + " Removed block on box " + getChessCoordinates(destination) + " with worker in box (" + getChessCoordinates(destination));
                break;
            }
        }
        return turnEvent;
    }

    /**
     * this method displays the winner
     * @param player
     */
    @Override
    public void setWinner(PlayerProxy player) {
        List<String>info;
        info=view.getTurnMessage();
        info.clear();
        view.setTurnMessage(info);
        view.clearScreen();
        view.turn();
        printStream.println("The match is over!");
        if (player.name.equals(myPlayer.name)) {
            printStream.println("You won the match, congratulations! you are on the right path to become a god!");
        } else {
            printStream.println(player.name + " won the match, better luck next time!");
        }
        printStream.println("press \"quit\" to exit ");
    }

    /**
     * this method displays the name of the player who has lost the game and updates the info message of attribute view
     * @param player the player who has lost
     */
    @Override
    public void setLoser(PlayerProxy player) {
        if(!opponents.isEmpty()) {
            List<String> info ;
            info = view.infoMessage();
            for (int i=0; i<info.size(); i++) {
                if (info.get(i).contains(player.name) && !info.get(i).contains("card"))
                    if(!info.get(i).contains("(eliminated)")) {
                        info.set(i, info.get(i).concat("(eliminated)"));
                    }

            }
            view.setInfoMessage(info);
            if (player.name.equals(myPlayer.name)) { //mettere di fianco al mio nome che ho perso
                printStream.println("You lost, better luck next time!");
                printStream.println("enter \"quit\" to exit the game, or stay to see who wins!");
                ended = true;

            }else{
                opponents.remove(player);
                printStream.println(player.name + " lost!");
            }
        }else printStream.println("a player lost connection or quit at the beginning of match");
    }

    /**
     * this method tells the user the count down in up
     */
    @Override
    public void timeOut() {
    printStream.println("More than 2 minutes passed and you ran out of time! you have been disconnected from the server ");
    }

    /**
     * this method asks to user the ip address
     * @return String
     */
    @Override
    public String askIp() {
        myTurn=true;
        boolean valid=false;
        String answer=null;
        while(!valid && !ended) {
            printStream.println("Write ip address to connect to:");
            eraseBuffer();

            try {
                answer = askAnswer(false);
                if (findQuitInString(answer)) {
                    valid=true;
                }
                else valid=true;
            } catch (TimeOutException ignored) {
                }

        }
        //if(findQuitInString(answer))
            //System.exit(0);
        myTurn=false;
        return answer;
    }

    /**
     * this method asks to the user the match type they want to play
     * @return int represents the number of the players
     */
    @Override
    public int askMatchType() {
        myTurn=true;
        boolean valid = false;
        int answer = 0;
        while (!valid) {
            printStream.println("Choose match type:\n" + "1   for 1 vs 1\n" + "2   Three For All");
            try {
                answer = askNumber(false);
            } catch (NumberFormatException e) {
                answer = 0;
            } catch (TimeOutException ignored){

            }
            if (answer == -1) {
                valid = true;
                System.exit(0);
            }
            else if(answer == 1 || answer == 2)
                valid=true;
            else
                System.out.println("Not valid answer. Try again");


        }
        if (answer != -1) {
            answer++;
            view.clearScreen();
            printStream.println("waiting for the match to start...");
        }

        myTurn=false;
        return answer;
    }

    /**
     * this method asks to the user the port to connect to
     * @return int
     */
    @Override
    public int askPort() {
        myTurn=true;
        boolean valid = false;
        int answer = 0;
        while (!valid) {
            System.out.println("Write port:");
            try {
                answer = askNumber(false);
                if (answer >= 1024) {
                    valid = true;
                } else if(answer==-1) {
                    System.exit(0);
                } else
                    System.out.println("Not valid answer. Try again");
            } catch (NumberFormatException e) {
                answer = 0;
            } catch (TimeOutException ignored){

            }

        }
        myTurn=false;
        return answer;
    }

    /**
     * this method asks the user name to the user
     * @return String
     * */
    @Override
    public String askUserName(CommunicationProtocol key) {
        myTurn=true;
        boolean valid=false;
        String answer=null;
        while(!valid) {
            if(key==UNIQUE_USERNAME)
                printStream.println("username not valid, choose another one");
            printStream.println("Write username:");
            eraseBuffer();
            try {
                answer = askAnswer(false);
                if(findQuitInString(answer)) {
                    System.exit(0);
                }
                else if(!answer.equals("")) {
                    valid = true;
                }
            } catch (TimeOutException ingnored) {
            }

        }
        myTurn=false;
        return answer;
    }

    /**
     * this method asks the challenger the game cards from which the other players will have to choose from
     * @param cards these are all the game cards available
     * @return int[] , a list of numbers that represent the indexes of the chosen cards
     * @throws TimeOutException
     */
    @Override
    public int[] askDeck(List<GodCardProxy> cards) throws TimeOutException {
        myTurn=true;
        boolean sure = false;
        boolean sameCard;
        boolean validCard;
        boolean validConfirmation;
        int[] answer = new int[(opponents.size() + 1)];

        while (!sure) {
            view.clearScreen();
            printStream.println("You are the challenger, choose the game cards! One for each player");
            printStream.println("GAME CARDS:");
            for (int i = 0; i < (opponents.size() + 1); i++) {
                if(i>0)
                    view.clearScreen();
                for (int j = 0; j < cards.size(); j++) {
                    printStream.println((j + 1) + " " + cards.get(j).name + ":    ");
                    if (cards.get(j).setUpDescription != null)
                        printStream.println(cards.get(j).setUpDescription);
                    if (cards.get(j).description != null)
                        printStream.println(cards.get(j).description);
                    if (cards.get(j).winDescription != null)
                        printStream.println(cards.get(j).winDescription);
                    if (cards.get(j).opponentsFxDescription!=null)
                        printStream.println(cards.get(j).opponentsFxDescription);

                }
                printStream.print("\n");
                printStream.println("press the number displayed next to the god's name to choose it, you have to choose " + (opponents.size() + 1) + " cards");

                validCard = false;
                while (!validCard) {
                    try {
                        answer[i] = askNumber(true);
                    } catch (NumberFormatException e) {
                        answer[i] = 0;
                    }
                    if ((answer[i] <= cards.size() && answer[i] > 0) ) {
                        sameCard = false;
                        for (int j = 0; j < i && !sameCard; j++) {
                            if (answer[i] == answer[j]) {
                                sameCard = true;
                                printStream.println("\nyou already chose " + cards.get((answer[i] - 1)).name + " choose another card");
                            }
                        }
                        if (!sameCard)
                            validCard = true;
                    } else if(answer[i] == -1) {
                        answer[0] = -1;
                        return answer;
                    } else {
                        printStream.println("Not valid answer. Try again");
                        printStream.println("press the number displayed next to the god's name to choose it");
                    }
                }
            }

            for (int i = 0; i < (opponents.size() + 1); i++) {
                answer[i]--;
            }

            printStream.print("you chose:");
            for (int i = 0; i < (opponents.size() + 1); i++) {
                printStream.print("   " + cards.get(answer[i]).name);
            }
            printStream.println(" ");
            printStream.println("these will be the game cards, are you sure? ");
            printStream.println("Press");
            printStream.println("1  for Yes | 2  for No");

            validConfirmation = false;
            int confirm = 0;
            while (!validConfirmation) {
                try {
                    confirm = askNumber(true);
                } catch (NumberFormatException e) {
                    confirm = 0;
                }
                if (confirm == 1 || confirm == 2)
                    validConfirmation = true;

                if (confirm == 1)
                    sure = true;
                else {
                    printStream.println("Not valid answer. Try again");
                    printStream.println("Press");
                    printStream.println("1  for Yes | 2  for No");
                }

            }
        }
        view.clearScreen();
        myTurn=false;
        return answer;
    }

    /**
     * run method in cli, it allows the UI to detect and elaborate the user's input constantly, even if it's not the user's turn
     */
    public void run(){
        Client client = new Client(this);
        view.title();
        client.start();
        String input;
        while (!ended) {
            restart = false;
            try {
                input = read();
            } catch (IOException e) {
                //e.printStackTrace();
                input = "";
                ended = true;
            }

            if (findQuitInString(input)) {//user wrote "quit"
                if (manageQuit()) {//Wrote quit and confirms quitting
                    if (!myTurn()) {//the match hasn't started/ it's not the user's turn
                        try {
                            input = read();
                            if (findRestartInString(input)) {
                                restart = true;
                                started = false;
                                try {
                                    view.clearScreen();
                                    view.title();
                                    client.restartClient();
                                } catch (ChannelClosedException e) {
                                    //e.printStackTrace();
                                }

                            } else {
                                ended = true;
                                try {
                                    client.end();
                                } catch (ChannelClosedException e) {
                                    //e.printStackTrace();
                                }
                                System.exit(0);
                            }
                        } catch (IOException e) {
                            //e.printStackTrace();
                        }
                    } else {//it's the user's turn and confirms quitting
                        //updateBuffer("quit");
                        try {
                            input = read();
                            if (input.equals("restart")) { //user's turn, confirming quit, entering 'restart'
                                restart = true;
                                started = false;
                                try {
                                    view.clearScreen();
                                    view.title();
                                    client.restartClient();
                                } catch (ChannelClosedException e) {
                                    //e.printStackTrace();
                                }
                                updateBuffer("quit");
                            } else {//user's turn, confirming quit, entering 'enter' instead of 'restart'
                                ended = true;
                                try {
                                    client.end();
                                } catch (ChannelClosedException e) {
                                    //e.printStackTrace();
                                }
                                System.exit(0);
                            }
                        } catch (IOException e) {
                            //e.printStackTrace();
                        }
                    }
                } else { //Wrote quit but stays in game
                    printStream.println("Please, complete the previous action");
                }
            } else {
                if (!input.equals(""))
                    updateBuffer(input);
            }

        }

    }

    /**
     * this method reads the user's input in command line
     * @return
     * @throws IOException
     */
    public String read() throws IOException {
        String answer;
        answer = commandline.readLine();
        return answer;
    }

    /**
     * this method updates the buffer with an element and notifies the countDown
     * @param string the element stored in the buffer
     */
    public synchronized void updateBuffer(String string) {
        buffer.add(string);
        notifyAll();

    }

    /**
     * this method tell's if it is the user's turn to interact with the UI
     * @return
     */
    public boolean myTurn() {
        return myTurn;
    }

    /**
     * this method announces to the user it has lost connection to the server
     */
    @Override
    public void connectionLost() {
        printStream.println("connection to the server lost");
        printStream.println("enter \"quit\" to exit game");
    }
}






























                                                                                                                                                                                      //grazie mamma, ti voglio bene, francesca.