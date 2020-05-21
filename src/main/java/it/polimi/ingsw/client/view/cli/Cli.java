package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.network.objects.BoxProxy;
import it.polimi.ingsw.network.objects.GodCardProxy;
import it.polimi.ingsw.network.objects.PlayerProxy;


import java.io.*;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.List;

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

    public int askNumber() throws IOException, NumberFormatException {
        return Integer.parseInt(askAnswer());
    }

    @Override
    public int askBox(List<int[]> boxes) {
        boolean valid = false;
        int answer = 0;
        for(int i=0; i<boxes.size(); i++){
           printStream.print((i+1)+"   for box in"+convertCoordinates(boxes.get(i))+"    ");
        }
        while (!valid) {

            try {
                answer = askNumber();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                answer = 0;
            }
            if (answer<=boxes.size() && answer>0) {
                valid = true;
            }
            else
                System.out.println("Not valid answer. Try again");
        }
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
            if (answer<=workers.size() &&answer>0) {
                valid = true;
            }
            else
                System.out.println("Not valid answer. Try again");
        }
        return answer;

    }

    @Override
    public int askCards(List<GodCardProxy> cards) {
        boolean valid = false;
        int answer = 0;
        printStream.println("GAME CARDS:");
        printStream.println("\n");
        for(int i=0; i<cards.size(); i++){
            printStream.println((i+1)+" "+cards.get(i).name +":    ");
            printStream.println(cards.get(i).description);
            printStream.println(" ");
        }
        for(int i=0; i<cards.size(); i++){
            printStream.println((i+1)+"   for card "+cards.get(i).name);

        }
        while (!valid) {

            try {
                answer = askNumber();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                answer = 0;
            }
            if (answer<=cards.size() && answer>0) {
                valid = true;
            }
            else
                System.out.println("Not valid answer. Try again");
        }
        return answer;
    }

    @Override
    public boolean askConfirmation() {

        boolean valid = false;
        int answer = 0;
        while (!valid) {

            try {
                answer = askNumber();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                answer = 0;
            }
            if (answer == 1 || answer == 2) {
                valid = true;
            }
            else
                System.out.println("Not valid answer. Try again");
        }
        if (answer==1) {
            return true;
        }
        else
            return false;

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
            case DECK:
                //view.clearScreen();
                //view.updateMap();
                view.turn();
                printStream.println("\nYou are the challenger, choose the game cards! One for each player");
                printStream.println("\n");
                break;
            case CARD:
                //view.clearScreen();
                //view.updateMap();
                view.turn();
                printStream.println("\n choose your game card!");
                printStream.println("\n");

            }
        }


    @Override
    public void updateMap(List<BoxProxy> boxes) {
        for(BoxProxy boxProxy:boxes) {

                if (boxProxy.level == 0) {
                    view.map().position(boxProxy.position[0], boxProxy.position[1]).groundFloorBlock();
                            if(boxProxy.dome==true){
                                view.map().position(boxProxy.position[0], boxProxy.position[1]).domeBlock();
                            }
                }
                if (boxProxy.level == 1) {
                    view.map().position(boxProxy.position[0], boxProxy.position[1]).firstFloorBlock();
                    if(boxProxy.dome==true){
                        view.map().position(boxProxy.position[0], boxProxy.position[1]).domeBlock();
                    }
                }
                if (boxProxy.level == 2) {
                    view.map().position(boxProxy.position[0], boxProxy.position[1]).secondFloorBlock();
                    if(boxProxy.dome==true){
                        view.map().position(boxProxy.position[0], boxProxy.position[1]).domeBlock();
                    }
                }
                if (boxProxy.level == 3) {
                    view.map().position(boxProxy.position[0], boxProxy.position[1]).firstFloorBlock();
                    if(boxProxy.dome==true){
                        view.map().position(boxProxy.position[0], boxProxy.position[1]).domeBlock();
                    }
                }
                if (boxProxy.occupier!=null) {
                    view.map().position(boxProxy.position[0], boxProxy.position[1]).withWorker(boxProxy.occupier.gender, boxProxy.occupier.colour);
                }

        }

    }

    @Override
    public void setMyPlayer(PlayerProxy player) {
        this.myPlayer=player;

    }

    @Override
    public void setOpponentsInfo(List<PlayerProxy> players) {
        opponents.addAll(players);
    }

    @Override
    public void connectionLost() {

    }

    @Override
    public void unknownHost(String host, UnknownHostException e) {
        System.err.println("Don't know about host " + host + "\nRetry.");
    }

    @Override
    public void connectionRefused(String host, ConnectException e) {
        System.err.println("Refused connection to" + host + "\nRetry.");
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
            System.out.println("Choose match type:\n" + "1   for 1 vs 1\n" + "2   Three For All");
            try {
                answer = askNumber();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                answer = 0;
            }
            if (answer == 1 || answer == 2) {
                valid = true;
            }
            else
                System.out.println("Not valid answer. Try again");
        }
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
