package it.polimi.ingsw.network;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;

public class CountDown extends Thread {
    final public CommunicationChannel communicationChannel;
    final public int MINUTE = 60;
    final public int SECOND = 1000;

    public CountDown(CommunicationChannel communicationChannel) {
        this.communicationChannel = communicationChannel;
    }

    public synchronized void run() {
        int i = MINUTE;
        while (i > 0 && !communicationChannel.hasMessages()) {
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
