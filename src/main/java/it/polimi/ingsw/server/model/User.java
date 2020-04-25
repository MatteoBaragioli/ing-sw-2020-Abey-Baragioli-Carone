package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.socket.SocketServer;

public class User {
    private SocketServer socket;
    final private String name;

    public User(String name, SocketServer socket){
        this.socket = socket;
        this.name = name;
    }

    public SocketServer socket() {
        return socket;
    }

    public void setSocket(SocketServer socket) {
        this.socket = socket;
    }

    public String name(){
        return name;
    }

    public void removeSocket() {
        setSocket(null);
    }

    public boolean hasSocket() {
        return socket() != null;
    }

    public String hear() {
        return socket().read();
    }

    public void tell(String message) {
        socket().write(message);
    }

    public boolean askTwoOrThreePlayerQueue() {
        tell("MATCHTYPE");
        String answer = hear();
        boolean valid = false;
        while (!valid) {
            if (answer.equals(3) || answer.equals(2))
                valid = true;
            tell("MATCHTYPE");
            answer = hear();
        }
        if (answer.equals(3))
            return true;
        return false;
    }
}
