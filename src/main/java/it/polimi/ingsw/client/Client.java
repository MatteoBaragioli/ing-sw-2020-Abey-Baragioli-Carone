package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.network.CommunicationChannel;
import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.network.exceptions.ChannelClosedException;

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
    private CommunicationChannel communicationChannel;
    private boolean restart = true;
    private boolean close = false;

    public Client(View view) {
        this.view = view;
    }

    public void setRestart(boolean restart){
        this.restart = restart;
    }

    /**
     * This method creates a socket after getting the ip and port from the user
     */
    @Override
    public void run(){
        while(restart && !close) {
            restart = false;

            Socket socket = setupSocket();

            if(socket==null)
                System.exit(1);

            PrintWriter out = null;
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                System.exit(1);
            }

            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                System.exit(1);
            }

            ClientController clientController = new ClientController();

            communicationChannel = new CommunicationChannel(in, out);
            try {
                communicationChannel.writeKeyWord(HI);
                Listener listener = new Listener(communicationChannel, view);
                listener.start();
            } catch (ChannelClosedException ignored) {
            }

            while (!communicationChannel.isClosed()) {
                CommunicationProtocol key = null;
                try {
                    key = communicationChannel.popKey();
                } catch (ChannelClosedException e) {
                    key = INVALID;
                }
                if(key != INVALID) {
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
                                restart = true;
                            }
                            break;
                        case CARD:
                            try {
                                clientController.manageListOfCards(communicationChannel, view);
                            } catch (ChannelClosedException e) {
                                restart = true;
                            }
                            break;
                        case CURRENT_PLAYER:
                        case LOSER:
                        case MY_PLAYER:
                        case WINNER:
                            try {
                                clientController.managePlayer(key, communicationChannel, view);
                            } catch (ChannelClosedException e) {
                                restart = true;
                            }
                            break;
                        case DECK:
                            try {
                                clientController.manageDeck(communicationChannel, view);
                            } catch (ChannelClosedException e) {
                                restart = true;
                            }
                            break;
                        case GOD_POWER:
                        case UNDO:
                            try {
                                communicationChannel.popMessage();
                                clientController.manageConfirmation(key, communicationChannel, view);
                            } catch (ChannelClosedException e) {
                                restart = true;
                            }
                            break;
                        case MAP:
                            try {
                                clientController.manageMapAsListOfBoxes(communicationChannel, view);
                            } catch (ChannelClosedException e) {
                                restart = true;
                            }
                            break;
                        case MATCH_STORY:
                            try {
                                clientController.manageMatchStory(communicationChannel, view);
                            } catch (ChannelClosedException e) {
                                restart = true;
                            }
                            break;
                        case MATCH_TYPE:
                            try {
                                communicationChannel.popMessage();
                                communicationChannel.writeMatchType(view.askMatchType());
                            } catch (ChannelClosedException e) {
                                restart = true;
                            }
                            break;
                        case OPPONENTS:
                            try {
                                clientController.manageListOfOpponents(communicationChannel, view);
                            } catch (ChannelClosedException e) {
                                restart = true;
                            }
                            break;
                        case UNIQUE_USERNAME:
                        case USERNAME:
                            try {
                                communicationChannel.popMessage();
                                communicationChannel.writeUsername(view.askUserName(key));
                            } catch (IOException ignored) {
                            } catch (ChannelClosedException e) {
                                restart = true;
                            }
                            break;
                        default:
                            try {
                                communicationChannel.popMessage();
                            } catch (ChannelClosedException e) {
                                restart = true;
                            }
                    }
                }
            }

            try {
                in.close();
            } catch (IOException e) {
                System.exit(1);
            }

            out.close();

            try {
                socket.close();
            } catch (IOException e) {
                System.exit(1);
            }
        }
    }

    /**
     * This method constructs the socket that will be used
     * @return Socket
     */
    private Socket setupSocket() {
        Socket socket = null;
        boolean valid = false;
        int portNumber = 0;
        while (!valid && !close) {
            String hostName = view.askIp();
            portNumber = view.askPort();
            try {
                socket = new Socket(hostName, portNumber);
                valid = true;
            } catch (IllegalArgumentException ignored) {
            } catch (UnknownHostException | SocketException e) {
                view.connectionFailed(hostName);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        return socket;
    }

    /**
     * This method closes the client
     * @throws ChannelClosedException If communicationChannel is already closed
     */
    public void end() throws ChannelClosedException {
        close = true;
        restart = false;
        closeConnection();
    }

    /**
     * This method restarts the client
     * @throws ChannelClosedException If communicationChannel is already closed
     */
    public void restartClient() throws ChannelClosedException {
        restart = true;
        closeConnection();
    }

    /**
     * This method closes the connection between client and server
     * @throws ChannelClosedException If communicationChannel is already closed
     */
    private void closeConnection()  throws ChannelClosedException {
        if(communicationChannel!=null && !communicationChannel.isClosed()) {
            communicationChannel.writeKeyWord(QUIT);
        }
    }
}
