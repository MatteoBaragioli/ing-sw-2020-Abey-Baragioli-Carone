package it.polimi.ingsw.client.view.cli;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.client.Client;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.network.CommunicationProtocol;
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

public class Cli implements View {
    private static BufferedReader commandline = new BufferedReader(new InputStreamReader(System.in));
    private PrintStream printStream = new PrintStream(System.out);
    private ScreenView view = new ScreenView(printStream);
    private PlayerProxy myPlayer;
    private PlayerProxy currentPlayer;
    private List<PlayerProxy> opponents;

    public ScreenView screenView() {
        return this.view;
    }

    public static String askAnswer() throws IOException {
        return commandline.readLine();
    }

    private boolean checkIfUserIsQuitting(String answer) {
        Pattern pattern1 = Pattern.compile("QUIT");
        Pattern pattern2 = Pattern.compile("Quit");
        Pattern pattern3 = Pattern.compile("quit");
        Matcher matcher1 = pattern1.matcher(answer);
        Matcher matcher2 = pattern2.matcher(answer);
        Matcher matcher3 = pattern3.matcher(answer);
        return matcher1.find() || matcher2.find() || matcher3.find();
    }

    public int askNumber() throws IOException, NumberFormatException {
        String answer = askAnswer();
        if (checkIfUserIsQuitting(answer))
            return -1;
        return Integer.parseInt(answer);
    }

    @Override
    public int askPosition(List<int[]> positions) { //nuovo ask position che legge stringa
        String answer = null;
        boolean validAnswer;
        boolean validConfirmation;
        boolean found ;
        boolean sure = false;
        int answerToInt = 0;
        Pattern pattern = Pattern.compile("[A-Ea-e].*[1-5]"); //regex for chess board coordinates input;
        view.clearScreen();
        view.turn();

        while (!sure) {
            printStream.println("Enter the coordinates of a box on the board to choose it");
            validAnswer = false;
            validConfirmation = false;
            found = false;
            while (!validAnswer) {
                try {
                    answer = askAnswer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (checkIfUserIsQuitting(answer))
                    return -1;

                Matcher matcher = pattern.matcher(answer);

                if (matcher.find()) {
                    answer = cleanPosition(answer, matcher);
                    printStream.println(answer);
                    int[] paragon=getCartesianCoordinates(answer);
                    for (int i = 0; i < positions.size() && !found; i++) {

                        if (Arrays.equals(positions.get(i), paragon)) {
                            answerToInt = i;
                            found = true;
                        }
                    }
                    if (!found) {
                        printStream.println("the box you chose is not available according to the game rules");
                        printStream.println("You must enter a box between ");
                        for(int[] position : positions){
                            printStream.print(getChessCoordinates(position)+"  ");
                        }
                    }
                    validAnswer=found;

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
                } catch (IOException e) {
                    e.printStackTrace();
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

        return answerToInt;
    }


    public String cleanPosition(String answer, Matcher matcher) {
        char letter;
        // matcher.find();
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



    /*  @Override
    public int askPosition(List<int[]> positions) {
        boolean valid = false;
        int answer = 0;
        for(int i = 0; i< positions.size(); i++){
           printStream.print((i+1)+"   for box in "+ convertToChessCoordinates(positions.get(i))+"    ");
            if (positions.size()%4==0) {
                if (i % 4 == 0)
                    printStream.print("\n");
            }
            if (positions.size()%5==0) {
                if (i % 5 == 0)
                    printStream.print("\n");
            }
        }
        while (!valid) {

            try {
                answer = askNumber();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                answer = 0;
            }
            if ((answer<= positions.size() && answer>0) || answer == -1) {
                valid = true;
            }
            else
                printStream.println("Not valid answer. Try again");

        }
        if(answer!=-1)
            answer--;
        return answer;
    }*/

    @Override
    public int askWorker(List<int[]> workers) {
        boolean valid = false;
        int answer = 0;
        for(int i=0; i<workers.size(); i++){
            printStream.print((i+1)+"   for worker in"+ getChessCoordinates(workers.get(i))+"    ");
        }
        while (!valid) {

            try {
                answer = askNumber();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                answer = 0;
            }
            if ((answer<=workers.size() &&answer>0) || answer == -1) {
                valid = true;
            }
            else
                System.out.println("Not valid answer. Try again");
        }
        if (answer!=-1)
            answer--;
        return answer;

    }

    @Override
    public int askCards(List<GodCardProxy> cards) {
        boolean sure=false;
        boolean validCard=false;
        boolean validConfirmation=false;
        int answer = 0;

        if(cards.size()==1)
            return 0;

        while (!sure) {
            answer = 0;
            validCard=false;
            validConfirmation=false;
            view.clearScreen();
            printStream.println("\n choose your game card!");

            for(int i=0; i<cards.size(); i++) {
                printStream.println((i+1)+" "+cards.get(i).name +":    ");
                if(cards.get(i).setUpDescription!=null)
                    printStream.println(cards.get(i).setUpDescription);
                if(cards.get(i).description!=null)
                    printStream.println(cards.get(i).description);
                if(cards.get(i).winDescription!=null)
                    printStream.println(cards.get(i).winDescription);
            }
            printStream.print("\n");
            printStream.println("press the number displayed next to the god's name to choose it");

            while (!validCard) {
                try {
                    answer = askNumber();
                    printStream.println(answer);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NumberFormatException e) {
                    answer = 0;
                }
                if ((answer <= cards.size() && answer > 0) || answer == -1) {
                    validCard = true;
                } else {
                    printStream.println("Not valid answer. Try again");
                    printStream.println("press the number displayed next to the god's name to choose it");
                }
                if (answer==-1)
                    return answer;
            }
            answer--;

            printStream.print("you chose:");
            printStream.print("   " + cards.get(answer).name);

            printStream.println(" ");
            printStream.println("these will be your game card, are you sure? ");
            printStream.println("1  yes     2   no");


            int confirm = 0;
            while (!validConfirmation) {
                try {
                    confirm = askNumber();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NumberFormatException e) {
                    confirm = 0;
                }
                if (confirm == 1 || confirm == 2 || confirm==-1)
                    validConfirmation = true;

                else {
                    printStream.println("Not valid answer. Try again");
                    printStream.println("1  yes     2   no");
                }

                if (confirm==-1)
                    return confirm;
            }
            if (confirm == 1)
                sure = true;
        }
        return answer;
        }

    @Override
    public int askConfirmation(CommunicationProtocol key) {

        boolean valid = false;
        int answer = 0;
        while (!valid) {

            try {
                answer = askNumber();
            } catch (IOException e) {
                e.printStackTrace();
                answer = -1;
            } catch (NumberFormatException e) {
                answer = 0;
            }
            if (answer == -1 || answer == 1 || answer == 2) {
                valid = true;
            }
            else
                printStream.println("Not valid answer. Try again");
        }
        if (answer != -1)
            answer--;
        return answer;
    }

    @Override
    public void prepareAdditionalCommunication(CommunicationProtocol key) {
        switch(key) {
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


    @Override
    public void updateMap(List<BoxProxy> boxes) {
        for(BoxProxy boxProxy:boxes) {

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
                    view.map().position(boxProxy.position[0], boxProxy.position[1]).firstFloorBlock();
                }

                if(boxProxy.dome){
                    view.map().position(boxProxy.position[0], boxProxy.position[1]).domeBlock();
                 }

                if (boxProxy.occupier!=null) {
                    view.map().position(boxProxy.position[0], boxProxy.position[1]).withWorker(boxProxy.occupier.gender, boxProxy.occupier.colour);
                }

        }
        view.clearScreen();
        view.turn();
    }

    @Override
    public void setMyPlayer(PlayerProxy player) {
        this.myPlayer=player;

    }

    @Override
    public void setOpponentsInfo(List<PlayerProxy> players) {
        opponents = players;
        if(players.get(0).godCardProxy!=null && players.get(0).colour!=null) {
            List<String> info = new ArrayList<>();
            info.add(getActualWrittenColor(myPlayer.colour)+"your card is "+ myPlayer.godCardProxy.name+ Colors.RESET);
            info.add("your opponents are:");
            for(PlayerProxy opponent: players){
                info.add(getActualWrittenColor(opponent.colour)+opponent.name+ ":"+Colors.RESET);
                info.add(getActualWrittenColor(opponent.colour)+opponent.name+ "'s card is "+opponent.godCardProxy.name+Colors.RESET);
            }
            view.setInfoMessageBox(info);
        }
    }

    @Override
    public void connectionLost() {

    }

    @Override
    public void connectionFailed(String host) {
        System.err.println("Refused connection to" + host + "\nRetry.");
    }

    @Override
    public void startMatch() {

    }

    @Override
    public void setCurrentPlayer(PlayerProxy player) {
        this.currentPlayer=currentPlayer;
    }

    @Override
    public void tellStory(List<String> events) {
        List<String> turnMessage = new ArrayList<>();
        for(String event : events){
            String[] content = event.split(MatchStory.STORY_SEPARATOR);

            String player = content[0];

            Type chosenWorkerType = new TypeToken<int[]>() {}.getType();
            int[] chosenWorker = new Gson().fromJson(content[1], chosenWorkerType);

            Type actionType = new TypeToken<CommunicationProtocol>() {}.getType();
            CommunicationProtocol action = new Gson().fromJson(content[2], actionType);

            Type destinationType = new TypeToken<int[]>() {}.getType();
            int[] destination = new Gson().fromJson(content[3], destinationType);
            turnMessage.addAll(writeStory(player, chosenWorker, action, destination));
        }
        view.setTurnMessageBox(turnMessage);
    }

    public List<String> writeStory(String player, int[] chosenWorker, CommunicationProtocol action, int[] destination) {
        List<String> turnEvent = new ArrayList<>();

        switch (action) {
            case DESTINATION: {
                turnEvent.add(player+ " moved from box " + getChessCoordinates(chosenWorker) + " to box " + getChessCoordinates(destination));
                break;
            }
            case BUILD:{
                turnEvent.add(player+" Built on box "+ getChessCoordinates(destination) );
                break;
            }
            case REMOVAL:{
                turnEvent.add(player+" Removed block on box "+ getChessCoordinates(destination)+ " with worker in box (" + getChessCoordinates(destination));
                break;
            }
        }
        return turnEvent;
    }

    @Override
    public void setWinner(PlayerProxy player) {
        view.clearScreen();
        view.turn();
        printStream.println("The match is over!");
        if(player.equals(myPlayer)){
            printStream.println("You won the match, congratulations! you are on the right patch to become a god!");
        }
        else{
            printStream.println(player.name +" won the match, better luck next time!");
        }

    }

    @Override
    public void setLoser(PlayerProxy player) {
        view.clearScreen();
        view.turn();
        if(player.equals(myPlayer)){
            printStream.println("You lost, better luck next time!");
        }
        else{
            opponents.remove(player);
            printStream.println(player.name +" lost!");
        }

    }

    @Override
    public String askIp() {
        System.out.println("Write ip address to connect to:");
        try {
            return askAnswer();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int askMatchType() {
        boolean valid = false;
        int answer = 0;
        while (!valid) {
            printStream.println("Choose match type:\n" + "1   for 1 vs 1\n" + "2   Three For All");
            try {
                answer = askNumber();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                answer = 0;
            }
            if (answer == -1 || answer == 1 || answer == 2) {
                valid = true;
            }
            else
                System.out.println("Not valid answer. Try again");
        }
        if (answer != -1)
            answer++;
        printStream.println("waiting for the match to start...");
        return answer;
    }

    @Override
    public int askPort() {
        boolean valid = false;
        int answer = 0;
        while (!valid) {
            System.out.println("Write port:");
            try {
                answer = askNumber();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                answer = 0;
            }
            if (answer >= 1024) {
                valid = true;
            }
            else
                System.out.println("Not valid answer. Try again");
        }
        return answer;
    }

    @Override
    public String askUserName() throws IOException {
        printStream.println("Write username:");
        return commandline.readLine();
    }


    @Override
    public int[] askDeck(List<GodCardProxy> cards) {
        boolean sure=false;
        boolean sameCard;
        boolean validCard;
        boolean validConfirmation;
        int[] answer=new int[(opponents.size()+1)];

        while(!sure) {
            view.clearScreen();
            printStream.println("\nYou are the challenger, choose the game cards! One for each player");
            printStream.println("GAME CARDS:");

            for(int i=0; i<cards.size(); i++) {
                printStream.println((i+1)+" "+cards.get(i).name +":    ");
                if(cards.get(i).setUpDescription!=null)
                    printStream.println(cards.get(i).setUpDescription);
                if(cards.get(i).description!=null)
                    printStream.println(cards.get(i).description);
                if(cards.get(i).winDescription!=null)
                    printStream.println(cards.get(i).winDescription);

            }
            printStream.print("\n");
            printStream.println("press the number displayed next to the god's name to choose it, you have to choose "+(opponents.size()+1)+"  cards");

            for (int i = 0; i < (opponents.size() + 1); i++) {
                validCard = false;
                while (!validCard) {
                    try {
                        answer[i] = askNumber();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NumberFormatException e) {
                        answer[i] = 0;
                    }
                    if ((answer[i] <= cards.size() && answer[i] > 0) || answer[i]==-1) {
                        sameCard=false;
                        for (int j=0; j<i && !sameCard; j++) {
                            if (answer[i] == answer[j] && answer[i]!=-1) {
                                sameCard=true;
                                printStream.println("\nyou already chose " + cards.get((answer[i]-1)).name + " choose another card");

                            }
                        }
                            if(!sameCard)
                                validCard = true;
                    }
                     else {
                       printStream.println("Not valid answer. Try again");
                       printStream.println("press the number displayed next to the god's name to choose it");
                    }
                    if (answer[i] == -1) {
                        answer[0] = -1;
                        return answer;
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
                } catch (IOException e) {
                    e.printStackTrace();
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
        return answer;
    }


    public static void main(String[] args){
        Cli cli = new Cli();
        Client client = new Client(cli);
        cli.view.title();
        client.start();
    }
}
