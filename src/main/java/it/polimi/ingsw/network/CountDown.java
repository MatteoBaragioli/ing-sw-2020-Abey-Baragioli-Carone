package it.polimi.ingsw.network;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;

public class CountDown extends Thread {
    public final CommunicationChannel communicationChannel;
    public final int MINUTE = 60;
    public final int SECOND = 1000;
    public final CommunicationProtocol key;

    public CountDown(CommunicationChannel communicationChannel, CommunicationProtocol key) {
        this.communicationChannel = communicationChannel;
        this.key = key;
    }

    public synchronized void run() {
        int i = MINUTE;
        while (i > 0 && !communicationChannel.hasMessages(key)) {
            try {
                sleep(SECOND);
                communicationChannel.countdown(i);
                i--;
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.err.println("WAIT NON FUNZIONA");
                System.exit(-2);
            } catch (ChannelClosedException e) {
                System.err.println("Connection closed");
                System.exit(-1);
            }
        }
        communicationChannel.timeIsOut();
        notifyAll();
    }
}
