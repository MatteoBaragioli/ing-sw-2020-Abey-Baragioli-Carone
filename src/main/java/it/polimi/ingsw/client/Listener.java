package it.polimi.ingsw.client;

import it.polimi.ingsw.network.*;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;

import java.io.IOException;

import static it.polimi.ingsw.network.CommunicationProtocol.*;

public class Listener extends Thread {
    private final CommunicationChannel communicationChannel;

    public Listener(CommunicationChannel communicationChannel) {
        this.communicationChannel = communicationChannel;
    }

    public void run() {
        while (!communicationChannel.isClosed()) {
            try {
                CommunicationProtocol key = communicationChannel.nextKey();
                switch (key) {
                    case PING:
                        try {
                            communicationChannel.writeKeyWord(PONG);
                        } catch (ChannelClosedException e) {
                            e.printStackTrace();
                            System.err.println("PING Error");
                        }
                        break;
                    case CURRENT_PLAYER:
                    case LOSER:
                    case MATCH_STORY:
                    case MAP:
                    case MY_PLAYER:
                    case OPPONENTS:
                    case TIMEOUT:
                    case WINNER:           //todo c'è??

                        try {
                            communicationChannel.writeKeyWord(RECEIVED);
                        } catch (ChannelClosedException e) {
                            e.printStackTrace();
                            System.err.println("RECEIVED Error");
                        }
                        break;
                }
            } catch (IOException e) {
                //todo chiusura o disconnessione del server
                e.printStackTrace();
                communicationChannel.close();
            }
        }
    }
}
