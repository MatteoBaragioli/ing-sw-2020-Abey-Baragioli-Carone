package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.network.objects.BoxProxy;
import it.polimi.ingsw.network.objects.GodCardProxy;
import it.polimi.ingsw.network.objects.PlayerProxy;


import java.io.*;
import java.util.List;

import static it.polimi.ingsw.network.CommunicationProtocol.QUIT;

public class Cli implements View{
    private static BufferedReader commandline = new BufferedReader(new InputStreamReader(System.in));
    private PrintStream printStream=new PrintStream(System.out);
    private ScreenView view=new ScreenView(printStream);
    private PlayerProxy myPlayer;
    private List<PlayerProxy> opponents;

    public ScreenView screenView(){
        return this.view;
    }

    public static String askAnswer() throws IOException {
        return commandline.readLine();
    }

    private boolean checkIfUserIsQuitting(String answer) {
        return answer.equals(QUIT.toString()) || answer.equals("quit");
    }

    public int askNumber() throws IOException, NumberFormatException {
        String answer = askAnswer();
        if (checkIfUserIsQuitting(answer))
            return -1;
        return Integer.parseInt(answer);
    }

    @Override
    public int askPosition(List<int[]> positions) {
        boolean valid = false;
        int answer = 0;
        for(int i = 0; i< positions.size(); i++){
           printStream.print((i+1)+"   for box in"+convertCoordinates(positions.get(i))+"    ");
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
                System.out.println("Not valid answer. Try again");
        }
        if(answer!=-1)
            answer--;
        return answer;
    }

    @Override
    public int askWorker(List<int[]> workers) {
        boolean valid = false;
        int answer = 0;
        for(int i=0; i<workers.size(); i++){
            printStream.print((i+1)+"   for worker in"+convertCoordinates(workers.get(i))+"    ");
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
            case GODPOWER:
                //view.clearScreen();
                //view.updateMap();
                view.turn();
                printStream.println("\nWould you like to use your power?");
                printStream.println("1  YES;  2 NO");
                break;
            case UNDO:
                //view.clearScreen();
                //view.updateMap();
                view.turn();
                printStream.println("\nDo you want end your turn?");
                printStream.println("1  UNDO;  2 GO AHEAD");
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
        System.out.println("Write username:");
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


    public String convertCoordinates(int[] coordinates){
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
