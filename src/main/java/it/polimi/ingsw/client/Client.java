package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.cli.Cli;
import it.polimi.ingsw.client.view.gui.Gui;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.network.CommunicationChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import static it.polimi.ingsw.network.CommunicationProtocol.*;

public class Client {
    public static void main(String[] args) {
        Scanner commandline = new Scanner(System.in);
        System.out.println("Choose view type:\n" + "1   CLI\n" + "2    GUI");
        int choice = commandline.nextInt();
        boolean valid = false;
        View view;
        while (!valid)
            if (choice == 1 || choice == 2)
                valid = true;
            else {
                System.out.println("Write 1 or 2");
                choice = commandline.nextInt();
            }
        if (choice == 1)
            view = new Cli();
        else
            view = new Gui();

        String hostName = view.askIp();
        int portNumber = view.askPort();
        Socket socket = null;
        try {
            socket = new Socket(hostName, portNumber);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }

        PrintWriter out = null;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Couldn't get I/O for the printer");
            System.exit(1);
        }

        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Couldn't get I/O for the buffer");
            System.exit(1);
        }

        CommunicationChannel communicationChannel = new CommunicationChannel(in, out);
        communicationChannel.writeKeyWord(HI);
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        ClientController clientController = new ClientController();
        while (!communicationChannel.isClosed()) {
            CommunicationProtocol key = null;
            try {
                key = communicationChannel.nextKey();
            }
            catch (IOException e) {
                e.printStackTrace();
                System.err.println("Couldn't read key CLIENT");
                System.exit(1);
            }
            view.prepareAdditionalCommunication(key);
            switch (key) {
                case BUILDS:
                case DESTINATIONS:
                case STARTPOSITION:
                    try {
                        clientController.manageListOfBoxes(communicationChannel, view);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Manage boxes error");
                    }
                    break;
                case CARD:
                case DECK:
                    try {
                        clientController.manageListOfCards(communicationChannel, view);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Manage cards error");
                    }
                    break;
                case GODPOWER:
                case UNDO:
                    clientController.manageConfirmation(communicationChannel, view);
                    break;
                case MAP:
                    try {
                        clientController.manageMapAsListOfBoxes(communicationChannel, view);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Manage map error");
                    }
                    break;
                case MATCHTYPE:
                    clientController.askMatchType(communicationChannel, view);
                    break;
                case UNIQUEUSERNAME:
                case USERNAME:
                    clientController.askUsername(communicationChannel, view);
                    break;
                case WORKER:
                    try {
                        clientController.manageListOfWorkers(communicationChannel, view);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Manage workers error");
                    }
                    break;
                default:
                    communicationChannel.write("WTF?!");
            }
        }
    }
}