package it.polimi.ingsw.network;

public class CountDown extends Thread {
    public final int MINUTE = 15;
    public final int SECOND = 1000;
    private boolean runnedOut = false;
    private int availableTime=2*MINUTE;
    private boolean finish =false;


    public boolean isRunnedOut() {
        return runnedOut;
    }
    public boolean isFinished() {
        return finish;
    }

    public void runOut() {
        runnedOut = true;
    }
    public void finish(){
        finish=true;
    }

    public void run() {
        while (availableTime > 0 && !finish) {
            try {
                sleep(SECOND);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.err.println("WAIT NON FUNZIONA");
                return;
            }
            availableTime--;
            if (availableTime <= 5)
                System.out.println(availableTime);
            if (availableTime == 0) {
                runOut();
                System.out.println("time runned out");
            }
        }

        notifyEndCountDown();

    }

    public synchronized void notifyEndCountDown(){
        if(!isFinished()) {
            finish();
            notifyAll();
            System.out.println("notifying");
        }
    }

    public int getAvailableTime() {
        return availableTime;
    }
}
