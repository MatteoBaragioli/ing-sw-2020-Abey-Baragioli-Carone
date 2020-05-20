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
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import static it.polimi.ingsw.network.CommunicationProtocol.HI;

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
        boolean valid = false;
        String hostName = null;
        int portNumber = 0;
        Socket socket = null;
        while (!valid) {
            hostName = view.askIp();
            portNumber = view.askPort();
            try {
                socket = new Socket(hostName, portNumber);
                valid = true;
            } catch (UnknownHostException e) {
                view.unknownHost(hostName, e);
                valid = false;
            } catch (ConnectException e) {
                view.connectionRefused(hostName, e);
                valid = false;
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Couldn't get I/O for the connection to " + hostName);
                System.exit(1);
            }
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
                    clientController.askMatchType(communicationChannel, view);
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
                        clientController.writeUsername(communicationChannel, view);
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
