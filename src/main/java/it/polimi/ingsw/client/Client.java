package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.view.cli.Cli;
import it.polimi.ingsw.client.view.gui.Gui;
import it.polimi.ingsw.network.CommunicationChannel;
import it.polimi.ingsw.network.CommunicationProtocol;
import javafx.application.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import static it.polimi.ingsw.network.CommunicationProtocol.HI;
import static it.polimi.ingsw.network.CommunicationProtocol.valueOf;

public class Client extends Thread{

    private final View view;

    public Client(View view) {
        this.view = view;
    }

    public static void main(String[] args){
        if (args.length > 2 && args[2].equals("-cli"))
            Cli.main(args);
        else
            Application.launch(Gui.class, args);
    }

    @Override
    public void run(){
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
        ClientController clientController = new ClientController();
        while (!communicationChannel.isClosed()) {
            CommunicationProtocol key = null;
            try {
                key = communicationChannel.nextKey();
            } catch (SocketException e) {
                e.printStackTrace();
                System.err.println("Connection lost");
                view.connectionLost();
                System.exit(1);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Server lost");
                //Chiudere Client il Server
                System.exit(1);
            }

            view.prepareAdditionalCommunication(key);
            switch (key) {
                case BUILDS:
                case DESTINATIONS:
                case REMOVALS:
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
                case MATCHSTART:
                    clientController.manageMatchStart(communicationChannel, view);
                    break;
                case MATCHTYPE:
                    view.askMatchType(clientController, communicationChannel);
                    break;
                case MYPLAYER:
                    try {
                        clientController.manageMyPlayer(communicationChannel, view);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println("Manage My Player Error");
                    }
                    break;
                case OPPONENTS:
                    try {
                        clientController.manageListOfOpponents(communicationChannel, view);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Manage Opponents Error");
                    }
                    break;
                case UNIQUEUSERNAME:
                case USERNAME:
                    try {
                        view.askUserName(clientController, communicationChannel);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Manage Username Error");
                    }
                    break;
                case WAITFORPLAYERS:
                    clientController.waitForPlayers(communicationChannel, view);
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
