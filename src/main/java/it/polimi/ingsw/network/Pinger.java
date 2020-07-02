package it.polimi.ingsw.network;

import it.polimi.ingsw.network.exceptions.ChannelClosedException;

import static it.polimi.ingsw.network.CommunicationProtocol.PING;

public class Pinger extends Thread {

    final public CommunicationChannel communicationChannel;
    private boolean ended = false;

    public Pinger(CommunicationChannel communicationChannel) {
        this.communicationChannel = communicationChannel;
    }

    public boolean isEnded() {
        return ended;
    }

    public synchronized void end() {
        ended = true;
        notifyAll();
    }

    /**
     * this thread pings the client every 5 seconds, if the client is no longer connected it closes the communication channel
     */
    public void run() {
        while (!communicationChannel.isClosed()) {
            int countdown = 5;

            try {
                communicationChannel.writeKeyWord(PING);
            } catch (ChannelClosedException e) {
                communicationChannel.close();
                break;
            }

            while (countdown > 0 && !communicationChannel.isClosed() && !communicationChannel.isPinged()) {
                try {
                    sleep(10000);
                    countdown--;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    countdown = 0;
                    communicationChannel.close();
                }
                if (!communicationChannel.isPinged())
                    System.out.println(countdown);
            }

            if (!communicationChannel.isClosed() && (countdown == 0 || !communicationChannel.isPinged())) {
                communicationChannel.close();
                end();
                System.out.println("Client non connesso");
            }
            else {
                communicationChannel.resetPing();
                System.out.println("Client connesso");
            }
        }
    }
}
