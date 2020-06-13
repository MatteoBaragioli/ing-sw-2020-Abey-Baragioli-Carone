package it.polimi.ingsw.network;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;

public class CountDown extends Thread {
    public final CommunicationChannel communicationChannel;
    public final int MINUTE = 60;
    public final int SECOND = 1000;
    public final CommunicationProtocol key;
    private boolean received = false;

    public CountDown(CommunicationChannel communicationChannel, CommunicationProtocol key) {
        this.communicationChannel = communicationChannel;
        this.key = key;
    }

    public void finish() {
        received = true;
    }

    public synchronized void run() {
        int i = 2*MINUTE;
        while (i > 0 && !communicationChannel.hasMessages(key) && !received) {
            try {
                sleep(SECOND);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.err.println("WAIT NON FUNZIONA");
                return;
            }
            if (i%10 == 0) {
                try {
                    communicationChannel.countdown(i);
                } catch (ChannelClosedException e) {
                    System.err.println("Connection closed");
                    return;
                }
            }
            i--;
        }
        notifyAll();
    }
}
