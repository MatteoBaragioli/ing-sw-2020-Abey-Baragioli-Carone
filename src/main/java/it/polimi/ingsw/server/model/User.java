package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.socket.CommunicationChannel;

import java.io.IOException;

public class User {
    final private CommunicationChannel communicationChannel;
    final private String name;

    public User(String name, CommunicationChannel communicationChannel){
        this.communicationChannel = communicationChannel;
        this.name = name;
    }

    public CommunicationChannel communicationChannel() {
        return communicationChannel;
    }

    public String name(){
        return name;
    }

    public boolean hasChannel() {
        return communicationChannel() != null;
    }

    public String hear() throws IOException {
        return communicationChannel().read();
    }

    public void tell(String message) {
        communicationChannel().write(message);
    }

    public int askTwoOrThreePlayerMatch() throws IOException {
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
            return 3;
        return 2;
    }
}
