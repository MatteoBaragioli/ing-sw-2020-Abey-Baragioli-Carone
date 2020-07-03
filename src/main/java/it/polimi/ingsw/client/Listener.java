package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.network.*;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;

import java.io.IOException;

import static it.polimi.ingsw.network.CommunicationProtocol.*;

public class Listener extends Thread {
    public final CommunicationChannel communicationChannel;
    public final View view;

    public Listener(CommunicationChannel communicationChannel, View view) {
        this.communicationChannel = communicationChannel;
        this.view = view;
    }

    @Override
    public void run() {
        while (!communicationChannel.isClosed()) {
            CommunicationProtocol key;
            try {
                key = communicationChannel.nextKey();
            } catch (IOException | ChannelClosedException e) {
                view.connectionLost();
                communicationChannel.close();
                return;
            }
            switch (key) {
                case PING:
                    try {
                        communicationChannel.writeKeyWord(PONG);
                    } catch (ChannelClosedException e) {
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
                case WINNER:
                    try {
                        communicationChannel.writeKeyWord(RECEIVED);
                    } catch (ChannelClosedException e) {
                        System.err.println("RECEIVED Error");
                    }
                    break;
                default:
            }
        }
    }
}
