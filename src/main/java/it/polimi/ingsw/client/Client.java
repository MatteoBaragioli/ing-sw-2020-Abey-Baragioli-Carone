package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.view.cli.Cli;
import it.polimi.ingsw.client.view.gui.*;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.network.exceptions.ChannelClosedException;
import javafx.application.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import static it.polimi.ingsw.network.CommunicationProtocol.*;

public class Client extends Thread {

    private final View view;
    private CommunicationChannel communicationChannel = null;

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
            } catch (IllegalArgumentException e) {
                valid = false;
            }catch (UnknownHostException | SocketException e) {
                view.connectionFailed(hostName);
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

        communicationChannel = new CommunicationChannel(in, out);
        try {
            communicationChannel.writeKeyWord(HI);
        } catch (ChannelClosedException e) {
            e.printStackTrace();
            view.connectionLost();
            System.exit(-1);
        }
        ClientController clientController = new ClientController();
        Listener listener = new Listener(communicationChannel);
        listener.start();

        while (!communicationChannel.isClosed()) {
            CommunicationProtocol key = null;
            try {
                key = communicationChannel.popKey();
            } catch (ChannelClosedException e) {
                e.printStackTrace();
                System.err.println("Connection lost");
                view.connectionLost();
                System.exit(1);
            }

            view.prepareAdditionalCommunication(key);
            switch (key) {
                case BUILD:
                case DESTINATION:
                case REMOVAL:
                case START_POSITION:
                case WORKER:
                    try {
                        clientController.manageListOfPositions(key, communicationChannel, view);
                    } catch (ChannelClosedException e) {
                        e.printStackTrace();
                        System.err.println("Manage boxes error");
                    }
                    break;
                case CARD:
                    try {
                        clientController.manageListOfCards(communicationChannel, view);
                    } catch (ChannelClosedException e) {
                        e.printStackTrace();
                        System.err.println("Connection closed Manage CARDS");
                        System.exit(-1);
                    }
                    break;
                case COUNTDOWN:
                    try {
                        clientController.manageCountDown(communicationChannel, view);
                    } catch (ChannelClosedException e) {
                        e.printStackTrace();
                        System.err.println("Connection closed COUNTDOWN");
                        System.exit(-1);
                    }
                    break;
                case CURRENT_PLAYER:
                case LOSER:
                case MY_PLAYER:
                case WINNER:
                    try {
                        clientController.managePlayer(key, communicationChannel, view);
                    } catch (ChannelClosedException e) {
                        e.printStackTrace();
                        System.err.println("Connection closed Manage Player");
                        System.exit(-1);
                    }
                    break;
                case DECK:
                    try {
                        clientController.manageDeck(communicationChannel, view);
                    } catch (ChannelClosedException e) {
                        e.printStackTrace();
                        System.err.println("Connection closed Manage DECK");
                        System.exit(-1);
                    }
                    break;
                case GOD_POWER:
                case UNDO:
                    try {
                        communicationChannel.popMessage();
                        clientController.manageConfirmation(key, communicationChannel, view);
                    } catch (ChannelClosedException e) {
                        e.printStackTrace();
                        System.err.println("Connection closed Manage CONFIRMATION");
                        System.exit(-1);
                    }
                    break;
                case MAP:
                    try {
                        clientController.manageMapAsListOfBoxes(communicationChannel, view);
                    } catch (ChannelClosedException e) {
                        e.printStackTrace();
                        System.err.println("Connection closed Manage MAP");
                        System.exit(-1);
                    }
                    break;
                case MATCH_START:
                    clientController.manageMatchStart(communicationChannel, view);
                    break;
                case MATCH_STORY:
                    try {
                        clientController.manageMatchStory(communicationChannel, view);
                    } catch (ChannelClosedException e) {
                        e.printStackTrace();
                        System.err.println("Connection closed Manage MATCH_STORY");
                        System.exit(-1);
                    }
                    break;
                case MATCH_TYPE:
                    try {
                        communicationChannel.popMessage();
                        communicationChannel.writeMatchType(view.askMatchType());
                    } catch (ChannelClosedException e) {
                        e.printStackTrace();
                        System.err.println("Connection closed MATCH_TYPE");
                        System.exit(-1);
                    }
                    break;
                case OPPONENTS:
                    try {
                        clientController.manageListOfOpponents(communicationChannel, view);
                    } catch (ChannelClosedException e) {
                        e.printStackTrace();
                        System.err.println("Connection closed OPPONENTS");
                        System.exit(-1);
                    }
                    break;
                case UNIQUE_USERNAME:
                case USERNAME:
                    try {
                        communicationChannel.popMessage();
                        communicationChannel.writeUsername(view.askUserName());
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("View Error");
                    } catch (ChannelClosedException e) {
                        e.printStackTrace();
                        System.err.println("Connection Lost");
                        System.exit(-1);
                    }
                    break;
                case WAIT_FOR_PLAYERS:
                    try {
                        communicationChannel.popMessage();
                        clientController.waitForPlayers(communicationChannel, view);
                    } catch (ChannelClosedException e) {
                        e.printStackTrace();
                        System.err.println("Connection Lost");
                        System.exit(-1);
                    }
                    break;
                default:

            }
        }

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Can't get close BufferReader Clientside");
            System.exit(1);
        }

        out.close();

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Can't get close socket Clientside");
            System.exit(1);
        }


    }

    public void end() throws ChannelClosedException {
        if(communicationChannel!=null) {
            communicationChannel.writeKeyWord(QUIT);
        }
        stop();
    }
}
