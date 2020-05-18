package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.server.model.Box;
import it.polimi.ingsw.server.model.GodCard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Worker;

import java.io.*;
import java.util.List;

public class Cli implements View{
    private static BufferedReader commandline = new BufferedReader(new InputStreamReader(System.in));

    private PrintStream printStream=new PrintStream(System.out);

    public static String askAnswer() throws IOException {
        return commandline.readLine();
    }

    public int askNumber() throws IOException, NumberFormatException {
        return Integer.parseInt(askAnswer());
    }

    @Override
    public int askBox(List<Box> boxes) {
        return 0;
    }

    @Override
    public int askWorker(List<Worker> workers) {
        return 0;
    }

    @Override
    public int askCards(List<GodCard> cards) {
        return 0;
    }

    @Override
    public boolean askConfirmation() {
        return false;
    }

    @Override
    public void prepareAdditionalCommunication(CommunicationProtocol key) {

    }

    @Override
    public void updateMap(List<Box> boxes) {

    }

    @Override
    public void setPlayersInfo(List<Player> players) {

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
            System.out.println("Choose match type:\n" + "1) 1 vs 1\n" + "2)Three For All");
            try {
                answer = askNumber();
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            } catch (NumberFormatException e) {
                answer = 0;
            }
            if (answer == 1 || answer == 2) {
                valid = true;
                System.out.println("Waiting for the match to start");
            }
            else
                System.out.println("Not valid answer. Try again");
        }
        return answer;
    }

    @Override
    public int askPort() {
        System.out.println("Write port:");
        try {
            return Integer.parseInt(commandline.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public String askUserName() {
        System.out.println("Write username:");
        try {
            return commandline.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args){
        Cli cli = new Cli();
        Client client = new Client();
        client.start(cli);
    }
}
