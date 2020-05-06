package it.polimi.ingsw.client;

import it.polimi.ingsw.server.socket.CommunicationChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner commandline = new Scanner(System.in);
        System.out.println("Choose view type:\n" + "1)CLI\n" + "2)GUI");
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
        communicationChannel.write("HI");
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        try {
            String message = communicationChannel.read();
            while (true) {
                view.prepareAdditionalCommunication(message);
                /*
                switch (message) {
                    case ("deck"):
                    case("choose your game card"):
                    case("card"):
                        manageListOfCards(communicationChannel(), view);
                        break;
                    case("possible destinations list of boxes"):
                    case("possible builds list of boxes"):
                    case("setup list of boxes"):
                        manageListOfBoxes(communicationChannel(), view);
                        break;
                    case("map as list of boxes"):
                        manageMapAsListOfBoxes(communicationChannel(), view);
                        break;
                    case("list of movable workers"):
                        manageListOfWorkers(communicationChannel(), view);
                        break;
                    case("undo"):
                    case("use power"):
                        manageConfirmation(communicationChannel(), view);
                        break;
                    default:
                        communicationChannel().write("WTF?!");
                }
                */
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}