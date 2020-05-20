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
        return 0;
    }

    @Override
    public int askWorker(List<int[]> workers) {
        return 0;
    }

    @Override
    public int askCards(List<GodCardProxy> cards) {
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
    public void updateMap(List<BoxProxy> boxes) {

    }

    @Override
    public void setMyPlayer(PlayerProxy player) {

    }

    @Override
    public void setOpponentsInfo(List<PlayerProxy> players) {

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

    public static void main(String[] args){
        Cli cli = new Cli();
        Client client = new Client(cli);
        cli.view.title();
        client.start();
    }
}
