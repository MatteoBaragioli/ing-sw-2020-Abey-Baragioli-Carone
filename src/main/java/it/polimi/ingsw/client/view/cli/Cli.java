package it.polimi.ingsw.client.view.cli;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.client.Client;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import it.polimi.ingsw.network.objects.BoxProxy;
import it.polimi.ingsw.network.objects.GodCardProxy;
import it.polimi.ingsw.network.objects.MatchStory;
import it.polimi.ingsw.network.objects.PlayerProxy;
import static it.polimi.ingsw.client.view.Coordinates.*;
import static it.polimi.ingsw.client.view.cli.Colors.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cli extends Thread implements View {
    private static BufferedReader commandline = new BufferedReader(new InputStreamReader(System.in));
    private PrintStream printStream = new PrintStream(System.out, true);
    private ScreenView view = new ScreenView(printStream);
    private PlayerProxy myPlayer;
    private PlayerProxy currentPlayer;
    private List<PlayerProxy> opponents=new ArrayList<>();
    private boolean ended=false;
    private boolean opponentsAnnounced=false;
    private List<String> buffer=new ArrayList<>();
    private boolean started=false;


    public ScreenView screenView() {
        return this.view;
    }

    public synchronized String askAnswer() {
        boolean received=false;
        String answer=null;
            while (!received)
                if (!buffer.isEmpty()) {
                    received = true;
                    answer = buffer.get(0);
                    eraseBuffer();
                }
                else
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

        return answer;
    }

    public synchronized void eraseBuffer() {
        buffer.clear();
    }

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
     * this method checks if a user is trying to quit using regex
     *
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
     *
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
        while (!valid) {
            if (started &&(!myTurn()||myPlayer.godCardProxy==null)) {
                try {
                    input = read();//però questo metodo la lancia
                    answer = Integer.parseInt(input);
                } catch (IOException e) {
                    answer = 0;
                } catch (NumberFormatException e) { //verificare catch: in teoria non fa il catch del number format perchè ask answer non lo fa
                    answer = 0;
                }
            }else {
                try{
                    answer=askNumber();
                }catch (NumberFormatException e) { //verificare catch: in teoria non fa il catch del number format perchè ask answer non lo fa
                    answer = 0;
                }
            }
        if (answer == -1 || answer == 1 || answer == 2) {
                valid = true;
                printStream.println("you pressed "+ answer);
            } else
                printStream.println("Not valid answer. Try again");
        }
        if (answer == -1 || answer == 1) {
            userIsQuitting = true;
            printStream.println("you are quitting");
        }
        return userIsQuitting;
    }

    /**
     * this method handles the answer inserted by the player when expected a number
     *
     * @return int, the answer expected
     * @throws IOException
     * @throws NumberFormatException
     */
    public int askNumber() throws NumberFormatException {
        String answer = askAnswer();
        if(findQuitInString(answer))
            return -1;
        return Integer.parseInt(answer);
    }

    /**
     * this method manages with regex the coordinates inserted in cli by the player, like A5 or B2
     *
     * @param positions list of coordinates available sent by the server
     * @return int, index of the correspondent coordinate inserted by the user
     */
    @Override
    public int askPosition(List<int[]> positions) { //nuovo ask position che legge stringa
        String answer = null;
        boolean validAnswer;
        boolean validConfirmation;
        boolean found;
        boolean sure = false;
        int answerToInt = 0;
        Pattern pattern = Pattern.compile("[A-Ea-e].*[1-5]"); //regex for chess board coordinates input;
        view.clearScreen();
        view.turn();

        while (!sure) {
            printStream.println("Enter the coordinates of a box on the board to choose it");
            eraseBuffer();
            validAnswer = false;
            validConfirmation = false;
            found = false;
            while (!validAnswer) {
                answer = askAnswer();

                Matcher matcher = pattern.matcher(answer);

                if (matcher.find()) {
                    answer = cleanPosition(answer, matcher);
                    printStream.println(answer);
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

                } else {
                    printStream.println("Not valid answer. Try again");
                    printStream.println("You must enter a letter and a number that refer to a box on the board");
                }
            }
            printStream.println("you entered box " + answer + " are you sure?");
            printStream.println("1  yes     2   no");
            int confirm = 0;
            while (!validConfirmation) {
                try {
                    confirm = askNumber();
                } catch (NumberFormatException e) {
                    confirm = 0;
                }
                if (confirm == 1 || confirm == 2 || confirm == -1)
                    validConfirmation = true;

                else {
                    printStream.println("Not valid answer. Try again");
                    printStream.println("1  yes     2   no");
                }

                if (confirm == -1)
                    return confirm;
            }
            if (confirm == 1)
                sure = true;
        }
        view.clearScreen();
        return answerToInt;
    }

    /**
     * this method "cleans" String answer of useless characters inserted by the player when inserting coordinates
     *
     * @param answer  the coordinates inserted by the player that may contain wrong characters such as spaces or "ù" ecc
     * @param matcher of regex pattern found in answer
     * @return
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
     *
     * @param workers is a list of coordinates that represent the workers' positions
     * @return int is the index in 'workers' list that represent the worker chosen by the player
     */
    @Override
    public int askWorker(List<int[]> workers) {
        boolean valid = false;
        int answer = 0;
        for (int i = 0; i < workers.size(); i++) {
            printStream.print((i + 1) + "   for worker in " + getChessCoordinates(workers.get(i)) + "    ");
        }
        while (!valid) {

            try {
                answer = askNumber();
            } catch (NumberFormatException e) {
                answer = 0;
            }
            if ((answer <= workers.size() && answer > 0) || answer == -1) {
                valid = true;
            } else
                System.out.println("Not valid answer. Try again");
        }
        if (answer != -1)
            answer--;
        view.clearScreen();
        //view.turn();
        return answer;

    }

    /**
     * this method handles the choice of the player's Godcard
     *
     * @param cards list of cards to choose from
     * @return int is the index in 'cards' list that represent the card chosen by the player
     */
    @Override
    public int askCards(List<GodCardProxy> cards) {
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
            view.clearScreen();
            printStream.println("\n choose your game card!");

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

            while (!validCard) {
                try {
                    answer = askNumber();
                    printStream.println(answer);
                } catch (NumberFormatException e) {
                    answer = 0;
                }
                if ((answer <= cards.size() && answer > 0) ) {
                    validCard = true;
                } else if(answer == -1)
                    if(manageQuit())
                        return answer;
                else {
                    printStream.println("Not valid answer. Try again");
                    printStream.println("press the number displayed next to the god's name to choose it");
                }
            }
            answer--;

            printStream.print("you chose:");
            printStream.print("   " + cards.get(answer).name);

            printStream.println(" ");
            printStream.println("these will be your game card, are you sure? ");
            printStream.println("1  yes     2   no");

            //todo change following code with ask confirmation
            int confirm = 0;
            while (!validConfirmation) {
                try {
                    confirm = askNumber();
                } catch (NumberFormatException e) {
                    confirm = 0;
                }
                if (confirm == 1 || confirm == 2 || confirm == -1)
                    validConfirmation = true;

                else {
                    printStream.println("Not valid answer. Try again");
                    printStream.println("1  yes     2   no");
                }

                if (confirm == -1)
                    return confirm;
            }
            if (confirm == 1)
                sure = true;
        }
        view.clearScreen();
        return answer;
    }

    /**
     * this method manages the confirmation of the user when they can undo or use their GodCard power
     *
     * @param key indicates what the user is chosing for, undo or use power
     * @return int that corresponds to the user's answer
     */
    @Override
    public int askConfirmation(CommunicationProtocol key) {

        boolean valid = false;
        int answer = 0;
        while (!valid) {

            try {
                answer = askNumber();
            } catch (NumberFormatException e) {
                answer = 0;
            }
            if (answer == -1 || answer == 1 || answer == 2) {
                valid = true;
            } else
                printStream.println("Not valid answer. Try again");
        }
        if (answer != -1)
            answer--;
        view.clearScreen();
        return answer;
    }

    /**
     * this method views additional communications depending on the key
     *
     * @param key is a communication protocol key that indicates what the server is about to send
     */
    @Override
    public void prepareAdditionalCommunication(CommunicationProtocol key) {
        switch (key) {
            case GOD_POWER:
                view.clearScreen();
                view.turn();
                printStream.println("\nWould you like to use your power?");
                printStream.println("1  YES;  2 NO");
                break;
            case UNDO:
                view.clearScreen();
                view.turn();
                printStream.println("\nDo you want end your turn?");
                printStream.println("1  GO AHEAD;  2 UNDO");
                break;
        }
    }

    /**
     * this method updates the user's view, showing the changes made during the previous turn
     *
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
        view.clearScreen();
        view.turn();
    }

    /**
     * this method stores the data of the UI's player
     *
     * @param player
     */
    @Override
    public void setMyPlayer(PlayerProxy player) {
        started=true;
        this.myPlayer = player;
    }

    /**
     * this method stores the data of the opponents (just their color, name and card)
     *
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
            view.setInfoMessageBox(info);

        } else {
            for (PlayerProxy opponent : players) {
                if (opponentsAnnounced) {
                    printStream.println(opponent.name + " joined the game");
                }

            }
            printStream.println("the game is ready to start!");
        }
    }

    @Override
    public void connectionLost() {

    }

    /**
     * this method communicates to the user that the connection was refused
     *
     * @param host
     */
    @Override
    public void connectionFailed(String host) {
        System.err.println("Refused connection to" + host + "\nRetry.");
    }

    @Override
    public void startMatch() {

    }

    /**
     * this method stores the player that is currently playing
     *
     * @param player current player
     */
    @Override
    public void setCurrentPlayer(PlayerProxy player) {
        this.currentPlayer = player;
    }

    /**
     * this method manages the string 'events' that represents the events that happened during the last turn and that is sent by the server
     *
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
        view.setTurnMessageBox(turnMessage);
    }

    /**
     * this method writes the events occurred in the last turn in a readable way for the user
     *
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
     *
     * @param player
     */
    @Override
    public void setWinner(PlayerProxy player) {
        view.clearScreen();
        view.turn();
        printStream.println("The match is over!");
        if (player.equals(myPlayer)) {
            printStream.println("You won the match, congratulations! you are on the right patch to become a god!");
        } else {
            printStream.println(player.name + " won the match, better luck next time!");
        }

    }

    /**
     * this method displays the name of the playes who has lost the game and updates the info message of attribute view
     *
     * @param player the player who has lost
     */
    @Override
    public void setLoser(PlayerProxy player) {
        view.clearScreen();
        view.turn();
        if (player.equals(myPlayer)) {
            printStream.println("You lost, better luck next time!");
        } else {
            if(!opponents.isEmpty()) {
                List<String> info = new ArrayList<>();
                info.add(getActualWrittenColor(myPlayer.colour) + "your card is " + myPlayer.godCardProxy.name + RESET);
                info.add("your opponents are:");
                for (PlayerProxy opponent : opponents) {
                    if (opponent.equals(player))
                        info.add(getActualWrittenColor(opponent.colour) + opponent.name + " (eliminated):" + RESET);
                    else
                        info.add(getActualWrittenColor(opponent.colour) + opponent.name + ":" + RESET);

                    info.add(getActualWrittenColor(opponent.colour) + opponent.name + "'s card is " + opponent.godCardProxy.name + RESET);
                }
                view.setInfoMessageBox(info);

                opponents.remove(player);
                printStream.println(player.name + " lost!");
            }
            else printStream.println("a player lost connection or quit at the beginning of match");
        }

    }

    @Override
    public void timeOut() {
    printStream.println("More than 2 minutes passed and you ran out of time! you have been disconnected from the server ");
    }

    /**
     * this method asks to user the ip address
     *
     * @return String
     */
    @Override
    public String askIp() {
        boolean valid=false;
        String answer=null;
        while(!valid && !ended) {
            System.out.println("Write ip address to connect to:");
            eraseBuffer();
            answer = askAnswer();
            if (findQuitInString(answer)) {
                if(manageQuit()) {
                    ended = true;
                }
            }
            else valid=true;
        }
        if(valid)
            return answer;
        System.exit(0);
        return answer;
    }

    /**
     * this method asks to the user the match type they want to play
     *
     * @return int represents the number of the players
     */
    @Override
    public int askMatchType() {
        boolean valid = false;
        int answer = 0;
        while (!valid) {
            printStream.println("Choose match type:\n" + "1   for 1 vs 1\n" + "2   Three For All");
            try {
                answer = askNumber();
            } catch (NumberFormatException e) {
                answer = 0;
            }
            if (answer == -1 ) {//|| answer == 1 || answer == 2) {
                if (manageQuit()) {
                    valid = true;
                    ended=true;
                }
            }
            else if(answer == 1 || answer == 2)
                valid=true;
            else
                System.out.println("Not valid answer. Try again");
        }
        if (answer != -1)
            answer++;
        view.clearScreen();
        printStream.println("waiting for the match to start...");
        return answer;
    }

    /**
     * this method asks to the user the port to connect to
     *
     * @return int
     */
    @Override
    public int askPort() {
        boolean valid = false;
        int answer = 0;
        while (!valid) {
            System.out.println("Write port:");
            try {
                answer = askNumber();
            } catch (NumberFormatException e) {
                answer = 0;
            }
            if (answer >= 1024) {
                valid = true;
            }
            else if(answer==-1) {
                if (manageQuit()) {
                    valid = true;
                    ended=true;
                }
            }
            else
                System.out.println("Not valid answer. Try again");
        }
        return answer;
    }

    /**
     * this method asks the user name to the user
     * @return String
     * @throws IOException
     */
    @Override
    public String askUserName(CommunicationProtocol key) {
        boolean valid=false;
        String answer=null;
        while(!valid && !ended) {
            printStream.println("Write username:");
            eraseBuffer();
            answer = askAnswer();
            if(findQuitInString(answer)) {
                if (manageQuit()) {
                    valid = true;
                    ended=true;
                }
            }
            else if(!answer.equals("")) {
                valid = true;
            }
        }
        if(ended)
            System.exit(0);
        return answer;
    }

    /**
     * this method asks the challenger the game cards from which the other players will have to choose from
     *
     * @param cards these are all the game cards available
     * @return int[] , a list of ints that represent the indexes of the chosen cards
     */
    @Override
    public int[] askDeck(List<GodCardProxy> cards) {
        boolean sure = false;
        boolean sameCard;
        boolean validCard;
        boolean validConfirmation;
        int[] answer = new int[(opponents.size() + 1)];

        while (!sure) {
            view.clearScreen();
            printStream.println("\nYou are the challenger, choose the game cards! One for each player");
            printStream.println("GAME CARDS:");

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
            printStream.println("press the number displayed next to the god's name to choose it, you have to choose " + (opponents.size() + 1) + "  cards");

            for (int i = 0; i < (opponents.size() + 1); i++) {
                validCard = false;
                while (!validCard) {
                    try {
                        answer[i] = askNumber();
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
                        if (manageQuit()) {
                            answer[0] = -1;
                            ended=true;
                            return answer;
                        }
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
            printStream.println("1  yes     2   no");

            validConfirmation = false;
            int confirm = 0;
            while (!validConfirmation) {
                try {
                    confirm = askNumber();
                } catch (NumberFormatException e) {
                    confirm = 0;
                }
                if (confirm == 1 || confirm == 2)
                    validConfirmation = true;

                if (confirm == 1)
                    sure = true;
                else {
                    printStream.println("Not valid answer. Try again");
                    printStream.println("1  yes     2   no");
                }

            }
        }
        view.clearScreen();
        return answer;
    }


    public void run(){

        Client client = new Client(this);
        view.title();
        client.start();
        String input;
        while (!ended) {
            try {
                input = read();
            } catch (IOException e) {
                e.printStackTrace();
                input = "";
                ended = true;
            }
                if (checkIfUserIsQuitting(input)) {
                    ended = true;
                    try {
                        client.end();
                    } catch (ChannelClosedException e) {
                        e.printStackTrace();
                    }
                } else if (!input.equals("") && !findQuitInString(input))
                    updateBuffer(input);
                //else(cli.printstream.println("answer previous question"))
        }
        if(ended)
            System.exit(0);

    }

    public String read() throws IOException {
        String answer;
        answer = commandline.readLine();
        return answer;
    }

    public synchronized void updateBuffer(String string) {
        buffer.add(string);
        notifyAll();

    }

    public boolean myTurn() {
        if(currentPlayer!=null) {
            if (myPlayer.name.equals(currentPlayer.name))
                return true;
        }
        return false;
    }
    
    @Override
    public void serverDisconnected() {

    }
}