package it.polimi.ingsw.network;

import java.util.List;

public class CountDown extends Thread {
    public final List<String> buffer;
    public final int MINUTE = 15;
    public final int SECOND = 1000;
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
    public synchronized boolean userAnswered() {
        if(!buffer.isEmpty())
            finish();
        return !buffer.isEmpty();
    }

    public void runOut() {
        runnedOut = true;
    }

    public void run() {
        while (availableTime > 0 && !userAnswered() && !isFinished()) {
            try {
                sleep(SECOND);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.err.println("WAIT NON FUNZIONA");
                return;
            }
            if (availableTime <= 5) {
                System.out.println(availableTime);
            }
            availableTime--;
        }

        if (availableTime == 0) {
            runOut();
            System.out.println("time runned out");
            //notifyEndCountDown();
        }
    }

    public synchronized void notifyEndCountDown(){
       // notifyAll();
        System.out.println("notifying");
    }
}
