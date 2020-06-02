package it.polimi.ingsw.network.exceptions;

public class ChannelClosedException extends Throwable {
    private String name = null;


    public ChannelClosedException() {}

    public ChannelClosedException(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        if (this.name == null)
            this.name = name;
    }
}
