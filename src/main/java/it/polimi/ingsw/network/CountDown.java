package it.polimi.ingsw.network;

import java.util.List;

public class CountDown extends Thread {
    public final List<String> buffer;
    private final int MINUTE = 60;
    private boolean runnedOut = false;
    private int availableTime=2*MINUTE;
    private boolean finish=false;

    public CountDown(List<String> buffer) {
        this.buffer = buffer;
    }
    public int getAvailableTime(){
        return availableTime*1000;
    }
    public boolean isRunnedOut() {
        return runnedOut;
    }
    public void finish(){
        finish=true;
    }
    public boolean isFinished(){
        return finish;
    }
    private synchronized boolean userAnswered() {
        if(!buffer.isEmpty())
            finish();
        return !buffer.isEmpty();
    }

    private void runOut() {
        runnedOut = true;
    }

    /**
     * 2 minutes countdown
     */
    @Override
    public void run() {
        while (availableTime > 0 && !userAnswered() && !isFinished()) {
            try {
                int SECOND = 1000;
                sleep(SECOND);
            } catch (InterruptedException e) {
                System.err.println("WAIT NON FUNZIONA");
                return;
            }
            availableTime--;
        }

        if (availableTime == 0) {
            runOut();
            System.out.println("time runned out");
        }
    }
}
