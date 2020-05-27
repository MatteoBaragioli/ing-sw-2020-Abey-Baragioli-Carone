package it.polimi.ingsw.client;

import it.polimi.ingsw.network.CommunicationChannel;
import it.polimi.ingsw.network.CommunicationProtocol;
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
                if (key == PING)
                    try {
                        communicationChannel.writeKeyWord(PONG);
                    } catch (ChannelClosedException e) {
                        e.printStackTrace();
                        System.err.println("PING Error");
                        System.exit(-1);
                    }
            } catch (IOException e) {
                e.printStackTrace();
                communicationChannel.close();
            }
        }
    }
}
