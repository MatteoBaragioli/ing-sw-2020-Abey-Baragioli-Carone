package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.network.objects.BoxProxy;
import it.polimi.ingsw.network.objects.GodCardProxy;
import it.polimi.ingsw.network.objects.PlayerProxy;
import static it.polimi.ingsw.client.view.cli.Coordinates.*;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cli implements View {
    private static BufferedReader commandline = new BufferedReader(new InputStreamReader(System.in));
    private PrintStream printStream = new PrintStream(System.out);
    private ScreenView view = new ScreenView(printStream);
    private PlayerProxy myPlayer;
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
        boolean sure = false;
        int answerToInt = 0;
        Pattern pattern = Pattern.compile("[A-Ea-e].*[1-5]"); //regex for chess board coordinates input;
        view.clearScreen();
        view.turn();
        printStream.println("enter the coordinates of a box on the board");
        while (!sure) {
            validAnswer = false;
            validConfirmation = false;
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
                    validAnswer = true;
                    answer = cleanPosition(answer, matcher);
                } else {
                    printStream.println("Not valid answer. Try again");
                    printStream.println("You must enter a letter and a number that refer to a box on the board");
                }
            }
            printStream.println("you entered box " + answer + " are you sure?");
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

        boolean found = false;
        for (int i = 0; i < positions.size() && !found; i++) {
            if (Arrays.equals(positions.get(i), getCartesianCoodinates(answer))) {
                answerToInt = i;
                found = true;
            }
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
            printStream.print((i+1)+"   for worker in"+ convertToChessCoordinates(workers.get(i))+"    ");
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

    }

    @Override
    public void tellStory(List<String> events) {

    }

    @Override
    public void setWinner(PlayerProxy player) {

    }

    @Override
    public void setLoser(PlayerProxy player) {

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
                boolean valid = false;
                while (!valid) {
                    try {
                        answer[i] = askNumber();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NumberFormatException e) {
                        answer[i] = 0;
                    }
                    if ((answer[i] <= cards.size() && answer[i] > 0) || answer[i]==-1) {
                        valid = true;
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

            boolean valid = false;
            int confirm = 0;
            while (!valid) {
                try {
                    confirm = askNumber();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NumberFormatException e) {
                    confirm = 0;
                }
                if (confirm == 1 || confirm == 2)
                    valid = true;

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


    public String convertToChessCoordinates(int[] coordinates){
        char first;
        char second;
        switch(coordinates[0]) {
            case 0:
                first = 'A';
                break;
            case 1:
                first = 'B';
                break;
            case 2:
                first = 'C';
                break;
            case 3:
                first = 'D';
                break;
            case 4:
                first = 'E';
                break;
            default:
                return "error in conversion of coordinates";
        }
        switch(coordinates[1]) {
            case 0:
                second = '1';
                break;
            case 1:
                second = '2';
                break;
            case 2:
                second = '3';
                break;
            case 3:
                second = '4';
                break;
            case 4:
                second = '5';
                break;
            default:
                return "error in conversion of coordinates";

        }
        return first+","+second;
    }

    public static void main(String[] args){
        Cli cli = new Cli();
        Client client = new Client(cli);
        cli.view.title();
        client.start();
    }
}
