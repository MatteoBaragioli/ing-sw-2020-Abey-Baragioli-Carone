package it.polimi.ingsw.network.exceptions;

public class TimeOutException extends Throwable {
    private String name = null;

    public TimeOutException() {}

    public TimeOutException(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        if (name()==null)
            this.name = name;
    }
}
