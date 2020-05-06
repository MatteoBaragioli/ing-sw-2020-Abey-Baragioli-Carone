package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Box;
import it.polimi.ingsw.server.model.GodCard;
import it.polimi.ingsw.server.model.Worker;

import java.util.List;
import java.util.Scanner;

public class Cli implements View {

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
    public void prepareAdditionalCommunication(String message) {

    }

    @Override
    public void updateMap(List<Box> boxes) {

    }

    @Override
    public String askIp() {
        Scanner commandline = new Scanner(System.in);
        System.out.println("Write ip address to connect to:");
        return commandline.nextLine();
    }

    @Override
    public int askPort() {
        Scanner commandline = new Scanner(System.in);
        System.out.println("Write port:");
        return commandline.nextInt();
    }
}
